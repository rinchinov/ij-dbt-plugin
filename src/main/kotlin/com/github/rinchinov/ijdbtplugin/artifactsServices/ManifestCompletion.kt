package com.github.rinchinov.ijdbtplugin.artifactsServices

import com.github.rinchinov.ijdbtplugin.CompletionInterface
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Manifest
import com.github.rinchinov.ijdbtplugin.artifactsVersions.NodeResourceType

interface ManifestCompletion: CompletionInterface {
    fun defaultManifest(): Manifest?
    override fun getMacrosList(): List<String> {
        val result = mutableListOf("ref()", "source()", "config()")
        defaultManifest()?.macros?.values?.forEach { macro ->
            result.add("${macro.name}()")
        }
        return result
    }

    override fun getSourcesNamesList(): List<String> {
        val result = mutableSetOf<String>()
        defaultManifest()?.sources?.values?.forEach { source ->
            result.add("\"${source.schema}\"")
        }
        return result.toList()
    }

    override fun getSourcesNamesList(schema: String): List<String> {
        val result = mutableSetOf<String>()
        defaultManifest()?.sources?.values?.forEach { source ->
            if (source.schema == schema) {
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
