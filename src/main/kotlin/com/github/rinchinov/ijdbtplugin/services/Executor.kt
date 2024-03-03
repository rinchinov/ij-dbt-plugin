package com.github.rinchinov.ijdbtplugin.services
import com.github.rinchinov.ijdbtplugin.extensions.FocusLogsTabAction
import com.google.gson.Gson
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.InputStreamReader
import com.intellij.notification.NotificationType
import java.io.File


@Service(Service.Level.PROJECT)
class Executor(private val project: Project){
    private val settings = project.service<ProjectSettings>()
    private val projectConfigurations = project.service<ProjectConfigurations>()
    private val dbtNotifications = project.service<Notifications>()
    private val eventLoggerManager = project.service<EventLoggerManager>()

    private fun waitProcess(process: Process): String{
        val reader = BufferedReader(InputStreamReader(process.inputStream))
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
                FocusLogsTabAction(project)
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
            listOf("parse", "--write-json", "--partial-parse"),
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
            listOf("docs", "generate", "--write-json", "--partial-parse"),
            mapOf(
                "target" to target,
                "target_path" to projectConfigurations.getDbtCachePath(target).toString(),
                "log_path" to projectConfigurations.getDbtCachePath(target).toString(),
                "profiles_dir" to projectConfigurations.getDbtProfileDirAbsolute().toString()
            )
        )
    }

    private fun runPython(command: List<String>, directory: File?): String {
        try {
            val pythonSdkPath = projectConfigurations.getProjectPythonSdk()
            eventLoggerManager.logLine("using $pythonSdkPath","core")
            val processBuilder = ProcessBuilder(
                listOf(
                    listOf(pythonSdkPath),
                    command
                ).flatten()
            )
            if (directory != null){
                processBuilder.directory(directory)
            }
            val process = processBuilder.start()
            return waitProcess(process)
        } catch (e: Exception) {
            dbtNotifications.sendNotification(
                "Failed to run dbt command",
                "Exception: $e",
                NotificationType.ERROR,
                FocusLogsTabAction(project)
            )
            eventLoggerManager.logLine("Caught an exception: ${e.message}", "core")
            eventLoggerManager.logLine(e.printStackTrace().toString(), "core")
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
