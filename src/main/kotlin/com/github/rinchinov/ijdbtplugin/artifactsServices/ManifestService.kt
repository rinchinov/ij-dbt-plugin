package com.github.rinchinov.ijdbtplugin.artifactsServices

import com.github.rinchinov.ijdbtplugin.AnnotationsInterface
import com.github.rinchinov.ijdbtplugin.ReferenceInterface
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.github.rinchinov.ijdbtplugin.artifactsVersions.*
import com.github.rinchinov.ijdbtplugin.extensions.FocusLogsTabAction
import com.github.rinchinov.ijdbtplugin.services.Executor
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.github.rinchinov.ijdbtplugin.services.ProjectSettings
import com.intellij.notification.NotificationType
import java.time.LocalDateTime
import java.time.Duration
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@Service(Service.Level.PROJECT)
class ManifestService(override var project: Project):
    ManifestLookup,
    ManifestCopyPasteActions,
    ManifestCompletion,
    AnnotationsInterface,
    ReferenceInterface
{
    companion object {
        const val UPDATE_INTERVAL = 5
    }
    override val projectConfigurations = project.service<ProjectConfigurations>()
    override val settings = project.service<ProjectSettings>()
    private val executor = project.service<Executor>()
    override val dbtPackageLocation = executor.getDbtPythonPackageLocation()
    override val dbtNotifications = project.service<Notifications>()
    private val eventLoggerManager = project.service<EventLoggerManager>()
    override val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    override val manifests: MutableMap<String, Manifest?> = projectConfigurations.dbtProjectConfig.targets.associateWith{ null }.toMutableMap()
    private val manifestLastUpdated: MutableMap<String, LocalDateTime> = projectConfigurations.dbtProjectConfig.targets.associateWith{ LocalDateTime.of(1, 1, 1, 0, 0) }.toMutableMap()
    private val mutex: MutableMap<String, Mutex> = projectConfigurations.dbtProjectConfig.targets.associateWith{ Mutex() }.toMutableMap()
    init {
        projectConfigurations.dbtProjectConfig.targets.forEach { target ->
            val path = Paths.get(projectConfigurations.getDbtCachePath(target).toString(), "manifest.json")
            if (Files.exists(path)) {
                val manifestJson = File(path.toString()).readText(Charsets.UTF_8)
                updateManifest(target, Manifest.fromJson(manifestJson))
            }
        }
    }
    override fun defaultManifest() = manifests[projectConfigurations.dbtProjectConfig.defaultTarget]
    override fun defaultProjectName() = defaultManifest()?.getPackageName()?: ""
    private fun updateManifest(target: String, manifest: Manifest) {
        manifests[target] = manifest // Directly modify the backing map
        manifestLastUpdated[target] = LocalDateTime.now()
        eventLoggerManager.notifyManifestChangeListeners(this)
    }
    override fun getManifest(target: String?): Manifest? {
        val cTarget: String = (target?: projectConfigurations.dbtProjectConfig.defaultTarget).toString()
        parseManifest(cTarget)
        return manifests[cTarget]
    }

    fun parseManifest(target: String) {
        val lastUpdated = manifestLastUpdated[target]?: LocalDateTime.of(1, 1, 1, 0, 0)
        if (Duration.between(lastUpdated, LocalDateTime.now()).toMinutes() <= UPDATE_INTERVAL) {
                return
        }

        coroutineScope.launch {
            if (mutex[target]?.tryLock() == true) {
                try {
                    val manifestString = executor.dbtParse(target)
                    updateManifest(target, Manifest.fromJson(manifestString))
                    dbtNotifications.sendNotification(
                        "Manifest reloaded for $target!",
                        "",
                        NotificationType.INFORMATION
                    )
                } catch (e: Exception) {
                    eventLoggerManager.logLines(e.toString().trim().lines(), "core")
                    dbtNotifications.sendNotification(
                        "Manifest reload failed for $target!!",
                        "",
                        NotificationType.ERROR,
                        FocusLogsTabAction(project)
                    )
                } finally {
                    mutex[target]?.unlock()
                }
            }
        }
    }
    fun getNodesCount(): Int? = defaultManifest()?.nodes?.size
    fun getMacrosCount(): Int? = defaultManifest()?.macros?.size
    fun getSourcesCount(): Int? = defaultManifest()?.sources?.size
    fun lastUpdated(): LocalDateTime? = manifestLastUpdated[projectConfigurations.dbtProjectConfig.defaultTarget]
    fun getStatus(): String = if (defaultManifest() == null) "Manifest parse failed" else "Manifest successfully parsed"

}
