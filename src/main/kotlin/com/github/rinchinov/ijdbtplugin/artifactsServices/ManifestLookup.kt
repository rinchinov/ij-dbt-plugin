package com.github.rinchinov.ijdbtplugin.artifactsServices

import com.github.rinchinov.ijdbtplugin.ReferenceInterface
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Macro
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Manifest
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Node
import com.github.rinchinov.ijdbtplugin.artifactsVersions.SourceDefinition
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.github.rinchinov.ijdbtplugin.services.ProjectSettings
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.first
import java.io.File

interface ManifestLookup: ReferenceInterface {
    val settings: ProjectSettings
    var project: Project
    val dbtPackageLocation: String
    val projectConfigurations: ProjectConfigurations

    fun defaultProjectName(): String
    fun getManifest(target: String?): Manifest?
    override fun findNode(packageName: String?, uniqueId: String, currentVersion: Int?, target: String?): Node? {
    val manifest = getManifest(target)
    if (manifest != null) {
        if (packageName == null || packageName == "") {
            manifest.resourceMap?.get("model")?.forEach{
                val node = findNode(it.key, uniqueId, currentVersion, manifest)
                if (node!=null) {
                    return node
                }
            }
        }
        else {
            return findNode(packageName, uniqueId, currentVersion, manifest)
        }
    }
    return null
}
    private fun findNode(packageName: String, uniqueId: String, currentVersion: Int?, manifest: Manifest): Node? {
        var nodeResult: Node? = null
        val nodesMap = manifest.resourceMap?.get("model")?.get(packageName)
        val fullUniqueId = nodesMap?.get(uniqueId)
        if (fullUniqueId != null) {
            nodeResult = manifest.nodes[fullUniqueId]
            if (nodeResult != null) return nodeResult
        }
        val seedUniqueId = manifest.resourceMap?.get("seed")?.get(packageName)?.get(uniqueId)
        if (seedUniqueId != null){
            nodeResult = manifest.nodes[seedUniqueId]
            if (nodeResult != null) return nodeResult
        }
        val versionsCurrentPackage = nodesMap?.filterKeys { it.startsWith(uniqueId) }
        if (!versionsCurrentPackage.isNullOrEmpty()){
            val latestVersion =  manifest.nodes[versionsCurrentPackage.first().value]?.latestVersion?.toJson()?.toInt()
            val version = currentVersion ?: latestVersion
            nodeResult = manifest.nodes["model.$packageName.$uniqueId.v$version"]
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
            val adapterType = manifest.metadata.adapterType ?: projectConfigurations.dbtProjectConfig.adapterName
            // start lookup from
            val mainLookupOrder = arrayOf(
                macros[packageName?: defaultProjectName()]?.get(macroName), // specified project or default
                macros["dbt_$adapterType"]?.get("${adapterType}__$macroName"), // lookup in adapters macros with dispatch
                macros["dbt_$adapterType"]?.get(macroName), // lookup in adapter without dispatch
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
                        !it.startsWith("dbt_")
            }.forEach{
                val macroId = macros[it]?.get(macroName)
                if (macroId != null) {
                    return manifest.macros.getValue(macroId)
                }
            }
        }
        return null
    }

    override fun getPackageDir(packageName: String): String {
        when {
            packageName == defaultProjectName() -> return project.basePath ?: ""
            packageName == "dbt" -> return "$dbtPackageLocation/include/global_project"
            else -> {
                if (packageName.startsWith("dbt_")) {
                    val path = """$dbtPackageLocation/include/${packageName.drop(4)}"""
                    val directory = File(path)
                    if (directory.exists() && directory.isDirectory) {
                        return path
                    }
                }
                return "${projectConfigurations.packagesPath().absolutePath}/$packageName/"
            }
        }
    }

}