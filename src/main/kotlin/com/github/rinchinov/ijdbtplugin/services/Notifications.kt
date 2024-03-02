package com.github.rinchinov.ijdbtplugin.services

import com.intellij.notification.NotificationType
import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class Notifications(private val project: Project){
    private val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("DBT Notification Group")
    fun sendNotification(title: String, content: String, notificationType: NotificationType) {
        sendNotification(title, content, notificationType, null)
    }
    fun sendNotification(title: String, content: String, notificationType: NotificationType, action: AnAction?) {
        val notification = notificationGroup
            .createNotification(
                title,
                content,
                notificationType
            )
        if (action != null){
            notification.addAction(action).notify(project)
        }
        else {
            notification.notify(project)
        }
    }
}