package com.github.rinchinov.ijdbtplugin

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations

interface ProjectInfoChangeListenerInterface {
    fun onManifestChanged(manifest: ManifestService)
    fun onProjectConfigurationsChanged(configurations: ProjectConfigurations)
}