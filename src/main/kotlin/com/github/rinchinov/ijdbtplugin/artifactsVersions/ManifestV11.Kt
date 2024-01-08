// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json        = Json { allowStructuredMapKeys = true }
// val manifestV11 = json.parse(ManifestV11.serializer(), jsonString)

package com.github.rinchinov.ijdbtplugin.artifactsVersions

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class ManifestV11 (
    /**
     * A mapping from parent nodes to their dependents
     */
    @SerialName("child_map")
    val childMap: Map<String, List<String>>? = null,

    /**
     * A mapping of the disabled nodes in the target
     */
    val disabled: Map<String, List<AnalysisNode>>? = null,

    /**
     * The docs defined in the dbt project and its dependencies
     */
    val docs: Map<String, Documentation>,

    /**
     * The exposures defined in the dbt project and its dependencies
     */
    val exposures: Map<String, Exposure>,

    /**
     * A mapping from group names to their nodes
     */
    @SerialName("group_map")
    val groupMap: Map<String, List<String>>? = null,

    /**
     * The groups defined in the dbt project
     */
    val groups: Map<String, Group>,

    /**
     * The macros defined in the dbt project and its dependencies
     */
    val macros: Map<String, Macro>,

    /**
     * Metadata about the manifest
     */
    val metadata: ManifestMetadata,

    /**
     * The metrics defined in the dbt project and its dependencies
     */
    val metrics: Map<String, Metric>,

    /**
     * The nodes defined in the dbt project and its dependencies
     */
    val nodes: Map<String, Node>,

    /**
     * A mapping from child nodes to their dependencies
     */
    @SerialName("parent_map")
    val parentMap: Map<String, List<String>>? = null,

    /**
     * The saved queries defined in the dbt project
     */
    @SerialName("saved_queries")
    val savedQueries: Map<String, SavedQuery>,

    /**
     * The selectors defined in selectors.yml
     */
    val selectors: JsonObject,

    /**
     * The semantic models defined in the dbt project
     */
    @SerialName("semantic_models")
    val semanticModels: Map<String, SemanticModel>,

    /**
     * The sources defined in the dbt project and its dependencies
     */
    val sources: Map<String, SourceDefinition>
)

@Serializable
data class AnalysisNode (
    @SerialName("_event_status")
    val eventStatus: JsonObject? = null,

    @SerialName("_pre_injected_sql")
    val preInjectedSql: String? = null,

    val alias: String? = null,

    @SerialName("build_path")
    val buildPath: String? = null,

    val checksum: FileHash? = null,
    val columns: Map<String, ColumnInfo>? = null,
    val compiled: Boolean? = null,

    @SerialName("compiled_code")
    val compiledCode: String? = null,

    @SerialName("compiled_path")
    val compiledPath: String? = null,

    val config: DisabledConfig? = null,

    @SerialName("config_call_dict")
    val configCallDict: JsonObject? = null,

    val contract: Contract? = null,

    @SerialName("created_at")
    val createdAt: Double? = null,

    val database: String? = null,
    val deferred: Boolean? = null,

    @SerialName("depends_on")
    val dependsOn: DependsOn? = null,

    val description: String? = null,
    val docs: Docs? = null,

    @SerialName("extra_ctes")
    val extraCtes: List<InjectedCte>? = null,

    @SerialName("extra_ctes_injected")
    val extraCtesInjected: Boolean? = null,

    val fqn: List<String>,
    val group: String? = null,
    val language: String? = null,
    val meta: JsonObject? = null,
    val metrics: List<Tags>? = null,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    @SerialName("patch_path")
    val patchPath: String? = null,

    val path: String,

    @SerialName("raw_code")
    val rawCode: String? = null,

    val refs: List<RefArgs>? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    @SerialName("resource_type")
    val resourceType: DisabledResourceType,

    val schema: String? = null,
    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @SerialName("unique_id")
    val uniqueId: String,

    @SerialName("unrendered_config")
    val unrenderedConfig: JsonObject? = null,

    val index: Long? = null,
    val access: Access? = null,
    val constraints: List<ModelLevelConstraint>? = null,

    @SerialName("defer_relation")
    val deferRelation: DeferRelation? = null,

    @SerialName("deprecation_date")
    val deprecationDate: String? = null,

    @SerialName("latest_version")
    val latestVersion: Version? = null,

    val version: Version? = null,

    @SerialName("attached_node")
    val attachedNode: String? = null,

    @SerialName("column_name")
    val columnName: String? = null,

    @SerialName("file_key_name")
    val fileKeyName: String? = null,

    @SerialName("test_metadata")
    val testMetadata: TestMetadata? = null,

    @SerialName("root_path")
    val rootPath: String? = null,

    val external: ExternalTable? = null,
    val freshness: FreshnessThreshold? = null,
    val identifier: String? = null,

    @SerialName("loaded_at_field")
    val loadedAtField: String? = null,

    val loader: String? = null,
    val quoting: Quoting? = null,

    @SerialName("source_description")
    val sourceDescription: String? = null,

    @SerialName("source_meta")
    val sourceMeta: JsonObject? = null,

    @SerialName("source_name")
    val sourceName: String? = null,

    val label: String? = null,
    val maturity: Maturity? = null,
    val owner: Owner? = null,
    val type: DisabledType? = null,
    val url: String? = null,
    val filter: WhereFilterIntersection? = null,
    val metadata: SourceFileMetadata? = null,

    @SerialName("type_params")
    val typeParams: MetricTypeParams? = null,

    @SerialName("group_bys")
    val groupBys: List<String>? = null,

    val where: WhereFilterIntersection? = null,
    val defaults: Defaults? = null,
    val dimensions: List<Dimension>? = null,
    val entities: List<Entity>? = null,
    val measures: List<Measure>? = null,
    val model: String? = null,

    @SerialName("node_relation")
    val nodeRelation: NodeRelation? = null,

    @SerialName("primary_entity")
    val primaryEntity: String? = null
)

@Serializable
enum class Access(val value: String) {
    @SerialName("private") Private("private"),
    @SerialName("protected") Protected("protected"),
    @SerialName("public") Public("public");
}

@Serializable
data class FileHash (
    val checksum: String,
    val name: String
)

@Serializable
data class ColumnInfo (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    val constraints: List<ColumnLevelConstraint>? = null,

    @SerialName("data_type")
    val dataType: String? = null,

    val description: String? = null,
    val meta: JsonObject? = null,
    val name: String,
    val quote: Boolean? = null,
    val tags: List<String>? = null
)

@Serializable
data class ColumnLevelConstraint (
    val expression: String? = null,
    val name: String? = null,
    val type: ConstraintType,

    @SerialName("warn_unenforced")
    val warnUnenforced: Boolean? = null,

    @SerialName("warn_unsupported")
    val warnUnsupported: Boolean? = null
)

@Serializable
enum class ConstraintType(val value: String) {
    @SerialName("check") Check("check"),
    @SerialName("custom") Custom("custom"),
    @SerialName("foreign_key") ForeignKey("foreign_key"),
    @SerialName("not_null") NotNull("not_null"),
    @SerialName("primary_key") PrimaryKey("primary_key"),
    @SerialName("unique") Unique("unique");
}

@Serializable
data class DisabledConfig (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    val alias: String? = null,

    @SerialName("column_types")
    val columnTypes: JsonObject? = null,

    val contract: ContractConfig? = null,
    val database: String? = null,
    val docs: Docs? = null,
    val enabled: Boolean? = null,

    @SerialName("full_refresh")
    val fullRefresh: Boolean? = null,

    val grants: JsonObject? = null,
    val group: String? = null,

    @SerialName("incremental_strategy")
    val incrementalStrategy: String? = null,

    val materialized: String? = null,
    val meta: JsonObject? = null,

    @SerialName("on_configuration_change")
    val onConfigurationChange: OnConfigurationChange? = null,

    @SerialName("on_schema_change")
    val onSchemaChange: String? = null,

    val packages: List<String>? = null,

    @SerialName("persist_docs")
    val persistDocs: JsonObject? = null,

    @SerialName("post-hook")
    val postHook: List<Hook>? = null,

    @SerialName("pre-hook")
    val preHook: List<Hook>? = null,

    val quoting: JsonObject? = null,
    val schema: String? = null,
    val tags: Tags? = null,

    @SerialName("unique_key")
    val uniqueKey: UniqueKey? = null,

    @SerialName("error_if")
    val errorIf: String? = null,

    @SerialName("fail_calc")
    val failCalc: String? = null,

    val limit: Long? = null,
    val severity: String? = null,

    @SerialName("store_failures")
    val storeFailures: Boolean? = null,

    @SerialName("warn_if")
    val warnIf: String? = null,

    val where: String? = null,
    val access: Access? = null,

    @SerialName("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @SerialName("target_database")
    val targetDatabase: String? = null,

    @SerialName("target_schema")
    val targetSchema: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    val delimiter: String? = null,

    @SerialName("quote_columns")
    val quoteColumns: Boolean? = null
)

@Serializable
sealed class UniqueKey {
    class StringArrayValue(val value: List<String>) : UniqueKey()
    class StringValue(val value: String)            : UniqueKey()
    class NullValue()                               : UniqueKey()
}

@Serializable
data class ContractConfig (
    @SerialName("alias_types")
    val aliasTypes: Boolean? = null,

    val enforced: Boolean? = null
)

@Serializable
data class Docs (
    @SerialName("node_color")
    val nodeColor: String? = null,

    val show: Boolean? = null
)

@Serializable
enum class OnConfigurationChange(val value: String) {
    @SerialName("apply") Apply("apply"),
    @SerialName("continue") Continue("continue"),
    @SerialName("fail") Fail("fail");
}

@Serializable
data class Hook (
    val index: Long? = null,
    val sql: String,
    val transaction: Boolean? = null
)

@Serializable
sealed class Tags {
    class StringArrayValue(val value: List<String>) : Tags()
    class StringValue(val value: String)            : Tags()
}

@Serializable
data class ModelLevelConstraint (
    val columns: List<String>? = null,
    val expression: String? = null,
    val name: String? = null,
    val type: ConstraintType,

    @SerialName("warn_unenforced")
    val warnUnenforced: Boolean? = null,

    @SerialName("warn_unsupported")
    val warnUnsupported: Boolean? = null
)

@Serializable
data class Contract (
    @SerialName("alias_types")
    val aliasTypes: Boolean? = null,

    val checksum: String? = null,
    val enforced: Boolean? = null
)

@Serializable
data class Defaults (
    @SerialName("agg_time_dimension")
    val aggTimeDimension: String? = null
)

@Serializable
data class DeferRelation (
    val alias: String,
    val database: String? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    val schema: String
)

@Serializable
data class DependsOn (
    val macros: List<String>? = null,
    val nodes: List<String>? = null
)

@Serializable
data class Dimension (
    val description: String? = null,
    val expr: String? = null,

    @SerialName("is_partition")
    val isPartition: Boolean? = null,

    val label: String? = null,
    val metadata: SourceFileMetadata? = null,
    val name: String,
    val type: DimensionType,

    @SerialName("type_params")
    val typeParams: DimensionTypeParams? = null
)

@Serializable
data class SourceFileMetadata (
    @SerialName("file_slice")
    val fileSlice: FileSlice,

    @SerialName("repo_file_path")
    val repoFilePath: String
)

@Serializable
data class FileSlice (
    val content: String,

    @SerialName("end_line_number")
    val endLineNumber: Long,

    val filename: String,

    @SerialName("start_line_number")
    val startLineNumber: Long
)

@Serializable
enum class DimensionType(val value: String) {
    @SerialName("categorical") Categorical("categorical"),
    @SerialName("time") Time("time");
}

@Serializable
data class DimensionTypeParams (
    @SerialName("time_granularity")
    val timeGranularity: Granularity,

    @SerialName("validity_params")
    val validityParams: DimensionValidityParams? = null
)

@Serializable
enum class Granularity(val value: String) {
    @SerialName("day") Day("day"),
    @SerialName("month") Month("month"),
    @SerialName("quarter") Quarter("quarter"),
    @SerialName("week") Week("week"),
    @SerialName("year") Year("year");
}

@Serializable
data class DimensionValidityParams (
    @SerialName("is_end")
    val isEnd: Boolean? = null,

    @SerialName("is_start")
    val isStart: Boolean? = null
)

@Serializable
data class Entity (
    val description: String? = null,
    val expr: String? = null,
    val label: String? = null,
    val name: String,
    val role: String? = null,
    val type: EntityType
)

@Serializable
enum class EntityType(val value: String) {
    @SerialName("foreign") Foreign("foreign"),
    @SerialName("natural") Natural("natural"),
    @SerialName("primary") Primary("primary"),
    @SerialName("unique") Unique("unique");
}

@Serializable
data class ExternalTable (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    @SerialName("file_format")
    val fileFormat: String? = null,

    val location: String? = null,
    val partitions: List<Partition>? = null,

    @SerialName("row_format")
    val rowFormat: String? = null,

    @SerialName("tbl_properties")
    val tblProperties: String? = null
)

@Serializable
sealed class Partition {
    class ExternalPartitionValue(val value: ExternalPartition) : Partition()
    class StringValue(val value: String)                       : Partition()
}

@Serializable
data class ExternalPartition (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    @SerialName("data_type")
    val dataType: String? = null,

    val description: String? = null,
    val meta: JsonObject? = null,
    val name: String? = null
)

@Serializable
data class InjectedCte (
    val id: String,
    val sql: String
)

@Serializable
data class WhereFilterIntersection (
    @SerialName("where_filters")
    val whereFilters: List<WhereFilter>
)

@Serializable
data class WhereFilter (
    @SerialName("where_sql_template")
    val whereSqlTemplate: String
)

@Serializable
data class FreshnessThreshold (
    @SerialName("error_after")
    val errorAfter: Time? = null,

    val filter: String? = null,

    @SerialName("warn_after")
    val warnAfter: Time? = null
)

@Serializable
data class Time (
    val count: Long? = null,
    val period: Period? = null
)

@Serializable
enum class Period(val value: String) {
    @SerialName("day") Day("day"),
    @SerialName("hour") Hour("hour"),
    @SerialName("minute") Minute("minute");
}

@Serializable
sealed class Version {
    class DoubleValue(val value: Double) : Version()
    class StringValue(val value: String) : Version()
    class NullValue()                    : Version()
}

@Serializable
enum class Maturity(val value: String) {
    @SerialName("high") High("high"),
    @SerialName("low") Low("low"),
    @SerialName("medium") Medium("medium");
}

@Serializable
data class Measure (
    val agg: Agg,

    @SerialName("agg_params")
    val aggParams: MeasureAggregationParameters? = null,

    @SerialName("agg_time_dimension")
    val aggTimeDimension: String? = null,

    @SerialName("create_metric")
    val createMetric: Boolean? = null,

    val description: String? = null,
    val expr: String? = null,
    val label: String? = null,
    val name: String,

    @SerialName("non_additive_dimension")
    val nonAdditiveDimension: NonAdditiveDimension? = null
)

@Serializable
enum class Agg(val value: String) {
    @SerialName("average") Average("average"),
    @SerialName("count") Count("count"),
    @SerialName("count_distinct") CountDistinct("count_distinct"),
    @SerialName("max") Max("max"),
    @SerialName("median") Median("median"),
    @SerialName("min") Min("min"),
    @SerialName("percentile") Percentile("percentile"),
    @SerialName("sum") Sum("sum"),
    @SerialName("sum_boolean") SumBoolean("sum_boolean");
}

@Serializable
data class MeasureAggregationParameters (
    val percentile: Double? = null,

    @SerialName("use_approximate_percentile")
    val useApproximatePercentile: Boolean? = null,

    @SerialName("use_discrete_percentile")
    val useDiscretePercentile: Boolean? = null
)

@Serializable
data class NonAdditiveDimension (
    val name: String,

    @SerialName("window_choice")
    val windowChoice: Agg,

    @SerialName("window_groupings")
    val windowGroupings: List<String>
)

@Serializable
data class NodeRelation (
    val alias: String,
    val database: String? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    @SerialName("schema_name")
    val schemaName: String
)

@Serializable
data class Owner (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    val email: String? = null,
    val name: String? = null
)

@Serializable
data class Quoting (
    val column: Boolean? = null,
    val database: Boolean? = null,
    val identifier: Boolean? = null,
    val schema: Boolean? = null
)

@Serializable
data class RefArgs (
    val name: String,

    @SerialName("package")
    val refArgsPackage: String? = null,

    val version: Version? = null
)

@Serializable
enum class DisabledResourceType(val value: String) {
    @SerialName("analysis") Analysis("analysis"),
    @SerialName("doc") Doc("doc"),
    @SerialName("exposure") Exposure("exposure"),
    @SerialName("group") Group("group"),
    @SerialName("macro") Macro("macro"),
    @SerialName("metric") Metric("metric"),
    @SerialName("model") Model("model"),
    @SerialName("operation") Operation("operation"),
    @SerialName("rpc") RPC("rpc"),
    @SerialName("sql_operation") SQLOperation("sql_operation"),
    @SerialName("saved_query") SavedQuery("saved_query"),
    @SerialName("seed") Seed("seed"),
    @SerialName("semantic_model") SemanticModel("semantic_model"),
    @SerialName("snapshot") Snapshot("snapshot"),
    @SerialName("source") Source("source"),
    @SerialName("test") Test("test");
}

@Serializable
data class TestMetadata (
    val kwargs: JsonObject? = null,
    val name: String,
    val namespace: String? = null
)

@Serializable
enum class DisabledType(val value: String) {
    @SerialName("analysis") Analysis("analysis"),
    @SerialName("application") Application("application"),
    @SerialName("cumulative") Cumulative("cumulative"),
    @SerialName("dashboard") Dashboard("dashboard"),
    @SerialName("derived") Derived("derived"),
    @SerialName("ml") Ml("ml"),
    @SerialName("notebook") Notebook("notebook"),
    @SerialName("ratio") Ratio("ratio"),
    @SerialName("simple") Simple("simple");
}

@Serializable
data class MetricTypeParams (
    val denominator: MetricInput? = null,
    val expr: String? = null,

    @SerialName("grain_to_date")
    val grainToDate: Granularity? = null,

    @SerialName("input_measures")
    val inputMeasures: List<MetricInputMeasure>? = null,

    val measure: MetricInputMeasure? = null,
    val metrics: List<MetricInput>? = null,
    val numerator: MetricInput? = null,
    val window: MetricTimeWindow? = null
)

@Serializable
data class MetricInput (
    val alias: String? = null,
    val filter: WhereFilterIntersection? = null,
    val name: String,

    @SerialName("offset_to_grain")
    val offsetToGrain: Granularity? = null,

    @SerialName("offset_window")
    val offsetWindow: MetricTimeWindow? = null
)

@Serializable
data class MetricTimeWindow (
    val count: Long,
    val granularity: Granularity
)

@Serializable
data class MetricInputMeasure (
    val alias: String? = null,

    @SerialName("fill_nulls_with")
    val fillNullsWith: Long? = null,

    val filter: WhereFilterIntersection? = null,

    @SerialName("join_to_timespine")
    val joinToTimespine: Boolean? = null,

    val name: String
)

@Serializable
data class Documentation (
    @SerialName("block_contents")
    val blockContents: String,

    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    val path: String,

    @SerialName("resource_type")
    val resourceType: DocResourceType,

    @SerialName("unique_id")
    val uniqueId: String
)

@Serializable
enum class DocResourceType(val value: String) {
    @SerialName("doc") Doc("doc");
}

@Serializable
data class Exposure (
    val config: ExposureConfig? = null,

    @SerialName("created_at")
    val createdAt: Double? = null,

    @SerialName("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    val description: String? = null,
    val fqn: List<String>,
    val label: String? = null,
    val maturity: Maturity? = null,
    val meta: JsonObject? = null,
    val metrics: List<List<String>>? = null,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    val owner: Owner,

    @SerialName("package_name")
    val packageName: String,

    val path: String,
    val refs: List<RefArgs>? = null,

    @SerialName("resource_type")
    val resourceType: ExposureResourceType,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,
    val type: ExposureType,

    @SerialName("unique_id")
    val uniqueId: String,

    @SerialName("unrendered_config")
    val unrenderedConfig: JsonObject? = null,

    val url: String? = null
)

@Serializable
data class ExposureConfig (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    val enabled: Boolean? = null
)

@Serializable
data class ExposureDependsOn (
    val macros: List<String>? = null,
    val nodes: List<String>? = null
)

@Serializable
enum class ExposureResourceType(val value: String) {
    @SerialName("exposure") Exposure("exposure");
}

@Serializable
enum class ExposureType(val value: String) {
    @SerialName("analysis") Analysis("analysis"),
    @SerialName("application") Application("application"),
    @SerialName("dashboard") Dashboard("dashboard"),
    @SerialName("ml") Ml("ml"),
    @SerialName("notebook") Notebook("notebook");
}

@Serializable
data class Group (
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    val owner: Owner,

    @SerialName("package_name")
    val packageName: String,

    val path: String,

    @SerialName("resource_type")
    val resourceType: GroupResourceType,

    @SerialName("unique_id")
    val uniqueId: String
)

@Serializable
enum class GroupResourceType(val value: String) {
    @SerialName("group") Group("group");
}

@Serializable
data class Macro (
    val arguments: List<MacroArgument>? = null,

    @SerialName("created_at")
    val createdAt: Double? = null,

    @SerialName("depends_on")
    val dependsOn: MacroDependsOn? = null,

    val description: String? = null,
    val docs: Docs? = null,

    @SerialName("macro_sql")
    val macroSql: String,

    val meta: JsonObject? = null,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    @SerialName("patch_path")
    val patchPath: String? = null,

    val path: String,

    @SerialName("resource_type")
    val resourceType: MacroResourceType,

    @SerialName("supported_languages")
    val supportedLanguages: List<SupportedLanguage>? = null,

    @SerialName("unique_id")
    val uniqueId: String
)

@Serializable
data class MacroArgument (
    val description: String? = null,
    val name: String,
    val type: String? = null
)

@Serializable
data class MacroDependsOn (
    val macros: List<String>? = null
)

@Serializable
enum class MacroResourceType(val value: String) {
    @SerialName("macro") Macro("macro");
}

@Serializable
enum class SupportedLanguage(val value: String) {
    @SerialName("python") Python("python"),
    @SerialName("sql") SQL("sql");
}

/**
 * Metadata about the manifest
 */
@Serializable
data class ManifestMetadata (
    /**
     * The type name of the adapter
     */
    @SerialName("adapter_type")
    val adapterType: String? = null,

    @SerialName("dbt_schema_version")
    val dbtSchemaVersion: String? = null,

    @SerialName("dbt_version")
    val dbtVersion: String? = null,

    val env: Map<String, String>? = null,

    @SerialName("generated_at")
    val generatedAt: String? = null,

    @SerialName("invocation_id")
    val invocationId: String? = null,

    /**
     * A unique identifier for the project, hashed from the project name
     */
    @SerialName("project_id")
    val projectId: String? = null,

    /**
     * Name of the root project
     */
    @SerialName("project_name")
    val projectName: String? = null,

    /**
     * Whether dbt is configured to send anonymous usage statistics
     */
    @SerialName("send_anonymous_usage_stats")
    val sendAnonymousUsageStats: Boolean? = null,

    /**
     * A unique identifier for the user
     */
    @SerialName("user_id")
    val userId: String? = null
)

@Serializable
data class Metric (
    val config: MetricConfig? = null,

    @SerialName("created_at")
    val createdAt: Double? = null,

    @SerialName("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    val description: String,
    val filter: WhereFilterIntersection? = null,
    val fqn: List<String>,
    val group: String? = null,
    val label: String,
    val meta: JsonObject? = null,
    val metadata: SourceFileMetadata? = null,
    val metrics: List<List<String>>? = null,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    val path: String,
    val refs: List<RefArgs>? = null,

    @SerialName("resource_type")
    val resourceType: MetricResourceType,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,
    val type: MetricType,

    @SerialName("type_params")
    val typeParams: MetricTypeParams,

    @SerialName("unique_id")
    val uniqueId: String,

    @SerialName("unrendered_config")
    val unrenderedConfig: JsonObject? = null
)

@Serializable
data class MetricConfig (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    val enabled: Boolean? = null,
    val group: String? = null
)

@Serializable
enum class MetricResourceType(val value: String) {
    @SerialName("metric") Metric("metric");
}

@Serializable
enum class MetricType(val value: String) {
    @SerialName("cumulative") Cumulative("cumulative"),
    @SerialName("derived") Derived("derived"),
    @SerialName("ratio") Ratio("ratio"),
    @SerialName("simple") Simple("simple");
}

@Serializable
data class Node (
    @SerialName("_event_status")
    val eventStatus: JsonObject? = null,

    @SerialName("_pre_injected_sql")
    val preInjectedSql: String? = null,

    val alias: String,

    @SerialName("build_path")
    val buildPath: String? = null,

    val checksum: FileHash,
    val columns: Map<String, ColumnInfo>? = null,
    val compiled: Boolean? = null,

    @SerialName("compiled_code")
    val compiledCode: String? = null,

    @SerialName("compiled_path")
    val compiledPath: String? = null,

    val config: NodeConfig? = null,

    @SerialName("config_call_dict")
    val configCallDict: JsonObject? = null,

    val contract: Contract? = null,

    @SerialName("created_at")
    val createdAt: Double? = null,

    val database: String? = null,
    val deferred: Boolean? = null,

    @SerialName("depends_on")
    val dependsOn: DependsOn? = null,

    val description: String? = null,
    val docs: Docs? = null,

    @SerialName("extra_ctes")
    val extraCtes: List<InjectedCte>? = null,

    @SerialName("extra_ctes_injected")
    val extraCtesInjected: Boolean? = null,

    val fqn: List<String>,
    val group: String? = null,
    val language: String? = null,
    val meta: JsonObject? = null,
    val metrics: List<List<String>>? = null,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    @SerialName("patch_path")
    val patchPath: String? = null,

    val path: String,

    @SerialName("raw_code")
    val rawCode: String? = null,

    val refs: List<RefArgs>? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    @SerialName("resource_type")
    val resourceType: NodeResourceType,

    val schema: String,
    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @SerialName("unique_id")
    val uniqueId: String,

    @SerialName("unrendered_config")
    val unrenderedConfig: JsonObject? = null,

    val index: Long? = null,
    val access: Access? = null,
    val constraints: List<ModelLevelConstraint>? = null,

    @SerialName("defer_relation")
    val deferRelation: DeferRelation? = null,

    @SerialName("deprecation_date")
    val deprecationDate: String? = null,

    @SerialName("latest_version")
    val latestVersion: Version? = null,

    val version: Version? = null,

    @SerialName("attached_node")
    val attachedNode: String? = null,

    @SerialName("column_name")
    val columnName: String? = null,

    @SerialName("file_key_name")
    val fileKeyName: String? = null,

    @SerialName("test_metadata")
    val testMetadata: TestMetadata? = null,

    @SerialName("root_path")
    val rootPath: String? = null
)

@Serializable
data class NodeConfig (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    val alias: String? = null,

    @SerialName("column_types")
    val columnTypes: JsonObject? = null,

    val contract: ContractConfig? = null,
    val database: String? = null,
    val docs: Docs? = null,
    val enabled: Boolean? = null,

    @SerialName("full_refresh")
    val fullRefresh: Boolean? = null,

    val grants: JsonObject? = null,
    val group: String? = null,

    @SerialName("incremental_strategy")
    val incrementalStrategy: String? = null,

    val materialized: String? = null,
    val meta: JsonObject? = null,

    @SerialName("on_configuration_change")
    val onConfigurationChange: OnConfigurationChange? = null,

    @SerialName("on_schema_change")
    val onSchemaChange: String? = null,

    val packages: List<String>? = null,

    @SerialName("persist_docs")
    val persistDocs: JsonObject? = null,

    @SerialName("post-hook")
    val postHook: List<Hook>? = null,

    @SerialName("pre-hook")
    val preHook: List<Hook>? = null,

    val quoting: JsonObject? = null,
    val schema: String? = null,
    val tags: Tags? = null,

    @SerialName("unique_key")
    val uniqueKey: UniqueKey? = null,

    @SerialName("error_if")
    val errorIf: String? = null,

    @SerialName("fail_calc")
    val failCalc: String? = null,

    val limit: Long? = null,
    val severity: String? = null,

    @SerialName("store_failures")
    val storeFailures: Boolean? = null,

    @SerialName("warn_if")
    val warnIf: String? = null,

    val where: String? = null,
    val access: Access? = null,

    @SerialName("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @SerialName("target_database")
    val targetDatabase: String? = null,

    @SerialName("target_schema")
    val targetSchema: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    val delimiter: String? = null,

    @SerialName("quote_columns")
    val quoteColumns: Boolean? = null
)

@Serializable
enum class NodeResourceType(val value: String) {
    @SerialName("analysis") Analysis("analysis"),
    @SerialName("model") Model("model"),
    @SerialName("operation") Operation("operation"),
    @SerialName("rpc") RPC("rpc"),
    @SerialName("sql_operation") SQLOperation("sql_operation"),
    @SerialName("seed") Seed("seed"),
    @SerialName("snapshot") Snapshot("snapshot"),
    @SerialName("test") Test("test");
}

@Serializable
data class SavedQuery (
    val config: SavedQueryConfig? = null,

    @SerialName("created_at")
    val createdAt: Double? = null,

    @SerialName("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    val description: String? = null,
    val fqn: List<String>,
    val group: String? = null,

    @SerialName("group_bys")
    val groupBys: List<String>,

    val label: String? = null,
    val metadata: SourceFileMetadata? = null,
    val metrics: List<String>,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    val path: String,
    val refs: List<RefArgs>? = null,

    @SerialName("resource_type")
    val resourceType: DisabledResourceType,

    @SerialName("unique_id")
    val uniqueId: String,

    @SerialName("unrendered_config")
    val unrenderedConfig: JsonObject? = null,

    val where: WhereFilterIntersection? = null
)

@Serializable
data class SavedQueryConfig (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    val enabled: Boolean? = null,
    val group: String? = null,
    val meta: JsonObject? = null
)

@Serializable
data class SemanticModel (
    val config: SemanticModelConfig? = null,

    @SerialName("created_at")
    val createdAt: Double? = null,

    val defaults: Defaults? = null,

    @SerialName("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    val description: String? = null,
    val dimensions: List<Dimension>? = null,
    val entities: List<Entity>? = null,
    val fqn: List<String>,
    val group: String? = null,
    val label: String? = null,
    val measures: List<Measure>? = null,
    val metadata: SourceFileMetadata? = null,
    val model: String,
    val name: String,

    @SerialName("node_relation")
    val nodeRelation: NodeRelation? = null,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    val path: String,

    @SerialName("primary_entity")
    val primaryEntity: String? = null,

    val refs: List<RefArgs>? = null,

    @SerialName("resource_type")
    val resourceType: DisabledResourceType,

    @SerialName("unique_id")
    val uniqueId: String,

    @SerialName("unrendered_config")
    val unrenderedConfig: JsonObject? = null
)

@Serializable
data class SemanticModelConfig (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    val enabled: Boolean? = null,
    val group: String? = null,
    val meta: JsonObject? = null
)

@Serializable
data class SourceDefinition (
    @SerialName("_event_status")
    val eventStatus: JsonObject? = null,

    val columns: Map<String, ColumnInfo>? = null,
    val config: SourceConfig? = null,

    @SerialName("created_at")
    val createdAt: Double? = null,

    val database: String? = null,
    val description: String? = null,
    val external: ExternalTable? = null,
    val fqn: List<String>,
    val freshness: FreshnessThreshold? = null,
    val identifier: String,

    @SerialName("loaded_at_field")
    val loadedAtField: String? = null,

    val loader: String,
    val meta: JsonObject? = null,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    @SerialName("patch_path")
    val patchPath: String? = null,

    val path: String,
    val quoting: Quoting? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    @SerialName("resource_type")
    val resourceType: SourceResourceType,

    val schema: String,

    @SerialName("source_description")
    val sourceDescription: String,

    @SerialName("source_meta")
    val sourceMeta: JsonObject? = null,

    @SerialName("source_name")
    val sourceName: String,

    val tags: List<String>? = null,

    @SerialName("unique_id")
    val uniqueId: String,

    @SerialName("unrendered_config")
    val unrenderedConfig: JsonObject? = null
)

@Serializable
data class SourceConfig (
    @SerialName("_extra")
    val extra: JsonObject? = null,

    val enabled: Boolean? = null
)

@Serializable
enum class SourceResourceType(val value: String) {
    @SerialName("source") Source("source");
}
