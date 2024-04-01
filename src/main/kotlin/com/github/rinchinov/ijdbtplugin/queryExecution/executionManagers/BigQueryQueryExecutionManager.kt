package com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionManagerInterface
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class BigQueryQueryExecutionManager(override val project: Project) : QueryExecutionManagerInterface {
    override val projectConfigurations = project.service<ProjectConfigurations>()
    override val manifest = project.service<ManifestService>()
    override val eventLoggerManager = project.service<EventLoggerManager>()
    override val dbtNotifications = project.service<Notifications>()
    override fun runQuery(query: String, target: String, queryExecutionBackend: QueryExecutionBackend) {
        TODO("Not yet implemented")
    }
    override fun getResultsWithPagination(queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int, queryExecutionBackend: QueryExecutionBackend): List<List<String>>? {
        TODO("Not yet implemented")
    }
    override fun runQuery(type: String, query: String, target: String, queryExecutionBackend: QueryExecutionBackend){
        TODO("Not yet implemented")
    }
}