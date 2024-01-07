package com.github.rinchinov.ijdbtplugin.extentions

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.rinchinov.ijdbtplugin.services.Executor
import javax.swing.JButton


class ToolMainWindow : ToolWindowFactory {
    override fun shouldBeAvailable(project: Project) = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val projectInfo = ProjectInfo(toolWindow)
        val debug = Debug(toolWindow)
        toolWindow.contentManager.addContent(
            contentFactory.createContent(projectInfo.getContent(), "Project Information", false)
        )
        toolWindow.contentManager.addContent(
            contentFactory.createContent(debug.getContent(), "Debug", false)
        )
    }

    class ProjectInfo(toolWindow: ToolWindow) {

//        private val executor = toolWindow.project.service<Executor>()
        fun getContent() = JBPanel<JBPanel<*>>().apply {
        }
    }
    class Debug(toolWindow: ToolWindow) {

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
