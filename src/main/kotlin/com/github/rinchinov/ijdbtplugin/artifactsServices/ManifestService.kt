package com.github.rinchinov.ijdbtplugin.artifactsServices
import com.github.rinchinov.ijdbtplugin.artifactInterfaces.ManifestInterface
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV4.ManifestV4
import com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV5.ManifestV5
import com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV6.ManifestV6
import com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV7.ManifestV7
import com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV8.ManifestV8
import com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV9.ManifestV9
import com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV10.ManifestV10
import com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV11.ManifestV11
import com.intellij.psi.PsiElement
import com.github.rinchinov.ijdbtplugin.ref.ReferencesProviderInterface
import com.github.rinchinov.ijdbtplugin.services.Executor
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.intellij.notification.NotificationType
import java.time.LocalDateTime
import java.time.Duration


@Service(Service.Level.PROJECT)
class ManifestService(project: Project): ArtifactVersion(), ReferencesProviderInterface {
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

    var manifest: ManifestInterface? = null
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
        val (jsonString, version) = loadArtifact(projectConfigurations.manifestPath())
        manifest = when (version) {
            4 -> ManifestV4.fromJson(jsonString)
            5 -> ManifestV5.fromJson(jsonString)
            6 -> ManifestV6.fromJson(jsonString)
            7 -> ManifestV7.fromJson(jsonString)
            8 -> ManifestV8.fromJson(jsonString)
            9 -> ManifestV9.fromJson(jsonString)
            11 -> ManifestV10.fromJson(jsonString)
            10 -> ManifestV11.fromJson(jsonString)
            else -> null
        }
        dbtNotifications.sendNotification("Manifest reloaded!", manifest.toString(), NotificationType.INFORMATION)
    }

    override fun modelReferenceFileByElement(element: PsiElement): String {
        if (manifest == null) {
            return ""
        } else {
            val uniqueId = "${(manifest as ManifestV10).metadata.projectName}.${element.text.trim('\"', '\'')}"
            val node = if ("model.$uniqueId" in (manifest as ManifestV10).nodes) {
                (this.manifest as ManifestV10).nodes["model.$uniqueId"]
            } else if ("seed.$uniqueId" in (manifest as ManifestV10).nodes) {
                (manifest as ManifestV10).nodes["seed.$uniqueId"]
            } else {
                null
            }
            return element.project.basePath + "/" + node?.originalFilePath
        }
    }

    override fun sourceReferenceFileByElement(element: PsiElement): String {
        return if (manifest == null) {
            ""
        } else {
            val source = """source\('([^']*)',\s*'([^']*)'\)""".toRegex().replace(element.parent.text) { matchResult ->
                "${matchResult.groupValues[1]}.${matchResult.groupValues[2]}"
            }
            val uniqueId = "source.${(manifest as ManifestV10).metadata.projectName}.$source"
            if (uniqueId in (manifest as ManifestV10).sources) {
                element.project.basePath + "/" + (this.manifest as ManifestV10).sources[uniqueId]?.originalFilePath
            } else {
                ""
            }
        }
    }
}
