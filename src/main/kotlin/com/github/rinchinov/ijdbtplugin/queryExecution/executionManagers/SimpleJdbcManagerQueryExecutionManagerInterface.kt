package com.github.rinchinov.ijdbtplugin.queryExecution.executionManagers

import com.intellij.database.dataSource.localDataSource
import com.intellij.database.util.DbUtil
import com.intellij.notification.NotificationType
import java.net.URLClassLoader
import java.nio.file.Paths
import java.sql.*
import java.util.*

interface SimpleJdbcManagerQueryExecutionManagerInterface: SimpleQueryExecutionManagerInterface {
    enum class JdbcUriType {
        USER_PASSWORD,
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

    override fun getQueryResults(target: String, query: String) : List<List<String>>{
        getConnection(target).use { connection ->
            connection.prepareStatement(query).use { statement ->
                val rs  = statement.executeQuery()
                return resultSetToList(rs)
            }
        }
    }
}