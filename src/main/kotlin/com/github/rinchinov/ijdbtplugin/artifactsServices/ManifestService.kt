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

    var manifest: Manifest? = null
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

    override fun modelReferenceFileByElement(element: PsiElement): String {
        if (manifest == null) {
            return ""
        } else {
            val nodes = (manifest as Manifest).nodes
            val uniqueId = "${(manifest as Manifest).metadata.projectName}.${element.text.trim('\"', '\'')}"
            val node = if ("model.$uniqueId" in nodes) {
                nodes["model.$uniqueId"]
            } else if ("seed.$uniqueId" in nodes) {
                nodes["seed.$uniqueId"]
            } else {
                val latestVersion = nodes.filterKeys { it.startsWith("model.$uniqueId") }.first().value.latestVersion?.toJson()
                    ?.toInt()
                val currentVersion = """(version|v)=["']?(\d+)["']?""".toRegex().find(element.parent.text)?.groupValues?.get(2)?.toInt()
                val version = currentVersion ?: latestVersion
                nodes["model.$uniqueId.v$version"]
            }
            return element.project.basePath + "/" + node?.originalFilePath
        }
    }

    override fun sourceReferenceFileByElement(element: PsiElement): String {
        return if (manifest == null) {
            ""
        } else {
            val sources = (manifest as Manifest).sources
            val source = """source\(['"]([^'"]*)['"],\s*["']([^"']*)['"]\)""".toRegex().replace(element.parent.text) { matchResult ->
                "${matchResult.groupValues[1]}.${matchResult.groupValues[2]}"
            }
            val uniqueId = "source.${(manifest as Manifest).metadata.projectName}.$source"
            if (uniqueId in sources) {
                element.project.basePath + "/" + sources[uniqueId]?.originalFilePath
            } else {
                ""
            }
        }
    }
}
