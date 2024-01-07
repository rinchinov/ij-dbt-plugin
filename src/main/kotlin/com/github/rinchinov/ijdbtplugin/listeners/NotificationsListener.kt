package com.github.rinchinov.ijdbtplugin.listeners
import com.intellij.notification.Notification
import com.intellij.notification.NotificationListener
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFileManager
import javax.swing.event.HyperlinkEvent

@Service(Service.Level.PROJECT)
class NotificationsListener(private val project: Project) : NotificationListener {

    override fun hyperlinkUpdate(notification: Notification, event: HyperlinkEvent) {
        if (event.eventType == HyperlinkEvent.EventType.ACTIVATED) {
            // Assuming the description contains a URL in the format file://<file_path>
            val filePath = event.description.substring("file://".length)
            val file = VirtualFileManager.getInstance().findFileByUrl("file://$filePath")

            file?.let {
                FileEditorManager.getInstance(project).openFile(it, true)
            }

        }
    }
}
