package com.github.rinchinov.ijdbtplugin

import com.github.rinchinov.ijdbtplugin.artifactsVersions.Macro
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Node
import com.github.rinchinov.ijdbtplugin.artifactsVersions.SourceDefinition
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.*
import com.jetbrains.jinja2.template.psi.impl.Jinja2MemberNameImpl
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import javax.swing.SwingUtilities


interface DbtCoreInterface {

    fun findNode(packageName: String?, uniqueId: String, currentVersion: Int?, target: String?): Node?
    fun findSourceDefinition(uniqueId: String, target: String?): SourceDefinition?
    fun findMacro(packageName: String?, macroName: String, target: String?): Macro?
    fun getPackageDir(packageName: String?): String
    fun modelReferenceFileByElement(packageName: String?, uniqueId: String, currentVersion: Int?, target: String?): String{
        val node = findNode(packageName, uniqueId, currentVersion, target)
        return if (node != null){
            getPackageDir(node.packageName) + "/" + node.originalFilePath
        } else {
            ""
        }
    }
    fun sourceReferenceFileByElement(uniqueId: String, target: String?): String{
        val source = findSourceDefinition(uniqueId, target)
        return if (source != null){
            getPackageDir(source.packageName) + "/" + source.originalFilePath
        } else {
            ""
        }
    }
    fun macroReferenceFileByElement(packageName: String, macroName: String, target: String?): String{
        val macro = findMacro(packageName, macroName, target)
        return if (macro != null){
            getPackageDir(macro.packageName) + "/" + macro.originalFilePath
        } else {
            ""
        }
    }

    fun macroReference(element: PsiElement): PsiReferenceBase<PsiElement> {
        return object : PsiReferenceBase<PsiElement>(element) {
            override fun resolve(): PsiElement? {
                val packageName = if (element is Jinja2MemberNameImpl) element.prevSibling.prevSibling.text else ""
                val macroName = element.text
                val path: String = macroReferenceFileByElement(packageName, macroName, null)
                val virtualFile = LocalFileSystem.getInstance().findFileByPath(
                    path
                ) ?: return null
                return PsiManager.getInstance(element.project).findFile(virtualFile)
            }
        }
    }

    fun refOrSourceReference(element: PsiElement): PsiReferenceBase<PsiElement> {
        return object : PsiReferenceBase<PsiElement>(element) {
            override fun resolve(): PsiElement? {
                val path: String = when {
                    element.parent.text.startsWith("ref(") -> {
                        val modelVersionRegex = """(version|v)=["']?(\d+)["']?""".toRegex()
                        val modelProjectRegex = """ref\((['"]([^'"]*)['"],\s*)?["']([^"']*)['"]""".toRegex()
                        val modelMatch = modelProjectRegex.find(element.parent.text)?.groups
                        val packageName = modelMatch?.get(2)?.value
                        val uniqueId = modelMatch?.get(3)?.value
                        val currentVersion = modelVersionRegex.find(element.parent.text)?.groupValues?.get(2)?.toInt()
                        if (uniqueId != null) {
                            modelReferenceFileByElement(packageName, uniqueId, currentVersion, null)
                        }
                        else {
                            ""
                        }
                    }
                    element.parent.text.startsWith("source(") -> {
                        val uniqueId = """source\(['"]([^'"]*)['"],\s*["']([^"']*)['"]\)""".toRegex().replace(element.parent.text,
                            fun(matchResult: MatchResult): CharSequence {
                                return "${matchResult.groupValues[1]}.${matchResult.groupValues[2]}"
                            })
                        sourceReferenceFileByElement(uniqueId, null)
                    }
                    else -> ""
                }
                val virtualFile = LocalFileSystem.getInstance().findFileByPath(
                    path
                ) ?: return null
                return PsiManager.getInstance(element.project).findFile(virtualFile)
            }
        }
    }

    fun replaceRefsAndSourcesFromJinja2(query: String, target: String): String
    fun replaceRefsAndSourcesToJinja2(query: String, target: String): String
    fun copyWithReplacingRefsAndSources(e: AnActionEvent, target: String){
        SwingUtilities.invokeLater {
            val editor: Editor = e.getRequiredData(CommonDataKeys.EDITOR)
            val document = editor.document
            val selectionModel = editor.selectionModel
            val selectedText = selectionModel.selectedText?: document.text
            val replacedContent = replaceRefsAndSourcesFromJinja2(selectedText, target)
            val copyPasteManager = CopyPasteManager.getInstance()
            copyPasteManager.setContents(StringSelection(replacedContent))
        }
    }
    fun pasteWithReplacedRefsAndSources(e: AnActionEvent, target: String){
        SwingUtilities.invokeLater {
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