package com.github.rinchinov.ijdbtplugin.extensions

import com.github.rinchinov.ijdbtplugin.ProjectInfoChangeListenerInterface
import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import kotlinx.coroutines.runBlocking
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.SwingUtilities


class ProjectInformationPanel(private val toolWindow: ToolWindow): ProjectInfoChangeListenerInterface {
    private val eventLoggerManager = toolWindow.project.service<EventLoggerManager>()
    private var options = NonEditableTableModel().apply {
        addColumn("")
        addColumn("")
        addRow("projectDetails", "DBT Project details:", "")
        addRow("dbtProjectName", "DBT project name:", "")
        addRow("dbtProjectFile", "DBT project file:", "")
        addRow("dbtAdapter", "DBT adapter:", "")
        addRow("manifestFile", "Manifest file:", "")
        addRow("targetsList", "Targets list:", "")
        addRow("defaultTarget", "Default target:", "")
        addRow("pythonSdk", "Python SDK:", "")
        addRow("parsedManifestDetails", "Parsed Manifest's details", "")
        addRow("status", "Status:", "")
        addRow("lastUpdateTime", "Last update time", "")
        addRow("totalNodesCount", "Total nodes count", "")
        addRow("totalSourcesCount", "Total sources count", "")
        addRow("totalMacrosCount", "Total macros count", "")
    }

    init {
        eventLoggerManager.addDataChangeListener(this)
    }
    fun getContent() = JPanel(BorderLayout()) .apply {
        val headerLabel = JLabel("")
        headerLabel.setHorizontalAlignment(SwingConstants.LEFT)
        add(headerLabel, BorderLayout.NORTH)
        val scrollPane = JBScrollPane(
            JBTable(options).apply {
                fillsViewportHeight = true
                tableHeader = null
            }
        )
        add(scrollPane)
    }
    override fun onManifestChanged(manifest: ManifestService)  = runBlocking {
        SwingUtilities.invokeLater {
            options.setValue("status", manifest.getStatus())
            options.setValue("lastUpdateTime", manifest.lastUpdated().toString())
            options.setValue("totalNodesCount", manifest.getNodesCount().toString())
            options.setValue("totalSourcesCount", manifest.getSourcesCount().toString())
            options.setValue("totalMacrosCount", manifest.getMacrosCount().toString())
        }
    }

    override fun onProjectConfigurationsChanged(configurations: ProjectConfigurations) = runBlocking {
        SwingUtilities.invokeLater {
            options.setValue("dbtProjectFile", configurations.settings.getDbtProjectPath())
            options.setValue("pythonSdk", configurations.settings.getDbtInterpreterPath())
            options.setValue("targetsList", configurations.settings.getDbtTargetList().joinToString(separator = ","))
            options.setValue("defaultTarget", configurations.settings.getDbtDefaultTarget())
            options.setValue("dbtAdapter", configurations.settings.getDbtAdapter())
        }
    }
}
