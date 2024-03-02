package com.github.rinchinov.ijdbtplugin.extentions;

import com.github.rinchinov.ijdbtplugin.ProjectInfoChangeListenerInterface
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowser
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.servlet.DefaultServlet
import java.nio.file.Path
import java.nio.file.Paths
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindow
import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import kotlinx.coroutines.runBlocking
import javax.swing.*
import java.awt.BorderLayout


class DocumentationViewPanel(private val toolWindow: ToolWindow): ProjectInfoChangeListenerInterface {
    private val eventLoggerManager = toolWindow.project.service<EventLoggerManager>()
    private val server = Server(0)
    private var actualPort: Int = 0
    private var jbCefBrowser = JBCefBrowser("http://localhost:$actualPort")
    init {
        eventLoggerManager.addDataChangeListener(this)
    }
    fun getContent(): JComponent = JPanel(BorderLayout()) .apply {
        val docsPanel = JPanel(BorderLayout())
        if (!JBCefApp.isSupported()) return JLabel("")
        jbCefBrowser.cefBrowser.createImmediately()
        val zoomLevel = -1.0 // Approximates to 50% zoom
        jbCefBrowser.cefBrowser.zoomLevel = zoomLevel
        docsPanel.add(jbCefBrowser.component, BorderLayout.CENTER)
        add(docsPanel)
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
        val targetPath = Paths.get("target")
        try {
            stopServeDocs()
        }
        finally {
            startServeDocs(targetPath)
            jbCefBrowser.loadURL("http://localhost:$actualPort")
        }
    }
}