package com.github.rinchinov.ijdbtplugin.actions


import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.ProjectSettings
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile


class DbtCopyPasteActionGroup : ActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {

        if ( e == null || e.project == null ){
            return EMPTY_ARRAY
        }
        val project = e.project!!
        val targets = project.service<ProjectSettings>().getDbtTargetList()
        val manifestService = project.service<ManifestService>()
        val actions: Array<AnAction> = targets.map { target ->
            listOf(
                object : AnAction("Copy for $target") {
                    override fun actionPerformed(e: AnActionEvent) = manifestService.copyWithReplacingRefsAndSources(e, target)
                },
                object : AnAction("Paste as $target") {
                    override fun actionPerformed(e: AnActionEvent) = manifestService.pasteWithReplacedRefsAndSources(e, target)
                }
            )
        }.flatten().toTypedArray()
        return actions
    }

    override fun update(e: AnActionEvent) {
        val virtualFile: VirtualFile? = FileDocumentManager.getInstance().getFile(e.getData(CommonDataKeys.EDITOR)?.document!!)
        val isSqlFile = virtualFile?.extension?.equals("sql", ignoreCase = true) ?: false
        e.presentation.isEnabledAndVisible = isSqlFile
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}