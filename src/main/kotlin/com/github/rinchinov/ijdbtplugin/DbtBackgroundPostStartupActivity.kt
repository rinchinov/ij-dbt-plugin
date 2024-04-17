package com.github.rinchinov.ijdbtplugin
import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.github.rinchinov.ijdbtplugin.services.ProjectSettings
import com.github.rinchinov.ijdbtplugin.services.Statistics
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vfs.VirtualFileManager


class DbtBackgroundPostStartupActivity: ProjectActivity {
    private val statistics =  Statistics.getInstance()

    override suspend fun execute(project: Project) {
        val settings = project.service<ProjectSettings>()
        arrayOf(
            settings.getDbtProjectPath(),
            "dbt_project.yml",
        ).forEach { path ->
            val virtualFile = VirtualFileManager.getInstance().findFileByUrl("file:///${project.basePath}/$path")
            if (virtualFile != null && virtualFile.exists()) {
                settings.setDbtProjectPath(path)
                loadPlugin(project)
                statistics.sendStatistics(Statistics.GroupName.CORE, "DbtBackgroundPostStartupActivity", "DBT project file loaded")
                return
            }
        }
        project.service<EventLoggerManager>().logLine(
            "Dbt project file not found, please check settings and make sure that this project is dbt",
            "core"
        )
        statistics.sendStatistics(Statistics.GroupName.CORE, "DbtBackgroundPostStartupActivity", "DBT project file load failed")
    }

    private fun loadPlugin(project: Project){
        val manifestService = project.service<ManifestService>()
        project.service<ProjectConfigurations>().dbtProjectConfig.targets.forEach{
            manifestService.parseManifest(it)
        }
    }
}
