package com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV10

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
 * dbt.contracts.graph.nodes.Metric], groups: Mapping[str, dbt.contracts.graph.nodes.Group],
 * selectors: Mapping[str, Any], disabled: Union[Mapping[str,
 * List[Union[dbt.contracts.graph.nodes.AnalysisNode,
 * dbt.contracts.graph.nodes.SingularTestNode, dbt.contracts.graph.nodes.HookNode,
 * dbt.contracts.graph.nodes.ModelNode, dbt.contracts.graph.nodes.RPCNode,
 * dbt.contracts.graph.nodes.SqlNode, dbt.contracts.graph.nodes.GenericTestNode,
 * dbt.contracts.graph.nodes.SnapshotNode, dbt.contracts.graph.nodes.SeedNode,
 * dbt.contracts.graph.nodes.SourceDefinition, dbt.contracts.graph.nodes.Exposure,
 * dbt.contracts.graph.nodes.Metric, dbt.contracts.graph.nodes.SemanticModel]]], NoneType],
 * parent_map: Union[Dict[str, List[str]], NoneType], child_map: Union[Dict[str, List[str]],
 * NoneType], group_map: Union[Dict[str, List[str]], NoneType], semantic_models:
 * Mapping[str, dbt.contracts.graph.nodes.SemanticModel])
 */
data class ManifestV10 (
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
     * The selectors defined in selectors.yml
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val selectors: Map<String, Any?>,

    /**
     * The semantic models defined in the dbt project
     */
    @get:JsonProperty("semantic_models", required=true)@field:JsonProperty("semantic_models", required=true)
    val semanticModels: Map<String, SemanticModel>,

    /**
     * The sources defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val sources: Map<String, SourceDefinition>
): ManifestInterface {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapper.readValue<ManifestV10>(json)
    }
}

/**
 * AnalysisNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>)
 *
 * SingularTestNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>)
 *
 * HookNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>, index:
 * Union[int, NoneType] = None)
 *
 * ModelNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>, access:
 * dbt.node_types.AccessType = <AccessType.Protected: 'protected'>, constraints:
 * List[dbt.contracts.graph.nodes.ModelLevelConstraint] = <factory>, version: Union[str,
 * float, NoneType] = None, latest_version: Union[str, float, NoneType] = None,
 * deprecation_date: Union[datetime.datetime, NoneType] = None, defer_relation:
 * Union[dbt.contracts.graph.nodes.DeferRelation, NoneType] = None)
 *
 * RPCNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>)
 *
 * SqlNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>)
 *
 * GenericTestNode(test_metadata: dbt.contracts.graph.nodes.TestMetadata, database:
 * Union[str, NoneType], schema: str, name: str, resource_type: dbt.node_types.NodeType,
 * package_name: str, path: str, original_file_path: str, unique_id: str, fqn: List[str],
 * alias: str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>, column_name:
 * Union[str, NoneType] = None, file_key_name: Union[str, NoneType] = None, attached_node:
 * Union[str, NoneType] = None)
 *
 * SnapshotNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.SnapshotConfig, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>,
 * defer_relation: Union[dbt.contracts.graph.nodes.DeferRelation, NoneType] = None)
 *
 * SeedNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.SeedConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', root_path: Union[str, NoneType] = None, depends_on:
 * dbt.contracts.graph.nodes.MacroDependsOn = <factory>, defer_relation:
 * Union[dbt.contracts.graph.nodes.DeferRelation, NoneType] = None)
 *
 * SourceDefinition(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], source_name: str, source_description: str, loader: str,
 * identifier: str, _event_status: Dict[str, Any] = <factory>, quoting:
 * dbt.contracts.graph.unparsed.Quoting = <factory>, loaded_at_field: Union[str, NoneType] =
 * None, freshness: Union[dbt.contracts.graph.unparsed.FreshnessThreshold, NoneType] = None,
 * external: Union[dbt.contracts.graph.unparsed.ExternalTable, NoneType] = None,
 * description: str = '', columns: Dict[str, dbt.contracts.graph.nodes.ColumnInfo] =
 * <factory>, meta: Dict[str, Any] = <factory>, source_meta: Dict[str, Any] = <factory>,
 * tags: List[str] = <factory>, config: dbt.contracts.graph.model_config.SourceConfig =
 * <factory>, patch_path: Union[str, NoneType] = None, unrendered_config: Dict[str, Any] =
 * <factory>, relation_name: Union[str, NoneType] = None, created_at: float = <factory>)
 *
 * Exposure(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path: str,
 * original_file_path: str, unique_id: str, fqn: List[str], type:
 * dbt.contracts.graph.unparsed.ExposureType, owner: dbt.contracts.graph.unparsed.Owner,
 * description: str = '', label: Union[str, NoneType] = None, maturity:
 * Union[dbt.contracts.graph.unparsed.MaturityType, NoneType] = None, meta: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, config:
 * dbt.contracts.graph.model_config.ExposureConfig = <factory>, unrendered_config: Dict[str,
 * Any] = <factory>, url: Union[str, NoneType] = None, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, created_at: float = <factory>)
 *
 * Metric(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path: str,
 * original_file_path: str, unique_id: str, fqn: List[str], description: str, label: str,
 * type: dbt_semantic_interfaces.type_enums.metric_type.MetricType, type_params:
 * dbt.contracts.graph.nodes.MetricTypeParams, filter:
 * Union[dbt.contracts.graph.nodes.WhereFilter, NoneType] = None, metadata:
 * Union[dbt.contracts.graph.semantic_models.SourceFileMetadata, NoneType] = None, meta:
 * Dict[str, Any] = <factory>, tags: List[str] = <factory>, config:
 * dbt.contracts.graph.model_config.MetricConfig = <factory>, unrendered_config: Dict[str,
 * Any] = <factory>, sources: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, metrics: List[List[str]] =
 * <factory>, created_at: float = <factory>, group: Union[str, NoneType] = None)
 *
 * SemanticModel(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path:
 * str, original_file_path: str, unique_id: str, fqn: List[str], model: str, node_relation:
 * Union[dbt.contracts.graph.nodes.NodeRelation, NoneType], description: Union[str,
 * NoneType] = None, label: Union[str, NoneType] = None, defaults:
 * Union[dbt.contracts.graph.semantic_models.Defaults, NoneType] = None, entities:
 * Sequence[dbt.contracts.graph.semantic_models.Entity] = <factory>, measures:
 * Sequence[dbt.contracts.graph.semantic_models.Measure] = <factory>, dimensions:
 * Sequence[dbt.contracts.graph.semantic_models.Dimension] = <factory>, metadata:
 * Union[dbt.contracts.graph.semantic_models.SourceFileMetadata, NoneType] = None,
 * depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>, refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, created_at: float = <factory>,
 * config: dbt.contracts.graph.model_config.SemanticModelConfig = <factory>, primary_entity:
 * Union[str, NoneType] = None)
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
    val filter: WhereFilter? = null,
    val metadata: SourceFileMetadata? = null,

    @get:JsonProperty("type_params")@field:JsonProperty("type_params")
    val typeParams: MetricTypeParams? = null,

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

/**
 * ColumnLevelConstraint(type: dbt.contracts.graph.nodes.ConstraintType, name: Union[str,
 * NoneType] = None, expression: Union[str, NoneType] = None, warn_unenforced: bool = True,
 * warn_unsupported: bool = True)
 */
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

/**
 * NodeConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str, NoneType] =
 * None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, group:
 * Union[str, NoneType] = None, materialized: str = 'view', incremental_strategy: Union[str,
 * NoneType] = None, persist_docs: Dict[str, Any] = <factory>, post_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Union[str,
 * NoneType] = 'ignore', on_configuration_change:
 * dbt.contracts.graph.model_config.OnConfigurationChangeOption = <factory>, grants:
 * Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>, contract:
 * dbt.contracts.graph.model_config.ContractConfig = <factory>)
 *
 * TestConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = 'dbt_test__audit', database: Union[str,
 * NoneType] = None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] =
 * <factory>, group: Union[str, NoneType] = None, materialized: str = 'test', severity:
 * dbt.contracts.graph.model_config.Severity = 'ERROR', store_failures: Union[bool,
 * NoneType] = None, where: Union[str, NoneType] = None, limit: Union[int, NoneType] = None,
 * fail_calc: str = 'count(*)', warn_if: str = '!= 0', error_if: str = '!= 0')
 *
 * SnapshotConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias:
 * Union[str, NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str,
 * NoneType] = None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] =
 * <factory>, group: Union[str, NoneType] = None, materialized: str = 'snapshot',
 * incremental_strategy: Union[str, NoneType] = None, persist_docs: Dict[str, Any] =
 * <factory>, post_hook: List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, NoneType] = None, on_schema_change: Union[str, NoneType] =
 * 'ignore', on_configuration_change:
 * dbt.contracts.graph.model_config.OnConfigurationChangeOption = <factory>, grants:
 * Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>, contract:
 * dbt.contracts.graph.model_config.ContractConfig = <factory>, strategy: Union[str,
 * NoneType] = None, target_schema: Union[str, NoneType] = None, target_database: Union[str,
 * NoneType] = None, updated_at: Union[str, NoneType] = None, check_cols: Union[str,
 * List[str], NoneType] = None)
 *
 * SeedConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str, NoneType] =
 * None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, group:
 * Union[str, NoneType] = None, materialized: str = 'seed', incremental_strategy: Union[str,
 * NoneType] = None, persist_docs: Dict[str, Any] = <factory>, post_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Union[str,
 * NoneType] = 'ignore', on_configuration_change:
 * dbt.contracts.graph.model_config.OnConfigurationChangeOption = <factory>, grants:
 * Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>, contract:
 * dbt.contracts.graph.model_config.ContractConfig = <factory>, quote_columns: Union[bool,
 * NoneType] = None)
 *
 * SourceConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 *
 * ExposureConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 *
 * MetricConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, group: Union[str,
 * NoneType] = None)
 *
 * SemanticModelConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
data class DisabledConfig (
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
 * ContractConfig(enforced: bool = False)
 */
data class ContractConfig (
    val enforced: Boolean? = null
)

/**
 * Docs(show: bool = True, node_color: Union[str, NoneType] = None)
 */
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

/**
 * Hook(sql: str, transaction: bool = True, index: Union[int, NoneType] = None)
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
 * ModelLevelConstraint(type: dbt.contracts.graph.nodes.ConstraintType, name: Union[str,
 * NoneType] = None, expression: Union[str, NoneType] = None, warn_unenforced: bool = True,
 * warn_unsupported: bool = True, columns: List[str] = <factory>)
 */
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

/**
 * Contract(enforced: bool = False, checksum: Union[str, NoneType] = None)
 */
data class Contract (
    val checksum: String? = null,
    val enforced: Boolean? = null
)

/**
 * Defaults(agg_time_dimension: Union[str, NoneType] = None)
 */
data class Defaults (
    @get:JsonProperty("agg_time_dimension")@field:JsonProperty("agg_time_dimension")
    val aggTimeDimension: String? = null
)

/**
 * DeferRelation(database: Union[str, NoneType], schema: str, alias: str, relation_name:
 * Union[str, NoneType])
 */
data class DeferRelation (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val alias: String,

    val database: String? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val schema: String
)

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
 * Dimension(name: str, type:
 * dbt_semantic_interfaces.type_enums.dimension_type.DimensionType, description: Union[str,
 * NoneType] = None, label: Union[str, NoneType] = None, is_partition: bool = False,
 * type_params: Union[dbt.contracts.graph.semantic_models.DimensionTypeParams, NoneType] =
 * None, expr: Union[str, NoneType] = None, metadata:
 * Union[dbt.contracts.graph.semantic_models.SourceFileMetadata, NoneType] = None)
 */
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

/**
 * Provides file context about what something was created from.
 *
 * Implementation of the dbt-semantic-interfaces `Metadata` protocol
 */
data class SourceFileMetadata (
    @get:JsonProperty("file_slice", required=true)@field:JsonProperty("file_slice", required=true)
    val fileSlice: FileSlice,

    @get:JsonProperty("repo_file_path", required=true)@field:JsonProperty("repo_file_path", required=true)
    val repoFilePath: String
)

/**
 * Provides file slice level context about what something was created from.
 *
 * Implementation of the dbt-semantic-interfaces `FileSlice` protocol
 */
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

/**
 * DimensionTypeParams(time_granularity:
 * dbt_semantic_interfaces.type_enums.time_granularity.TimeGranularity, validity_params:
 * Union[dbt.contracts.graph.semantic_models.DimensionValidityParams, NoneType] = None)
 */
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

/**
 * DimensionValidityParams(is_start: bool = False, is_end: bool = False)
 */
data class DimensionValidityParams (
    @get:JsonProperty("is_end")@field:JsonProperty("is_end")
    val isEnd: Boolean? = null,

    @get:JsonProperty("is_start")@field:JsonProperty("is_start")
    val isStart: Boolean? = null
)

/**
 * Entity(name: str, type: dbt_semantic_interfaces.type_enums.entity_type.EntityType,
 * description: Union[str, NoneType] = None, label: Union[str, NoneType] = None, role:
 * Union[str, NoneType] = None, expr: Union[str, NoneType] = None)
 */
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

/**
 * ExternalTable(_extra: Dict[str, Any] = <factory>, location: Union[str, NoneType] = None,
 * file_format: Union[str, NoneType] = None, row_format: Union[str, NoneType] = None,
 * tbl_properties: Union[str, NoneType] = None, partitions: Union[List[str],
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
 * WhereFilter(where_sql_template: str)
 */
data class WhereFilter (
    @get:JsonProperty("where_sql_template", required=true)@field:JsonProperty("where_sql_template", required=true)
    val whereSqlTemplate: String
)

/**
 * FreshnessThreshold(warn_after: Union[dbt.contracts.graph.unparsed.Time, NoneType] =
 * <factory>, error_after: Union[dbt.contracts.graph.unparsed.Time, NoneType] = <factory>,
 * filter: Union[str, NoneType] = None)
 */
data class FreshnessThreshold (
    @get:JsonProperty("error_after")@field:JsonProperty("error_after")
    val errorAfter: Time? = null,

    val filter: String? = null,

    @get:JsonProperty("warn_after")@field:JsonProperty("warn_after")
    val warnAfter: Time? = null
)

/**
 * Time(count: Union[int, NoneType] = None, period:
 * Union[dbt.contracts.graph.unparsed.TimePeriod, NoneType] = None)
 */
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
    class StringValue(val value: String) : Version()
    class NullValue()                    : Version()

    fun toJson(): String = mapper.writeValueAsString(when (this) {
        is DoubleValue -> this.value
        is StringValue -> this.value
        is NullValue   -> "null"
    })

    companion object {
        fun fromJson(jn: JsonNode): Version = when (jn) {
            is DoubleNode -> DoubleValue(mapper.treeToValue(jn))
            is TextNode   -> StringValue(mapper.treeToValue(jn))
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

/**
 * Measure(name: str, agg:
 * dbt_semantic_interfaces.type_enums.aggregation_type.AggregationType, description:
 * Union[str, NoneType] = None, label: Union[str, NoneType] = None, create_metric: bool =
 * False, expr: Union[str, NoneType] = None, agg_params:
 * Union[dbt.contracts.graph.semantic_models.MeasureAggregationParameters, NoneType] = None,
 * non_additive_dimension: Union[dbt.contracts.graph.semantic_models.NonAdditiveDimension,
 * NoneType] = None, agg_time_dimension: Union[str, NoneType] = None)
 */
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

/**
 * MeasureAggregationParameters(percentile: Union[float, NoneType] = None,
 * use_discrete_percentile: bool = False, use_approximate_percentile: bool = False)
 */
data class MeasureAggregationParameters (
    val percentile: Double? = null,

    @get:JsonProperty("use_approximate_percentile")@field:JsonProperty("use_approximate_percentile")
    val useApproximatePercentile: Boolean? = null,

    @get:JsonProperty("use_discrete_percentile")@field:JsonProperty("use_discrete_percentile")
    val useDiscretePercentile: Boolean? = null
)

/**
 * NonAdditiveDimension(name: str, window_choice:
 * dbt_semantic_interfaces.type_enums.aggregation_type.AggregationType, window_groupings:
 * List[str])
 */
data class NonAdditiveDimension (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("window_choice", required=true)@field:JsonProperty("window_choice", required=true)
    val windowChoice: Agg,

    @get:JsonProperty("window_groupings", required=true)@field:JsonProperty("window_groupings", required=true)
    val windowGroupings: List<String>
)

/**
 * NodeRelation(alias: str, schema_name: str, database: Union[str, NoneType] = None,
 * relation_name: Union[str, NoneType] = None)
 */
data class NodeRelation (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val alias: String,

    val database: String? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

    @get:JsonProperty("schema_name", required=true)@field:JsonProperty("schema_name", required=true)
    val schemaName: String
)

/**
 * Owner(_extra: Dict[str, Any] = <factory>, email: Union[str, NoneType] = None, name:
 * Union[str, NoneType] = None)
 */
data class Owner (
    val email: String? = null,
    val name: String? = null
)

/**
 * Quoting(database: Union[bool, NoneType] = None, schema: Union[bool, NoneType] = None,
 * identifier: Union[bool, NoneType] = None, column: Union[bool, NoneType] = None)
 */
data class Quoting (
    val column: Boolean? = null,
    val database: Boolean? = null,
    val identifier: Boolean? = null,
    val schema: Boolean? = null
)

/**
 * RefArgs(name: str, package: Union[str, NoneType] = None, version: Union[str, float,
 * NoneType] = None)
 */
data class RefArgs (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("package")@field:JsonProperty("package")
    val refArgsPackage: String? = null,

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
            "seed"           -> Seed
            "semantic_model" -> SemanticModel
            "snapshot"       -> Snapshot
            "source"         -> Source
            "test"           -> Test
            else             -> throw IllegalArgumentException()
        }
    }
}

/**
 * TestMetadata(name: str, kwargs: Dict[str, Any] = <factory>, namespace: Union[str,
 * NoneType] = None)
 */
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

/**
 * MetricTypeParams(measure: Union[dbt.contracts.graph.nodes.MetricInputMeasure, NoneType] =
 * None, input_measures: List[dbt.contracts.graph.nodes.MetricInputMeasure] = <factory>,
 * numerator: Union[dbt.contracts.graph.nodes.MetricInput, NoneType] = None, denominator:
 * Union[dbt.contracts.graph.nodes.MetricInput, NoneType] = None, expr: Union[str, NoneType]
 * = None, window: Union[dbt.contracts.graph.nodes.MetricTimeWindow, NoneType] = None,
 * grain_to_date: Union[dbt_semantic_interfaces.type_enums.time_granularity.TimeGranularity,
 * NoneType] = None, metrics: Union[List[dbt.contracts.graph.nodes.MetricInput], NoneType] =
 * None)
 */
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

/**
 * MetricInput(name: str, filter: Union[dbt.contracts.graph.nodes.WhereFilter, NoneType] =
 * None, alias: Union[str, NoneType] = None, offset_window:
 * Union[dbt.contracts.graph.nodes.MetricTimeWindow, NoneType] = None, offset_to_grain:
 * Union[dbt_semantic_interfaces.type_enums.time_granularity.TimeGranularity, NoneType] =
 * None)
 */
data class MetricInput (
    val alias: String? = null,
    val filter: WhereFilter? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("offset_to_grain")@field:JsonProperty("offset_to_grain")
    val offsetToGrain: Granularity? = null,

    @get:JsonProperty("offset_window")@field:JsonProperty("offset_window")
    val offsetWindow: MetricTimeWindow? = null
)

/**
 * MetricTimeWindow(count: int, granularity:
 * dbt_semantic_interfaces.type_enums.time_granularity.TimeGranularity)
 */
data class MetricTimeWindow (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val count: Long,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val granularity: Granularity
)

/**
 * MetricInputMeasure(name: str, filter: Union[dbt.contracts.graph.nodes.WhereFilter,
 * NoneType] = None, alias: Union[str, NoneType] = None, join_to_timespine: bool = False,
 * fill_nulls_with: Union[int, NoneType] = None)
 */
data class MetricInputMeasure (
    val alias: String? = null,

    @get:JsonProperty("fill_nulls_with")@field:JsonProperty("fill_nulls_with")
    val fillNullsWith: Long? = null,

    val filter: WhereFilter? = null,

    @get:JsonProperty("join_to_timespine")@field:JsonProperty("join_to_timespine")
    val joinToTimespine: Boolean? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String
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
 * dbt.contracts.graph.unparsed.ExposureType, owner: dbt.contracts.graph.unparsed.Owner,
 * description: str = '', label: Union[str, NoneType] = None, maturity:
 * Union[dbt.contracts.graph.unparsed.MaturityType, NoneType] = None, meta: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, config:
 * dbt.contracts.graph.model_config.ExposureConfig = <factory>, unrendered_config: Dict[str,
 * Any] = <factory>, url: Union[str, NoneType] = None, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, created_at: float = <factory>)
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

/**
 * Group(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path: str,
 * original_file_path: str, unique_id: str, owner: dbt.contracts.graph.unparsed.Owner)
 */
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

/**
 * Macro(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path: str,
 * original_file_path: str, unique_id: str, macro_sql: str, depends_on:
 * dbt.contracts.graph.nodes.MacroDependsOn = <factory>, description: str = '', meta:
 * Dict[str, Any] = <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, arguments:
 * List[dbt.contracts.graph.unparsed.MacroArgument] = <factory>, created_at: float =
 * <factory>, supported_languages: Union[List[dbt.node_types.ModelLanguage], NoneType] =
 * None)
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
 * MacroArgument(name: str, type: Union[str, NoneType] = None, description: str = '')
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

/**
 * Metric(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path: str,
 * original_file_path: str, unique_id: str, fqn: List[str], description: str, label: str,
 * type: dbt_semantic_interfaces.type_enums.metric_type.MetricType, type_params:
 * dbt.contracts.graph.nodes.MetricTypeParams, filter:
 * Union[dbt.contracts.graph.nodes.WhereFilter, NoneType] = None, metadata:
 * Union[dbt.contracts.graph.semantic_models.SourceFileMetadata, NoneType] = None, meta:
 * Dict[str, Any] = <factory>, tags: List[str] = <factory>, config:
 * dbt.contracts.graph.model_config.MetricConfig = <factory>, unrendered_config: Dict[str,
 * Any] = <factory>, sources: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, metrics: List[List[str]] =
 * <factory>, created_at: float = <factory>, group: Union[str, NoneType] = None)
 */
data class Metric (
    val config: MetricConfig? = null,

    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val description: String,

    val filter: WhereFilter? = null,

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

/**
 * MetricConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, group: Union[str,
 * NoneType] = None)
 */
data class MetricConfig (
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

/**
 * AnalysisNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>)
 *
 * SingularTestNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>)
 *
 * HookNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>, index:
 * Union[int, NoneType] = None)
 *
 * ModelNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>, access:
 * dbt.node_types.AccessType = <AccessType.Protected: 'protected'>, constraints:
 * List[dbt.contracts.graph.nodes.ModelLevelConstraint] = <factory>, version: Union[str,
 * float, NoneType] = None, latest_version: Union[str, float, NoneType] = None,
 * deprecation_date: Union[datetime.datetime, NoneType] = None, defer_relation:
 * Union[dbt.contracts.graph.nodes.DeferRelation, NoneType] = None)
 *
 * RPCNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>)
 *
 * SqlNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>)
 *
 * GenericTestNode(test_metadata: dbt.contracts.graph.nodes.TestMetadata, database:
 * Union[str, NoneType], schema: str, name: str, resource_type: dbt.node_types.NodeType,
 * package_name: str, path: str, original_file_path: str, unique_id: str, fqn: List[str],
 * alias: str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>, column_name:
 * Union[str, NoneType] = None, file_key_name: Union[str, NoneType] = None, attached_node:
 * Union[str, NoneType] = None)
 *
 * SnapshotNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.SnapshotConfig, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', language: str = 'sql', refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, sources: List[List[str]] =
 * <factory>, metrics: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.nodes.DependsOn = <factory>, compiled_path: Union[str, NoneType] =
 * None, compiled: bool = False, compiled_code: Union[str, NoneType] = None,
 * extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.nodes.InjectedCTE] = <factory>, _pre_injected_sql: Union[str,
 * NoneType] = None, contract: dbt.contracts.graph.nodes.Contract = <factory>,
 * defer_relation: Union[dbt.contracts.graph.nodes.DeferRelation, NoneType] = None)
 *
 * SeedNode(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], alias: str, checksum: dbt.contracts.files.FileHash,
 * config: dbt.contracts.graph.model_config.SeedConfig = <factory>, _event_status: Dict[str,
 * Any] = <factory>, tags: List[str] = <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.nodes.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * group: Union[str, NoneType] = None, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] = None,
 * deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at: float
 * = <factory>, config_call_dict: Dict[str, Any] = <factory>, relation_name: Union[str,
 * NoneType] = None, raw_code: str = '', root_path: Union[str, NoneType] = None, depends_on:
 * dbt.contracts.graph.nodes.MacroDependsOn = <factory>, defer_relation:
 * Union[dbt.contracts.graph.nodes.DeferRelation, NoneType] = None)
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

/**
 * NodeConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str, NoneType] =
 * None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, group:
 * Union[str, NoneType] = None, materialized: str = 'view', incremental_strategy: Union[str,
 * NoneType] = None, persist_docs: Dict[str, Any] = <factory>, post_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Union[str,
 * NoneType] = 'ignore', on_configuration_change:
 * dbt.contracts.graph.model_config.OnConfigurationChangeOption = <factory>, grants:
 * Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>, contract:
 * dbt.contracts.graph.model_config.ContractConfig = <factory>)
 *
 * TestConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = 'dbt_test__audit', database: Union[str,
 * NoneType] = None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] =
 * <factory>, group: Union[str, NoneType] = None, materialized: str = 'test', severity:
 * dbt.contracts.graph.model_config.Severity = 'ERROR', store_failures: Union[bool,
 * NoneType] = None, where: Union[str, NoneType] = None, limit: Union[int, NoneType] = None,
 * fail_calc: str = 'count(*)', warn_if: str = '!= 0', error_if: str = '!= 0')
 *
 * SnapshotConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias:
 * Union[str, NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str,
 * NoneType] = None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] =
 * <factory>, group: Union[str, NoneType] = None, materialized: str = 'snapshot',
 * incremental_strategy: Union[str, NoneType] = None, persist_docs: Dict[str, Any] =
 * <factory>, post_hook: List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, NoneType] = None, on_schema_change: Union[str, NoneType] =
 * 'ignore', on_configuration_change:
 * dbt.contracts.graph.model_config.OnConfigurationChangeOption = <factory>, grants:
 * Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>, contract:
 * dbt.contracts.graph.model_config.ContractConfig = <factory>, strategy: Union[str,
 * NoneType] = None, target_schema: Union[str, NoneType] = None, target_database: Union[str,
 * NoneType] = None, updated_at: Union[str, NoneType] = None, check_cols: Union[str,
 * List[str], NoneType] = None)
 *
 * SeedConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str, NoneType] =
 * None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>, group:
 * Union[str, NoneType] = None, materialized: str = 'seed', incremental_strategy: Union[str,
 * NoneType] = None, persist_docs: Dict[str, Any] = <factory>, post_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Union[str,
 * NoneType] = 'ignore', on_configuration_change:
 * dbt.contracts.graph.model_config.OnConfigurationChangeOption = <factory>, grants:
 * Dict[str, Any] = <factory>, packages: List[str] = <factory>, docs:
 * dbt.contracts.graph.unparsed.Docs = <factory>, contract:
 * dbt.contracts.graph.model_config.ContractConfig = <factory>, quote_columns: Union[bool,
 * NoneType] = None)
 */
data class NodeConfig (
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

/**
 * SemanticModel(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path:
 * str, original_file_path: str, unique_id: str, fqn: List[str], model: str, node_relation:
 * Union[dbt.contracts.graph.nodes.NodeRelation, NoneType], description: Union[str,
 * NoneType] = None, label: Union[str, NoneType] = None, defaults:
 * Union[dbt.contracts.graph.semantic_models.Defaults, NoneType] = None, entities:
 * Sequence[dbt.contracts.graph.semantic_models.Entity] = <factory>, measures:
 * Sequence[dbt.contracts.graph.semantic_models.Measure] = <factory>, dimensions:
 * Sequence[dbt.contracts.graph.semantic_models.Dimension] = <factory>, metadata:
 * Union[dbt.contracts.graph.semantic_models.SourceFileMetadata, NoneType] = None,
 * depends_on: dbt.contracts.graph.nodes.DependsOn = <factory>, refs:
 * List[dbt.contracts.graph.nodes.RefArgs] = <factory>, created_at: float = <factory>,
 * config: dbt.contracts.graph.model_config.SemanticModelConfig = <factory>, primary_entity:
 * Union[str, NoneType] = None)
 */
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
    val uniqueId: String
)

/**
 * SemanticModelConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
data class SemanticModelConfig (
    val enabled: Boolean? = null
)

/**
 * SourceDefinition(database: Union[str, NoneType], schema: str, name: str, resource_type:
 * dbt.node_types.NodeType, package_name: str, path: str, original_file_path: str,
 * unique_id: str, fqn: List[str], source_name: str, source_description: str, loader: str,
 * identifier: str, _event_status: Dict[str, Any] = <factory>, quoting:
 * dbt.contracts.graph.unparsed.Quoting = <factory>, loaded_at_field: Union[str, NoneType] =
 * None, freshness: Union[dbt.contracts.graph.unparsed.FreshnessThreshold, NoneType] = None,
 * external: Union[dbt.contracts.graph.unparsed.ExternalTable, NoneType] = None,
 * description: str = '', columns: Dict[str, dbt.contracts.graph.nodes.ColumnInfo] =
 * <factory>, meta: Dict[str, Any] = <factory>, source_meta: Dict[str, Any] = <factory>,
 * tags: List[str] = <factory>, config: dbt.contracts.graph.model_config.SourceConfig =
 * <factory>, patch_path: Union[str, NoneType] = None, unrendered_config: Dict[str, Any] =
 * <factory>, relation_name: Union[str, NoneType] = None, created_at: float = <factory>)
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

