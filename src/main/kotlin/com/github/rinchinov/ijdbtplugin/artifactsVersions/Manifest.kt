package com.github.rinchinov.ijdbtplugin.artifactsVersions

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.*
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.*
import com.jetbrains.rd.util.first


fun listToNestedMap(list: List<String>): Map<String, Map<String, Map<String, String>>> {
    val result = mutableMapOf<String, MutableMap<String, MutableMap<String, String>>>()
    list.forEach { value ->
        val parts = value.split(".")

        if (parts.size >= 3) {
            val firstKey = parts[0]
            val secondKey = parts[1]
            val thirdKey = parts.drop(2).joinToString(separator = ".")

            // Ensure the nested maps exist
            val secondLevelMap = result.getOrPut(firstKey) { mutableMapOf() }
            val thirdLevelMap = secondLevelMap.getOrPut(secondKey) { mutableMapOf() }

            // Assign the value
            thirdLevelMap[thirdKey] = value
        }
    }
    return result
}
@Suppress("UNCHECKED_CAST")
private fun <T> ObjectMapper.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonNode) -> T, toJson: (T) -> String, isUnion: Boolean = false) = registerModule(SimpleModule().apply {
    addSerializer(k.java as Class<T>, object : StdSerializer<T>(k.java as Class<T>) {
        override fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider) = gen.writeRawValue(toJson(value))
    })
    addDeserializer(k.java as Class<T>, object : StdDeserializer<T>(k.java as Class<T>) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = fromJson(p.readValueAsTree())
    })
})

val mapperManifest = jacksonObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    convert(Access::class,                { Access.fromValue(it.asText()) },                { "\"${it.value}\"" })
    convert(ConstraintType::class,        { ConstraintType.fromValue(it.asText()) },        { "\"${it.value}\"" })
    convert(OnConfigurationChange::class, { OnConfigurationChange.fromValue(it.asText()) }, { "\"${it.value}\"" })
    convert(DimensionType::class,         { DimensionType.fromValue(it.asText()) },         { "\"${it.value}\"" })
    convert(Granularity::class,           { Granularity.fromValue(it.asText()) },           { "\"${it.value}\"" })
    convert(EntityType::class,            { EntityType.fromValue(it.asText()) },            { "\"${it.value}\"" })
    convert(Period::class,                { Period.fromValue(it.asText()) },                { "\"${it.value}\"" })
    convert(Maturity::class,              { Maturity.fromValue(it.asText()) },              { "\"${it.value}\"" })
    convert(Agg::class,                   { Agg.fromValue(it.asText()) },                   { "\"${it.value}\"" })
    convert(DisabledResourceType::class,  { DisabledResourceType.fromValue(it.asText()) },  { "\"${it.value}\"" })
    convert(DisabledType::class,          { DisabledType.fromValue(it.asText()) },          { "\"${it.value}\"" })
    convert(DocResourceType::class,       { DocResourceType.fromValue(it.asText()) },       { "\"${it.value}\"" })
    convert(ExposureResourceType::class,  { ExposureResourceType.fromValue(it.asText()) },  { "\"${it.value}\"" })
    convert(ExposureType::class,          { ExposureType.fromValue(it.asText()) },          { "\"${it.value}\"" })
    convert(GroupResourceType::class,     { GroupResourceType.fromValue(it.asText()) },     { "\"${it.value}\"" })
    convert(MacroResourceType::class,     { MacroResourceType.fromValue(it.asText()) },     { "\"${it.value}\"" })
    convert(SupportedLanguage::class,     { SupportedLanguage.fromValue(it.asText()) },     { "\"${it.value}\"" })
    convert(MetricResourceType::class,    { MetricResourceType.fromValue(it.asText()) },    { "\"${it.value}\"" })
    convert(MetricType::class,            { MetricType.fromValue(it.asText()) },            { "\"${it.value}\"" })
    convert(NodeResourceType::class,      { NodeResourceType.fromValue(it.asText()) },      { "\"${it.value}\"" })
    convert(SourceResourceType::class,    { SourceResourceType.fromValue(it.asText()) },    { "\"${it.value}\"" })
    convert(UniqueKey::class,             { UniqueKey.fromJson(it) },                       { it.toJson() }, true)
    convert(Tags::class,                  { Tags.fromJson(it) },                            { it.toJson() }, true)
    convert(Partition::class,             { Partition.fromJson(it) },                       { it.toJson() }, true)
    convert(Version::class,               { Version.fromJson(it) },                         { it.toJson() }, true)
}

data class Manifest (
    /**
     * A mapping from parent nodes to their dependents
     */
    @get:JsonProperty("child_map")@field:JsonProperty("child_map")
    val childMap: Map<String, List<String>>? = null,

    /**
     * A mapping of the disabled nodes in the target
     */
    val disabled: Map<String, List<AnalysisNode>>? = null,

    /**
     * The docs defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val docs: Map<String, Documentation>,

    /**
     * The exposures defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val exposures: Map<String, Exposure>,

    /**
     * A mapping from group names to their nodes
     */
    @get:JsonProperty("group_map")@field:JsonProperty("group_map")
    val groupMap: Map<String, List<String>>? = null,

    /**
     * The groups defined in the dbt project
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val groups: Map<String, Group>,

    /**
     * The macros defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val macros: Map<String, Macro>,

    /**
     * Metadata about the manifest
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val metadata: ManifestMetadata,

    /**
     * The metrics defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val metrics: Map<String, Metric>,

    /**
     * The nodes defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val nodes: Map<String, Node>,

    /**
     * A mapping from child nodes to their dependencies
     */
    @get:JsonProperty("parent_map")@field:JsonProperty("parent_map")
    val parentMap: Map<String, List<String>>? = null,

    /**
     * The saved queries defined in the dbt project
     */
    @get:JsonProperty("saved_queries")@field:JsonProperty("saved_queries")
    val savedQueries: Map<String, SavedQuery>?,

    /**
     * The selectors defined in selectors.yml
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val selectors: Map<String, Any?>,

    /**
     * The semantic models defined in the dbt project
     */
    @get:JsonProperty("semantic_models", required=true)@field:JsonProperty("semantic_models")
    val semanticModels: Map<String, SemanticModel>?,

    /**
     * The sources defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val sources: Map<String, SourceDefinition>,

    /**
     * Maps that help to navigate over manifest faster
     */
    val resourceMap: Map<
            String, // type model/source/test/macro/etc
            Map<
                    String, // packageName
                    Map<
                            String, // lastPart
                            String // uniqueId
                            >
                    >
            >? = listToNestedMap(macros.keys.toList().plus(nodes.keys.toList()).plus(sources.keys.toList()))
){
    fun toJson() = mapperManifest.writeValueAsString(this)
    fun getProjectName(): String{
        return metadata.projectName ?: nodes.first().value.packageName
    }
    companion object {
        fun fromJson(json: String) = mapperManifest.readValue<Manifest>(json)
    }
}

data class AnalysisNode (
    @get:JsonProperty("_event_status")@field:JsonProperty("_event_status")
    val eventStatus: Map<String, Any?>? = null,

    @get:JsonProperty("_pre_injected_sql")@field:JsonProperty("_pre_injected_sql")
    val preInjectedSql: String? = null,

    val alias: String? = null,

    @get:JsonProperty("build_path")@field:JsonProperty("build_path")
    val buildPath: String? = null,

    val checksum: FileHash? = null,
    val columns: Map<String, ColumnInfo>? = null,
    val compiled: Boolean? = null,

    @get:JsonProperty("compiled_code")@field:JsonProperty("compiled_code")
    val compiledCode: String? = null,

    @get:JsonProperty("compiled_path")@field:JsonProperty("compiled_path")
    val compiledPath: String? = null,

    val config: DisabledConfig? = null,

    @get:JsonProperty("config_call_dict")@field:JsonProperty("config_call_dict")
    val configCallDict: Map<String, Any?>? = null,

    val contract: Contract? = null,

    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    val database: String? = null,
    val deferred: Boolean? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: DependsOn? = null,

    val description: String? = null,
    val docs: Docs? = null,

    @get:JsonProperty("extra_ctes")@field:JsonProperty("extra_ctes")
    val extraCtes: List<InjectedCte>? = null,

    @get:JsonProperty("extra_ctes_injected")@field:JsonProperty("extra_ctes_injected")
    val extraCtesInjected: Boolean? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val fqn: List<String>,

    val group: String? = null,
    val language: String? = null,
    val meta: Map<String, Any?>? = null,
    val metrics: List<Tags>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty("patch_path")@field:JsonProperty("patch_path")
    val patchPath: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    @get:JsonProperty("raw_code")@field:JsonProperty("raw_code")
    val rawCode: String? = null,

    val refs: List<RefArgs>? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: DisabledResourceType,

    val schema: String? = null,
    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    @get:JsonProperty("unrendered_config")@field:JsonProperty("unrendered_config")
    val unrenderedConfig: Map<String, Any?>? = null,

    val index: Long? = null,
    val access: Access? = null,
    val constraints: List<ModelLevelConstraint>? = null,

    @get:JsonProperty("defer_relation")@field:JsonProperty("defer_relation")
    val deferRelation: DeferRelation? = null,

    @get:JsonProperty("deprecation_date")@field:JsonProperty("deprecation_date")
    val deprecationDate: String? = null,

    @get:JsonProperty("latest_version")@field:JsonProperty("latest_version")
    val latestVersion: Version? = null,

    val version: Version? = null,

    @get:JsonProperty("attached_node")@field:JsonProperty("attached_node")
    val attachedNode: String? = null,

    @get:JsonProperty("column_name")@field:JsonProperty("column_name")
    val columnName: String? = null,

    @get:JsonProperty("file_key_name")@field:JsonProperty("file_key_name")
    val fileKeyName: String? = null,

    @get:JsonProperty("test_metadata")@field:JsonProperty("test_metadata")
    val testMetadata: TestMetadata? = null,

    @get:JsonProperty("root_path")@field:JsonProperty("root_path")
    val rootPath: String? = null,

    val external: ExternalTable? = null,
    val freshness: FreshnessThreshold? = null,
    val identifier: String? = null,

    @get:JsonProperty("loaded_at_field")@field:JsonProperty("loaded_at_field")
    val loadedAtField: String? = null,

    val loader: String? = null,
    val quoting: Quoting? = null,

    @get:JsonProperty("source_description")@field:JsonProperty("source_description")
    val sourceDescription: String? = null,

    @get:JsonProperty("source_meta")@field:JsonProperty("source_meta")
    val sourceMeta: Map<String, Any?>? = null,

    @get:JsonProperty("source_name")@field:JsonProperty("source_name")
    val sourceName: String? = null,

    val label: String? = null,
    val maturity: Maturity? = null,
    val owner: Owner? = null,
    val type: DisabledType? = null,
    val url: String? = null,
    val filter: WhereFilterIntersection? = null,
    val metadata: SourceFileMetadata? = null,

    @get:JsonProperty("type_params")@field:JsonProperty("type_params")
    val typeParams: MetricTypeParams? = null,

    @get:JsonProperty("group_bys")@field:JsonProperty("group_bys")
    val groupBys: List<String>? = null,

    val where: WhereFilterIntersection? = null,
    val defaults: Defaults? = null,
    val dimensions: List<Dimension>? = null,
    val entities: List<Entity>? = null,
    val measures: List<Measure>? = null,
    val model: String? = null,

    @get:JsonProperty("node_relation")@field:JsonProperty("node_relation")
    val nodeRelation: NodeRelation? = null,

    @get:JsonProperty("primary_entity")@field:JsonProperty("primary_entity")
    val primaryEntity: String? = null
)

enum class Access(val value: String) {
    Private("private"),
    Protected("protected"),
    Public("public");

    companion object {
        fun fromValue(value: String): Access = when (value) {
            "private"   -> Private
            "protected" -> Protected
            "public"    -> Public
            else        -> throw IllegalArgumentException()
        }
    }
}

data class FileHash (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val checksum: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String
)

data class ColumnInfo (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    val constraints: List<ColumnLevelConstraint>? = null,

    @get:JsonProperty("data_type")@field:JsonProperty("data_type")
    val dataType: String? = null,

    val description: String? = null,
    val meta: Map<String, Any?>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    val quote: Boolean? = null,
    val tags: List<String>? = null
)

data class ColumnLevelConstraint (
    val expression: String? = null,
    val name: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val type: ConstraintType,

    @get:JsonProperty("warn_unenforced")@field:JsonProperty("warn_unenforced")
    val warnUnenforced: Boolean? = null,

    @get:JsonProperty("warn_unsupported")@field:JsonProperty("warn_unsupported")
    val warnUnsupported: Boolean? = null
)

enum class ConstraintType(val value: String) {
    Check("check"),
    Custom("custom"),
    ForeignKey("foreign_key"),
    NotNull("not_null"),
    PrimaryKey("primary_key"),
    Unique("unique");

    companion object {
        fun fromValue(value: String): ConstraintType = when (value) {
            "check"       -> Check
            "custom"      -> Custom
            "foreign_key" -> ForeignKey
            "not_null"    -> NotNull
            "primary_key" -> PrimaryKey
            "unique"      -> Unique
            else          -> throw IllegalArgumentException()
        }
    }
}

data class DisabledConfig (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    val alias: String? = null,

    @get:JsonProperty("column_types")@field:JsonProperty("column_types")
    val columnTypes: Map<String, Any?>? = null,

    val contract: ContractConfig? = null,
    val database: String? = null,
    val docs: Docs? = null,
    val enabled: Boolean? = null,

    @get:JsonProperty("full_refresh")@field:JsonProperty("full_refresh")
    val fullRefresh: Boolean? = null,

    val grants: Map<String, Any?>? = null,
    val group: String? = null,

    @get:JsonProperty("incremental_strategy")@field:JsonProperty("incremental_strategy")
    val incrementalStrategy: String? = null,

    val materialized: String? = null,
    val meta: Map<String, Any?>? = null,

    @get:JsonProperty("on_configuration_change")@field:JsonProperty("on_configuration_change")
    val onConfigurationChange: OnConfigurationChange? = null,

    @get:JsonProperty("on_schema_change")@field:JsonProperty("on_schema_change")
    val onSchemaChange: String? = null,

    val packages: List<String>? = null,

    @get:JsonProperty("persist_docs")@field:JsonProperty("persist_docs")
    val persistDocs: Map<String, Any?>? = null,

    @get:JsonProperty("post-hook")@field:JsonProperty("post-hook")
    val postHook: List<Hook>? = null,

    @get:JsonProperty("pre-hook")@field:JsonProperty("pre-hook")
    val preHook: List<Hook>? = null,

    val quoting: Map<String, Any?>? = null,
    val schema: String? = null,
    val tags: Tags? = null,

    @get:JsonProperty("unique_key")@field:JsonProperty("unique_key")
    val uniqueKey: UniqueKey? = null,

    @get:JsonProperty("error_if")@field:JsonProperty("error_if")
    val errorIf: String? = null,

    @get:JsonProperty("fail_calc")@field:JsonProperty("fail_calc")
    val failCalc: String? = null,

    val limit: Long? = null,
    val severity: String? = null,

    @get:JsonProperty("store_failures")@field:JsonProperty("store_failures")
    val storeFailures: Boolean? = null,

    @get:JsonProperty("warn_if")@field:JsonProperty("warn_if")
    val warnIf: String? = null,

    val where: String? = null,
    val access: Access? = null,

    @get:JsonProperty("check_cols")@field:JsonProperty("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @get:JsonProperty("target_database")@field:JsonProperty("target_database")
    val targetDatabase: String? = null,

    @get:JsonProperty("target_schema")@field:JsonProperty("target_schema")
    val targetSchema: String? = null,

    @get:JsonProperty("updated_at")@field:JsonProperty("updated_at")
    val updatedAt: String? = null,

    val delimiter: String? = null,

    @get:JsonProperty("quote_columns")@field:JsonProperty("quote_columns")
    val quoteColumns: Boolean? = null
)

sealed class UniqueKey {
    class StringArrayValue(val value: List<String>) : UniqueKey()
    class StringValue(val value: String)            : UniqueKey()
    class NullValue()                               : UniqueKey()

    fun toJson(): String = mapperManifest.writeValueAsString(when (this) {
        is StringArrayValue -> this.value
        is StringValue -> this.value
        is NullValue -> "null"
    })

    companion object {
        fun fromJson(jn: JsonNode): UniqueKey = when (jn) {
            is ArrayNode -> StringArrayValue(mapperManifest.treeToValue(jn))
            is TextNode  -> StringValue(mapperManifest.treeToValue(jn))
            null         -> NullValue()
            else         -> throw IllegalArgumentException()
        }
    }
}

data class ContractConfig (
    @get:JsonProperty("alias_types")@field:JsonProperty("alias_types")
    val aliasTypes: Boolean? = null,

    val enforced: Boolean? = null
)

data class Docs (
    @get:JsonProperty("node_color")@field:JsonProperty("node_color")
    val nodeColor: String? = null,

    val show: Boolean? = null
)

enum class OnConfigurationChange(val value: String) {
    Apply("apply"),
    Continue("continue"),
    Fail("fail");

    companion object {
        fun fromValue(value: String): OnConfigurationChange = when (value) {
            "apply"    -> Apply
            "continue" -> Continue
            "fail"     -> Fail
            else       -> throw IllegalArgumentException()
        }
    }
}

data class Hook (
    val index: Long? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val sql: String,

    val transaction: Boolean? = null
)

sealed class Tags {
    class StringArrayValue(val value: List<String>) : Tags()
    class StringValue(val value: String)            : Tags()

    fun toJson(): String = mapperManifest.writeValueAsString(when (this) {
        is StringArrayValue -> this.value
        is StringValue -> this.value
    })

    companion object {
        fun fromJson(jn: JsonNode): Tags = when (jn) {
            is ArrayNode -> StringArrayValue(mapperManifest.treeToValue(jn))
            is TextNode  -> StringValue(mapperManifest.treeToValue(jn))
            else         -> throw IllegalArgumentException()
        }
    }
}

data class ModelLevelConstraint (
    val columns: List<String>? = null,
    val expression: String? = null,
    val name: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val type: ConstraintType,

    @get:JsonProperty("warn_unenforced")@field:JsonProperty("warn_unenforced")
    val warnUnenforced: Boolean? = null,

    @get:JsonProperty("warn_unsupported")@field:JsonProperty("warn_unsupported")
    val warnUnsupported: Boolean? = null
)

data class Contract (
    @get:JsonProperty("alias_types")@field:JsonProperty("alias_types")
    val aliasTypes: Boolean? = null,

    val checksum: String? = null,
    val enforced: Boolean? = null
)

data class Defaults (
    @get:JsonProperty("agg_time_dimension")@field:JsonProperty("agg_time_dimension")
    val aggTimeDimension: String? = null
)

data class DeferRelation (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val alias: String,

    val database: String? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val schema: String
)

data class DependsOn (
    val macros: List<String>? = null,
    val nodes: List<String>? = null
)

data class Dimension (
    val description: String? = null,
    val expr: String? = null,

    @get:JsonProperty("is_partition")@field:JsonProperty("is_partition")
    val isPartition: Boolean? = null,

    val label: String? = null,
    val metadata: SourceFileMetadata? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val type: DimensionType,

    @get:JsonProperty("type_params")@field:JsonProperty("type_params")
    val typeParams: DimensionTypeParams? = null
)

data class SourceFileMetadata (
    @get:JsonProperty("file_slice", required=true)@field:JsonProperty("file_slice", required=true)
    val fileSlice: FileSlice,

    @get:JsonProperty("repo_file_path", required=true)@field:JsonProperty("repo_file_path", required=true)
    val repoFilePath: String
)

data class FileSlice (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val content: String,

    @get:JsonProperty("end_line_number", required=true)@field:JsonProperty("end_line_number", required=true)
    val endLineNumber: Long,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val filename: String,

    @get:JsonProperty("start_line_number", required=true)@field:JsonProperty("start_line_number", required=true)
    val startLineNumber: Long
)

enum class DimensionType(val value: String) {
    Categorical("categorical"),
    Time("time");

    companion object {
        fun fromValue(value: String): DimensionType = when (value) {
            "categorical" -> Categorical
            "time"        -> Time
            else          -> throw IllegalArgumentException()
        }
    }
}

data class DimensionTypeParams (
    @get:JsonProperty("time_granularity", required=true)@field:JsonProperty("time_granularity", required=true)
    val timeGranularity: Granularity,

    @get:JsonProperty("validity_params")@field:JsonProperty("validity_params")
    val validityParams: DimensionValidityParams? = null
)

enum class Granularity(val value: String) {
    Day("day"),
    Month("month"),
    Quarter("quarter"),
    Week("week"),
    Year("year");

    companion object {
        fun fromValue(value: String): Granularity = when (value) {
            "day"     -> Day
            "month"   -> Month
            "quarter" -> Quarter
            "week"    -> Week
            "year"    -> Year
            else      -> throw IllegalArgumentException()
        }
    }
}

data class DimensionValidityParams (
    @get:JsonProperty("is_end")@field:JsonProperty("is_end")
    val isEnd: Boolean? = null,

    @get:JsonProperty("is_start")@field:JsonProperty("is_start")
    val isStart: Boolean? = null
)

data class Entity (
    val description: String? = null,
    val expr: String? = null,
    val label: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    val role: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val type: EntityType
)

enum class EntityType(val value: String) {
    Foreign("foreign"),
    Natural("natural"),
    Primary("primary"),
    Unique("unique");

    companion object {
        fun fromValue(value: String): EntityType = when (value) {
            "foreign" -> Foreign
            "natural" -> Natural
            "primary" -> Primary
            "unique"  -> Unique
            else      -> throw IllegalArgumentException()
        }
    }
}

data class ExternalTable (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    @get:JsonProperty("file_format")@field:JsonProperty("file_format")
    val fileFormat: String? = null,

    val location: String? = null,
    val partitions: List<Partition>? = null,

    @get:JsonProperty("row_format")@field:JsonProperty("row_format")
    val rowFormat: String? = null,

    @get:JsonProperty("tbl_properties")@field:JsonProperty("tbl_properties")
    val tblProperties: String? = null
)

sealed class Partition {
    class ExternalPartitionValue(val value: ExternalPartition) : Partition()
    class StringValue(val value: String)                       : Partition()

    fun toJson(): String = mapperManifest.writeValueAsString(when (this) {
        is ExternalPartitionValue -> this.value
        is StringValue -> this.value
    })

    companion object {
        fun fromJson(jn: JsonNode): Partition = when (jn) {
            is ObjectNode -> ExternalPartitionValue(mapperManifest.treeToValue(jn))
            is TextNode   -> StringValue(mapperManifest.treeToValue(jn))
            else          -> throw IllegalArgumentException()
        }
    }
}

data class ExternalPartition (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    @get:JsonProperty("data_type")@field:JsonProperty("data_type")
    val dataType: String? = null,

    val description: String? = null,
    val meta: Map<String, Any?>? = null,
    val name: String? = null
)

data class InjectedCte (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val id: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val sql: String
)

data class WhereFilterIntersection (
    @get:JsonProperty("where_filters", required=true)@field:JsonProperty("where_filters", required=true)
    val whereFilters: List<WhereFilter>
)

data class WhereFilter (
    @get:JsonProperty("where_sql_template", required=true)@field:JsonProperty("where_sql_template", required=true)
    val whereSqlTemplate: String
)

data class FreshnessThreshold (
    @get:JsonProperty("error_after")@field:JsonProperty("error_after")
    val errorAfter: Time? = null,

    val filter: String? = null,

    @get:JsonProperty("warn_after")@field:JsonProperty("warn_after")
    val warnAfter: Time? = null
)

data class Time (
    val count: Long? = null,
    val period: Period? = null
)

enum class Period(val value: String) {
    Day("day"),
    Hour("hour"),
    Minute("minute");

    companion object {
        fun fromValue(value: String): Period = when (value) {
            "day"    -> Day
            "hour"   -> Hour
            "minute" -> Minute
            else     -> throw IllegalArgumentException()
        }
    }
}

sealed class Version {
    class DoubleValue(val value: Double) : Version()
    class IntValue(val value: Int)       : Version()
    class StringValue(val value: String) : Version()
    class NullValue()                    : Version()

    fun toJson(): String = mapperManifest.writeValueAsString(when (this) {
        is DoubleValue -> this.value
        is StringValue -> this.value
        is IntValue -> this.value
        is NullValue -> "null"
    })

    companion object {
        fun fromJson(jn: JsonNode): Version = when (jn) {
            is DoubleNode -> DoubleValue(mapperManifest.treeToValue(jn))
            is IntNode    -> IntValue(mapperManifest.treeToValue(jn))
            is TextNode   -> StringValue(mapperManifest.treeToValue(jn))
            null          -> NullValue()
            else          -> throw IllegalArgumentException()
        }
    }
}

enum class Maturity(val value: String) {
    High("high"),
    Low("low"),
    Medium("medium");

    companion object {
        fun fromValue(value: String): Maturity = when (value) {
            "high"   -> High
            "low"    -> Low
            "medium" -> Medium
            else     -> throw IllegalArgumentException()
        }
    }
}

data class Measure (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val agg: Agg,

    @get:JsonProperty("agg_params")@field:JsonProperty("agg_params")
    val aggParams: MeasureAggregationParameters? = null,

    @get:JsonProperty("agg_time_dimension")@field:JsonProperty("agg_time_dimension")
    val aggTimeDimension: String? = null,

    @get:JsonProperty("create_metric")@field:JsonProperty("create_metric")
    val createMetric: Boolean? = null,

    val description: String? = null,
    val expr: String? = null,
    val label: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("non_additive_dimension")@field:JsonProperty("non_additive_dimension")
    val nonAdditiveDimension: NonAdditiveDimension? = null
)

enum class Agg(val value: String) {
    Average("average"),
    Count("count"),
    CountDistinct("count_distinct"),
    Max("max"),
    Median("median"),
    Min("min"),
    Percentile("percentile"),
    Sum("sum"),
    SumBoolean("sum_boolean");

    companion object {
        fun fromValue(value: String): Agg = when (value) {
            "average"        -> Average
            "count"          -> Count
            "count_distinct" -> CountDistinct
            "max"            -> Max
            "median"         -> Median
            "min"            -> Min
            "percentile"     -> Percentile
            "sum"            -> Sum
            "sum_boolean"    -> SumBoolean
            else             -> throw IllegalArgumentException()
        }
    }
}

data class MeasureAggregationParameters (
    val percentile: Double? = null,

    @get:JsonProperty("use_approximate_percentile")@field:JsonProperty("use_approximate_percentile")
    val useApproximatePercentile: Boolean? = null,

    @get:JsonProperty("use_discrete_percentile")@field:JsonProperty("use_discrete_percentile")
    val useDiscretePercentile: Boolean? = null
)

data class NonAdditiveDimension (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("window_choice", required=true)@field:JsonProperty("window_choice", required=true)
    val windowChoice: Agg,

    @get:JsonProperty("window_groupings", required=true)@field:JsonProperty("window_groupings", required=true)
    val windowGroupings: List<String>
)

data class NodeRelation (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val alias: String,

    val database: String? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

    @get:JsonProperty("schema_name", required=true)@field:JsonProperty("schema_name", required=true)
    val schemaName: String
)

data class Owner (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    val email: String? = null,
    val name: String? = null
)

data class Quoting (
    val column: Boolean? = null,
    val database: Boolean? = null,
    val identifier: Boolean? = null,
    val schema: Boolean? = null
)

data class RefArgs (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("package")@field:JsonProperty("package")
    val refArgsPackage: String? = null,

    @get:JsonProperty("version")@field:JsonProperty("version")
    val version: Version? = null
)

enum class DisabledResourceType(val value: String) {
    Analysis("analysis"),
    Doc("doc"),
    Exposure("exposure"),
    Group("group"),
    Macro("macro"),
    Metric("metric"),
    Model("model"),
    Operation("operation"),
    RPC("rpc"),
    SQLOperation("sql_operation"),
    SavedQuery("saved_query"),
    Seed("seed"),
    SemanticModel("semantic_model"),
    Snapshot("snapshot"),
    Source("source"),
    Test("test");

    companion object {
        fun fromValue(value: String): DisabledResourceType = when (value) {
            "analysis"       -> Analysis
            "doc"            -> Doc
            "exposure"       -> Exposure
            "group"          -> Group
            "macro"          -> Macro
            "metric"         -> Metric
            "model"          -> Model
            "operation"      -> Operation
            "rpc"            -> RPC
            "sql_operation"  -> SQLOperation
            "saved_query"    -> SavedQuery
            "seed"           -> Seed
            "semantic_model" -> SemanticModel
            "snapshot"       -> Snapshot
            "source"         -> Source
            "test"           -> Test
            else             -> throw IllegalArgumentException()
        }
    }
}

data class TestMetadata (
    val kwargs: Map<String, Any?>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    val namespace: String? = null
)

enum class DisabledType(val value: String) {
    Analysis("analysis"),
    Application("application"),
    Cumulative("cumulative"),
    Dashboard("dashboard"),
    Derived("derived"),
    Ml("ml"),
    Notebook("notebook"),
    Ratio("ratio"),
    Simple("simple");

    companion object {
        fun fromValue(value: String): DisabledType = when (value) {
            "analysis"    -> Analysis
            "application" -> Application
            "cumulative"  -> Cumulative
            "dashboard"   -> Dashboard
            "derived"     -> Derived
            "ml"          -> Ml
            "notebook"    -> Notebook
            "ratio"       -> Ratio
            "simple"      -> Simple
            else          -> throw IllegalArgumentException()
        }
    }
}

data class MetricTypeParams (
    val denominator: MetricInput? = null,
    val expr: String? = null,

    @get:JsonProperty("grain_to_date")@field:JsonProperty("grain_to_date")
    val grainToDate: Granularity? = null,

    @get:JsonProperty("input_measures")@field:JsonProperty("input_measures")
    val inputMeasures: List<MetricInputMeasure>? = null,

    val measure: MetricInputMeasure? = null,
    val metrics: List<MetricInput>? = null,
    val numerator: MetricInput? = null,
    val window: MetricTimeWindow? = null
)

data class MetricInput (
    val alias: String? = null,
    val filter: WhereFilterIntersection? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("offset_to_grain")@field:JsonProperty("offset_to_grain")
    val offsetToGrain: Granularity? = null,

    @get:JsonProperty("offset_window")@field:JsonProperty("offset_window")
    val offsetWindow: MetricTimeWindow? = null
)

data class MetricTimeWindow (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val count: Long,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val granularity: Granularity
)

data class MetricInputMeasure (
    val alias: String? = null,

    @get:JsonProperty("fill_nulls_with")@field:JsonProperty("fill_nulls_with")
    val fillNullsWith: Long? = null,

    val filter: WhereFilterIntersection? = null,

    @get:JsonProperty("join_to_timespine")@field:JsonProperty("join_to_timespine")
    val joinToTimespine: Boolean? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String
)

data class Documentation (
    @get:JsonProperty("block_contents", required=true)@field:JsonProperty("block_contents", required=true)
    val blockContents: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: DocResourceType,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String
)

enum class DocResourceType(val value: String) {
    Doc("doc");

    companion object {
        fun fromValue(value: String): DocResourceType = when (value) {
            "doc" -> Doc
            else  -> throw IllegalArgumentException()
        }
    }
}

data class Exposure (
    val config: ExposureConfig? = null,

    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    val description: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val fqn: List<String>,

    val label: String? = null,
    val maturity: Maturity? = null,
    val meta: Map<String, Any?>? = null,
    val metrics: List<List<String>>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val owner: Owner,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    val refs: List<RefArgs>? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: ExposureResourceType,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val type: ExposureType,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    @get:JsonProperty("unrendered_config")@field:JsonProperty("unrendered_config")
    val unrenderedConfig: Map<String, Any?>? = null,

    val url: String? = null
)

data class ExposureConfig (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    val enabled: Boolean? = null
)

data class ExposureDependsOn (
    val macros: List<String>? = null,
    val nodes: List<String>? = null
)

enum class ExposureResourceType(val value: String) {
    Exposure("exposure");

    companion object {
        fun fromValue(value: String): ExposureResourceType = when (value) {
            "exposure" -> Exposure
            else       -> throw IllegalArgumentException()
        }
    }
}

enum class ExposureType(val value: String) {
    Analysis("analysis"),
    Application("application"),
    Dashboard("dashboard"),
    Ml("ml"),
    Notebook("notebook");

    companion object {
        fun fromValue(value: String): ExposureType = when (value) {
            "analysis"    -> Analysis
            "application" -> Application
            "dashboard"   -> Dashboard
            "ml"          -> Ml
            "notebook"    -> Notebook
            else          -> throw IllegalArgumentException()
        }
    }
}

data class Group (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val owner: Owner,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: GroupResourceType,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String
)

enum class GroupResourceType(val value: String) {
    Group("group");

    companion object {
        fun fromValue(value: String): GroupResourceType = when (value) {
            "group" -> Group
            else    -> throw IllegalArgumentException()
        }
    }
}

data class Macro (
    val arguments: List<MacroArgument>? = null,

    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: MacroDependsOn? = null,

    val description: String? = null,
    val docs: Docs? = null,

    @get:JsonProperty("macro_sql", required=true)@field:JsonProperty("macro_sql", required=true)
    val macroSql: String,

    val meta: Map<String, Any?>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty("patch_path")@field:JsonProperty("patch_path")
    val patchPath: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: MacroResourceType,

    @get:JsonProperty("supported_languages")@field:JsonProperty("supported_languages")
    val supportedLanguages: List<SupportedLanguage>? = null,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String
)

data class MacroArgument (
    val description: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    val type: String? = null
)

data class MacroDependsOn (
    val macros: List<String>? = null
)

enum class MacroResourceType(val value: String) {
    Macro("macro");

    companion object {
        fun fromValue(value: String): MacroResourceType = when (value) {
            "macro" -> Macro
            else    -> throw IllegalArgumentException()
        }
    }
}

enum class SupportedLanguage(val value: String) {
    Python("python"),
    SQL("sql");

    companion object {
        fun fromValue(value: String): SupportedLanguage = when (value) {
            "python" -> Python
            "sql"    -> SQL
            else     -> throw IllegalArgumentException()
        }
    }
}

/**
 * Metadata about the manifest
 */
data class ManifestMetadata (
    /**
     * The type name of the adapter
     */
    @get:JsonProperty("adapter_type")@field:JsonProperty("adapter_type")
    val adapterType: String? = null,

    @get:JsonProperty("dbt_schema_version")@field:JsonProperty("dbt_schema_version")
    val dbtSchemaVersion: String? = null,

    @get:JsonProperty("dbt_version")@field:JsonProperty("dbt_version")
    val dbtVersion: String? = null,

    val env: Map<String, String>? = null,

    @get:JsonProperty("generated_at")@field:JsonProperty("generated_at")
    val generatedAt: String? = null,

    @get:JsonProperty("invocation_id")@field:JsonProperty("invocation_id")
    val invocationId: String? = null,

    /**
     * A unique identifier for the project, hashed from the project name
     */
    @get:JsonProperty("project_id")@field:JsonProperty("project_id")
    val projectId: String? = null,

    /**
     * Name of the root project
     */
    @get:JsonProperty("project_name")@field:JsonProperty("project_name")
    val projectName: String? = null,

    /**
     * Whether dbt is configured to send anonymous usage statistics
     */
    @get:JsonProperty("send_anonymous_usage_stats")@field:JsonProperty("send_anonymous_usage_stats")
    val sendAnonymousUsageStats: Boolean? = null,

    /**
     * A unique identifier for the user
     */
    @get:JsonProperty("user_id")@field:JsonProperty("user_id")
    val userId: String? = null
)

data class Metric (
    val config: MetricConfig? = null,

    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val description: String,

    val filter: WhereFilterIntersection? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val fqn: List<String>,

    val group: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val label: String,

    val meta: Map<String, Any?>? = null,
    val metadata: SourceFileMetadata? = null,
    val metrics: List<List<String>>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    val refs: List<RefArgs>? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: MetricResourceType,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val type: MetricType,

    @get:JsonProperty("type_params", required=true)@field:JsonProperty("type_params", required=true)
    val typeParams: MetricTypeParams,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    @get:JsonProperty("unrendered_config")@field:JsonProperty("unrendered_config")
    val unrenderedConfig: Map<String, Any?>? = null
)

data class MetricConfig (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    val enabled: Boolean? = null,
    val group: String? = null
)

enum class MetricResourceType(val value: String) {
    Metric("metric");

    companion object {
        fun fromValue(value: String): MetricResourceType = when (value) {
            "metric" -> Metric
            else     -> throw IllegalArgumentException()
        }
    }
}

enum class MetricType(val value: String) {
    Cumulative("cumulative"),
    Derived("derived"),
    Ratio("ratio"),
    Simple("simple");

    companion object {
        fun fromValue(value: String): MetricType = when (value) {
            "cumulative" -> Cumulative
            "derived"    -> Derived
            "ratio"      -> Ratio
            "simple"     -> Simple
            else         -> throw IllegalArgumentException()
        }
    }
}

data class Node (
    @get:JsonProperty("_event_status")@field:JsonProperty("_event_status")
    val eventStatus: Map<String, Any?>? = null,

    @get:JsonProperty("_pre_injected_sql")@field:JsonProperty("_pre_injected_sql")
    val preInjectedSql: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val alias: String,

    @get:JsonProperty("build_path")@field:JsonProperty("build_path")
    val buildPath: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val checksum: FileHash,

    val columns: Map<String, ColumnInfo>? = null,
    val compiled: Boolean? = null,

    @get:JsonProperty("compiled_code")@field:JsonProperty("compiled_code")
    val compiledCode: String? = null,

    @get:JsonProperty("compiled_path")@field:JsonProperty("compiled_path")
    val compiledPath: String? = null,

    val config: NodeConfig? = null,

    @get:JsonProperty("config_call_dict")@field:JsonProperty("config_call_dict")
    val configCallDict: Map<String, Any?>? = null,

    val contract: Contract? = null,

    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    val database: String? = null,
    val deferred: Boolean? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: DependsOn? = null,

    val description: String? = null,
    val docs: Docs? = null,

    @get:JsonProperty("extra_ctes")@field:JsonProperty("extra_ctes")
    val extraCtes: List<InjectedCte>? = null,

    @get:JsonProperty("extra_ctes_injected")@field:JsonProperty("extra_ctes_injected")
    val extraCtesInjected: Boolean? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val fqn: List<String>,

    val group: String? = null,
    val language: String? = null,
    val meta: Map<String, Any?>? = null,
    val metrics: List<List<String>>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty("patch_path")@field:JsonProperty("patch_path")
    val patchPath: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    @get:JsonProperty("raw_code")@field:JsonProperty("raw_code")
    val rawCode: String? = null,

    val refs: List<RefArgs>? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: NodeResourceType,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val schema: String,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    @get:JsonProperty("unrendered_config")@field:JsonProperty("unrendered_config")
    val unrenderedConfig: Map<String, Any?>? = null,

    val index: Long? = null,
    val access: Access? = null,
    val constraints: List<ModelLevelConstraint>? = null,

    @get:JsonProperty("defer_relation")@field:JsonProperty("defer_relation")
    val deferRelation: DeferRelation? = null,

    @get:JsonProperty("deprecation_date")@field:JsonProperty("deprecation_date")
    val deprecationDate: String? = null,

    @get:JsonProperty("latest_version")@field:JsonProperty("latest_version")
    val latestVersion: Version? = null,

    val version: Version? = null,

    @get:JsonProperty("attached_node")@field:JsonProperty("attached_node")
    val attachedNode: String? = null,

    @get:JsonProperty("column_name")@field:JsonProperty("column_name")
    val columnName: String? = null,

    @get:JsonProperty("file_key_name")@field:JsonProperty("file_key_name")
    val fileKeyName: String? = null,

    @get:JsonProperty("test_metadata")@field:JsonProperty("test_metadata")
    val testMetadata: TestMetadata? = null,

    @get:JsonProperty("root_path")@field:JsonProperty("root_path")
    val rootPath: String? = null
)

data class NodeConfig (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    val alias: String? = null,

    @get:JsonProperty("column_types")@field:JsonProperty("column_types")
    val columnTypes: Map<String, Any?>? = null,

    val contract: ContractConfig? = null,
    val database: String? = null,
    val docs: Docs? = null,
    val enabled: Boolean? = null,

    @get:JsonProperty("full_refresh")@field:JsonProperty("full_refresh")
    val fullRefresh: Boolean? = null,

    val grants: Map<String, Any?>? = null,
    val group: String? = null,

    @get:JsonProperty("incremental_strategy")@field:JsonProperty("incremental_strategy")
    val incrementalStrategy: String? = null,

    val materialized: String? = null,
    val meta: Map<String, Any?>? = null,

    @get:JsonProperty("on_configuration_change")@field:JsonProperty("on_configuration_change")
    val onConfigurationChange: OnConfigurationChange? = null,

    @get:JsonProperty("on_schema_change")@field:JsonProperty("on_schema_change")
    val onSchemaChange: String? = null,

    val packages: List<String>? = null,

    @get:JsonProperty("persist_docs")@field:JsonProperty("persist_docs")
    val persistDocs: Map<String, Any?>? = null,

    @get:JsonProperty("post-hook")@field:JsonProperty("post-hook")
    val postHook: List<Hook>? = null,

    @get:JsonProperty("pre-hook")@field:JsonProperty("pre-hook")
    val preHook: List<Hook>? = null,

    val quoting: Map<String, Any?>? = null,
    val schema: String? = null,
    val tags: Tags? = null,

    @get:JsonProperty("unique_key")@field:JsonProperty("unique_key")
    val uniqueKey: UniqueKey? = null,

    @get:JsonProperty("error_if")@field:JsonProperty("error_if")
    val errorIf: String? = null,

    @get:JsonProperty("fail_calc")@field:JsonProperty("fail_calc")
    val failCalc: String? = null,

    val limit: Long? = null,
    val severity: String? = null,

    @get:JsonProperty("store_failures")@field:JsonProperty("store_failures")
    val storeFailures: Boolean? = null,

    @get:JsonProperty("warn_if")@field:JsonProperty("warn_if")
    val warnIf: String? = null,

    val where: String? = null,
    val access: Access? = null,

    @get:JsonProperty("check_cols")@field:JsonProperty("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @get:JsonProperty("target_database")@field:JsonProperty("target_database")
    val targetDatabase: String? = null,

    @get:JsonProperty("target_schema")@field:JsonProperty("target_schema")
    val targetSchema: String? = null,

    @get:JsonProperty("updated_at")@field:JsonProperty("updated_at")
    val updatedAt: String? = null,

    val delimiter: String? = null,

    @get:JsonProperty("quote_columns")@field:JsonProperty("quote_columns")
    val quoteColumns: Boolean? = null
)

enum class NodeResourceType(val value: String) {
    Analysis("analysis"),
    Model("model"),
    Operation("operation"),
    RPC("rpc"),
    SQLOperation("sql_operation"),
    Seed("seed"),
    Snapshot("snapshot"),
    Test("test");

    companion object {
        fun fromValue(value: String): NodeResourceType = when (value) {
            "analysis"      -> Analysis
            "model"         -> Model
            "operation"     -> Operation
            "rpc"           -> RPC
            "sql_operation" -> SQLOperation
            "seed"          -> Seed
            "snapshot"      -> Snapshot
            "test"          -> Test
            else            -> throw IllegalArgumentException()
        }
    }
}

data class SavedQuery (
    val config: SavedQueryConfig? = null,

    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    val description: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val fqn: List<String>,

    val group: String? = null,

    @get:JsonProperty("group_bys", required=true)@field:JsonProperty("group_bys", required=true)
    val groupBys: List<String>,

    val label: String? = null,
    val metadata: SourceFileMetadata? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val metrics: List<String>,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    val refs: List<RefArgs>? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: DisabledResourceType,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    @get:JsonProperty("unrendered_config")@field:JsonProperty("unrendered_config")
    val unrenderedConfig: Map<String, Any?>? = null,

    val where: WhereFilterIntersection? = null
)

data class SavedQueryConfig (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    val enabled: Boolean? = null,
    val group: String? = null,
    val meta: Map<String, Any?>? = null
)

data class SemanticModel (
    val config: SemanticModelConfig? = null,

    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    val defaults: Defaults? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    val description: String? = null,
    val dimensions: List<Dimension>? = null,
    val entities: List<Entity>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val fqn: List<String>,

    val group: String? = null,
    val label: String? = null,
    val measures: List<Measure>? = null,
    val metadata: SourceFileMetadata? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val model: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("node_relation")@field:JsonProperty("node_relation")
    val nodeRelation: NodeRelation? = null,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    @get:JsonProperty("primary_entity")@field:JsonProperty("primary_entity")
    val primaryEntity: String? = null,

    val refs: List<RefArgs>? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: DisabledResourceType,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    @get:JsonProperty("unrendered_config")@field:JsonProperty("unrendered_config")
    val unrenderedConfig: Map<String, Any?>? = null
)

data class SemanticModelConfig (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    val enabled: Boolean? = null,
    val group: String? = null,
    val meta: Map<String, Any?>? = null
)

data class SourceDefinition (
    @get:JsonProperty("_event_status")@field:JsonProperty("_event_status")
    val eventStatus: Map<String, Any?>? = null,

    val columns: Map<String, ColumnInfo>? = null,
    val config: SourceConfig? = null,

    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    val database: String? = null,
    val description: String? = null,
    val external: ExternalTable? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val fqn: List<String>,

    val freshness: FreshnessThreshold? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val identifier: String,

    @get:JsonProperty("loaded_at_field")@field:JsonProperty("loaded_at_field")
    val loadedAtField: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val loader: String,

    val meta: Map<String, Any?>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty("patch_path")@field:JsonProperty("patch_path")
    val patchPath: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    val quoting: Quoting? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: SourceResourceType,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val schema: String,

    @get:JsonProperty("source_description", required=true)@field:JsonProperty("source_description", required=true)
    val sourceDescription: String,

    @get:JsonProperty("source_meta")@field:JsonProperty("source_meta")
    val sourceMeta: Map<String, Any?>? = null,

    @get:JsonProperty("source_name", required=true)@field:JsonProperty("source_name", required=true)
    val sourceName: String,

    val tags: List<String>? = null,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    @get:JsonProperty("unrendered_config")@field:JsonProperty("unrendered_config")
    val unrenderedConfig: Map<String, Any?>? = null
)

data class SourceConfig (
    @get:JsonProperty("_extra")@field:JsonProperty("_extra")
    val extra: Map<String, Any?>? = null,

    val enabled: Boolean? = null
)

enum class SourceResourceType(val value: String) {
    Source("source");

    companion object {
        fun fromValue(value: String): SourceResourceType = when (value) {
            "source" -> Source
            else     -> throw IllegalArgumentException()
        }
    }
}

