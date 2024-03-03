package com.github.rinchinov.ijdbtplugin.extensions

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager

class FocusLogsTabAction(val project: Project) : NotificationAction("Go to the logs") {
    private val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("DBT")
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        toolWindow?.contentManager?.let { contentManager ->
            for (content in contentManager.contents) {
                if (content.displayName == "Logs") {
                    contentManager.setSelectedContent(content)
                    break
                }
            }
        }
        notification.expire()
    }
}
