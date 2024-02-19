package com.github.rinchinov.ijdbtplugin.extentions

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.rinchinov.ijdbtplugin.services.Executor
import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import javax.swing.JButton
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import javax.swing.*
import java.awt.BorderLayout
import javax.swing.table.DefaultTableModel


class NonEditableTableModel : DefaultTableModel() {
    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }
}

class ToolMainWindow : ToolWindowFactory {
    override fun shouldBeAvailable(project: Project) = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val projectInfo = ProjectInfo(toolWindow)
        val dag = DAG(toolWindow)
        toolWindow.contentManager.addContent(
            contentFactory.createContent(projectInfo.getContent(), "Project Information", false)
        )
        toolWindow.contentManager.addContent(
            contentFactory.createContent(dag.getContent(), "DAG", false)
        )
    }

    class ProjectInfo(private val toolWindow: ToolWindow): MyDataChangeListener {
        private val updater = toolWindow.project.service<ToolWindowUpdater>()
        private var options = NonEditableTableModel().apply {
            addColumn("")
            addColumn("")
            addRow(arrayOf("DBT Project details:", ""))
            addRow(arrayOf("DBT project name:", ""))
            addRow(arrayOf("DBT project file:", ""))
            addRow(arrayOf("Manifest file:", ""))
            addRow(arrayOf("Python SDK:", ""))
            addRow(arrayOf("Parsed Manifest's details", ""))
            addRow(arrayOf("Status:", ""))
            addRow(arrayOf("Last update time", ""))
            addRow(arrayOf("Total nodes count", ""))
            addRow(arrayOf("Total sources count", ""))
            addRow(arrayOf("Total macros count", ""))
        }
        private val projectFileIndex = 1
        private val projectNameIndex = 2
        private val manifestFileIndex = 3
        private val pythonSdkPathIndex = 4
        private val manifestStatusIndex = 6
        private val lastManifestUpdateIndex = 7
        private val totalNodesCountIndex = 8
        private val totalSourcesCountIndex = 9
        private val totalMacrosCountIndex = 10
        init {
            updater.addDataChangeListener(this)
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
            onManifestChanged(
                toolWindow.project.service<ManifestService>()
            )
            onProjectConfigurationsChanged(
                toolWindow.project.service<ProjectConfigurations>()
            )
        }
        override fun onManifestChanged(manifest: ManifestService) {
            SwingUtilities.invokeLater {
                options.setValueAt(manifest.getStatus(), manifestStatusIndex, 1)
                options.setValueAt(manifest.lastUpdated, lastManifestUpdateIndex, 1)
                options.setValueAt(manifest.getNodesCount(), totalNodesCountIndex, 1)
                options.setValueAt(manifest.getSourcesCount(), totalMacrosCountIndex, 1)
                options.setValueAt(manifest.getMacrosCount(), totalSourcesCountIndex, 1)
            }
        }

        override fun onProjectConfigurationsChanged(configurations: ProjectConfigurations) {
            SwingUtilities.invokeLater {
                options.setValueAt(configurations.dbtProjectConfig.name, projectFileIndex, 1)
                options.setValueAt(configurations.dbtProjectPath().relativePath, projectNameIndex, 1)
                options.setValueAt(configurations.manifestPath().relativePath, manifestFileIndex, 1)
                options.setValueAt(configurations.sdkPath().relativePath, pythonSdkPathIndex, 1)
            }
        }
    }
    class DAG(toolWindow: ToolWindow) {

        private val executor = toolWindow.project.service<Executor>()
        fun getContent() = JBPanel<JBPanel<*>>().apply {
            val label = JBLabel()
            add(label)
            add(JButton("Debug").apply {
                addActionListener {
                    label.text = executor.executeDbt(listOf("run"), mapOf()).toString()
                }
            })
        }
    }
}
