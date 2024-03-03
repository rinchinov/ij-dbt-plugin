package com.github.rinchinov.ijdbtplugin.extensions

import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory


class ToolMainWindow : ToolWindowFactory {
    override fun shouldBeAvailable(project: Project) = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val projectInfo = ProjectInformationPanel(toolWindow)
        val docs = DocumentationViewPanel(toolWindow)
        val loggingPanel = LoggingPanel()
        project.service<EventLoggerManager>().addLogger(loggingPanel)
        toolWindow.contentManager.addContent(
            contentFactory.createContent(projectInfo.getContent(), "Project Information", false)
        )
        toolWindow.contentManager.addContent(
            contentFactory.createContent(docs.getContent(), "Documentation", false)
        )
        toolWindow.contentManager.addContent(
            contentFactory.createContent(loggingPanel, "Logs", false)
        )
    }
}
