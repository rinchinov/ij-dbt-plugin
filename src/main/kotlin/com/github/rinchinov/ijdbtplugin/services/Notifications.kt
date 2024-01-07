package com.github.rinchinov.ijdbtplugin.services

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.github.rinchinov.ijdbtplugin.listeners.NotificationListener

@Service(Service.Level.PROJECT)
class Notifications(private val project: Project){
    private val listener = project.service<NotificationListener>()

    fun sendNotification(title: String, content: String, notificationType: NotificationType) {
        val notification = Notification("DBT Notification Group", title, content, notificationType, listener)
        Notifications.Bus.notify(notification, project)
    }
}