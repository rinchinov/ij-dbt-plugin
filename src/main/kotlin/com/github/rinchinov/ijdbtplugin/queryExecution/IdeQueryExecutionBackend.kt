package com.github.rinchinov.ijdbtplugin.queryExecution

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.intellij.database.console.JdbcConsole
import com.intellij.database.console.JdbcConsoleProvider
import com.intellij.database.console.session.DatabaseSessionManager
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.run.ConsoleDataRequest
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightVirtualFile
import javax.swing.SwingUtilities


@Service(Service.Level.PROJECT)
class IdeQueryExecutionBackend(private val project: Project) {
    private val manifestService = project.service<ManifestService>()
    private val virtualFiles: MutableMap<String, LightVirtualFile> = mutableMapOf()
    private val dbtNotifications = project.service<Notifications>()

    fun runQuery(editor: Editor, target: String) {
        SwingUtilities.invokeLater {
            val query = manifestService.getWithReplacingRefsAndSources(editor, target)
            val virtualFile = getVirtualFile(query, target)
            val console = getJdbcConsole(virtualFile, target)
            val consoleDataRequest = console?.let {
                ConsoleDataRequest.newConsoleRequest(
                    it,
                    editor, console.scriptModel, false)
            }
            if (consoleDataRequest == null){
                dbtNotifications.sendNotification(
                    "Failed preparations for query",
                    "Please, check connection settings",
                    NotificationType.ERROR
                )
            }
            else {
                console.getMessageBus().dataProducer.processRequest(consoleDataRequest)
            }
        }
    }

    private fun getVirtualFile(query: String, target: String): LightVirtualFile {
        if (!virtualFiles.containsKey(target)){
            virtualFiles[target] = LightVirtualFile(target, PlainTextFileType.INSTANCE, query)
        }
        val documentManager = FileDocumentManager.getInstance()
        val virtualFile = virtualFiles[target]
        val document = documentManager.getDocument(virtualFile as VirtualFile)
        document?.let { doc ->
            ApplicationManager.getApplication().runWriteAction {
                doc.setText(query)
            }
        }
        return virtualFile
    }

    private fun getJdbcConsole(virtualFile: LightVirtualFile, target: String): JdbcConsole? {
        val dataSource = LocalDataSourceManager.getInstance(project).dataSources
            .find { it.name == "${manifestService.defaultProjectName()}__$target" }
        if (dataSource == null) {
            dbtNotifications.sendNotification(
                "Can't find datasource",
                "Please create datasource with name `${manifestService.defaultProjectName()}__$target`",
                NotificationType.ERROR
            )
            return null
        }
        val databaseSession = DatabaseSessionManager.getSessions(project)
            .find { it.title == target && it.project == project }
            ?: dataSource.let { DatabaseSessionManager.openSession(project, it, target) }
        val existingJdbcConsole =
            databaseSession.clientsWithFile.firstOrNull { it.virtualFile.name == virtualFile.name } as? JdbcConsole
        val console = existingJdbcConsole ?: JdbcConsoleProvider.getValidConsole(project, virtualFile)
        ?: virtualFile.let { JdbcConsoleProvider.attachConsole(project, databaseSession, it) }
        if (console == null) {
            dbtNotifications.sendNotification(
                "Can't open console",
                "Please, check connection settings",
                NotificationType.ERROR
            )
        }
        return console
    }
}