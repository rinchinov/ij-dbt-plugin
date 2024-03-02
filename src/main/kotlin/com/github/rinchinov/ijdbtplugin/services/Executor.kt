package com.github.rinchinov.ijdbtplugin.services
import com.github.rinchinov.ijdbtplugin.extentions.FocusLogsTabAction
import com.google.gson.Gson
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.InputStreamReader
import com.intellij.notification.NotificationType


@Service(Service.Level.PROJECT)
class Executor(private val project: Project){
    private val settings = project.service<ProjectSettings>()
    private val projectConfigurations = project.service<ProjectConfigurations>()
    private val dbtNotifications = project.service<Notifications>()
    private val eventLoggerManager = project.service<EventLoggerManager>()

    private fun waitProcess(process: Process): String{
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = reader.readText()

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
        }
        else {
            if (logs.size > 1){
                eventLoggerManager.logLines(logs.dropLast(1), "dbt")
            }
        }
        if (logs.isEmpty()){
            return output.trim()
        }
        return logs.last().trim()
    }
    fun executeDbt(args: List<String>, kwargs: Map<String, String>): String {
        // Access the Python script as a resource
        val url = this::class.java.classLoader.getResource("python/cli.py")
        val scriptTemplate = url?.readText() ?: throw IllegalArgumentException("Script not found")
        val pythonSdkPath = settings.getDbtInterpreterPath()
        val script = scriptTemplate.replace("RUNNER_IMPORT", settings.getDbtRunnerImport())
        val gson = Gson()
        val processBuilder = ProcessBuilder(
            pythonSdkPath,
            "-c",
            script,
            gson.toJson(args),
            gson.toJson(kwargs),
        )
        processBuilder.directory(
            projectConfigurations.dbtProjectPath().absoluteDir.toFile()
        )
        return waitProcess(processBuilder.start())
    }
    fun getDbtPythonPackageLocation(): String {
        val pythonSdkPath = settings.getDbtInterpreterPath()
        val process = ProcessBuilder(
            pythonSdkPath,
            "-c",
            "import os,dbt;print(os.path.dirname(dbt.__file__))"
        ).start()
        return waitProcess(process)
    }
}
