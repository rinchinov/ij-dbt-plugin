package com.github.rinchinov.ijdbtplugin.actions


import com.github.rinchinov.ijdbtplugin.queryExecution.IdeQueryExecutionBackend
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
        val nativeQueryExecutionBackend = project.service<IdeQueryExecutionBackend>()
        val actions: Array<AnAction> = targets.map { target ->
            listOf(
                object : AnAction("Run with database tools: $target") {
                    override fun actionPerformed(e: AnActionEvent) = nativeQueryExecutionBackend.runQuery(e.getRequiredData(CommonDataKeys.EDITOR), target, false)
                },
                object : AnAction("Run: $target") {
                    override fun actionPerformed(e: AnActionEvent) = queryExecutionBackend.runQuery(e.getRequiredData(CommonDataKeys.EDITOR), target, QueryExecutionBackend.QueryTypes.PAGINATED, false)
                },
                object : AnAction("Query plan: $target") {
                    override fun actionPerformed(e: AnActionEvent) = queryExecutionBackend.runQuery(e.getRequiredData(CommonDataKeys.EDITOR), target, QueryExecutionBackend.QueryTypes.PLAN, false)
                },
                object : AnAction("Dry run: $target") {
                    override fun actionPerformed(e: AnActionEvent) = queryExecutionBackend.runQuery(e.getRequiredData(CommonDataKeys.EDITOR), target, QueryExecutionBackend.QueryTypes.DRY, false)
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