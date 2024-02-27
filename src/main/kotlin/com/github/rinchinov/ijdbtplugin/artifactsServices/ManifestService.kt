package com.github.rinchinov.ijdbtplugin.artifactsServices
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Manifest
import com.github.rinchinov.ijdbtplugin.extentions.ToolWindowUpdater
import com.intellij.psi.PsiElement
import com.github.rinchinov.ijdbtplugin.ref.ReferencesProviderInterface
import com.github.rinchinov.ijdbtplugin.services.Executor
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.intellij.notification.NotificationType
import com.jetbrains.rd.util.first
import java.io.File
import java.time.LocalDateTime
import java.time.Duration
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex


@Service(Service.Level.PROJECT)
class ManifestService(project: Project): ReferencesProviderInterface {
    var lastUpdated: LocalDateTime = LocalDateTime.of(1, 1, 1, 0, 0)
    companion object {
        const val UPDATE_INTERVAL = 5
    }
    private val projectConfigurations = project.service<ProjectConfigurations>()
    private val executor = project.service<Executor>()
    private val dbtNotifications = project.service<Notifications>()
    private val toolWindowUpdater = project.service<ToolWindowUpdater>()
    private val mutex = Mutex()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    init {
        parseManifest()
    }
    private var manifest: Manifest? = null
        set(value) {
            field = value
            lastUpdated = LocalDateTime.now()
            toolWindowUpdater.notifyManifestChangeListeners(this)
        }

    override fun toString(): String = "Data:, Last Updated: $lastUpdated"

    private fun parseManifest() {
        if (Duration.between(lastUpdated, LocalDateTime.now()).toMinutes() <= UPDATE_INTERVAL) {
            return
        }

        coroutineScope.launch {
            if (mutex.tryLock()) {
                try {
                    dbtNotifications.sendNotification("Manifest reload started!", "", NotificationType.INFORMATION)
                    executor.executeDbt(listOf("parse"), mapOf()).toString()
                    val jsonString =
                        File(projectConfigurations.manifestPath().absolutePath.toString()).readText(Charsets.UTF_8)
                    manifest = Manifest.fromJson(jsonString)
                    dbtNotifications.sendNotification(
                        "Manifest reloaded!",
                        manifest.toString(),
                        NotificationType.INFORMATION
                    )
                } catch (e: Exception) {
                    dbtNotifications.sendNotification(
                        "Manifest reload failed!",
                        e.toString(),
                        NotificationType.ERROR
                    )
                } finally {
                    mutex.unlock()
                }
            }
        }
    }

    private fun getPackageInfo(packageName: String?): Pair<String, String> {
        return if (packageName == null || packageName == "" || (manifest as Manifest).metadata.projectName == packageName) {
            Pair(
                (manifest as Manifest).metadata.projectName ?: "",
                projectConfigurations.dbtProjectPath().absoluteDir.toString() + "/"
            )
        }
        else {
            Pair(
                packageName,
                "${projectConfigurations.packagesPath().absolutePath}/$packageName/"
            )
        }
    }
    fun getNodesCount(): Int? = manifest?.nodes?.size
    fun getMacrosCount(): Int? = manifest?.macros?.size
    fun getSourcesCount(): Int? = manifest?.sources?.size
    fun getStatus(): String = if (manifest == null) "Manifest parse failed" else "Manifest successfully parsed"

    override fun modelReferenceFileByElement(packageName: String?, uniqueId: String, currentVersion: Int?, element: PsiElement): String {
        parseManifest()
        if (manifest == null) {
            return ""
        } else {
            val nodes = (manifest as Manifest).nodes
            val packageInfo = getPackageInfo(packageName)
            val packageId = packageInfo.first
            val path = if ("model.$packageId.$uniqueId" in nodes) {
                val node = nodes["model.$packageId.$uniqueId"]
                packageInfo.second + node?.originalFilePath
            } else if ("seed.$packageId.$uniqueId" in nodes) {
                val node = nodes["seed.$packageId.$uniqueId"]
                packageInfo.second + node?.originalFilePath
            } else {
                val versionsCurrentPackage = nodes.filterKeys { it.startsWith("model.$packageId.$uniqueId") }
                if (versionsCurrentPackage.isNotEmpty()){
                    val latestVersion = versionsCurrentPackage.first().value.latestVersion?.toJson()
                        ?.toInt()
                    val version = currentVersion ?: latestVersion
                    val node = nodes["model.$packageId.$uniqueId.v$version"]
                    packageInfo.second + node?.originalFilePath
                }
                else {
                    val versionsAnyPackage = nodes.filterKeys { it.contains(uniqueId) }
                    if (versionsAnyPackage.isNotEmpty()){
                        val anyNode = versionsAnyPackage.first().value
                        val anyPackageInfo = getPackageInfo(anyNode.packageName)
                        anyPackageInfo.second + anyNode.originalFilePath
                    }
                    else {
                        ""
                    }
                }
            }
            return path
        }
    }

    override fun sourceReferenceFileByElement(uniqueId: String, element: PsiElement): String {
        parseManifest()
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
        parseManifest()
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
