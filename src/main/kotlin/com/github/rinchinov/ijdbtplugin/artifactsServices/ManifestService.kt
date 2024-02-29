package com.github.rinchinov.ijdbtplugin.artifactsServices
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Macro
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Manifest
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Node
import com.github.rinchinov.ijdbtplugin.artifactsVersions.SourceDefinition
import com.github.rinchinov.ijdbtplugin.extentions.ToolWindowUpdater
import com.github.rinchinov.ijdbtplugin.DbtCoreInterface
import com.github.rinchinov.ijdbtplugin.services.Executor
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.intellij.notification.NotificationType
import com.jetbrains.rd.util.first
import java.io.File
import java.time.LocalDateTime
import java.time.Duration
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex


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
    private val executor = project.service<Executor>()
    private val dbtPackageLocation = executor.getDbtPythonPackageLocation()
    private val dbtNotifications = project.service<Notifications>()
    private val toolWindowUpdater = project.service<ToolWindowUpdater>()
    private val mutex = Mutex()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val manifests: MutableMap<String, Manifest?> = projectConfigurations.targetList().associateWith{ null }.toMutableMap()
    private val manifestLastUpdated: MutableMap<String, LocalDateTime> = projectConfigurations.targetList().associateWith{ LocalDateTime.of(1, 1, 1, 0, 0) }.toMutableMap()
    init {
        parseManifest()
    }
    private fun defaultManifest() = manifests[projectConfigurations.defaultTarget()]
    private fun defaultProjectName() = defaultManifest()?.getProjectName()?: ""
    private fun updateManifest(target: String, manifest: Manifest) {
        manifests[target] = manifest // Directly modify the backing map
        toolWindowUpdater.notifyManifestChangeListeners(this)
        manifestLastUpdated[target] = LocalDateTime.now()
    }
    private fun getManifest(target: String?): Manifest? {
        val cTarget: String = target?: projectConfigurations.defaultTarget()
        parseManifest(cTarget)
        return manifests[cTarget]
    }
    private fun parseManifest() {
        parseManifest(projectConfigurations.defaultTarget())
    }

    private fun parseManifest(target: String) {
        val lastUpdated = manifestLastUpdated[target]
        if (Duration.between(lastUpdated, LocalDateTime.now()).toMinutes() <= UPDATE_INTERVAL) {
            return
        }

        coroutineScope.launch {
            if (mutex.tryLock()) {
                try {
                    dbtNotifications.sendNotification(
                        "Manifest reload started!",
                        "",
                        NotificationType.INFORMATION
                    )
                    executor.executeDbt(listOf("parse"), mapOf()).toString()
                    val jsonString =
                        File(projectConfigurations.manifestPath().absolutePath.toString())
                            .readText(Charsets.UTF_8)
                    updateManifest(target, Manifest.fromJson(jsonString))
                    dbtNotifications.sendNotification(
                        "Manifest reloaded for $target!",
                        "",
                        NotificationType.INFORMATION
                    )
                } catch (e: Exception) {
                    dbtNotifications.sendNotification(
                        "Manifest reload failed for $target!!",
                        e.toString(),
                        NotificationType.ERROR
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
    fun lastUpdated(): LocalDateTime? = manifestLastUpdated[projectConfigurations.defaultTarget()]
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
        if (manifest!=null){
            val lookupArray: Array<String> = if (packageName.isNullOrEmpty()){
                arrayOf(
                    arrayOf(defaultProjectName(), "dbt"),
                    manifest.resourceMap?.get("macro")?.keys?.filter { it.startsWith("dbt_") }?.toTypedArray()?: emptyArray<String>(),
                    manifest.resourceMap?.get("macro")?.keys?.filter { ! it.startsWith("dbt_") }?.toTypedArray()?: emptyArray<String>()
                ).flatten().toTypedArray()
            }
            else arrayOf(packageName)
            lookupArray.forEach {
                val macroId = manifest.resourceMap?.get("macro")?.get(it)?.get(macroName)
                if (macroId != null) {
                    return manifest.macros.getValue(macroId)
                }
            }
        }
        return null
    }

}
