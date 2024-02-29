package com.github.rinchinov.ijdbtplugin.services
import com.google.gson.Gson
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.InputStreamReader
import com.intellij.notification.NotificationType


@Service(Service.Level.PROJECT)
class Executor(project: Project){
    private val settings = project.service<ProjectSettings>()
    private val projectConfigurations = project.service<ProjectConfigurations>()
    private val dbtNotifications = project.service<Notifications>()

    private fun waitProcess(process: Process): String{
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = reader.readText()

        // Wait for the process to complete
        val exitCode = process.waitFor()

        // Handle the output and exit code
        if (exitCode != 0) {
            val logUrl = "<a href='file://${projectConfigurations.logPath().absolutePath}'>Open log</a>."
            dbtNotifications.sendNotification("Process finished with exit code $exitCode", "$logUrl Output: $output", NotificationType.ERROR)
        }
        return output
    }
    fun executeDbt(args: List<String>, kwargs: Map<String, String>) {
        // Access the Python script as a resource
        val url = this::class.java.classLoader.getResource("python/cli.py")
        val scriptTemplate = url?.readText() ?: throw IllegalArgumentException("Script not found")
        val pythonSdkPath = projectConfigurations.sdkPath().absolutePath.toString()
        val script = scriptTemplate.replace("RUNNER_IMPORT", settings.getDbtRunnerImport())
        val gson = Gson()
        val processBuilder = ProcessBuilder(
                pythonSdkPath,
                "-c",
                script,
                gson.toJson(args),
                gson.toJson(kwargs)
        )
        processBuilder.directory(
            projectConfigurations.dbtProjectPath().absoluteDir.toFile()
        )
        waitProcess(processBuilder.start())
    }
    fun getDbtPythonPackageLocation(): String {
        val pythonSdkPath = projectConfigurations.sdkPath().absolutePath.toString()
        val process = ProcessBuilder(
            pythonSdkPath,
            "-c",
            "import os, dbt;print(os.path.dirname(dbt.__file__))"
        ).start()
        return waitProcess(process).trim()
    }
}
