package com.github.rinchinov.ijdbtplugin.artifactsServices
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.github.rinchinov.ijdbtplugin.services.EventLoggerManager
import com.github.rinchinov.ijdbtplugin.DbtCoreInterface
import com.github.rinchinov.ijdbtplugin.artifactsVersions.*
import com.github.rinchinov.ijdbtplugin.extentions.FocusLogsTabAction
import com.github.rinchinov.ijdbtplugin.services.Executor
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.github.rinchinov.ijdbtplugin.services.ProjectSettings
import com.intellij.notification.NotificationType
import com.jetbrains.python.profiler.getPackageName
import com.jetbrains.rd.util.first
import java.time.LocalDateTime
import java.time.Duration
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import javax.swing.SwingUtilities

@Service(Service.Level.PROJECT)
class ManifestService(var project: Project): DbtCoreInterface {
    companion object {
        const val UPDATE_INTERVAL = 5
        val ADAPTERS: Map<String, String> = mapOf(
            "dbt_postgres" to "postgres",
            "dbt_bigquery" to "bigquery"
        )
    }
    private val projectConfigurations = project.service<ProjectConfigurations>()
    private val settings = project.service<ProjectSettings>()
    private val executor = project.service<Executor>()
    private val dbtPackageLocation = executor.getDbtPythonPackageLocation()
    private val dbtNotifications = project.service<Notifications>()
    private val eventLoggerManager = project.service<EventLoggerManager>()
    private val mutex = Mutex()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val manifests: MutableMap<String, Manifest?> = settings.getDbtTargetList().associateWith{ null }.toMutableMap()
    private val manifestLastUpdated: MutableMap<String, LocalDateTime> = settings.getDbtTargetList().associateWith{ LocalDateTime.of(1, 1, 1, 0, 0) }.toMutableMap()
    init {
        parseManifest()
    }
    private fun defaultManifest() = manifests[settings.getDbtDefaultTarget()]
    private fun defaultProjectName() = defaultManifest()?.getProjectName()?: ""
    private fun updateManifest(target: String, manifest: Manifest) {
        manifests[target] = manifest // Directly modify the backing map
        eventLoggerManager.notifyManifestChangeListeners(this)
        manifestLastUpdated[target] = LocalDateTime.now()
    }
    private fun getManifest(target: String?): Manifest? {
        val cTarget: String = target?: settings.getDbtDefaultTarget()
        parseManifest(cTarget)
        return manifests[cTarget]
    }
    private fun parseManifest() {
        parseManifest(settings.getDbtDefaultTarget())
    }

    private fun parseManifest(target: String) {
        val lastUpdated = manifestLastUpdated[target]?: LocalDateTime.of(1, 1, 1, 0, 0)
        if (Duration.between(lastUpdated, LocalDateTime.now()).toMinutes() <= UPDATE_INTERVAL) {
                return
        }

        coroutineScope.launch {
            if (mutex.tryLock()) {
                try {
                    val manifestString = executor.dbtParse(target)
                    updateManifest(target, Manifest.fromJson(manifestString))
                    dbtNotifications.sendNotification(
                        "Manifest reloaded for $target!",
                        "",
                        NotificationType.INFORMATION
                    )
                } catch (e: Exception) {
                    eventLoggerManager.logLines(e.toString().trim().lines(), "core")
                    dbtNotifications.sendNotification(
                        "Manifest reload failed for $target!!",
                        "",
                        NotificationType.ERROR,
                        FocusLogsTabAction(project)
                    )
                } finally {
                    mutex.unlock()
                }
            }
        }
    }

    override fun getPackageDir(packageName: String?): String {
        return when (packageName) {
            defaultProjectName() -> {
                project.basePath?: ""
            }
            "dbt" -> "$dbtPackageLocation/include/global_project"
            in ADAPTERS.keys -> "$dbtPackageLocation/include/${ADAPTERS[packageName]}"
            else -> "${projectConfigurations.packagesPath().absolutePath}/$packageName/"
        }
    }
    fun getNodesCount(): Int? = defaultManifest()?.nodes?.size
    fun getMacrosCount(): Int? = defaultManifest()?.macros?.size
    fun getSourcesCount(): Int? = defaultManifest()?.sources?.size
    fun lastUpdated(): LocalDateTime? = manifestLastUpdated[settings.getDbtDefaultTarget()]
    fun getStatus(): String = if (defaultManifest() == null) "Manifest parse failed" else "Manifest successfully parsed"

    override fun findNode(packageName: String?, uniqueId: String, currentVersion: Int?, target: String?): Node? {
        val manifest= getManifest(target)
        var nodeResult: Node? = null
        if (manifest != null) {
            val packageId = if (packageName == null || packageName == "") defaultProjectName() else packageName
            val nodesMap = manifest.resourceMap?.get("model")?.get(packageId)
            val fullUniqueId = nodesMap?.get(uniqueId)
            if (fullUniqueId != null) {
                nodeResult = manifest.nodes[fullUniqueId]
                if (nodeResult != null) return nodeResult
            }
            val seedUniqueId = manifest.resourceMap?.get("seed")?.get(packageId)?.get(uniqueId)
            if (seedUniqueId != null){
                nodeResult = manifest.nodes[seedUniqueId]
                if (nodeResult != null) return nodeResult
            }
            val versionsCurrentPackage = nodesMap?.filterKeys { it.startsWith(uniqueId) }
            if (!versionsCurrentPackage.isNullOrEmpty()){
                val latestVersion =  manifest.nodes[versionsCurrentPackage.first().value]?.latestVersion?.toJson()?.toInt()
                val version = currentVersion ?: latestVersion
                nodeResult = manifest.nodes["model.$packageId.$uniqueId.v$version"]
            }
        }
        return nodeResult
    }
    override fun findSourceDefinition(uniqueId: String, target: String?): SourceDefinition? {
        val manifest= getManifest(target)
        if (manifest!=null){
            val matchedSources = manifest.sources.filterKeys {
                it.endsWith(uniqueId)
            }
            if (matchedSources.isNotEmpty()){
                return matchedSources.first().value
            }
        }
        return null
    }
    override fun findMacro(packageName: String?, macroName: String, target: String?): Macro? {
        val manifest= getManifest(target)
        val macros = manifest?.resourceMap?.get("macro")
        if (manifest!=null && macros !=null){
            val adapterName = ADAPTERS[settings.getDbtAdapter()]
            // start lookup from
            val mainLookupOrder = arrayOf(
                macros[packageName?: defaultProjectName()]?.get(macroName), // specified project or default
                macros[settings.getDbtAdapter()]?.get("${adapterName}__$macroName"), // lookup in adapters macros with dispatch
                macros[settings.getDbtAdapter()]?.get(macroName), // lookup in adapter without dispatch
                macros["dbt"]?.get(macroName), // lookup in core macros
            )
            mainLookupOrder.forEach {
                if (it != null) {
                    return manifest.macros.getValue(it)
                }
            }
            // if not find try to lookup in the rest packages excluding already checked and adapters
            macros.keys.filter {
                it !in arrayOf("dbt", packageName?: defaultProjectName()) &&
                        it !in ADAPTERS.keys
            }.forEach{
                val macroId = macros[it]?.get(macroName)
                if (macroId != null) {
                    return manifest.macros.getValue(macroId)
                }
            }
        }
        return null
    }

    override fun replaceRefsAndSourcesFromJinja2(query: String, target: String): String {
        val result = executor.dbtCompileInline(target, query)
        val jsonNode = ObjectMapper().readTree(result)
        val compiledCode = jsonNode.at("/results/0/node/compiled_code").asText()
        if (compiledCode == null){
            dbtNotifications.sendNotification(
                "Failed to replace ref/source for copying!",
                "",
                NotificationType.ERROR,
                FocusLogsTabAction(project)
            )
            return query
        }
        else {
            dbtNotifications.sendNotification(
                "Copied with replaced refs/sources",
                "",
                NotificationType.INFORMATION
            )
            return compiledCode
        }
    }

    override fun replaceRefsAndSourcesToJinja2(query: String, target: String): String {
        var replaced = query
        manifests[target]?.relationMap?.forEach{
            replaced = replaced.replace(it.key, it.value)
        }
        return replaced.replace("'${defaultProjectName()}', ", "")
    }

}
