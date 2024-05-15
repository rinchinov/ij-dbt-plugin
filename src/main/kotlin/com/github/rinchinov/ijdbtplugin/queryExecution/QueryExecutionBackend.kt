package com.github.rinchinov.ijdbtplugin.queryExecution

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.extensions.MainToolWindowService
import com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers.*
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.*
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
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
    enum class QueryTypes {
        DRY,
        PLAN,
        PAGINATED
    }
    data class QueryExecution(
        var query: String,
        val target: String,
        var executionId: String,
        val rowsPerPage: Int = 15,
        var totalPages: Int = 1,
        var pages: MutableMap<Int, List<List<String>>> = mutableMapOf(),
        var status: QueryStatus = QueryStatus.RUNNING
    ){
        fun getPage(page: Int): List<List<String>> = pages[page]!!
        override fun toString(): String {
            return "$target: ${query.trim().take(128)}"
        }
    }
    class State {
        var executions: MutableList<QueryExecution> = mutableListOf()
    }
    companion object {
        const val MAX_EXECUTIONS = 20
    }

    private var myState = State()
    private val mainToolService = project.service<MainToolWindowService>()

    private var queryExecutionManager: BaseQueryExecutionManagerInterface = when (project.service<ProjectConfigurations>().dbtProjectConfig.adapterName) {
        "bigquery" -> BigQueryQueryExecutionManager(project)
        "postgres" -> PostgresQueryExecutionManager(project)
        else -> DbtManagerQueryExecutionManager(project)
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


    abstract inner class BaseQueryTask(
        project: Project?,
        title: String,
    ) : Task.Backgroundable(project, title, true) {
        var queryExecution: QueryExecution? = null
        var pageNumber: Int = 1
        var query: String? = null
        var target: String? = null

        abstract fun executeTask(indicator: ProgressIndicator): QueryExecution

        override fun run(indicator: ProgressIndicator) {
            indicator.isIndeterminate = false
            listeners.forEach { it.displayQueryLoading() }
            queryExecution = executeTask(indicator)
        }

        override fun onSuccess() {
            super.onSuccess()
            queryExecution!!.status = QueryStatus.SUCCESS
            listeners.forEach { it.displayExecutedQuery(queryExecution!!, pageNumber) }
        }

        override fun onThrowable(error: Throwable) {
            if (queryExecution == null){
                queryExecution = QueryExecution(query?: "", target ?: "", "")
            }
            queryExecution!!.status = QueryStatus.FAILED
            queryExecution!!.pages[1] = listOf(
                listOf("Execution error:"),
                listOf(error.message.toString()),
                listOf("Query:"),
                listOf(queryExecution!!.query)
            )
            project.service<Notifications>().sendNotification(
                "Failed to run query",
                "Exception: ${error.message.toString()}",
                NotificationType.ERROR,
                MainToolWindowService.Tab.LOGS
            )
            val eventLoggerManager = project.service<EventLoggerManager>()
            eventLoggerManager.logLine("Caught an exception: ${error.message.toString()}", "core")
            eventLoggerManager.logLines(error.stackTraceToString().lines(), "core")
            listeners.forEach { it.displayExecutedQuery(queryExecution!!, pageNumber) }
        }
    }

    fun clearExecutions() {
        myState.executions.clear()
    }

    fun getExecutionResultPageNumber(queryExecution: QueryExecution, pageNumber: Int) {
        ProgressManager.getInstance().run(object : BaseQueryTask(project, "Running query in Background") {
                override fun executeTask(indicator: ProgressIndicator): QueryExecution {
                    if (!queryExecution.pages.contains(pageNumber)) {
                        queryExecution.pages[pageNumber] = queryExecutionManager.getResultsPageNumber(queryExecution, pageNumber)
                    }
                    this.pageNumber = pageNumber
                    return queryExecution
                }
            }
        )
    }
    fun runQuery(editor: Editor, target: String, type: QueryTypes, dbtCompile: Boolean){
        mainToolService.activateTab(MainToolWindowService.Tab.QUERY_RUN)
        ProgressManager.getInstance().run(object : BaseQueryTask(project, "Running query in Background"){
                override fun executeTask(indicator: ProgressIndicator): QueryExecution {
                    val manifestService = project.service<ManifestService>()
                    val query = manifestService.getWithReplacingRefsAndSources(editor, target, dbtCompile)
                    this.query = query
                    this.target = target
                    queryExecution = queryExecutionManager.runQuery(type, query, target)
                    pushExecution(queryExecution!!)
                    return queryExecution!!
                }
            }
        )
    }
}
