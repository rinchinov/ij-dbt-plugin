package com.github.rinchinov.ijdbtplugin.queryExecution

interface QueryChangeListener {
    fun onQueryAdd(queryExecution: QueryExecutionBackend.QueryExecution, keep: Int)
    fun displayExecutedQuery(queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int)
    fun clearQueryPanel()
}
