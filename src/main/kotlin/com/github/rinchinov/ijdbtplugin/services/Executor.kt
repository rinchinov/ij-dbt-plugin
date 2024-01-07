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
    private val pathsUtils = project.service<PathsUtils>()
    private val dbtNotifications = project.service<Notifications>()
    fun executeDbt(args: List<String>, kwargs: Map<String, String>) {
        // Access the Python script as a resource
        val url = this::class.java.classLoader.getResource("python/cli.py")
        val scriptTemplate = url?.readText() ?: throw IllegalArgumentException("Script not found")
        val pythonSdkPath = pathsUtils.sdkPath().absolutePath.toString()
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
            pathsUtils.dbtProjectPath().absoluteDir.toFile()
        )

        val process = processBuilder.start()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = reader.readText()

        // Wait for the process to complete
        val exitCode = process.waitFor()

        // Handle the output and exit code
        if (exitCode == 0) {
            dbtNotifications.sendNotification("Process finished successfully.", "Output: $output", NotificationType.INFORMATION)
        } else {
            val logUrl = "<a href='file://${pathsUtils.logPath().absolutePath}'>Open log</a>."
            dbtNotifications.sendNotification("Process finished with exit code $exitCode", "$logUrl Output: $output", NotificationType.ERROR)
        }
    }
}
