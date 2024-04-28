package com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ImpersonatedCredentials
import com.google.cloud.bigquery.*
import com.google.gson.Gson
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.io.ByteArrayInputStream
import java.io.FileInputStream


@Service(Service.Level.PROJECT)
class BigQueryQueryExecutionManager(override val project: Project) : BaseQueryExecutionManagerInterface {
    override val projectConfigurations = project.service<ProjectConfigurations>()
    override val manifest = project.service<ManifestService>()
    override val dbtNotifications = project.service<Notifications>()
    override val eventLoggerManager = project.service<EventLoggerManager>()
    private val jobs: MutableMap<String, TableResult> = mutableMapOf()

    private fun getClient(target: String): BigQuery {
        val profile = projectConfigurations.getProfileDetails(target)
        val builder = BigQueryOptions.newBuilder()
        val authMethod: String = profile?.get("method") as String
        val scopes: List<String> = profile.getOrDefault("scopes", listOf("https://www.googleapis.com/auth/bigquery")) as List<String>
        profile["location"]?.let {
            builder.setLocation(it as String)
        }
        (profile["execution_project"] ?: profile["project"] ?: profile["database"])?.let {
            builder.setProjectId(it as String)
        }
        when (authMethod) {
            "oauth" -> {
                GoogleCredentials.getApplicationDefault().createScoped(scopes)
            }
            "service-account" -> {
                val filePath = profile["keyfile"] as String
                GoogleCredentials.fromStream(FileInputStream(filePath)).createScoped(scopes)
            }
            "service-account-json" -> {
                val key = profile["keyfile_json"] as Map<String, String>
                val jsonKey = Gson().toJson(key)
                val credentialsStream = ByteArrayInputStream(jsonKey.toByteArray())
                GoogleCredentials.fromStream(credentialsStream).createScoped(scopes)
            }
            "oauth-secrets" -> {
                dbtNotifications.sendNotification(
                    "BigQuery adapter profile",
                    "`oauth-secrets` not supported, please use `oauth`, `service-account` or `service-account-json`",
                    NotificationType.ERROR
                )
                null
            }
            else -> {
                dbtNotifications.sendNotification(
                    "BigQuery adapter profile",
                    "Please check auth type in profiles.yml",
                    NotificationType.ERROR
                )
                null
            }
        }?.let { credentials ->
            profile["impersonate_service_account"]?.let { targetPrincipal ->
                val impersonatedCredentials = ImpersonatedCredentials.create(
                    credentials,
                    targetPrincipal as String,
                    null,
                    scopes,
                    3600
                )
                builder.setCredentials(impersonatedCredentials)
                return builder.build().service
            }
            builder.setCredentials(credentials)
        }
        return builder.build().service

    }

    private fun getJobConfiguration(query: String, target: String): QueryJobConfiguration {
        val jobConfiguration = QueryJobConfiguration.newBuilder(query)
        val profile = projectConfigurations.getProfileDetails(target)
        profile?.get("priority")?.let {
            when (it as String) {
                "batch" -> jobConfiguration.setPriority(QueryJobConfiguration.Priority.BATCH)
                "interactive" -> jobConfiguration.setPriority(QueryJobConfiguration.Priority.INTERACTIVE)
                else -> {}
            }
        }
        profile?.get("job_execution_timeout_seconds")?.let {
            jobConfiguration.setJobTimeoutMs((it as Long) * 1000)
        }
        profile?.get("maximum_bytes_billed")?.let {
            jobConfiguration.setMaximumBytesBilled((it as Long))
        }
        return jobConfiguration.build()
    }

    override fun runQuery(
        type: QueryExecutionBackend.QueryTypes,
        query: String,
        target: String
    ): QueryExecutionBackend.QueryExecution {
        val bigquery = getClient(target)
        val queryConfig = getJobConfiguration(query, target)
        val queryResult: TableResult = bigquery.query(queryConfig)
        val executionId = queryResult.jobId?.job ?: query
        val queryExecution = QueryExecutionBackend.QueryExecution(
            query,
            target,
            executionId,
        )
        val firstRow = queryResult.schema?.fields?.map { it.name }?.toList() ?: emptyList()
        var pageCount = 0
        val page = mutableListOf<List<String>>()
        page.add(firstRow)
        val pageIterator = queryResult.iterateAll().iterator()
        while (pageIterator.hasNext() && pageCount < 400) {
            val row = pageIterator.next()
            page.add(row.map { it.value.toString() }.toList())
            if (page.size == queryExecution.rowsPerPage + 1) {
                queryExecution.pages[pageCount] = page.toList()
                page.clear()
                page.add(firstRow)
                pageCount++
            }
        }
        queryExecution.totalPages = pageCount - 1
        return queryExecution
    }

    override fun getResultsPageNumber(
        queryExecution: QueryExecutionBackend.QueryExecution,
        pageNumber: Int
    ): List<List<String>> {
        return queryExecution.pages[pageNumber] ?: emptyList()
    }
}
