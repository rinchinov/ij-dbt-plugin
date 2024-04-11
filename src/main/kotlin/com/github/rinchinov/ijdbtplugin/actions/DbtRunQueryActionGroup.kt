package com.github.rinchinov.ijdbtplugin.actions


import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile


class DbtRunQueryActionGroup : ActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        if ( e == null || e.project == null ){
            return EMPTY_ARRAY
        }
        val project = e.project!!
        val targets = project.service<ProjectConfigurations>().dbtProjectConfig.targets
        val queryExecutionBackend = project.service<QueryExecutionBackend>()
        val actions: Array<AnAction> = targets.map { target ->
            listOf(
                object : AnAction("Run query for $target") {
                    override fun actionPerformed(e: AnActionEvent) = queryExecutionBackend.runQuery(e, target, QueryExecutionBackend.QueryTypes.PAGINATED)
                },
                object : AnAction("Get query plan for $target") {
                    override fun actionPerformed(e: AnActionEvent) = queryExecutionBackend.runQuery(e, target, QueryExecutionBackend.QueryTypes.PLAN)
                },
                object : AnAction("Dry run query for $target") {
                    override fun actionPerformed(e: AnActionEvent) = queryExecutionBackend.runQuery(e, target, QueryExecutionBackend.QueryTypes.DRY)
                },
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