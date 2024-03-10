package com.github.rinchinov.ijdbtplugin

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

interface CopyPasteActionsInterface {
    val coroutineScope: CoroutineScope

    fun replaceRefsAndSourcesFromJinja2(query: String, target: String): String
    fun replaceRefsAndSourcesToJinja2(query: String, target: String): String
    fun copyWithReplacingRefsAndSources(e: AnActionEvent, target: String){
        coroutineScope.launch {
            ApplicationManager.getApplication().runReadAction {
                val editor: Editor = e.getRequiredData(CommonDataKeys.EDITOR)
                val document = editor.document
                val selectionModel = editor.selectionModel
                val selectedText = selectionModel.selectedText ?: document.text
                val replacedContent = replaceRefsAndSourcesFromJinja2(selectedText, target)
                val copyPasteManager = CopyPasteManager.getInstance()
                copyPasteManager.setContents(StringSelection(replacedContent))
            }
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