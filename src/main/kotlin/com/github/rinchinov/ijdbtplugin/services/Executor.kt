package com.github.rinchinov.ijdbtplugin.services
import com.github.rinchinov.ijdbtplugin.extensions.MainToolWindowService
import com.google.gson.Gson
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.InputStreamReader
import com.intellij.notification.NotificationType
import java.io.File
import java.nio.file.Paths


@Service(Service.Level.PROJECT)
class Executor(project: Project){
    private val settings = project.service<ProjectSettings>()
    private val projectConfigurations = project.service<ProjectConfigurations>()
    private val dbtNotifications = project.service<Notifications>()
    private val eventLoggerManager = project.service<EventLoggerManager>()

    private fun waitProcess(process: Process): String{
        val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
        val errorOutput = BufferedReader(InputStreamReader(process.errorStream)).use { it.readText() }
        val errorLogs = errorOutput.trim().lines()

        // Wait for the process to complete
        val exitCode = process.waitFor()
        val logs = output.trim().lines()
        // Handle the output and exit code
        if (exitCode != 0) {
            dbtNotifications.sendNotification(
                "Process finished with exit code $exitCode",
                "Output: $output",
                NotificationType.ERROR,
                MainToolWindowService.Tab.LOGS
            )
            if (logs.isNotEmpty()){
                eventLoggerManager.logLines(logs, "dbt")
            }
            if (errorLogs.isNotEmpty()){
                eventLoggerManager.logLines(errorLogs, "dbt")
            }
        }
        else {
            if (logs.size > 1){
                eventLoggerManager.logLines(logs.dropLast(1), "dbt")
            }
            if (errorLogs.isNotEmpty()){
                eventLoggerManager.logLines(errorLogs, "dbt")
            }
        }
        if (logs.isEmpty()){
            return output.trim()
        }
        return logs.last().trim()
    }

    fun dbtCompileInline(target: String, query: String): String {
        return dbtInvoke(
            listOf("compile", "--inline", query),
            mapOf(
                "target" to target,
                "target_path" to projectConfigurations.getDbtCachePath(target).toString(),
                "log_path" to projectConfigurations.getDbtCachePath(target).toString(),
                "profiles_dir" to projectConfigurations.getDbtProfileDirAbsolute().toString()
            )
        )
    }
    fun dbtParse(target: String): String {
        return dbtInvoke(
            listOf("parse", "--partial-parse"),
            mapOf(
                "target" to target,
                "target_path" to projectConfigurations.getDbtCachePath(target).toString(),
                "log_path" to projectConfigurations.getDbtCachePath(target).toString(),
                "profiles_dir" to projectConfigurations.getDbtProfileDirAbsolute().toString()
            )
        )
    }
    fun dbtDocsGenerate(target: String): String {
        return dbtInvoke(
            listOf("docs", "generate", "--partial-parse"),
            mapOf(
                "target" to target,
                "target_path" to projectConfigurations.getDbtCachePath(target).toString(),
                "log_path" to projectConfigurations.getDbtCachePath(target).toString(),
                "profiles_dir" to projectConfigurations.getDbtProfileDirAbsolute().toString()
            )
        )
    }

    private fun runPython(command: List<String>, directory: File?): String {
        val pythonSdkPath = projectConfigurations.getProjectPythonSdk()
        try {
            eventLoggerManager.logLine("using $pythonSdkPath","core")
            val pythonSdkPathNormalized = Paths.get(pythonSdkPath).toAbsolutePath().toString()
            val processBuilder = ProcessBuilder(
                listOf(pythonSdkPathNormalized) + command
            )
            if (directory != null){
                processBuilder.directory(directory)
            }
            val environment = processBuilder.environment()
            environment.putAll(settings.getDbtEnvVariables())
            val process = processBuilder.start()
            return waitProcess(process)
        } catch (e: Exception) {
            dbtNotifications.sendNotification(
                "Failed to run dbt command",
                "Exception: $e",
                NotificationType.ERROR,
                MainToolWindowService.Tab.LOGS
            )
            eventLoggerManager.logLine("Caught an exception: ${e.message}", "core")
            eventLoggerManager.logLine(e.printStackTrace().toString(), "core")
            (listOf(pythonSdkPath) + command).forEach {
                eventLoggerManager.logLine(it, "core")
            }
            settings.getDbtEnvVariables().forEach { (t, u) ->
                eventLoggerManager.logLine("$t: $u", "core")
            }
            return e.message.toString()
        }
    }

    private fun dbtInvoke(args: List<String>, kwargs: Map<String, String>): String {
        val url = this::class.java.classLoader.getResource("python/cli.py")
        val scriptTemplate = url?.readText() ?: throw IllegalArgumentException("Script not found")
        val script = scriptTemplate.replace("RUNNER_IMPORT", settings.getDbtRunnerImport())
        val gson = Gson()
        return runPython(
            listOf(
                "-c",
                script,
                gson.toJson(args),
                gson.toJson(kwargs),
            ),
            projectConfigurations.dbtProjectPath().absoluteDir.toFile()
        )
    }

    fun dbtQueryCall(target: String, sql: String, fetch: Boolean): String {
        val gson = Gson()
        val url = this::class.java.classLoader.getResource("python/db.py")
        val script = url?.readText() ?: throw IllegalArgumentException("Script not found")
        return runPython(
            listOf(
                "-c",
                script,
                "run_query",
                "--target",
                target,
                "--_plugin_custom_sql",
                sql,
                "--_plugin_custom_fetch",
                if (fetch) "true" else "false"
            ),
            projectConfigurations.dbtProjectPath().absoluteDir.toFile()
        )
    }
    fun getDbtPythonPackageLocation(): String {
         return runPython(
             listOf(
                "-c",
                "import os,dbt;print(os.path.dirname(dbt.__file__))"
             ),
             null
        )
    }
}
