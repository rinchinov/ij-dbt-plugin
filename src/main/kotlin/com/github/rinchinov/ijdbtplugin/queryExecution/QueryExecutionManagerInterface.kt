package com.github.rinchinov.ijdbtplugin.queryExecution

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.project.Project


interface QueryExecutionManagerInterface {
    val project: Project
    val projectConfigurations: ProjectConfigurations
    val dbtNotifications: Notifications
    val manifest: ManifestService
    val eventLoggerManager: EventLoggerManager
    fun runQuery(query: String, target: String, queryExecutionBackend: QueryExecutionBackend)
    fun getResultsWithPagination(queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int, queryExecutionBackend: QueryExecutionBackend): List<List<String>>?
    fun runQuery(type: String, query: String, target: String, queryExecutionBackend: QueryExecutionBackend)
}
