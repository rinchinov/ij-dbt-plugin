package com.github.rinchinov.ijdbtplugin.queryExecution

import com.intellij.database.dataSource.localDataSource
import com.intellij.database.util.DbUtil
import com.intellij.notification.NotificationType
import java.net.URLClassLoader
import java.nio.file.Paths
import java.sql.*
import java.util.*

interface QueryExecutionJdbcManagerInterface: QueryExecutionManagerInterface {
    val dryRunQueryTemplate: String
    val paginationQueryTemplate: String
    val queryPlanTemplate: String
    val countQuery: String
    enum class JdbcUriType {
        USER_PASSWORD,
        AS_IS // template for the rest
    }
    val jdbcUriType: JdbcUriType
    private class DriverShim(private val driver: Driver) : Driver {
        override fun connect(url: String?, info: Properties?): Connection = driver.connect(url, info)
        override fun acceptsURL(url: String?): Boolean = driver.acceptsURL(url)
        override fun getPropertyInfo(url: String?, info: Properties?): Array<DriverPropertyInfo> = driver.getPropertyInfo(url, info)
        override fun getMajorVersion(): Int = driver.majorVersion
        override fun getMinorVersion(): Int = driver.minorVersion
        override fun jdbcCompliant(): Boolean = driver.jdbcCompliant()
        override fun getParentLogger(): java.util.logging.Logger = driver.parentLogger
    }
    private fun prepareJdbcUrl(url: String, target: String): String {
        val profile = projectConfigurations.getProfileDetails(target)
        return when (jdbcUriType) {
            JdbcUriType.USER_PASSWORD -> {
                if (profile != null) {
                    val separator = if (url.contains("&")) "&" else "?"
                    val user = profile["user"] as String
                    val password = profile["password"] as String
                    "$url${separator}user=$user&password=$password"
                }
                else {
                    eventLoggerManager.logLine("Profile was not found for target $target", "core")
                    url
                }
            }
            else -> url
        }
    }
    private fun getConnection(target: String): Connection {
        val datasourceName = "${manifest.defaultProjectName()}__$target"
        val datasource = DbUtil.getDataSources(project).filter { it.name == "${manifest.defaultProjectName()}__$target" }.firstOrNull()
        if (datasource != null){
            val localDatasource = datasource.delegateDataSource.localDataSource
            if (localDatasource != null) {
                val paths = localDatasource.classpathElements.map{ classPathElement ->
                    classPathElement.classesRootUrls.map {
                        Paths.get(it.replace("file://", "")).toUri().toURL()
                    }
                }.flatten().toTypedArray()
                val classLoader = URLClassLoader(paths, Thread.currentThread().contextClassLoader)
                val driverClass = Class.forName(localDatasource.driverClass, true, classLoader)
                val driver = driverClass.getDeclaredConstructor().newInstance() as Driver
                DriverManager.registerDriver(DriverShim(driver))
                if (localDatasource.url == null) {
                    dbtNotifications.sendNotification(
                        "Datasource's connection url is empty",
                        "Please create connection $datasourceName",
                        NotificationType.ERROR
                    )
                }
                else {
                    return DriverManager.getConnection(prepareJdbcUrl(localDatasource.url!!, target))
                }
            }
        }
        else {
            dbtNotifications.sendNotification(
                "Can't find datasource connection named `$datasourceName`",
                "Please create connection with specified name",
                NotificationType.ERROR
            )
        }
        throw Error("Can't establish connection to $target")
    }

    private fun <T> jdbcStatementRun(queryExecution: QueryExecutionBackend.QueryExecution, queryExecutionBackend: QueryExecutionBackend, block: (Connection) -> T): T?{
        try {
            getConnection(queryExecution.target).use { connection ->
                return block(connection)
            }
        } catch (e: SQLException) {
            dbtNotifications.sendNotification("Query run failed!", ": ${e.message}", NotificationType.ERROR)
            queryExecutionBackend.updateExecutionResults(
                queryExecution.executionId,
                QueryExecutionBackend.QueryStatus.FAILED,
                listOf(
                    listOf("Database error:"),
                    listOf(""),
                    listOf("Query:"),
                    queryExecution.query.lines().toList(),
                    listOf(""),
                    listOf(""),
                    e.stackTrace.toString().lines()
                ),
                1
            )
        } catch (e: Exception) {
            dbtNotifications.sendNotification("Query run failed!", ": ${e.message}", NotificationType.ERROR)
            queryExecutionBackend.updateExecutionResults(
                queryExecution.executionId,
                QueryExecutionBackend.QueryStatus.FAILED,
                listOf(
                    listOf("General error:"),
                    listOf(""),
                    listOf("Query:"),
                    queryExecution.query.lines().toList(),
                    listOf(""),
                    listOf(""),
                    e.stackTrace.toString().lines()
                ),
                1
            )
        }
        return null
    }

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
    override fun runQuery(query: String, target: String, queryExecutionBackend: QueryExecutionBackend) {
        val countQuery = countQuery.format(query)
        val queryExecution = createExecution(query, target, queryExecutionBackend)
        jdbcStatementRun(queryExecution, queryExecutionBackend) { conn ->
            val totalRecords = conn.prepareStatement(countQuery).use { statement ->
                val resultSet = statement.executeQuery()
                resultSet.next()
                resultSet.getInt(1)
            }
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
    }
    private fun resultSetToList(rs: ResultSet): List<List<String>> {
        val resultList = mutableListOf<List<String>>()
        val metaData = rs.metaData
        val columnCount = metaData.columnCount

        // Add column names as the first list
        val columnNames = MutableList(columnCount) { i -> metaData.getColumnName(i + 1) }
        resultList.add(columnNames)

        // Add row data
        while (rs.next()) {
            val row = MutableList(columnCount) { i -> rs.getString(i + 1) ?: "" }
            resultList.add(row)
        }

        return resultList
    }
    override fun getResultsWithPagination(queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int, queryExecutionBackend: QueryExecutionBackend): List<List<String>>? {
        val paginatedQuery = paginationQueryTemplate.format(queryExecution.query)
        return jdbcStatementRun(queryExecution, queryExecutionBackend) { conn ->
            conn.prepareStatement(paginatedQuery).use { statement ->
                statement.setInt(1, queryExecution.rowsPerPage)
                statement.setInt(2, queryExecution.rowsPerPage * (pageNumber - 1))
                resultSetToList(statement.executeQuery())
            }
        }
    }
    override fun runQuery(type: String, query: String, target: String, queryExecutionBackend: QueryExecutionBackend) {
        val templatedQuery = when (type){
            "plan" -> queryPlanTemplate.format(query)
            "dry" -> dryRunQueryTemplate.format(query)
            else -> query
        }
        val queryExecution = createExecution(templatedQuery, target, queryExecutionBackend)
        val rows = jdbcStatementRun(queryExecution, queryExecutionBackend) { conn ->
            conn.prepareStatement(templatedQuery).use { statement ->
                val rs  = statement.executeQuery()
                resultSetToList(rs)
            }
        }
        if (rows != null) {
            queryExecutionBackend.updateExecutionResults(queryExecution.executionId, QueryExecutionBackend.QueryStatus.SUCCESS, rows, 1)
        }
    }

}