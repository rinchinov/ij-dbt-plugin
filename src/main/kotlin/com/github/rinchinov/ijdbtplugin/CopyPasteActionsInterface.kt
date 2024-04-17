package com.github.rinchinov.ijdbtplugin

import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.github.rinchinov.ijdbtplugin.services.Statistics
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ide.CopyPasteManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

interface CopyPasteActionsInterface {
    val coroutineScope: CoroutineScope
    val statistics: Statistics

    fun replaceRefsAndSourcesFromJinja2(query: String, target: String): String
    fun replaceRefsAndSourcesToJinja2(query: String, target: String): String
    fun getWithReplacingRefsAndSources(e: AnActionEvent, target: String): String {
            var replacedContentResult = ""
            ApplicationManager.getApplication().runReadAction {
                val editor: Editor = e.getRequiredData(CommonDataKeys.EDITOR)
                val document = editor.document
                val selectionModel = editor.selectionModel
                val selectedText = selectionModel.selectedText ?: document.text
                replacedContentResult = replaceRefsAndSourcesFromJinja2(selectedText, target)
            }
            return replacedContentResult
        }
    fun copyWithReplacingRefsAndSources(e: AnActionEvent, target: String){
        coroutineScope.launch {
            val replacedContentResult = getWithReplacingRefsAndSources(e, target)
            val copyPasteManager = CopyPasteManager.getInstance()
            copyPasteManager.setContents(StringSelection(replacedContentResult))
            e.project?.service<Notifications>()?.sendNotification(
                "Copied with replaced refs/sources",
                "",
                NotificationType.INFORMATION
            )
        }
    }
    fun pasteWithReplacedRefsAndSources(e: AnActionEvent, target: String){
        coroutineScope.launch {
            val clipboard = CopyPasteManager.getInstance()
            val clipboardContents = clipboard.contents
            val stringContent = clipboardContents?.getTransferData(DataFlavor.stringFlavor) as? String
            if (!stringContent.isNullOrEmpty()) {
                val replacedQuery = replaceRefsAndSourcesToJinja2(stringContent, target)
                WriteCommandAction.runWriteCommandAction(e.project) {
                    val editor: Editor = e.getRequiredData(CommonDataKeys.EDITOR)
                    val document = editor.document
                    if (editor.selectionModel.hasSelection()) {
                        val start = editor.selectionModel.selectionStart
                        val end = editor.selectionModel.selectionEnd
                        document.replaceString(start, end, replacedQuery)
                    } else {
                        val caretModel = editor.caretModel
                        val offset = caretModel.offset
                        document.insertString(offset, replacedQuery)
                    }
                }
            }
        }
    }
}