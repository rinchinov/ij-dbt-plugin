package com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionJdbcManagerInterface
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

class PostgresQueryExecutionManager(override val project: Project): QueryExecutionJdbcManagerInterface {
    override val projectConfigurations = project.service<ProjectConfigurations>()
    override val manifest = project.service<ManifestService>()
    override val eventLoggerManager = project.service<EventLoggerManager>()
    override val dbtNotifications = project.service<Notifications>()
    override val dryRunQueryTemplate: String = "%s LIMIT 10"
    override val queryPlanTemplate: String = "EXPLAIN %s"
    override val paginationQueryTemplate: String = "%s LIMIT ? OFFSET ?"
    override val countQuery: String = "SELECT COUNT(*) FROM (%s)"
    override val jdbcUriType = QueryExecutionJdbcManagerInterface.JdbcUriType.USER_PASSWORD
}
