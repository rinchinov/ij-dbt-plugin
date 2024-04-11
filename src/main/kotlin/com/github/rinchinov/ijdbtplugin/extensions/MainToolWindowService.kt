package com.github.rinchinov.ijdbtplugin.extensions

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager

@Service(Service.Level.PROJECT)
class MainToolWindowService(project: Project){
    private val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("DBT")
    enum class Tab {
        QUERY_RUN, PROJECT_INFORMATION, DOCUMENTATION, LOGS;
        override fun toString(): String {
            return when(this) {
                QUERY_RUN -> "Query Run Results"
                PROJECT_INFORMATION -> "Project Information"
                DOCUMENTATION -> "Documentation"
                LOGS -> "Logs"
            }
        }
    }

    private fun activateWindow(){
        toolWindow?.show(null)
        toolWindow?.activate(null)
    }

    fun activateTab(tab: Tab){
        toolWindow?.contentManager?.let { contentManager ->
            activateWindow()
            for (content in contentManager.contents) {
                if (content.displayName == tab.toString()) {
                    contentManager.setSelectedContent(content)
                    break
                }
            }
        }
    }

    fun activateTabNotificationAction(tab: Tab): NotificationAction {
        return object: NotificationAction("Go to $tab"){
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                activateTab(tab)
                notification.expire()
            }
        }
    }
}
