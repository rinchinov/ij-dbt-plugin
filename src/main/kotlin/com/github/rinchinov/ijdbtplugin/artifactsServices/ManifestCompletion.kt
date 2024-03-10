package com.github.rinchinov.ijdbtplugin.artifactsServices

import com.github.rinchinov.ijdbtplugin.CompletionInterface
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Manifest
import com.github.rinchinov.ijdbtplugin.artifactsVersions.NodeResourceType

interface ManifestCompletion: CompletionInterface {
    fun defaultManifest(): Manifest?
    override fun getMacrosList(): List<String> {
        val result = mutableListOf("ref()", "source()", "config()")
        defaultManifest()?.macros?.values?.forEach { macro ->
            val shortName = macro.name.split("__").last()
            if (macro.packageName == defaultManifest()?.getPackageName()){
                result.add("${shortName}()")
            }
            else {
                result.add("${macro.packageName}.${shortName}()")
            }
        }
        return result
    }
    override fun getMacrosList(packageName: String): List<String> {
        val result = mutableListOf<String>()
        defaultManifest()?.macros?.values?.filter { it.packageName == packageName }?.forEach { macro ->
            result.add("${macro.name}()")
        }
        return result
    }

    override fun getSourcesNamesList(): List<String> {
        val result = mutableSetOf<String>()
        defaultManifest()?.sources?.values?.forEach { source ->
            result.add("\"${source.sourceName}\"")
        }
        return result.toList()
    }

    override fun getSourcesNamesList(sourceName: String): List<String> {
        val result = mutableSetOf<String>()
        defaultManifest()?.sources?.values?.forEach { source ->
            if (source.sourceName == sourceName) {
                result.add("\"${source.name}\"")
            }
        }
        return result.toList()
    }

    override fun getRefNamesList(): List<String> {
        val result = mutableSetOf<String>()
        defaultManifest()?.nodes?.values?.forEach { node ->
            if (node.resourceType != NodeResourceType.Test){
                result.add("\"${node.packageName}\"")
                result.add("\"${node.name}\"")
            }
        }
        return result.toList()
    }

    override fun getRefNamesList(packageName: String): List<String> {
        val result = mutableSetOf<String>()
        defaultManifest()?.nodes?.values?.forEach { node ->
            if (node.packageName == packageName && node.resourceType != NodeResourceType.Test) {
                result.add("\"${node.name}\"")
            }
        }
        return result.toList()
    }
}
