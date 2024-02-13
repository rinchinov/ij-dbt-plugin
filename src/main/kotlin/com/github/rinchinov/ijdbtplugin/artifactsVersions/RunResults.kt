package com.github.rinchinov.ijdbtplugin.artifactsVersions

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.*
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.*


@Suppress("UNCHECKED_CAST")
private fun <T> ObjectMapper.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonNode) -> T, toJson: (T) -> String, isUnion: Boolean = false) = registerModule(SimpleModule().apply {
    addSerializer(k.java as Class<T>, object : StdSerializer<T>(k.java as Class<T>) {
        override fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider) = gen.writeRawValue(toJson(value))
    })
    addDeserializer(k.java as Class<T>, object : StdDeserializer<T>(k.java as Class<T>) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = fromJson(p.readValueAsTree())
    })
})

val mapperRunResults = jacksonObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    convert(Status::class, { Status.fromValue(it.asText()) }, { "\"${it.value}\"" })
}

data class RunResults (
    val args: Map<String, Any?>? = null,

    @get:JsonProperty("elapsed_time", required=true)@field:JsonProperty("elapsed_time", required=true)
    val elapsedTime: Double,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val metadata: BaseArtifactMetadata,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val results: List<RunResultOutput>
) {
    fun toJson() = mapperRunResults.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapperRunResults.readValue<RunResults>(json)
    }
}

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

data class RunResultOutput (
    @get:JsonProperty("adapter_response", required=true)@field:JsonProperty("adapter_response", required=true)
    val adapterResponse: Map<String, Any?>,

    val compiled: Boolean? = null,

    @get:JsonProperty("compiled_code")@field:JsonProperty("compiled_code")
    val compiledCode: String? = null,

    @get:JsonProperty("execution_time", required=true)@field:JsonProperty("execution_time", required=true)
    val executionTime: Double,

    val failures: Long? = null,
    val message: String? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

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

data class TimingInfo (
    @get:JsonProperty("completed_at")@field:JsonProperty("completed_at")
    val completedAt: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("started_at")@field:JsonProperty("started_at")
    val startedAt: String? = null
)

