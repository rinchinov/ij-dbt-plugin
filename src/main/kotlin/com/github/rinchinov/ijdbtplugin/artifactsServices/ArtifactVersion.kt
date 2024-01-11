package com.github.rinchinov.ijdbtplugin.artifactsServices

import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.util.regex.Pattern

open class ArtifactVersion{
    fun loadArtifact(path: ProjectConfigurations.SettingPath): Pair<String, Int?>  {
        val jsonString = File(path.absolutePath.toString()).readText(Charsets.UTF_8)
        val rawJson = Json.parseToJsonElement(jsonString) as JsonObject
        val jsonMetadata = rawJson["metadata"] as JsonObject
        val pattern = Pattern.compile("v(\\d+)\\.json")
        val url = jsonMetadata["dbt_schema_version"]?.jsonPrimitive?.content ?: return Pair(jsonString, null)
        val matcher = pattern.matcher(url)
        return if (matcher.find()) {
            Pair(jsonString, matcher.group(1)?.toInt())
        } else {
            Pair(jsonString, null)
        }

    }

}