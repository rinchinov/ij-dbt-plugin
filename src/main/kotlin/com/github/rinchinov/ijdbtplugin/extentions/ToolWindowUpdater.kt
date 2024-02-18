package com.github.rinchinov.ijdbtplugin.extentions

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.components.Service
interface MyDataChangeListener {
    fun onManifestChanged(manifest: ManifestService)
    fun onProjectConfigurationsChanged(configurations: ProjectConfigurations)
}
@Service(Service.Level.PROJECT)
class ToolWindowUpdater {
    private val listeners = mutableListOf<MyDataChangeListener>()
    fun addDataChangeListener(listener: MyDataChangeListener) {
        listeners.add(listener)
    }
    fun notifyProjectConfigurationsChangeListeners(configurations: ProjectConfigurations) {
        listeners.forEach { it.onProjectConfigurationsChanged(configurations) }
    }
    fun notifyManifestChangeListeners(manifestService: ManifestService) {
        listeners.forEach { it.onManifestChanged(manifestService) }
    }
}