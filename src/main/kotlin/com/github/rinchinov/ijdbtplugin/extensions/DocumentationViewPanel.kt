package com.github.rinchinov.ijdbtplugin.extensions;

import com.github.rinchinov.ijdbtplugin.ProjectInfoChangeListenerInterface
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowser
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.servlet.DefaultServlet
import java.nio.file.Paths
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindow
import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.github.rinchinov.ijdbtplugin.services.Executor
import com.intellij.openapi.ui.ComboBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.swing.*
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.nio.file.Files


class DocumentationViewPanel(private val toolWindow: ToolWindow): ProjectInfoChangeListenerInterface {
    private val eventLoggerManager = toolWindow.project.service<EventLoggerManager>()
    private val executor = toolWindow.project.service<Executor>()
    private val configurations = toolWindow.project.service<ProjectConfigurations>()
    private val server = Server(0)
    private var actualPort: Int = 0
    private var jbCefBrowser = JBCefBrowser()
    private var comboBox: JComboBox<String> = ComboBox<String>()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        eventLoggerManager.addDataChangeListener(this)
        startServeDocs(configurations.settings.getDbtDefaultTarget())
    }
    fun getContent(): JComponent {
        val mainPanel = JPanel(BorderLayout())

        if (!JBCefApp.isSupported()) {
            return JLabel("JCEF is not supported on this platform")
        }

        // Panel that contains the browser
        val docsPanel = JPanel(BorderLayout()).apply {
            add(jbCefBrowser.component, BorderLayout.CENTER)
        }

        // Panel for the button and dropdown
        val controlsPanel = JPanel().apply {
            layout = FlowLayout(FlowLayout.LEFT)

            val documentation = JButton("Generate Documentation").apply {
                addActionListener {
                    comboBox.selectedItem?.let {
                        coroutineScope.launch {
                            executor.dbtDocsGenerate(it.toString())
                            restartServeDocs()
                        }
                    }
                }
            }
            add(documentation)
            // Add a button
            val reload = JButton("Reload").apply {
                addActionListener {
                    coroutineScope.launch {
                    restartServeDocs()
                        }
                }
            }
            add(reload)

            comboBox = JComboBox<String>(configurations.settings.getDbtTargetList().toTypedArray()).apply {
                addItemListener {
                    coroutineScope.launch {
                        restartServeDocs()
                    }
                }
            }
            add(comboBox)
        }

        mainPanel.add(controlsPanel, BorderLayout.NORTH)
        mainPanel.add(docsPanel, BorderLayout.CENTER)

        return mainPanel
    }

    private fun startServeDocs(target: String) {
        val context = ServletContextHandler(ServletContextHandler.SESSIONS).apply {
            contextPath = "/"
            resourceBase = configurations.getDbtCachePath(target).toString()
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
    private fun restartServeDocs() {
        try {
            server.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        comboBox.selectedItem?.let {
            startServeDocs(it.toString())
            loadDocs(it.toString())
        }
    }

    private fun loadDocs(target: String){
        if (Files.exists(Paths.get(configurations.getDbtCachePath(target).toString(), "index.html"))) {
            jbCefBrowser.loadURL("http://localhost:$actualPort/index.html")
        }
        else {
            jbCefBrowser.zoomLevel = 1.0
            jbCefBrowser.loadURL("")
        }
    }
    override fun onManifestChanged(manifest: ManifestService) = restartServeDocs()
    override fun onProjectConfigurationsChanged(configurations: ProjectConfigurations) = restartServeDocs()
}