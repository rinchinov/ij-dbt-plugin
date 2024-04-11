package com.github.rinchinov.ijdbtplugin.services

import com.github.rinchinov.ijdbtplugin.extensions.MainToolWindowService
import com.intellij.notification.NotificationType
import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class Notifications(private val project: Project){
    private val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("DBT Notification Group")
    private val mainToolService = project.service<MainToolWindowService>()
    fun sendNotification(title: String, content: String, notificationType: NotificationType) {
        sendNotification(title, content, notificationType, null)
    }
    fun sendNotification(title: String, content: String, notificationType: NotificationType, goToTab: MainToolWindowService.Tab?) {
        val notification = notificationGroup
            .createNotification(
                title,
                content,
                notificationType
            )
        if (goToTab != null){
            val action= mainToolService.activateTabNotificationAction(goToTab)
            notification.addAction(action).notify(project)
        }
        else {
            notification.notify(project)
        }
    }
}