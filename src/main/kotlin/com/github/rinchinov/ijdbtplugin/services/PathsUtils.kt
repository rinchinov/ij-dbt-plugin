package com.github.rinchinov.ijdbtplugin.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.nio.file.Path
import java.nio.file.Paths

@Service(Service.Level.PROJECT)
class PathsUtils(private val project: Project) {
    private val settings = project.service<ProjectSettings>()
    class SettingPath(relativePath: String, basePath: String?){
        var absolutePath: Path = Paths.get(basePath.toString(), relativePath)
        var absoluteDir: Path = absolutePath.parent
    }
    fun sdkPath(): SettingPath {
        return SettingPath(settings.getDbtInterpreterPath(), "")
    }
    fun logPath(): SettingPath {
        return SettingPath("logs/dbt.log", project.basePath)
    }
    fun dbtProjectPath(): SettingPath {
        return SettingPath(settings.getProjectPath(), project.basePath)
    }
}
