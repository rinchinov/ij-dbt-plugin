package com.github.rinchinov.ijdbtplugin.artifactsVersions.runResultsV3

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.*
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.*
import com.github.rinchinov.ijdbtplugin.artifactInterfaces.RunResultsInterface


@Suppress("UNCHECKED_CAST")
private fun <T> ObjectMapper.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonNode) -> T, toJson: (T) -> String, isUnion: Boolean = false) = registerModule(SimpleModule().apply {
    addSerializer(k.java as Class<T>, object : StdSerializer<T>(k.java as Class<T>) {
        override fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider) = gen.writeRawValue(toJson(value))
    })
    addDeserializer(k.java as Class<T>, object : StdDeserializer<T>(k.java as Class<T>) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = fromJson(p.readValueAsTree())
    })
})

val mapper = jacksonObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    convert(Status::class, { Status.fromValue(it.asText()) }, { "\"${it.value}\"" })
}

/**
 * RunResultsArtifact(metadata: dbt.contracts.util.BaseArtifactMetadata, results:
 * Sequence[dbt.contracts.results.RunResultOutput], elapsed_time: float, args: Dict[str,
 * Any] = <factory>)
 */
data class RunResultsV3 (
    val args: Map<String, Any?>? = null,

    @get:JsonProperty("elapsed_time", required=true)@field:JsonProperty("elapsed_time", required=true)
    val elapsedTime: Double,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val metadata: BaseArtifactMetadata,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val results: List<RunResultOutput>
): RunResultsInterface {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapper.readValue<RunResultsV3>(json)
    }
}

/**
 * BaseArtifactMetadata(dbt_schema_version: str, dbt_version: str = '0.21.0rc1',
 * generated_at: datetime.datetime = <factory>, invocation_id: Union[str, NoneType] =
 * <factory>, env: Dict[str, str] = <factory>)
 */
data class BaseArtifactMetadata (
    @get:JsonProperty("dbt_schema_version", required=true)@field:JsonProperty("dbt_schema_version", required=true)
    val dbtSchemaVersion: String,

    @get:JsonProperty("dbt_version")@field:JsonProperty("dbt_version")
    val dbtVersion: String? = null,

    val env: Map<String, String>? = null,

    @get:JsonProperty("generated_at")@field:JsonProperty("generated_at")
    val generatedAt: String? = null,

    @get:JsonProperty("invocation_id")@field:JsonProperty("invocation_id")
    val invocationId: String? = null
)

/**
 * RunResultOutput(status: Union[dbt.contracts.results.RunStatus,
 * dbt.contracts.results.TestStatus, dbt.contracts.results.FreshnessStatus], timing:
 * List[dbt.contracts.results.TimingInfo], thread_id: str, execution_time: float,
 * adapter_response: Dict[str, Any], message: Union[str, NoneType], failures: Union[int,
 * NoneType], unique_id: str)
 */
data class RunResultOutput (
    @get:JsonProperty("adapter_response", required=true)@field:JsonProperty("adapter_response", required=true)
    val adapterResponse: Map<String, Any?>,

    @get:JsonProperty("execution_time", required=true)@field:JsonProperty("execution_time", required=true)
    val executionTime: Double,

    val failures: Long? = null,
    val message: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val status: Status,

    @get:JsonProperty("thread_id", required=true)@field:JsonProperty("thread_id", required=true)
    val threadId: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val timing: List<TimingInfo>,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String
)

enum class Status(val value: String) {
    Error("error"),
    Fail("fail"),
    Pass("pass"),
    RuntimeError("runtime error"),
    Skipped("skipped"),
    Success("success"),
    Warn("warn");

    companion object {
        fun fromValue(value: String): Status = when (value) {
            "error"         -> Error
            "fail"          -> Fail
            "pass"          -> Pass
            "runtime error" -> RuntimeError
            "skipped"       -> Skipped
            "success"       -> Success
            "warn"          -> Warn
            else            -> throw IllegalArgumentException()
        }
    }
}

/**
 * TimingInfo(name: str, started_at: Union[datetime.datetime, NoneType] = None,
 * completed_at: Union[datetime.datetime, NoneType] = None)
 */
data class TimingInfo (
    @get:JsonProperty("completed_at")@field:JsonProperty("completed_at")
    val completedAt: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("started_at")@field:JsonProperty("started_at")
    val startedAt: String? = null
)

