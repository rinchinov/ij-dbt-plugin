package com.github.rinchinov.ijdbtplugin.services

import com.github.rinchinov.ijdbtplugin.extensions.MainToolWindowService
import com.github.rinchinov.ijdbtplugin.utils.Jinja2Utils
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
    private val jinja2Utils = project.service<Jinja2Utils>()
    val statistics = Statistics.getInstance()
    private val dbtNotifications = project.service<Notifications>()
    private val eventLoggerManager = project.service<EventLoggerManager>()
    val dbtProjectConfig = DbtProjectConfig(
        "",
        "dbt_packages",
        null,
        "default",
        emptyList(),
        null
    )
    data class DbtProjectConfig(
        var profile: String,
        var packagesInstallPath: String,
        var projectProfiles: Map<String, Map<String, Any>>?,
        var defaultTarget: String,
        var targets: List<String>,
        var adapterName: String?
    )

    init {
        reloadDbtProjectSettings()
    }
    fun getProfileDetails(target: String): Map<String, Any>? {
        return dbtProjectConfig.projectProfiles?.get(target)
    }

    private fun loadProfileDetails(){
        val profileFile = Paths.get(getDbtProfileDirAbsolute().toString(), "profiles.yml")
        try {
            val inputStream: InputStream = Files.newInputStream(profileFile)
            val profilesRaw = Yaml().load(inputStream) as Map<String, Map<String, Map<String, Map<String, Any>>>>?
            val raw = profilesRaw?.get(dbtProjectConfig.profile)
            dbtProjectConfig.projectProfiles = raw?.getOrDefault("outputs", emptyMap()) as Map<String, Map<String, Any>>
            dbtProjectConfig.defaultTarget = raw.getOrDefault("target", "default").toString()
            dbtProjectConfig.targets = dbtProjectConfig.projectProfiles?.keys?.toList() ?: emptyList()
            dbtProjectConfig.adapterName = dbtProjectConfig.projectProfiles!![dbtProjectConfig.defaultTarget]?.get("type") as String
        } catch (e: FileNotFoundException) {
            eventLoggerManager.logLines(e.stackTraceToString().lines(), "core")
            dbtNotifications.sendNotification("File not found", "Failed to open `${profileFile}`", NotificationType.ERROR, MainToolWindowService.Tab.LOGS)
            statistics.setProjectConfigurations(this)
            statistics.sendStatistics(Statistics.GroupName.CORE, "ProjectConfigurations", "Profile details load failed: file not found")
        }
        catch (e: Exception) {
            eventLoggerManager.logLines(e.stackTraceToString().lines(), "core")
            dbtNotifications.sendNotification("Error loading YAML file", "Failed to parse `$profileFile`", NotificationType.ERROR, MainToolWindowService.Tab.LOGS)
            statistics.setProjectConfigurations(this)
            statistics.sendStatistics(Statistics.GroupName.CORE, "ProjectConfigurations", "Profile details load failed: other")
        }
    }

    fun reloadDbtProjectSettings(){
        val filePath = dbtProjectPath().absolutePath.toString()
        try {
            val inputStream: InputStream = Files.newInputStream(Paths.get(filePath))
            val projectSettingRaw = Yaml().load(inputStream) as Map<String, Any>?
            if (projectSettingRaw != null) {
                dbtProjectConfig.profile = projectSettingRaw["profile"] as String
                val packagesInstallPath = projectSettingRaw.getOrDefault(
                    "packages-install-path",
                    dbtProjectConfig.packagesInstallPath
                ) as String
                dbtProjectConfig.packagesInstallPath = jinja2Utils.renderJinjaEnvVar(packagesInstallPath)
                loadProfileDetails()
                statistics.setProjectConfigurations(this)
                statistics.sendStatistics(Statistics.GroupName.CORE, "ProjectConfigurations", "Project configuration loaded")
            }
            else {
                dbtNotifications.sendNotification("Load project failed", filePath, NotificationType.ERROR)
                statistics.setProjectConfigurations(this)
                statistics.sendStatistics(Statistics.GroupName.CORE, "ProjectConfigurations", "Project configuration load failed: yml is null")
            }
        } catch (e: FileNotFoundException) {
            eventLoggerManager.logLines(e.stackTraceToString().lines(), "core")
            dbtNotifications.sendNotification("File not found", "Failed to open `$filePath`", NotificationType.ERROR, MainToolWindowService.Tab.LOGS)
            statistics.setProjectConfigurations(this)
            statistics.sendStatistics(Statistics.GroupName.CORE, "ProjectConfigurations", "Project configuration load failed: file not found")
        } catch (e: Exception) {
            eventLoggerManager.logLines(e.stackTraceToString().lines(), "core")
            dbtNotifications.sendNotification("Error loading YAML file", "Failed to open `$filePath`", NotificationType.ERROR, MainToolWindowService.Tab.LOGS)
            statistics.setProjectConfigurations(this)
            statistics.sendStatistics(Statistics.GroupName.CORE, "ProjectConfigurations", "Project configuration load failed: other")
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
        statistics.setProjectConfigurations(this)
        statistics.sendStatistics(Statistics.GroupName.CORE, "ProjectConfigurations", "Project configuration load failed: can't find python SDK")
        return ""
    }

}
