package com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers

import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend

interface SimpleQueryExecutionManagerInterface: BaseQueryExecutionManagerInterface {
    val dryRunQueryTemplate: String
    val queryPlanTemplate: String
    val paginationQueryTemplate: String
    val countQuery: String
    fun getQueryResults(target: String, query: String) : List<List<String>>

    override fun runQuery(type: QueryExecutionBackend.QueryTypes, query: String, target: String): QueryExecutionBackend.QueryExecution {
        val executionId = query
        val queryExecution = QueryExecutionBackend.QueryExecution(
            query,
            target,
            executionId,
        )
        return when (type){
            QueryExecutionBackend.QueryTypes.PAGINATED -> {
                val countQuery = countQuery.format(query)
                val totalRecords = getQueryResults(target, countQuery)[1][0].toInt()
                queryExecution.totalPages = (totalRecords + queryExecution.rowsPerPage - 1) / queryExecution.rowsPerPage
                queryExecution.pages[1] = getResultsPageNumber(queryExecution, 1)
                queryExecution
            }
            QueryExecutionBackend.QueryTypes.DRY -> {
                queryExecution.pages[1] = getQueryResults(target, dryRunQueryTemplate.format(query))
                queryExecution.totalPages = 1
                queryExecution
            }
            QueryExecutionBackend.QueryTypes.PLAN -> {
                queryExecution.pages[1] = getQueryResults(target, queryPlanTemplate.format(query))
                queryExecution.totalPages = 1
                queryExecution
            }
        }
    }

    override fun getResultsPageNumber(
        queryExecution: QueryExecutionBackend.QueryExecution,
        pageNumber: Int
    ): List<List<String>> {
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
        return getQueryResults(queryExecution.target, query)
    }

}