package com.github.rinchinov.ijdbtplugin.artifactsServices
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Manifest
import com.intellij.psi.PsiElement
import com.github.rinchinov.ijdbtplugin.ref.ReferencesProviderInterface
import com.github.rinchinov.ijdbtplugin.services.Executor
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.intellij.notification.NotificationType
import com.jetbrains.rd.util.first
import java.io.File
import java.time.LocalDateTime
import java.time.Duration


@Service(Service.Level.PROJECT)
class ManifestService(project: Project): ReferencesProviderInterface {
    private var lastUpdated: LocalDateTime = LocalDateTime.of(1, 1, 1, 0, 0)
    companion object {
        const val UPDATE_INTERVAL = 5
    }
    private val projectConfigurations = project.service<ProjectConfigurations>()
    private val executor = project.service<Executor>()
    private val dbtNotifications = project.service<Notifications>()
    init {
        parseManifest()
    }
    private var manifest: Manifest? = null
        set(value) {
            field = value
            lastUpdated = LocalDateTime.now()
        }

    override fun toString(): String = "Data:, Last Updated: $lastUpdated"

    fun parseManifest() {
        if (Duration.between(lastUpdated, LocalDateTime.now()).toMinutes() <= UPDATE_INTERVAL) {
            return
        }
        executor.executeDbt(listOf("parse"), mapOf()).toString()
        val jsonString = File(projectConfigurations.manifestPath().absolutePath.toString()).readText(Charsets.UTF_8)
        manifest = Manifest.fromJson(jsonString)
        dbtNotifications.sendNotification("Manifest reloaded!", manifest.toString(), NotificationType.INFORMATION)
    }

    private fun getPackageInfo(packageName: String?): Pair<String, String> {
        return if (packageName == null || packageName == "" || (manifest as Manifest).metadata.projectName == packageName) {
            Pair((manifest as Manifest).metadata.projectName ?: "", "/")
        }
        else {
            // to do get it from project yml
            // packages-install-path: dbt_packages
            // https://docs.getdbt.com/reference/project-configs/packages-install-path
            Pair(packageName, "/dbt_packages/$packageName/")
        }
    }

    override fun modelReferenceFileByElement(packageName: String?, uniqueId: String, currentVersion: Int?, element: PsiElement): String {
        if (manifest == null) {
            return ""
        } else {
            val nodes = (manifest as Manifest).nodes
            val packageInfo = getPackageInfo(packageName)
            val packageId = packageInfo.first
            val node = if ("model.$packageId.$uniqueId" in nodes) {
                nodes["model.$packageId.$uniqueId"]
            } else if ("seed.$packageId.$uniqueId" in nodes) {
                nodes["seed.$packageId.$uniqueId"]
            } else {
                val latestVersion = nodes.filterKeys { it.startsWith("model.$packageId.$uniqueId") }.first().value.latestVersion?.toJson()
                    ?.toInt()
                val version = currentVersion ?: latestVersion
                nodes["model.$packageId.$uniqueId.v$version"]
            }
            return element.project.basePath + packageInfo.second + node?.originalFilePath
        }
    }

    override fun sourceReferenceFileByElement(uniqueId: String, element: PsiElement): String {
        return if (manifest == null) {
            ""
        } else {
            val matchedSources = (manifest as Manifest).sources.filterKeys {
                it.endsWith(uniqueId)
            }
            if (matchedSources.isEmpty()){
                ""
            }
            else {
                val source = matchedSources.first().value
                val packageInfo = getPackageInfo(source.packageName)
                element.project.basePath + packageInfo.second + source.originalFilePath
            }
        }
    }
    override fun macroReferenceFileByElement(packageName: String, macroName: String, element: PsiElement): String {
        return if (manifest == null) {
            ""
        } else {
            val macros = (manifest as Manifest).macros
            val packageInfo = getPackageInfo(packageName)
            val packageId = packageInfo.first
            val macro = macros["macro.$packageId.$macroName"]
            element.project.basePath + packageInfo.second + macro?.originalFilePath
        }
    }
}
