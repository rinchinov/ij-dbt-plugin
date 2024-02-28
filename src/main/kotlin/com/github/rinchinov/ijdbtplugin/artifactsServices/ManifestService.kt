package com.github.rinchinov.ijdbtplugin.artifactsServices
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Manifest
import com.github.rinchinov.ijdbtplugin.extentions.ToolWindowUpdater
import com.intellij.psi.PsiElement
import com.github.rinchinov.ijdbtplugin.ref.ReferencesProviderInterface
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
class ManifestService(project: Project): ReferencesProviderInterface {
    companion object {
        const val UPDATE_INTERVAL = 5
    }
    private val projectConfigurations = project.service<ProjectConfigurations>()
    private val executor = project.service<Executor>()
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
    private fun updateManifest(target: String, manifest: Manifest) {
        manifests[target] = manifest // Directly modify the backing map
        toolWindowUpdater.notifyManifestChangeListeners(this)
        manifestLastUpdated[target] = LocalDateTime.now()
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

    private fun getPackageInfo(packageName: String?): Pair<String, String> {
        return if (packageName == null || packageName == "" || defaultManifest()?.metadata?.projectName == packageName) {
            Pair(
                defaultManifest()?.metadata?.projectName ?: "",
                projectConfigurations.dbtProjectPath().absoluteDir.toString() + "/"
            )
        }
        else {
            Pair(
                packageName,
                "${projectConfigurations.packagesPath().absolutePath}/$packageName/"
            )
        }
    }
    fun getNodesCount(): Int? = defaultManifest()?.nodes?.size
    fun getMacrosCount(): Int? = defaultManifest()?.macros?.size
    fun getSourcesCount(): Int? = defaultManifest()?.sources?.size
    fun lastUpdated(): LocalDateTime? = manifestLastUpdated[projectConfigurations.defaultTarget()]
    fun getStatus(): String = if (defaultManifest() == null) "Manifest parse failed" else "Manifest successfully parsed"

    override fun modelReferenceFileByElement(packageName: String?, uniqueId: String, currentVersion: Int?, element: PsiElement): String {
        parseManifest()
        val manifest = defaultManifest()
        if (manifest == null) {
            return ""
        } else {
            val nodes = manifest.nodes
            val packageInfo = getPackageInfo(packageName)
            val packageId = packageInfo.first
            val path = if ("model.$packageId.$uniqueId" in nodes) {
                val node = nodes["model.$packageId.$uniqueId"]
                packageInfo.second + node?.originalFilePath
            } else if ("seed.$packageId.$uniqueId" in nodes) {
                val node = nodes["seed.$packageId.$uniqueId"]
                packageInfo.second + node?.originalFilePath
            } else {
                val versionsCurrentPackage = nodes.filterKeys { it.startsWith("model.$packageId.$uniqueId") }
                if (versionsCurrentPackage.isNotEmpty()){
                    val latestVersion = versionsCurrentPackage.first().value.latestVersion?.toJson()
                        ?.toInt()
                    val version = currentVersion ?: latestVersion
                    val node = nodes["model.$packageId.$uniqueId.v$version"]
                    packageInfo.second + node?.originalFilePath
                }
                else {
                    val versionsAnyPackage = nodes.filterKeys { it.contains(uniqueId) }
                    if (versionsAnyPackage.isNotEmpty()){
                        val anyNode = versionsAnyPackage.first().value
                        val anyPackageInfo = getPackageInfo(anyNode.packageName)
                        anyPackageInfo.second + anyNode.originalFilePath
                    }
                    else {
                        ""
                    }
                }
            }
            return path
        }
    }

    override fun sourceReferenceFileByElement(uniqueId: String, element: PsiElement): String {
        parseManifest()
        val manifest = defaultManifest()
        return if (manifest == null) {
            ""
        } else {
            val matchedSources = manifest.sources.filterKeys {
                it.endsWith(uniqueId)
            }
            if (matchedSources.isEmpty()){
                ""
            }
            else {
                val source = matchedSources.first().value
                val packageInfo = getPackageInfo(source.packageName)
                element.project.basePath + packageInfo.second + source.originalFilePath
            }
        }
    }
    override fun macroReferenceFileByElement(packageName: String, macroName: String, element: PsiElement): String {
        parseManifest()
        val manifest = defaultManifest()
        return if (manifest == null) {
            ""
        } else {
            val macros = manifest.macros
            val packageInfo = getPackageInfo(packageName)
            val packageId = packageInfo.first
            val macro = macros["macro.$packageId.$macroName"]
            element.project.basePath + packageInfo.second + macro?.originalFilePath
        }
    }
}
