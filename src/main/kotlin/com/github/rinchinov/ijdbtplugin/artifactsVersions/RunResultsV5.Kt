// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json         = Json { allowStructuredMapKeys = true }
// val runResultsV5 = json.parse(RunResultsV5.serializer(), jsonString)

package com.github.rinchinov.ijdbtplugin.artifactsVersions

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class RunResultsV5 (
    val args: JsonObject? = null,

    @SerialName("elapsed_time")
    val elapsedTime: Double,

    val metadata: BaseArtifactMetadata,
    val results: List<RunResultOutput>
)

@Serializable
data class BaseArtifactMetadata (
    @SerialName("dbt_schema_version")
    val dbtSchemaVersion: String,

    @SerialName("dbt_version")
    val dbtVersion: String? = null,

    val env: Map<String, String>? = null,

    @SerialName("generated_at")
    val generatedAt: String? = null,

    @SerialName("invocation_id")
    val invocationId: String? = null
)

@Serializable
data class RunResultOutput (
    @SerialName("adapter_response")
    val adapterResponse: JsonObject,

    val compiled: Boolean? = null,

    @SerialName("compiled_code")
    val compiledCode: String? = null,

    @SerialName("execution_time")
    val executionTime: Double,

    val failures: Long? = null,
    val message: String? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    val status: Status,

    @SerialName("thread_id")
    val threadId: String,

    val timing: List<TimingInfo>,

    @SerialName("unique_id")
    val uniqueId: String
)

@Serializable
enum class Status(val value: String) {
    @SerialName("error") Error("error"),
    @SerialName("fail") Fail("fail"),
    @SerialName("pass") Pass("pass"),
    @SerialName("runtime error") RuntimeError("runtime error"),
    @SerialName("skipped") Skipped("skipped"),
    @SerialName("success") Success("success"),
    @SerialName("warn") Warn("warn");
}

@Serializable
data class TimingInfo (
    @SerialName("completed_at")
    val completedAt: String? = null,

    val name: String,

    @SerialName("started_at")
    val startedAt: String? = null
)
