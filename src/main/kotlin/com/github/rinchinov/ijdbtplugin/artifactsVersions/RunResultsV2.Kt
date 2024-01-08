// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json         = Json { allowStructuredMapKeys = true }
// val runResultsV2 = json.parse(RunResultsV2.serializer(), jsonString)

package com.github.rinchinov.ijdbtplugin.artifactsVersions

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

/**
 * RunResultsArtifact(metadata: dbt.contracts.util.BaseArtifactMetadata, results:
 * Sequence[dbt.contracts.results.RunResultOutput], elapsed_time: float, args: Dict[str,
 * Any] = <factory>)
 */
@Serializable
data class RunResultsV2 (
    val args: JsonObject? = null,

    @SerialName("elapsed_time")
    val elapsedTime: Double,

    val metadata: BaseArtifactMetadata,
    val results: List<RunResultOutput>
)

/**
 * BaseArtifactMetadata(dbt_schema_version: str, dbt_version: str = '0.20.0rc1',
 * generated_at: datetime.datetime = <factory>, invocation_id: Union[str, NoneType] =
 * <factory>, env: Dict[str, str] = <factory>)
 */
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
): RunResultsInterface()

/**
 * RunResultOutput(status: Union[dbt.contracts.results.RunStatus,
 * dbt.contracts.results.TestStatus, dbt.contracts.results.FreshnessStatus], timing:
 * List[dbt.contracts.results.TimingInfo], thread_id: str, execution_time: float,
 * adapter_response: Dict[str, Any], message: Union[str, NoneType], failures: Union[int,
 * NoneType], unique_id: str)
 */
@Serializable
data class RunResultOutput (
    @SerialName("adapter_response")
    val adapterResponse: JsonObject,

    @SerialName("execution_time")
    val executionTime: Double,

    val failures: Long? = null,
    val message: String? = null,
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

/**
 * TimingInfo(name: str, started_at: Union[datetime.datetime, NoneType] = None,
 * completed_at: Union[datetime.datetime, NoneType] = None)
 */
@Serializable
data class TimingInfo (
    @SerialName("completed_at")
    val completedAt: String? = null,

    val name: String,

    @SerialName("started_at")
    val startedAt: String? = null
)
