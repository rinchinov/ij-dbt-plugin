package com.github.rinchinov.ijdbtplugin.queryExecution

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers.BigQueryQueryExecutionManager
import com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers.PostgresQueryExecutionManager
import com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers.QueryExecutionDbtManager
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project


@Service(Service.Level.PROJECT)
@State(
    name = "QueryExecutionStorage",
    storages = [Storage("dbtQueryExecution.xml")]
)
class QueryExecutionBackend(private val project: Project): PersistentStateComponent<QueryExecutionBackend.State> {
    enum class QueryStatus {
        RUNNING, FAILED, SUCCESS
    }
    data class QueryExecution(
        val queryExecutionManager: QueryExecutionManagerInterface,
        var query: String,
        val target: String,
        var executionId: String,
        val rowsPerPage: Int = 15,
        var totalPages: Int = 1,
        var pages: MutableMap<Int, List<List<String>>> = mutableMapOf(),
        var status: QueryStatus = QueryStatus.RUNNING
    )
    class State {
        var executions: MutableList<QueryExecution> = mutableListOf()
    }

    companion object {
        const val MAX_EXECUTIONS = 20
    }

    private var myState = State()

    private var queryExecutionManager: QueryExecutionManagerInterface = when (project.service<ProjectConfigurations>().dbtProjectConfig.adapterName) {
//        "bigquery" -> {
//            BigQueryQueryExecutionManager(project)
//        }
//        "postgres" -> {
//            PostgresQueryExecutionManager(project)
//        }
        else -> QueryExecutionDbtManager(project)
    }
    private val listeners = mutableListOf<QueryChangeListener>()
    fun addQueryChangeListener(listener: QueryChangeListener) {
        listeners.add(listener)
    }
    override fun getState(): State = myState
    override fun loadState(state: State) {
        myState = state
    }
    fun pushExecution(execution: QueryExecution) {
        with(myState.executions) {
            add(execution)
            if (size > MAX_EXECUTIONS) removeAt(0) // Keep only the last N executions
            listeners.forEach { it.onQueryAdd(execution, MAX_EXECUTIONS) }
        }
    }
    private fun getQuery(e: AnActionEvent, target: String): String {
        val manifestService = project.service<ManifestService>()
        return manifestService.getWithReplacingRefsAndSources(e, target)
    }
    fun runQuery(e: AnActionEvent, target: String, type: String) {
        val query = getQuery(e, target)
        when (type) {
            "runQuery" -> queryExecutionManager.runQuery(query, target, this)
            "runQueryPlan" -> queryExecutionManager.runQuery("plan", query, target, this)
            "dryRunQuery" -> queryExecutionManager.runQuery("dry", query, target,this)
        }
    }
    fun updateExecutionResults(executionId: String, status: QueryStatus, results: List<List<String>>, pageNumber: Int) {
        val execution = getExecutionById(executionId)
        if (execution != null){
            execution.pages[pageNumber] = results
            execution.status = status
            listeners.forEach { it.displayExecutedQuery(execution, pageNumber) }
        }
    }
    fun getExecutions(): List<QueryExecution> = myState.executions
    fun clearExecutions() {
        myState.executions.clear()
    }
    fun getExecutionById(executionId: String): QueryExecution? =
        myState.executions.find { it.executionId == executionId }

    fun getExecutionResultPageNumber(queryExecution: QueryExecution, pageNumber: Int): List<List<String>>? {
        return queryExecution.pages[pageNumber] ?: queryExecution.queryExecutionManager.getResultsWithPagination(
            queryExecution,
            pageNumber,
            this
        )
    }
}
