package com.github.rinchinov.ijdbtplugin.artifactsVersions.runResultsV1

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
    convert(Status::class,  { Status.fromValue(it.asText()) }, { "\"${it.value}\"" })
    convert(Message::class, { Message.fromJson(it) },          { it.toJson() }, true)
}

/**
 * RunResultsArtifact(metadata: dbt.contracts.util.BaseArtifactMetadata, results:
 * Sequence[dbt.contracts.results.RunResultOutput], elapsed_time: float, args: Dict[str,
 * Any] = <factory>)
 */
data class RunResultsV1 (
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
        fun fromJson(json: String) = mapper.readValue<RunResultsV1>(json)
    }
}

/**
 * BaseArtifactMetadata(dbt_schema_version: str, dbt_version: str = '0.19.0', generated_at:
 * datetime.datetime = <factory>, invocation_id: Union[str, NoneType] = <factory>, env:
 * Dict[str, str] = <factory>)
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
 * List[dbt.contracts.results.TimingInfo], thread_id: str, execution_time: float, message:
 * Union[str, int, NoneType], adapter_response: Dict[str, Any], unique_id: str)
 */
data class RunResultOutput (
    @get:JsonProperty("adapter_response", required=true)@field:JsonProperty("adapter_response", required=true)
    val adapterResponse: Map<String, Any?>,

    @get:JsonProperty("execution_time", required=true)@field:JsonProperty("execution_time", required=true)
    val executionTime: Double,

    val message: Message? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val status: Status,

    @get:JsonProperty("thread_id", required=true)@field:JsonProperty("thread_id", required=true)
    val threadId: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val timing: List<TimingInfo>,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String
)

sealed class Message {
    class IntegerValue(val value: Long)  : Message()
    class StringValue(val value: String) : Message()
    class NullValue()                    : Message()

    fun toJson(): String = mapper.writeValueAsString(when (this) {
        is IntegerValue -> this.value
        is StringValue  -> this.value
        is NullValue    -> "null"
    })

    companion object {
        fun fromJson(jn: JsonNode): Message = when (jn) {
            is IntNode, is LongNode -> IntegerValue(mapper.treeToValue(jn))
            is TextNode             -> StringValue(mapper.treeToValue(jn))
            null                    -> NullValue()
            else                    -> throw IllegalArgumentException()
        }
    }
}

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

