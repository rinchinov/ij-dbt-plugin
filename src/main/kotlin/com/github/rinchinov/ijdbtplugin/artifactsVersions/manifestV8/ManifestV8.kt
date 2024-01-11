package com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV8

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.*
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.*
import com.github.rinchinov.ijdbtplugin.artifactInterfaces.ManifestInterface


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
    convert(TimePeriod::class,           { TimePeriod.fromValue(it.asText()) },           { "\"${it.value}\"" })
    convert(DisabledResourceType::class, { DisabledResourceType.fromValue(it.asText()) }, { "\"${it.value}\"" })
    convert(DocResourceType::class,      { DocResourceType.fromValue(it.asText()) },      { "\"${it.value}\"" })
    convert(Maturity::class,             { Maturity.fromValue(it.asText()) },             { "\"${it.value}\"" })
    convert(ExposureResourceType::class, { ExposureResourceType.fromValue(it.asText()) }, { "\"${it.value}\"" })
    convert(Type::class,                 { Type.fromValue(it.asText()) },                 { "\"${it.value}\"" })
    convert(MacroResourceType::class,    { MacroResourceType.fromValue(it.asText()) },    { "\"${it.value}\"" })
    convert(SupportedLanguage::class,    { SupportedLanguage.fromValue(it.asText()) },    { "\"${it.value}\"" })
    convert(MetricResourceType::class,   { MetricResourceType.fromValue(it.asText()) },   { "\"${it.value}\"" })
    convert(MetricTimePeriod::class,     { MetricTimePeriod.fromValue(it.asText()) },     { "\"${it.value}\"" })
    convert(NodeResourceType::class,     { NodeResourceType.fromValue(it.asText()) },     { "\"${it.value}\"" })
    convert(SourceResourceType::class,   { SourceResourceType.fromValue(it.asText()) },   { "\"${it.value}\"" })
    convert(UniqueKey::class,            { UniqueKey.fromJson(it) },                      { it.toJson() }, true)
    convert(Tags::class,                 { Tags.fromJson(it) },                           { it.toJson() }, true)
    convert(Partition::class,            { Partition.fromJson(it) },                      { it.toJson() }, true)
}

/**
 * WritableManifest(metadata: dbt.contracts.graph.manifest.ManifestMetadata, nodes:
 * Mapping[str, Union[dbt.contracts.graph.nodes.AnalysisNode,
 * dbt.contracts.graph.nodes.SingularTestNode, dbt.contracts.graph.nodes.HookNode,
 * dbt.contracts.graph.nodes.ModelNode, dbt.contracts.graph.nodes.RPCNode,
 * dbt.contracts.graph.nodes.SqlNode, dbt.contracts.graph.nodes.GenericTestNode,
 * dbt.contracts.graph.nodes.SnapshotNode, dbt.contracts.graph.nodes.SeedNode]], sources:
 * Mapping[str, dbt.contracts.graph.nodes.SourceDefinition], macros: Mapping[str,
 * dbt.contracts.graph.nodes.Macro], docs: Mapping[str,
 * dbt.contracts.graph.nodes.Documentation], exposures: Mapping[str,
 * dbt.contracts.graph.nodes.Exposure], metrics: Mapping[str,
 * dbt.contracts.graph.nodes.Metric], selectors: Mapping[str, Any], disabled:
 * Optional[Mapping[str, List[Union[dbt.contracts.graph.nodes.AnalysisNode,
 * dbt.contracts.graph.nodes.SingularTestNode, dbt.contracts.graph.nodes.HookNode,
 * dbt.contracts.graph.nodes.ModelNode, dbt.contracts.graph.nodes.RPCNode,
 * dbt.contracts.graph.nodes.SqlNode, dbt.contracts.graph.nodes.GenericTestNode,
 * dbt.contracts.graph.nodes.SnapshotNode, dbt.contracts.graph.nodes.SeedNode,
 * dbt.contracts.graph.nodes.SourceDefinition]]]], parent_map: Optional[Dict[str,
 * List[str]]], child_map: Optional[Dict[str, List[str]]])
 */
data class ManifestV8 (
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
     * The selectors defined in selectors.yml
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val selectors: Map<String, Any?>,

    /**
     * The sources defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val sources: Map<String, SourceDefinition>
): ManifestInterface {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapper.readValue<ManifestV8>(json)
    }
}

/**
 * AnalysisNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * SingularTestNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * HookNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None, index: Optional[int] = None)
 *
 * ModelNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * RPCNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * SqlNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * GenericTestNode(test_metadata: dbt.contracts.graph.nodes.TestMetadata, database:
 * Optional[str], schema: str, name: str, resource_type: dbt.node_types.NodeType,
 * package_name: str, path: str, original_file_path: str, unique_id: str, fqn: List[str],
 * alias: str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None, column_name: Optional[str] = None, file_key_name: Optional[str] = None)
 *
 * SnapshotNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.SnapshotConfig, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * SeedNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.SeedConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', root_path:
 * Optional[str] = None, depends_on: dbt.contracts.graph.nodes.MacroDependsOn = <factory>)
 *
 * SourceDefinition(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], source_name: str, source_description: str, loader: str,
 * identifier: str, _event_status: Dict[str, Any] = <factory>, quoting:
 * dbt.contracts.graph.unparsed.Quoting = <factory>, loaded_at_field: Optional[str] = None,
 * freshness: Optional[dbt.contracts.graph.unparsed.FreshnessThreshold] = None, external:
 * Optional[dbt.contracts.graph.unparsed.ExternalTable] = None, description: str = '',
 * columns: Dict[str, dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str,
 * Any] = <factory>, source_meta: Dict[str, Any] = <factory>, tags: List[str] = <factory>,
 * config: dbt.contracts.graph.model_config.SourceConfig = <factory>, patch_path:
 * Optional[str] = None, unrendered_config: Dict[str, Any] = <factory>, relation_name:
 * Optional[str] = None, created_at: float = <factory>)
 */
data class AnalysisNode (
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

    val refs: List<List<String>>? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: DisabledResourceType,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val schema: String,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    @get:JsonProperty("unrendered_config")@field:JsonProperty("unrendered_config")
    val unrenderedConfig: Map<String, Any?>? = null,

    val index: Long? = null,

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
    val sourceName: String? = null
)

/**
 * FileHash(name: str, checksum: str)
 */
data class FileHash (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val checksum: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String
)

/**
 * Used in all ManifestNodes and SourceDefinition
 */
data class ColumnInfo (
    @get:JsonProperty("data_type")@field:JsonProperty("data_type")
    val dataType: String? = null,

    val description: String? = null,
    val meta: Map<String, Any?>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    val quote: Boolean? = null,
    val tags: List<String>? = null
)

/**
 * NodeConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Optional[str]
 * = None, schema: Optional[str] = None, database: Optional[str] = None, tags:
 * Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, materialized: str =
 * 'view', incremental_strategy: Optional[str] = None, persist_docs: Dict[str, Any] =
 * <factory>, post_hook: List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Optional[bool] = None,
 * unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Optional[str] =
 * 'ignore', grants: Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>)
 *
 * TestConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Optional[str]
 * = None, schema: Optional[str] = 'dbt_test__audit', database: Optional[str] = None, tags:
 * Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, materialized: str =
 * 'test', severity: dbt.contracts.graph.model_config.Severity = 'ERROR', store_failures:
 * Optional[bool] = None, where: Optional[str] = None, limit: Optional[int] = None,
 * fail_calc: str = 'count(*)', warn_if: str = '!= 0', error_if: str = '!= 0')
 *
 * SnapshotConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias:
 * Optional[str] = None, schema: Optional[str] = None, database: Optional[str] = None, tags:
 * Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, materialized: str =
 * 'snapshot', incremental_strategy: Optional[str] = None, persist_docs: Dict[str, Any] =
 * <factory>, post_hook: List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Optional[bool] = None,
 * unique_key: Optional[str] = None, on_schema_change: Optional[str] = 'ignore', grants:
 * Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>, strategy: Optional[str] = None,
 * target_schema: Optional[str] = None, target_database: Optional[str] = None, updated_at:
 * Optional[str] = None, check_cols: Union[str, List[str], NoneType] = None)
 *
 * SeedConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Optional[str]
 * = None, schema: Optional[str] = None, database: Optional[str] = None, tags:
 * Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, materialized: str =
 * 'seed', incremental_strategy: Optional[str] = None, persist_docs: Dict[str, Any] =
 * <factory>, post_hook: List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Optional[bool] = None,
 * unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Optional[str] =
 * 'ignore', grants: Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>, quote_columns: Optional[bool] = None)
 *
 * SourceConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
data class DisabledConfig (
    val alias: String? = null,

    @get:JsonProperty("column_types")@field:JsonProperty("column_types")
    val columnTypes: Map<String, Any?>? = null,

    val database: String? = null,
    val docs: Docs? = null,
    val enabled: Boolean? = null,

    @get:JsonProperty("full_refresh")@field:JsonProperty("full_refresh")
    val fullRefresh: Boolean? = null,

    val grants: Map<String, Any?>? = null,

    @get:JsonProperty("incremental_strategy")@field:JsonProperty("incremental_strategy")
    val incrementalStrategy: String? = null,

    val materialized: String? = null,
    val meta: Map<String, Any?>? = null,

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

    @get:JsonProperty("check_cols")@field:JsonProperty("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @get:JsonProperty("target_database")@field:JsonProperty("target_database")
    val targetDatabase: String? = null,

    @get:JsonProperty("target_schema")@field:JsonProperty("target_schema")
    val targetSchema: String? = null,

    @get:JsonProperty("updated_at")@field:JsonProperty("updated_at")
    val updatedAt: String? = null,

    @get:JsonProperty("quote_columns")@field:JsonProperty("quote_columns")
    val quoteColumns: Boolean? = null
)

sealed class UniqueKey {
    class StringArrayValue(val value: List<String>) : UniqueKey()
    class StringValue(val value: String)            : UniqueKey()
    class NullValue()                               : UniqueKey()

    fun toJson(): String = mapper.writeValueAsString(when (this) {
        is StringArrayValue -> this.value
        is StringValue      -> this.value
        is NullValue        -> "null"
    })

    companion object {
        fun fromJson(jn: JsonNode): UniqueKey = when (jn) {
            is ArrayNode -> StringArrayValue(mapper.treeToValue(jn))
            is TextNode  -> StringValue(mapper.treeToValue(jn))
            null         -> NullValue()
            else         -> throw IllegalArgumentException()
        }
    }
}

/**
 * Docs(show: bool = True, node_color: Optional[str] = None)
 */
data class Docs (
    @get:JsonProperty("node_color")@field:JsonProperty("node_color")
    val nodeColor: String? = null,

    val show: Boolean? = null
)

/**
 * Hook(sql: str, transaction: bool = True, index: Optional[int] = None)
 */
data class Hook (
    val index: Long? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val sql: String,

    val transaction: Boolean? = null
)

sealed class Tags {
    class StringArrayValue(val value: List<String>) : Tags()
    class StringValue(val value: String)            : Tags()

    fun toJson(): String = mapper.writeValueAsString(when (this) {
        is StringArrayValue -> this.value
        is StringValue      -> this.value
    })

    companion object {
        fun fromJson(jn: JsonNode): Tags = when (jn) {
            is ArrayNode -> StringArrayValue(mapper.treeToValue(jn))
            is TextNode  -> StringValue(mapper.treeToValue(jn))
            else         -> throw IllegalArgumentException()
        }
    }
}

/**
 * DependsOn(macros: List[str] = <factory>, nodes: List[str] = <factory>)
 *
 * Used only in the Macro class
 */
data class DependsOn (
    val macros: List<String>? = null,
    val nodes: List<String>? = null
)

/**
 * ExternalTable(_extra: Dict[str, Any] = <factory>, location: Optional[str] = None,
 * file_format: Optional[str] = None, row_format: Optional[str] = None, tbl_properties:
 * Optional[str] = None, partitions: Union[List[str],
 * List[dbt.contracts.graph.unparsed.ExternalPartition], NoneType] = None)
 */
data class ExternalTable (
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

    fun toJson(): String = mapper.writeValueAsString(when (this) {
        is ExternalPartitionValue -> this.value
        is StringValue            -> this.value
    })

    companion object {
        fun fromJson(jn: JsonNode): Partition = when (jn) {
            is ObjectNode -> ExternalPartitionValue(mapper.treeToValue(jn))
            is TextNode   -> StringValue(mapper.treeToValue(jn))
            else          -> throw IllegalArgumentException()
        }
    }
}

/**
 * ExternalPartition(_extra: Dict[str, Any] = <factory>, name: str = '', description: str =
 * '', data_type: str = '', meta: Dict[str, Any] = <factory>)
 */
data class ExternalPartition (
    @get:JsonProperty("data_type")@field:JsonProperty("data_type")
    val dataType: String? = null,

    val description: String? = null,
    val meta: Map<String, Any?>? = null,
    val name: String? = null
)

/**
 * Used in CompiledNodes as part of ephemeral model processing
 */
data class InjectedCte (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val id: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val sql: String
)

/**
 * FreshnessThreshold(warn_after: Optional[dbt.contracts.graph.unparsed.Time] = <factory>,
 * error_after: Optional[dbt.contracts.graph.unparsed.Time] = <factory>, filter:
 * Optional[str] = None)
 */
data class FreshnessThreshold (
    @get:JsonProperty("error_after")@field:JsonProperty("error_after")
    val errorAfter: Time? = null,

    val filter: String? = null,

    @get:JsonProperty("warn_after")@field:JsonProperty("warn_after")
    val warnAfter: Time? = null
)

/**
 * Time(count: Optional[int] = None, period:
 * Optional[dbt.contracts.graph.unparsed.TimePeriod] = None)
 */
data class Time (
    val count: Long? = null,
    val period: TimePeriod? = null
)

enum class TimePeriod(val value: String) {
    Day("day"),
    Hour("hour"),
    Minute("minute");

    companion object {
        fun fromValue(value: String): TimePeriod = when (value) {
            "day"    -> Day
            "hour"   -> Hour
            "minute" -> Minute
            else     -> throw IllegalArgumentException()
        }
    }
}

/**
 * Quoting(database: Optional[bool] = None, schema: Optional[bool] = None, identifier:
 * Optional[bool] = None, column: Optional[bool] = None)
 */
data class Quoting (
    val column: Boolean? = null,
    val database: Boolean? = null,
    val identifier: Boolean? = null,
    val schema: Boolean? = null
)

enum class DisabledResourceType(val value: String) {
    Analysis("analysis"),
    Model("model"),
    Operation("operation"),
    RPC("rpc"),
    SQLOperation("sql operation"),
    Seed("seed"),
    Snapshot("snapshot"),
    Source("source"),
    Test("test");

    companion object {
        fun fromValue(value: String): DisabledResourceType = when (value) {
            "analysis"      -> Analysis
            "model"         -> Model
            "operation"     -> Operation
            "rpc"           -> RPC
            "sql operation" -> SQLOperation
            "seed"          -> Seed
            "snapshot"      -> Snapshot
            "source"        -> Source
            "test"          -> Test
            else            -> throw IllegalArgumentException()
        }
    }
}

/**
 * TestMetadata(name: str, kwargs: Dict[str, Any] = <factory>, namespace: Optional[str] =
 * None)
 */
data class TestMetadata (
    val kwargs: Map<String, Any?>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    val namespace: String? = null
)

/**
 * Documentation(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path:
 * str, original_file_path: str, unique_id: str, block_contents: str)
 */
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

/**
 * Exposure(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path: str,
 * original_file_path: str, unique_id: str, fqn: List[str], type:
 * dbt.contracts.graph.unparsed.ExposureType, owner:
 * dbt.contracts.graph.unparsed.ExposureOwner, description: str = '', label: Optional[str] =
 * None, maturity: Optional[dbt.contracts.graph.unparsed.MaturityType] = None, meta:
 * Dict[str, Any] = <factory>, tags: List[str] = <factory>, config:
 * dbt.contracts.graph.model_config.ExposureConfig = <factory>, unrendered_config: Dict[str,
 * Any] = <factory>, url: Optional[str] = None, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, refs: List[List[str]] = <factory>,
 * sources: List[List[str]] = <factory>, metrics: List[List[str]] = <factory>, created_at:
 * float = <factory>)
 */
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
    val owner: ExposureOwner,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    val refs: List<List<String>>? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: ExposureResourceType,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val type: Type,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    @get:JsonProperty("unrendered_config")@field:JsonProperty("unrendered_config")
    val unrenderedConfig: Map<String, Any?>? = null,

    val url: String? = null
)

/**
 * ExposureConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
data class ExposureConfig (
    val enabled: Boolean? = null
)

/**
 * DependsOn(macros: List[str] = <factory>, nodes: List[str] = <factory>)
 */
data class ExposureDependsOn (
    val macros: List<String>? = null,
    val nodes: List<String>? = null
)

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

/**
 * ExposureOwner(email: str, name: Optional[str] = None)
 */
data class ExposureOwner (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val email: String,

    val name: String? = null
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

enum class Type(val value: String) {
    Analysis("analysis"),
    Application("application"),
    Dashboard("dashboard"),
    Ml("ml"),
    Notebook("notebook");

    companion object {
        fun fromValue(value: String): Type = when (value) {
            "analysis"    -> Analysis
            "application" -> Application
            "dashboard"   -> Dashboard
            "ml"          -> Ml
            "notebook"    -> Notebook
            else          -> throw IllegalArgumentException()
        }
    }
}

/**
 * Macro(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path: str,
 * original_file_path: str, unique_id: str, macro_sql: str, depends_on:
 * dbt.contracts.graph.nodes.MacroDependsOn = <factory>, description: str = '', meta:
 * Dict[str, Any] = <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Optional[str] = None, arguments:
 * List[dbt.contracts.graph.unparsed.MacroArgument] = <factory>, created_at: float =
 * <factory>, supported_languages: Optional[List[dbt.node_types.ModelLanguage]] = None)
 */
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

/**
 * MacroArgument(name: str, type: Optional[str] = None, description: str = '')
 */
data class MacroArgument (
    val description: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    val type: String? = null
)

/**
 * Used only in the Macro class
 */
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
 *
 * Metadata for the manifest.
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
     * A unique identifier for the project
     */
    @get:JsonProperty("project_id")@field:JsonProperty("project_id")
    val projectId: String? = null,

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

/**
 * Metric(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path: str,
 * original_file_path: str, unique_id: str, fqn: List[str], description: str, label: str,
 * calculation_method: str, expression: str, filters:
 * List[dbt.contracts.graph.unparsed.MetricFilter], time_grains: List[str], dimensions:
 * List[str], timestamp: Optional[str] = None, window:
 * Optional[dbt.contracts.graph.unparsed.MetricTime] = None, model: Optional[str] = None,
 * model_unique_id: Optional[str] = None, meta: Dict[str, Any] = <factory>, tags: List[str]
 * = <factory>, config: dbt.contracts.graph.model_config.MetricConfig = <factory>,
 * unrendered_config: Dict[str, Any] = <factory>, sources: List[List[str]] = <factory>,
 * depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>, refs: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, created_at: float = <factory>)
 */
data class Metric (
    @get:JsonProperty("calculation_method", required=true)@field:JsonProperty("calculation_method", required=true)
    val calculationMethod: String,

    val config: MetricConfig? = null,

    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val description: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val dimensions: List<String>,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val expression: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val filters: List<MetricFilter>,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val fqn: List<String>,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val label: String,

    val meta: Map<String, Any?>? = null,
    val metrics: List<List<String>>? = null,
    val model: String? = null,

    @get:JsonProperty("model_unique_id")@field:JsonProperty("model_unique_id")
    val modelUniqueId: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    val refs: List<List<String>>? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: MetricResourceType,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @get:JsonProperty("time_grains", required=true)@field:JsonProperty("time_grains", required=true)
    val timeGrains: List<String>,

    val timestamp: String? = null,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    @get:JsonProperty("unrendered_config")@field:JsonProperty("unrendered_config")
    val unrenderedConfig: Map<String, Any?>? = null,

    val window: MetricTime? = null
)

/**
 * MetricConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
data class MetricConfig (
    val enabled: Boolean? = null
)

/**
 * MetricFilter(field: str, operator: str, value: str)
 */
data class MetricFilter (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val field: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val operator: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val value: String
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

/**
 * MetricTime(count: Optional[int] = None, period:
 * Optional[dbt.contracts.graph.unparsed.MetricTimePeriod] = None)
 */
data class MetricTime (
    val count: Long? = null,
    val period: MetricTimePeriod? = null
)

enum class MetricTimePeriod(val value: String) {
    Day("day"),
    Month("month"),
    Week("week"),
    Year("year");

    companion object {
        fun fromValue(value: String): MetricTimePeriod = when (value) {
            "day"   -> Day
            "month" -> Month
            "week"  -> Week
            "year"  -> Year
            else    -> throw IllegalArgumentException()
        }
    }
}

/**
 * AnalysisNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * SingularTestNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * HookNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None, index: Optional[int] = None)
 *
 * ModelNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * RPCNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * SqlNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * GenericTestNode(test_metadata: dbt.contracts.graph.nodes.TestMetadata, database:
 * Optional[str], schema: str, name: str, resource_type: dbt.node_types.NodeType,
 * package_name: str, path: str, original_file_path: str, unique_id: str, fqn: List[str],
 * alias: str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None, column_name: Optional[str] = None, file_key_name: Optional[str] = None)
 *
 * SnapshotNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.SnapshotConfig, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', language: str =
 * 'sql', refs: List[List[str]] = <factory>, sources: List[List[str]] = <factory>, metrics:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>,
 * compiled_path: Optional[str] = None, compiled: bool = False, compiled_code: Optional[str]
 * = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Optional[str]
 * = None)
 *
 * SeedNode(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.SeedConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Optional[str] = None,
 * build_path: Optional[str] = None, deferred: bool = False, unrendered_config: Dict[str,
 * Any] = <factory>, created_at: float = <factory>, config_call_dict: Dict[str, Any] =
 * <factory>, relation_name: Optional[str] = None, raw_code: str = '', root_path:
 * Optional[str] = None, depends_on: dbt.contracts.graph.nodes.MacroDependsOn = <factory>)
 */
data class Node (
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

    val refs: List<List<String>>? = null,

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

    @get:JsonProperty("column_name")@field:JsonProperty("column_name")
    val columnName: String? = null,

    @get:JsonProperty("file_key_name")@field:JsonProperty("file_key_name")
    val fileKeyName: String? = null,

    @get:JsonProperty("test_metadata")@field:JsonProperty("test_metadata")
    val testMetadata: TestMetadata? = null,

    @get:JsonProperty("root_path")@field:JsonProperty("root_path")
    val rootPath: String? = null
)

/**
 * NodeConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Optional[str]
 * = None, schema: Optional[str] = None, database: Optional[str] = None, tags:
 * Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, materialized: str =
 * 'view', incremental_strategy: Optional[str] = None, persist_docs: Dict[str, Any] =
 * <factory>, post_hook: List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Optional[bool] = None,
 * unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Optional[str] =
 * 'ignore', grants: Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>)
 *
 * TestConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Optional[str]
 * = None, schema: Optional[str] = 'dbt_test__audit', database: Optional[str] = None, tags:
 * Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, materialized: str =
 * 'test', severity: dbt.contracts.graph.model_config.Severity = 'ERROR', store_failures:
 * Optional[bool] = None, where: Optional[str] = None, limit: Optional[int] = None,
 * fail_calc: str = 'count(*)', warn_if: str = '!= 0', error_if: str = '!= 0')
 *
 * SnapshotConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias:
 * Optional[str] = None, schema: Optional[str] = None, database: Optional[str] = None, tags:
 * Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, materialized: str =
 * 'snapshot', incremental_strategy: Optional[str] = None, persist_docs: Dict[str, Any] =
 * <factory>, post_hook: List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Optional[bool] = None,
 * unique_key: Optional[str] = None, on_schema_change: Optional[str] = 'ignore', grants:
 * Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>, strategy: Optional[str] = None,
 * target_schema: Optional[str] = None, target_database: Optional[str] = None, updated_at:
 * Optional[str] = None, check_cols: Union[str, List[str], NoneType] = None)
 *
 * SeedConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Optional[str]
 * = None, schema: Optional[str] = None, database: Optional[str] = None, tags:
 * Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, materialized: str =
 * 'seed', incremental_strategy: Optional[str] = None, persist_docs: Dict[str, Any] =
 * <factory>, post_hook: List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Optional[bool] = None,
 * unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Optional[str] =
 * 'ignore', grants: Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>, quote_columns: Optional[bool] = None)
 */
data class NodeConfig (
    val alias: String? = null,

    @get:JsonProperty("column_types")@field:JsonProperty("column_types")
    val columnTypes: Map<String, Any?>? = null,

    val database: String? = null,
    val docs: Docs? = null,
    val enabled: Boolean? = null,

    @get:JsonProperty("full_refresh")@field:JsonProperty("full_refresh")
    val fullRefresh: Boolean? = null,

    val grants: Map<String, Any?>? = null,

    @get:JsonProperty("incremental_strategy")@field:JsonProperty("incremental_strategy")
    val incrementalStrategy: String? = null,

    val materialized: String? = null,
    val meta: Map<String, Any?>? = null,

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

    @get:JsonProperty("check_cols")@field:JsonProperty("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @get:JsonProperty("target_database")@field:JsonProperty("target_database")
    val targetDatabase: String? = null,

    @get:JsonProperty("target_schema")@field:JsonProperty("target_schema")
    val targetSchema: String? = null,

    @get:JsonProperty("updated_at")@field:JsonProperty("updated_at")
    val updatedAt: String? = null,

    @get:JsonProperty("quote_columns")@field:JsonProperty("quote_columns")
    val quoteColumns: Boolean? = null
)

enum class NodeResourceType(val value: String) {
    Analysis("analysis"),
    Model("model"),
    Operation("operation"),
    RPC("rpc"),
    SQLOperation("sql operation"),
    Seed("seed"),
    Snapshot("snapshot"),
    Test("test");

    companion object {
        fun fromValue(value: String): NodeResourceType = when (value) {
            "analysis"      -> Analysis
            "model"         -> Model
            "operation"     -> Operation
            "rpc"           -> RPC
            "sql operation" -> SQLOperation
            "seed"          -> Seed
            "snapshot"      -> Snapshot
            "test"          -> Test
            else            -> throw IllegalArgumentException()
        }
    }
}

/**
 * SourceDefinition(database: Optional[str], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], source_name: str, source_description: str, loader: str,
 * identifier: str, _event_status: Dict[str, Any] = <factory>, quoting:
 * dbt.contracts.graph.unparsed.Quoting = <factory>, loaded_at_field: Optional[str] = None,
 * freshness: Optional[dbt.contracts.graph.unparsed.FreshnessThreshold] = None, external:
 * Optional[dbt.contracts.graph.unparsed.ExternalTable] = None, description: str = '',
 * columns: Dict[str, dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str,
 * Any] = <factory>, source_meta: Dict[str, Any] = <factory>, tags: List[str] = <factory>,
 * config: dbt.contracts.graph.model_config.SourceConfig = <factory>, patch_path:
 * Optional[str] = None, unrendered_config: Dict[str, Any] = <factory>, relation_name:
 * Optional[str] = None, created_at: float = <factory>)
 */
data class SourceDefinition (
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

/**
 * SourceConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
data class SourceConfig (
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

