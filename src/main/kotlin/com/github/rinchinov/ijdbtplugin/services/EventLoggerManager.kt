package com.github.rinchinov.ijdbtplugin.services

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.LoggingInterface
import com.github.rinchinov.ijdbtplugin.ProjectInfoChangeListenerInterface
import com.intellij.openapi.components.Service


@Service(Service.Level.PROJECT)
class EventLoggerManager {
    private val listeners = mutableListOf<ProjectInfoChangeListenerInterface>()
    private val loggers = mutableListOf<LoggingInterface>()
    fun addLogger(logger: LoggingInterface) {
        loggers.add(logger)
    }
    fun logLines(lines: Iterable<String>, logType: String) {
        lines.forEach{ logLine(it, logType) }
    }
    fun logLine(line: String, logType: String){
        loggers.forEach { it.appendLog(line, logType) }
    }
    fun addDataChangeListener(listener: ProjectInfoChangeListenerInterface) {
        listeners.add(listener)
    }
    fun notifyProjectConfigurationsChangeListeners(configurations: ProjectConfigurations) {
        listeners.forEach { it.onProjectConfigurationsChanged(configurations) }
    }
    fun notifyManifestChangeListeners(manifestService: ManifestService) {
        listeners.forEach { it.onManifestChanged(manifestService) }
    }
}