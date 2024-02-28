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
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import kotlinx.coroutines.runBlocking
import javax.swing.*
import java.awt.BorderLayout
import javax.swing.table.DefaultTableModel
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowser
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.servlet.DefaultServlet
import java.nio.file.Path


class NonEditableTableModel : DefaultTableModel() {
    private var keys: MutableMap<String, Int> = mutableMapOf()
    private var index: Int = 0
    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }
    fun addRow(id: String, name: String, value: String) {
        super.addRow(arrayOf(name, value))
        keys[id] = index
        index += 1
    }
    fun setValue(id: String, value: String) {
        keys[id]?.let { setValueAt(value, it, 1) }
    }
}


class ToolMainWindow : ToolWindowFactory {
    override fun shouldBeAvailable(project: Project) = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val projectInfo = ProjectInfo(toolWindow)
        val docs = Docs(toolWindow)
        toolWindow.contentManager.addContent(
            contentFactory.createContent(projectInfo.getContent(), "Project Information", false)
        )
        toolWindow.contentManager.addContent(
            contentFactory.createContent(docs.getContent(), "Docs", false)
        )
    }

    class ProjectInfo(private val toolWindow: ToolWindow): MyDataChangeListener {
        private val updater = toolWindow.project.service<ToolWindowUpdater>()
        private var options = NonEditableTableModel().apply {
            addColumn("")
            addColumn("")
            addRow("projectDetails", "DBT Project details:", "")
            addRow("dbtProjectName", "DBT project name:", "")
            addRow("dbtProjectFile", "DBT project file:", "")
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
                options.setValue("dbtProjectName", configurations.dbtProjectConfig.name)
                options.setValue("dbtProjectFile", configurations.dbtProjectPath().relativePath.toString())
                options.setValue("manifestFile", configurations.manifestPath().relativePath.toString())
                options.setValue("pythonSdk", configurations.sdkPath().relativePath.toString())
                options.setValue("targetsList", configurations.targetList().joinToString(separator = ","))
                options.setValue("defaultTarget", configurations.defaultTarget())
            }
        }
    }

    class Docs(private val toolWindow: ToolWindow): MyDataChangeListener{
        private val updater = toolWindow.project.service<ToolWindowUpdater>()
        private val server = Server(0)
        private var actualPort: Int = 0
        private var jbCefBrowser = JBCefBrowser("http://localhost:$actualPort")
        init {
            updater.addDataChangeListener(this)
        }
        fun getContent(): JComponent = JPanel(BorderLayout()) .apply {
            val docsPanel = JPanel(BorderLayout())
            if (!JBCefApp.isSupported()) return JLabel("")
            jbCefBrowser.cefBrowser.createImmediately()
            val zoomLevel = -1.0 // Approximates to 50% zoom
            jbCefBrowser.cefBrowser.zoomLevel = zoomLevel
            docsPanel.add(jbCefBrowser.component, BorderLayout.CENTER)
            add(docsPanel)
            onManifestChanged(
                toolWindow.project.service<ManifestService>()
            )
            onProjectConfigurationsChanged(
                toolWindow.project.service<ProjectConfigurations>()
            )
        }

        private fun startServeDocs(path: Path) {
            val context = ServletContextHandler(ServletContextHandler.SESSIONS).apply {
                contextPath = "/"
                resourceBase = path.toString()
                addServlet(ServletHolder(DefaultServlet::class.java), "/")
            }
            server.setHandler(context)
            try {
                server.start()
                actualPort = server.uri.port // Retrieve the actual port the server has bound to
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        private fun stopServeDocs() {
            try {
                server.stop()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        override fun onManifestChanged(manifest: ManifestService)  = runBlocking {
            jbCefBrowser.loadURL("http://localhost:$actualPort")
        }

        override fun onProjectConfigurationsChanged(configurations: ProjectConfigurations) = runBlocking {
            val targetPath = configurations.targetPath().absolutePath
            try {
                stopServeDocs()
            }
            finally {
                startServeDocs(targetPath)
                jbCefBrowser.loadURL("http://localhost:$actualPort")
            }
        }
    }
}
