package com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.project.Project


interface BaseQueryExecutionManagerInterface {
    val project: Project
    val projectConfigurations: ProjectConfigurations
    val dbtNotifications: Notifications
    val manifest: ManifestService
    val eventLoggerManager: EventLoggerManager
    fun runQuery(type: QueryExecutionBackend.QueryTypes, query: String, target: String): QueryExecutionBackend.QueryExecution
    fun getResultsPageNumber(queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int): List<List<String>>
}
