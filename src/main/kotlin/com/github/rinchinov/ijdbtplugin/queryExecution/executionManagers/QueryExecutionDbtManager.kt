package com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionManagerInterface
import com.github.rinchinov.ijdbtplugin.services.*
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class QueryExecutionDbtManager(override val project: Project): QueryExecutionManagerInterface {
    override val projectConfigurations = project.service<ProjectConfigurations>()
    private val projectSettings = project.service<ProjectSettings>()
    override val manifest = project.service<ManifestService>()
    override val eventLoggerManager = project.service<EventLoggerManager>()
    override val dbtNotifications = project.service<Notifications>()
    private val dbtExecutor = project.service<Executor>()
    private val dryRunQueryTemplate: String = projectSettings.getDbtQueryRunDryTemplate()
    private val queryPlanTemplate: String = projectSettings.getDbtQueryRunPlanTemplate()
    private val paginationQueryTemplate: String = projectSettings.getDbtQueryRunPaginationTemplate()
    private val countQuery: String = projectSettings.getDbtQueryRunCountTemplate()

    @Serializable
    data class Data(
        val adapterResponse: AdapterResponse,
        val data: List<List<String>>
    )

    @Serializable
    data class AdapterResponse(
        val _message: String,
        val code: String,
        val rows_affected: Int
    )


    private fun createExecution(query: String, target: String, queryExecutionBackend: QueryExecutionBackend): QueryExecutionBackend.QueryExecution{
        val executionId = query
        val queryExecution = QueryExecutionBackend.QueryExecution(
            this,
            query,
            target,
            executionId,
        )
        queryExecutionBackend.pushExecution(queryExecution)
        return queryExecution
    }

    private fun callDbtQuery(target: String, query: String) : List<List<String>>? {
        val rawData = dbtExecutor.dbtQueryCall(target, query, true)
        val json = Json { ignoreUnknownKeys = true }
        val result = json.decodeFromString<Data>(rawData)
        // check adapter response
        return result.data
    }
    override fun runQuery(query: String, target: String, queryExecutionBackend: QueryExecutionBackend) {
        val countQuery = countQuery.format(query)
        val queryExecution = createExecution(query, target, queryExecutionBackend)
        val totalRecords = callDbtQuery(target, countQuery)?.get(1)?.get(0)?.toInt() ?: 100
        queryExecution.totalPages = (totalRecords + queryExecution.rowsPerPage - 1) / queryExecution.rowsPerPage
        val results = getResultsWithPagination(queryExecution, 1, queryExecutionBackend)
        if (results != null) {
            queryExecutionBackend.updateExecutionResults(
                queryExecution.executionId,
                QueryExecutionBackend.QueryStatus.SUCCESS,
                results,
                1
            )
        }
    }

    override fun getResultsWithPagination(queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int, queryExecutionBackend: QueryExecutionBackend): List<List<String>>? {
        val paginatedQuery = paginationQueryTemplate.format(queryExecution.query)
        val values = listOf(
            queryExecution.rowsPerPage.toString(),
            (queryExecution.rowsPerPage * (pageNumber - 1)).toString()
        )
        val regex = "\\?".toRegex()
        var index = 0 // Keep track of the replacement index
        val query = regex.replace(paginatedQuery) { matchResult ->
            values.getOrElse(index++) { matchResult.value }
        }
        return callDbtQuery(queryExecution.target, query)
    }

    override fun runQuery(type: String, query: String, target: String, queryExecutionBackend: QueryExecutionBackend) {
        val templatedQuery = when (type){
            "plan" -> queryPlanTemplate.format(query)
            "dry" -> dryRunQueryTemplate.format(query)
            else -> query
        }
        val queryExecution = createExecution(templatedQuery, target, queryExecutionBackend)
        val rows = callDbtQuery(queryExecution.target, templatedQuery)
        if (rows != null) {
            queryExecutionBackend.updateExecutionResults(queryExecution.executionId, QueryExecutionBackend.QueryStatus.SUCCESS, rows, 1)
        }
    }

}
