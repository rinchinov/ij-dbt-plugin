package com.github.rinchinov.ijdbtplugin.services

import com.github.rinchinov.ijdbtplugin.utils.renderJinjaEnvVar
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.jetbrains.python.sdk.PythonSdkType
import org.yaml.snakeyaml.Yaml
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service(Service.Level.PROJECT)
class ProjectConfigurations(private val project: Project) {
    val settings = project.service<ProjectSettings>()
    private val dbtNotifications = project.service<Notifications>()
    private val eventLoggerManager = project.service<EventLoggerManager>()
    private val dbtProjectConfig = DbtProjectConfig(
        "",
        1,
        "",
        "",
        "target",
        "dbt_packages"
    )
    data class DbtProjectConfig(
        var name: String,
        var configVersion: Int,
        var version: String,
        var profile: String,
        var targetPath: String,
        var packagesInstallPath: String
    )
    init {
        reloadDbtProjectSettings()
    }
    fun reloadDbtProjectSettings(){
        val filePath = dbtProjectPath().absolutePath.toString()
        try {
            val inputStream: InputStream = Files.newInputStream(Paths.get(filePath))
            val projectSettingRaw = Yaml().load(inputStream) as Map<String, Any>?
            if (projectSettingRaw != null) {
                dbtProjectConfig.name = projectSettingRaw["name"] as String
                dbtProjectConfig.configVersion = projectSettingRaw["config-version"] as Int
                dbtProjectConfig.version = projectSettingRaw["version"] as String
                dbtProjectConfig.profile = projectSettingRaw["profile"] as String
                dbtProjectConfig.targetPath = projectSettingRaw["target-path"] as String
                val packagesInstallPath = projectSettingRaw.getOrDefault(
                    "packages-install-path",
                    dbtProjectConfig.packagesInstallPath
                ) as String
                dbtProjectConfig.packagesInstallPath = renderJinjaEnvVar(packagesInstallPath)
            }
            else {
                dbtNotifications.sendNotification("Load project failed", filePath, NotificationType.ERROR)
            }
        } catch (e: FileNotFoundException) {
            dbtNotifications.sendNotification("File not found", "$filePath\n TBD Instruction and link to the doc", NotificationType.ERROR)
        } catch (e: Exception) {
            dbtNotifications.sendNotification("Error loading YAML file", ": ${e.message}\nTBD Instruction and link to the doc", NotificationType.ERROR)
        }
        eventLoggerManager.notifyProjectConfigurationsChangeListeners(this)
    }
    class SettingPath(relativePath: String, basePath: String) {
        var absolutePath: Path = Paths.get(basePath, relativePath)
        var absoluteDir: Path = absolutePath.parent
    }
    fun dbtProjectPath(): SettingPath {
        return SettingPath(settings.getDbtProjectPath(), project.basePath?: "")
    }
    fun packagesPath(): SettingPath {
        return if (dbtProjectConfig.packagesInstallPath.startsWith("/")) {
            SettingPath("", dbtProjectConfig.packagesInstallPath)
        } else {
            SettingPath(dbtProjectConfig.packagesInstallPath, dbtProjectPath().absoluteDir.toString())
        }
    }

    fun getDbtProfileDirAbsolute(): Path {
        return when {
            settings.getDbtProfileDir().startsWith("~/") -> Paths.get(System.getProperty("user.home"), settings.getDbtProfileDir().replace("~/", ""))
            else -> Paths.get(settings.getDbtProfileDir())
        }
    }

    fun getDbtCachePath(target: String): Path {
        val cacheDir = Paths.get(project.basePath?: System.getProperty("user.home"),".dbt_plugin", target)
        Files.createDirectories(cacheDir) // Ensure the cache directory exists
        return cacheDir
    }

    private fun findDbtPython(): String? {
        val command = if (System.getProperty("os.name").startsWith("Windows")) "where dbt" else "which dbt"
        try {
            val process = Runtime.getRuntime().exec(command)
            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                val path = reader.readLine()
                if (path != null && path.isNotEmpty()) {
                    val file = File(path)
                    val shebangRegex = "^#!\\s*(.*/python[23]?)".toRegex()
                    file.useLines { lines ->
                        val shebangLine = lines.firstOrNull()
                        val matchResult = shebangRegex.find(shebangLine ?: "")
                        return matchResult?.value?.trim()?.substringAfter("#!")?.trim()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    fun getProjectPythonSdk(): String {
        if (settings.getDbtInterpreterPath().isNotEmpty()){
            return settings.getDbtInterpreterPath()
        }
        val projectSdk = ProjectRootManager.getInstance(project).projectSdk
        if (projectSdk != null && projectSdk.sdkType is PythonSdkType) {
            return projectSdk.homePath.toString()
        }
        val dbtPython = findDbtPython()
        if (dbtPython != null) {
            return dbtPython
        }
        eventLoggerManager.logLine("Can't find python interpreter", "core")
        dbtNotifications.sendNotification("Can't find python interpreter", "", NotificationType.ERROR)
        return ""
    }
}
