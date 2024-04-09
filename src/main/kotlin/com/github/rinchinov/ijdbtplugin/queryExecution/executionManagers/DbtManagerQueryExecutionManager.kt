package com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.services.*
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class DbtManagerQueryExecutionManager(override val project: Project): SimpleQueryExecutionManagerInterface {
    override val projectConfigurations = project.service<ProjectConfigurations>()
    override val manifest = project.service<ManifestService>()
    override val eventLoggerManager = project.service<EventLoggerManager>()
    override val dbtNotifications = project.service<Notifications>()
    private val dbtExecutor = project.service<Executor>()
    private val projectSettings = project.service<ProjectSettings>()
    override val dryRunQueryTemplate: String = projectSettings.getDbtQueryRunDryTemplate()
    override val queryPlanTemplate: String = projectSettings.getDbtQueryRunPlanTemplate()
    override val paginationQueryTemplate: String = projectSettings.getDbtQueryRunPaginationTemplate()
    override val countQuery: String = projectSettings.getDbtQueryRunCountTemplate()

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

    override fun getQueryResults(target: String, query: String): List<List<String>> {
        val rawData = dbtExecutor.dbtQueryCall(target, query, true)
        val json = Json { ignoreUnknownKeys = true }
        val result = json.decodeFromString<Data>(rawData)
        return result.data
    }

}
