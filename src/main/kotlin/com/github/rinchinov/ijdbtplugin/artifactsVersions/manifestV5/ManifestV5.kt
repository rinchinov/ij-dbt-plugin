package com.github.rinchinov.ijdbtplugin.artifactsVersions.manifestV5

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
    convert(Period::class,               { Period.fromValue(it.asText()) },               { "\"${it.value}\"" })
    convert(DisabledResourceType::class, { DisabledResourceType.fromValue(it.asText()) }, { "\"${it.value}\"" })
    convert(Maturity::class,             { Maturity.fromValue(it.asText()) },             { "\"${it.value}\"" })
    convert(ExposureResourceType::class, { ExposureResourceType.fromValue(it.asText()) }, { "\"${it.value}\"" })
    convert(Type::class,                 { Type.fromValue(it.asText()) },                 { "\"${it.value}\"" })
    convert(MacroResourceType::class,    { MacroResourceType.fromValue(it.asText()) },    { "\"${it.value}\"" })
    convert(NodeResourceType::class,     { NodeResourceType.fromValue(it.asText()) },     { "\"${it.value}\"" })
    convert(SourceResourceType::class,   { SourceResourceType.fromValue(it.asText()) },   { "\"${it.value}\"" })
    convert(UniqueKey::class,            { UniqueKey.fromJson(it) },                      { it.toJson() }, true)
    convert(Tags::class,                 { Tags.fromJson(it) },                           { it.toJson() }, true)
}

/**
 * WritableManifest(metadata: dbt.contracts.graph.manifest.ManifestMetadata, nodes:
 * Mapping[str, Union[dbt.contracts.graph.compiled.CompiledAnalysisNode,
 * dbt.contracts.graph.compiled.CompiledSingularTestNode,
 * dbt.contracts.graph.compiled.CompiledModelNode,
 * dbt.contracts.graph.compiled.CompiledHookNode,
 * dbt.contracts.graph.compiled.CompiledRPCNode,
 * dbt.contracts.graph.compiled.CompiledSqlNode,
 * dbt.contracts.graph.compiled.CompiledGenericTestNode,
 * dbt.contracts.graph.compiled.CompiledSeedNode,
 * dbt.contracts.graph.compiled.CompiledSnapshotNode,
 * dbt.contracts.graph.parsed.ParsedAnalysisNode,
 * dbt.contracts.graph.parsed.ParsedSingularTestNode,
 * dbt.contracts.graph.parsed.ParsedHookNode, dbt.contracts.graph.parsed.ParsedModelNode,
 * dbt.contracts.graph.parsed.ParsedRPCNode, dbt.contracts.graph.parsed.ParsedSqlNode,
 * dbt.contracts.graph.parsed.ParsedGenericTestNode,
 * dbt.contracts.graph.parsed.ParsedSeedNode,
 * dbt.contracts.graph.parsed.ParsedSnapshotNode]], sources: Mapping[str,
 * dbt.contracts.graph.parsed.ParsedSourceDefinition], macros: Mapping[str,
 * dbt.contracts.graph.parsed.ParsedMacro], docs: Mapping[str,
 * dbt.contracts.graph.parsed.ParsedDocumentation], exposures: Mapping[str,
 * dbt.contracts.graph.parsed.ParsedExposure], metrics: Mapping[str,
 * dbt.contracts.graph.parsed.ParsedMetric], selectors: Mapping[str, Any], disabled:
 * Union[Mapping[str, List[Union[dbt.contracts.graph.compiled.CompiledAnalysisNode,
 * dbt.contracts.graph.compiled.CompiledSingularTestNode,
 * dbt.contracts.graph.compiled.CompiledModelNode,
 * dbt.contracts.graph.compiled.CompiledHookNode,
 * dbt.contracts.graph.compiled.CompiledRPCNode,
 * dbt.contracts.graph.compiled.CompiledSqlNode,
 * dbt.contracts.graph.compiled.CompiledGenericTestNode,
 * dbt.contracts.graph.compiled.CompiledSeedNode,
 * dbt.contracts.graph.compiled.CompiledSnapshotNode,
 * dbt.contracts.graph.parsed.ParsedAnalysisNode,
 * dbt.contracts.graph.parsed.ParsedSingularTestNode,
 * dbt.contracts.graph.parsed.ParsedHookNode, dbt.contracts.graph.parsed.ParsedModelNode,
 * dbt.contracts.graph.parsed.ParsedRPCNode, dbt.contracts.graph.parsed.ParsedSqlNode,
 * dbt.contracts.graph.parsed.ParsedGenericTestNode,
 * dbt.contracts.graph.parsed.ParsedSeedNode, dbt.contracts.graph.parsed.ParsedSnapshotNode,
 * dbt.contracts.graph.parsed.ParsedSourceDefinition]]], NoneType], parent_map:
 * Union[Dict[str, List[str]], NoneType], child_map: Union[Dict[str, List[str]], NoneType])
 */
data class ManifestV5 (
    /**
     * A mapping from parent nodes to their dependents
     */
    @get:JsonProperty("child_map")@field:JsonProperty("child_map")
    val childMap: Map<String, List<String>>? = null,

    /**
     * A mapping of the disabled nodes in the target
     */
    val disabled: Map<String, List<CompiledAnalysisNode>>? = null,

    /**
     * The docs defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val docs: Map<String, ParsedDocumentation>,

    /**
     * The exposures defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val exposures: Map<String, ParsedExposure>,

    /**
     * The macros defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val macros: Map<String, ParsedMacro>,

    /**
     * Metadata about the manifest
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val metadata: ManifestMetadata,

    /**
     * The metrics defined in the dbt project and its dependencies
     */
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val metrics: Map<String, ParsedMetric>,

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
    val sources: Map<String, ParsedSourceDefinition>
): ManifestInterface {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapper.readValue<ManifestV5>(json)
    }
}

/**
 * CompiledAnalysisNode(raw_sql: str, compiled: bool, database: Union[str, NoneType],
 * schema: str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias:
 * str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledSingularTestNode(raw_sql: str, compiled: bool, database: Union[str, NoneType],
 * schema: str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias:
 * str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledModelNode(raw_sql: str, compiled: bool, database: Union[str, NoneType], schema:
 * str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledHookNode(raw_sql: str, compiled: bool, database: Union[str, NoneType], schema:
 * str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None, index: Union[int,
 * NoneType] = None)
 *
 * CompiledRPCNode(raw_sql: str, compiled: bool, database: Union[str, NoneType], schema:
 * str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledSqlNode(raw_sql: str, compiled: bool, database: Union[str, NoneType], schema:
 * str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledGenericTestNode(raw_sql: str, test_metadata:
 * dbt.contracts.graph.parsed.TestMetadata, compiled: bool, database: Union[str, NoneType],
 * schema: str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias:
 * str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None, column_name: Union[str,
 * NoneType] = None, file_key_name: Union[str, NoneType] = None)
 *
 * CompiledSeedNode(raw_sql: str, compiled: bool, database: Union[str, NoneType], schema:
 * str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.SeedConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledSnapshotNode(raw_sql: str, compiled: bool, database: Union[str, NoneType],
 * schema: str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias:
 * str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * ParsedAnalysisNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn:
 * List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedSingularTestNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn:
 * List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedHookNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn: List[str],
 * unique_id: str, package_name: str, root_path: str, path: str, original_file_path: str,
 * name: str, resource_type: dbt.node_types.NodeType, alias: str, checksum:
 * dbt.contracts.files.FileHash, config: dbt.contracts.graph.model_config.NodeConfig =
 * <factory>, _event_status: Dict[str, Any] = <factory>, tags: List[str] = <factory>, refs:
 * List[List[str]] = <factory>, sources: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.parsed.DependsOn = <factory>, description: str = '', columns:
 * Dict[str, dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] =
 * <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str,
 * NoneType] = None, compiled_path: Union[str, NoneType] = None, build_path: Union[str,
 * NoneType] = None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>,
 * created_at: float = <factory>, config_call_dict: Dict[str, Any] = <factory>, index:
 * Union[int, NoneType] = None)
 *
 * ParsedModelNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn:
 * List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedRPCNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn: List[str],
 * unique_id: str, package_name: str, root_path: str, path: str, original_file_path: str,
 * name: str, resource_type: dbt.node_types.NodeType, alias: str, checksum:
 * dbt.contracts.files.FileHash, config: dbt.contracts.graph.model_config.NodeConfig =
 * <factory>, _event_status: Dict[str, Any] = <factory>, tags: List[str] = <factory>, refs:
 * List[List[str]] = <factory>, sources: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.parsed.DependsOn = <factory>, description: str = '', columns:
 * Dict[str, dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] =
 * <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str,
 * NoneType] = None, compiled_path: Union[str, NoneType] = None, build_path: Union[str,
 * NoneType] = None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>,
 * created_at: float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedSqlNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn: List[str],
 * unique_id: str, package_name: str, root_path: str, path: str, original_file_path: str,
 * name: str, resource_type: dbt.node_types.NodeType, alias: str, checksum:
 * dbt.contracts.files.FileHash, config: dbt.contracts.graph.model_config.NodeConfig =
 * <factory>, _event_status: Dict[str, Any] = <factory>, tags: List[str] = <factory>, refs:
 * List[List[str]] = <factory>, sources: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.parsed.DependsOn = <factory>, description: str = '', columns:
 * Dict[str, dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] =
 * <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str,
 * NoneType] = None, compiled_path: Union[str, NoneType] = None, build_path: Union[str,
 * NoneType] = None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>,
 * created_at: float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedGenericTestNode(raw_sql: str, test_metadata:
 * dbt.contracts.graph.parsed.TestMetadata, database: Union[str, NoneType], schema: str,
 * fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, column_name: Union[str,
 * NoneType] = None, file_key_name: Union[str, NoneType] = None)
 *
 * ParsedSeedNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn: List[str],
 * unique_id: str, package_name: str, root_path: str, path: str, original_file_path: str,
 * name: str, resource_type: dbt.node_types.NodeType, alias: str, checksum:
 * dbt.contracts.files.FileHash, config: dbt.contracts.graph.model_config.SeedConfig =
 * <factory>, _event_status: Dict[str, Any] = <factory>, tags: List[str] = <factory>, refs:
 * List[List[str]] = <factory>, sources: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.parsed.DependsOn = <factory>, description: str = '', columns:
 * Dict[str, dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] =
 * <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str,
 * NoneType] = None, compiled_path: Union[str, NoneType] = None, build_path: Union[str,
 * NoneType] = None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>,
 * created_at: float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedSnapshotNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn:
 * List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.SnapshotConfig, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedSourceDefinition(fqn: List[str], database: Union[str, NoneType], schema: str,
 * unique_id: str, package_name: str, root_path: str, path: str, original_file_path: str,
 * name: str, source_name: str, source_description: str, loader: str, identifier: str,
 * resource_type: dbt.node_types.NodeType, _event_status: Dict[str, Any] = <factory>,
 * quoting: dbt.contracts.graph.unparsed.Quoting = <factory>, loaded_at_field: Union[str,
 * NoneType] = None, freshness: Union[dbt.contracts.graph.unparsed.FreshnessThreshold,
 * NoneType] = None, external: Union[dbt.contracts.graph.unparsed.ExternalTable, NoneType] =
 * None, description: str = '', columns: Dict[str, dbt.contracts.graph.parsed.ColumnInfo] =
 * <factory>, meta: Dict[str, Any] = <factory>, source_meta: Dict[str, Any] = <factory>,
 * tags: List[str] = <factory>, config: dbt.contracts.graph.model_config.SourceConfig =
 * <factory>, patch_path: Union[pathlib.Path, NoneType] = None, unrendered_config: Dict[str,
 * Any] = <factory>, relation_name: Union[str, NoneType] = None, created_at: float =
 * <factory>)
 */
data class CompiledAnalysisNode (
    val alias: String? = null,

    @get:JsonProperty("build_path")@field:JsonProperty("build_path")
    val buildPath: String? = null,

    val checksum: FileHash? = null,
    val columns: Map<String, ColumnInfo>? = null,
    val compiled: Boolean? = null,

    @get:JsonProperty("compiled_path")@field:JsonProperty("compiled_path")
    val compiledPath: String? = null,

    @get:JsonProperty("compiled_sql")@field:JsonProperty("compiled_sql")
    val compiledSql: String? = null,

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

    @get:JsonProperty("raw_sql")@field:JsonProperty("raw_sql")
    val rawSql: String? = null,

    val refs: List<List<String>>? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: DisabledResourceType,

    @get:JsonProperty("root_path", required=true)@field:JsonProperty("root_path", required=true)
    val rootPath: String,

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
 * ColumnInfo(name: str, description: str = '', meta: Dict[str, Any] = <factory>, data_type:
 * Union[str, NoneType] = None, quote: Union[bool, NoneType] = None, tags: List[str] =
 * <factory>, _extra: Dict[str, Any] = <factory>)
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
 * NodeConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str, NoneType] =
 * None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>,
 * materialized: str = 'view', persist_docs: Dict[str, Any] = <factory>, post_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Union[str,
 * NoneType] = 'ignore')
 *
 * TestConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = 'dbt_test__audit', database: Union[str,
 * NoneType] = None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] =
 * <factory>, materialized: str = 'test', severity:
 * dbt.contracts.graph.model_config.Severity = 'ERROR', store_failures: Union[bool,
 * NoneType] = None, where: Union[str, NoneType] = None, limit: Union[int, NoneType] = None,
 * fail_calc: str = 'count(*)', warn_if: str = '!= 0', error_if: str = '!= 0')
 *
 * SeedConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str, NoneType] =
 * None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>,
 * materialized: str = 'seed', persist_docs: Dict[str, Any] = <factory>, post_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Union[str,
 * NoneType] = 'ignore', quote_columns: Union[bool, NoneType] = None)
 *
 * SnapshotConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias:
 * Union[str, NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str,
 * NoneType] = None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] =
 * <factory>, materialized: str = 'snapshot', persist_docs: Dict[str, Any] = <factory>,
 * post_hook: List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, NoneType] = None, on_schema_change: Union[str, NoneType] =
 * 'ignore', strategy: Union[str, NoneType] = None, target_schema: Union[str, NoneType] =
 * None, target_database: Union[str, NoneType] = None, updated_at: Union[str, NoneType] =
 * None, check_cols: Union[str, List[str], NoneType] = None)
 *
 * SourceConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
data class DisabledConfig (
    val alias: String? = null,

    @get:JsonProperty("column_types")@field:JsonProperty("column_types")
    val columnTypes: Map<String, Any?>? = null,

    val database: String? = null,
    val enabled: Boolean? = null,

    @get:JsonProperty("full_refresh")@field:JsonProperty("full_refresh")
    val fullRefresh: Boolean? = null,

    val materialized: String? = null,
    val meta: Map<String, Any?>? = null,

    @get:JsonProperty("on_schema_change")@field:JsonProperty("on_schema_change")
    val onSchemaChange: String? = null,

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

    @get:JsonProperty("quote_columns")@field:JsonProperty("quote_columns")
    val quoteColumns: Boolean? = null,

    @get:JsonProperty("check_cols")@field:JsonProperty("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @get:JsonProperty("target_database")@field:JsonProperty("target_database")
    val targetDatabase: String? = null,

    @get:JsonProperty("target_schema")@field:JsonProperty("target_schema")
    val targetSchema: String? = null,

    @get:JsonProperty("updated_at")@field:JsonProperty("updated_at")
    val updatedAt: String? = null
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
 * DependsOn(macros: List[str] = <factory>, nodes: List[str] = <factory>)
 */
data class DependsOn (
    val macros: List<String>? = null,
    val nodes: List<String>? = null
)

/**
 * Docs(show: bool = True)
 */
data class Docs (
    val show: Boolean? = null
)

/**
 * ExternalTable(_extra: Dict[str, Any] = <factory>, location: Union[str, NoneType] = None,
 * file_format: Union[str, NoneType] = None, row_format: Union[str, NoneType] = None,
 * tbl_properties: Union[str, NoneType] = None, partitions:
 * Union[List[dbt.contracts.graph.unparsed.ExternalPartition], NoneType] = None)
 */
data class ExternalTable (
    @get:JsonProperty("file_format")@field:JsonProperty("file_format")
    val fileFormat: String? = null,

    val location: String? = null,
    val partitions: List<ExternalPartition>? = null,

    @get:JsonProperty("row_format")@field:JsonProperty("row_format")
    val rowFormat: String? = null,

    @get:JsonProperty("tbl_properties")@field:JsonProperty("tbl_properties")
    val tblProperties: String? = null
)

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
 * InjectedCTE(id: str, sql: str)
 */
data class InjectedCte (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val id: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val sql: String
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

enum class DisabledResourceType(val value: String) {
    Analysis("analysis"),
    Model("model"),
    Operation("operation"),
    RPC("rpc"),
    SQL("sql"),
    Seed("seed"),
    Snapshot("snapshot"),
    Source("source"),
    Test("test");

    companion object {
        fun fromValue(value: String): DisabledResourceType = when (value) {
            "analysis"  -> Analysis
            "model"     -> Model
            "operation" -> Operation
            "rpc"       -> RPC
            "sql"       -> SQL
            "seed"      -> Seed
            "snapshot"  -> Snapshot
            "source"    -> Source
            "test"      -> Test
            else        -> throw IllegalArgumentException()
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

/**
 * ParsedDocumentation(unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, block_contents: str)
 */
data class ParsedDocumentation (
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

    @get:JsonProperty("root_path", required=true)@field:JsonProperty("root_path", required=true)
    val rootPath: String,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String
)

/**
 * ParsedExposure(fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, name: str, type: dbt.contracts.graph.unparsed.ExposureType,
 * owner: dbt.contracts.graph.unparsed.ExposureOwner, resource_type: dbt.node_types.NodeType
 * = <NodeType.Exposure: 'exposure'>, description: str = '', maturity:
 * Union[dbt.contracts.graph.unparsed.MaturityType, NoneType] = None, meta: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, url: Union[str, NoneType] = None, depends_on:
 * dbt.contracts.graph.parsed.DependsOn = <factory>, refs: List[List[str]] = <factory>,
 * sources: List[List[str]] = <factory>, created_at: float = <factory>)
 */
data class ParsedExposure (
    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: DependsOn? = null,

    val description: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val fqn: List<String>,

    val maturity: Maturity? = null,
    val meta: Map<String, Any?>? = null,

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

    @get:JsonProperty("resource_type")@field:JsonProperty("resource_type")
    val resourceType: ExposureResourceType? = null,

    @get:JsonProperty("root_path", required=true)@field:JsonProperty("root_path", required=true)
    val rootPath: String,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val type: Type,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String,

    val url: String? = null
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
 * ExposureOwner(email: str, name: Union[str, NoneType] = None)
 */
data class ExposureOwner (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val email: String,

    val name: String? = null
)

enum class ExposureResourceType(val value: String) {
    Analysis("analysis"),
    Docs("docs"),
    Exposure("exposure"),
    Macro("macro"),
    Metric("metric"),
    Model("model"),
    Operation("operation"),
    RPC("rpc"),
    SQL("sql"),
    Seed("seed"),
    Snapshot("snapshot"),
    Source("source"),
    Test("test");

    companion object {
        fun fromValue(value: String): ExposureResourceType = when (value) {
            "analysis"  -> Analysis
            "docs"      -> Docs
            "exposure"  -> Exposure
            "macro"     -> Macro
            "metric"    -> Metric
            "model"     -> Model
            "operation" -> Operation
            "rpc"       -> RPC
            "sql"       -> SQL
            "seed"      -> Seed
            "snapshot"  -> Snapshot
            "source"    -> Source
            "test"      -> Test
            else        -> throw IllegalArgumentException()
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
 * ParsedMacro(unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, macro_sql: str, resource_type:
 * dbt.node_types.NodeType, tags: List[str] = <factory>, depends_on:
 * dbt.contracts.graph.parsed.MacroDependsOn = <factory>, description: str = '', meta:
 * Dict[str, Any] = <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, arguments:
 * List[dbt.contracts.graph.unparsed.MacroArgument] = <factory>, created_at: float =
 * <factory>)
 */
data class ParsedMacro (
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

    @get:JsonProperty("root_path", required=true)@field:JsonProperty("root_path", required=true)
    val rootPath: String,

    val tags: List<String>? = null,

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
 * MacroDependsOn(macros: List[str] = <factory>)
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
 * ParsedMetric(fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, model: str, name: str, description: str, label: str, type:
 * str, sql: Union[str, NoneType], timestamp: Union[str, NoneType], filters:
 * List[dbt.contracts.graph.unparsed.MetricFilter], time_grains: List[str], dimensions:
 * List[str], resource_type: dbt.node_types.NodeType = <NodeType.Metric: 'metric'>, meta:
 * Dict[str, Any] = <factory>, tags: List[str] = <factory>, sources: List[List[str]] =
 * <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn = <factory>, refs:
 * List[List[str]] = <factory>, created_at: float = <factory>)
 */
data class ParsedMetric (
    @get:JsonProperty("created_at")@field:JsonProperty("created_at")
    val createdAt: Double? = null,

    @get:JsonProperty("depends_on")@field:JsonProperty("depends_on")
    val dependsOn: DependsOn? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val description: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val dimensions: List<String>,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val filters: List<MetricFilter>,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val fqn: List<String>,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val label: String,

    val meta: Map<String, Any?>? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val model: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val name: String,

    @get:JsonProperty("original_file_path", required=true)@field:JsonProperty("original_file_path", required=true)
    val originalFilePath: String,

    @get:JsonProperty("package_name", required=true)@field:JsonProperty("package_name", required=true)
    val packageName: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val path: String,

    val refs: List<List<String>>? = null,

    @get:JsonProperty("resource_type")@field:JsonProperty("resource_type")
    val resourceType: ExposureResourceType? = null,

    @get:JsonProperty("root_path", required=true)@field:JsonProperty("root_path", required=true)
    val rootPath: String,

    val sources: List<List<String>>? = null,
    val sql: String? = null,
    val tags: List<String>? = null,

    @get:JsonProperty("time_grains", required=true)@field:JsonProperty("time_grains", required=true)
    val timeGrains: List<String>,

    val timestamp: String? = null,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val type: String,

    @get:JsonProperty("unique_id", required=true)@field:JsonProperty("unique_id", required=true)
    val uniqueId: String
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

/**
 * CompiledAnalysisNode(raw_sql: str, compiled: bool, database: Union[str, NoneType],
 * schema: str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias:
 * str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledSingularTestNode(raw_sql: str, compiled: bool, database: Union[str, NoneType],
 * schema: str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias:
 * str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledModelNode(raw_sql: str, compiled: bool, database: Union[str, NoneType], schema:
 * str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledHookNode(raw_sql: str, compiled: bool, database: Union[str, NoneType], schema:
 * str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None, index: Union[int,
 * NoneType] = None)
 *
 * CompiledRPCNode(raw_sql: str, compiled: bool, database: Union[str, NoneType], schema:
 * str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledSqlNode(raw_sql: str, compiled: bool, database: Union[str, NoneType], schema:
 * str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledGenericTestNode(raw_sql: str, test_metadata:
 * dbt.contracts.graph.parsed.TestMetadata, compiled: bool, database: Union[str, NoneType],
 * schema: str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias:
 * str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None, column_name: Union[str,
 * NoneType] = None, file_key_name: Union[str, NoneType] = None)
 *
 * CompiledSeedNode(raw_sql: str, compiled: bool, database: Union[str, NoneType], schema:
 * str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.SeedConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * CompiledSnapshotNode(raw_sql: str, compiled: bool, database: Union[str, NoneType],
 * schema: str, fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias:
 * str, checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, compiled_sql: Union[str,
 * NoneType] = None, extra_ctes_injected: bool = False, extra_ctes:
 * List[dbt.contracts.graph.compiled.InjectedCTE] = <factory>, relation_name: Union[str,
 * NoneType] = None, _pre_injected_sql: Union[str, NoneType] = None)
 *
 * ParsedAnalysisNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn:
 * List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedSingularTestNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn:
 * List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedHookNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn: List[str],
 * unique_id: str, package_name: str, root_path: str, path: str, original_file_path: str,
 * name: str, resource_type: dbt.node_types.NodeType, alias: str, checksum:
 * dbt.contracts.files.FileHash, config: dbt.contracts.graph.model_config.NodeConfig =
 * <factory>, _event_status: Dict[str, Any] = <factory>, tags: List[str] = <factory>, refs:
 * List[List[str]] = <factory>, sources: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.parsed.DependsOn = <factory>, description: str = '', columns:
 * Dict[str, dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] =
 * <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str,
 * NoneType] = None, compiled_path: Union[str, NoneType] = None, build_path: Union[str,
 * NoneType] = None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>,
 * created_at: float = <factory>, config_call_dict: Dict[str, Any] = <factory>, index:
 * Union[int, NoneType] = None)
 *
 * ParsedModelNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn:
 * List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.NodeConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedRPCNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn: List[str],
 * unique_id: str, package_name: str, root_path: str, path: str, original_file_path: str,
 * name: str, resource_type: dbt.node_types.NodeType, alias: str, checksum:
 * dbt.contracts.files.FileHash, config: dbt.contracts.graph.model_config.NodeConfig =
 * <factory>, _event_status: Dict[str, Any] = <factory>, tags: List[str] = <factory>, refs:
 * List[List[str]] = <factory>, sources: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.parsed.DependsOn = <factory>, description: str = '', columns:
 * Dict[str, dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] =
 * <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str,
 * NoneType] = None, compiled_path: Union[str, NoneType] = None, build_path: Union[str,
 * NoneType] = None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>,
 * created_at: float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedSqlNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn: List[str],
 * unique_id: str, package_name: str, root_path: str, path: str, original_file_path: str,
 * name: str, resource_type: dbt.node_types.NodeType, alias: str, checksum:
 * dbt.contracts.files.FileHash, config: dbt.contracts.graph.model_config.NodeConfig =
 * <factory>, _event_status: Dict[str, Any] = <factory>, tags: List[str] = <factory>, refs:
 * List[List[str]] = <factory>, sources: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.parsed.DependsOn = <factory>, description: str = '', columns:
 * Dict[str, dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] =
 * <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str,
 * NoneType] = None, compiled_path: Union[str, NoneType] = None, build_path: Union[str,
 * NoneType] = None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>,
 * created_at: float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedGenericTestNode(raw_sql: str, test_metadata:
 * dbt.contracts.graph.parsed.TestMetadata, database: Union[str, NoneType], schema: str,
 * fqn: List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.TestConfig = <factory>, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>, column_name: Union[str,
 * NoneType] = None, file_key_name: Union[str, NoneType] = None)
 *
 * ParsedSeedNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn: List[str],
 * unique_id: str, package_name: str, root_path: str, path: str, original_file_path: str,
 * name: str, resource_type: dbt.node_types.NodeType, alias: str, checksum:
 * dbt.contracts.files.FileHash, config: dbt.contracts.graph.model_config.SeedConfig =
 * <factory>, _event_status: Dict[str, Any] = <factory>, tags: List[str] = <factory>, refs:
 * List[List[str]] = <factory>, sources: List[List[str]] = <factory>, depends_on:
 * dbt.contracts.graph.parsed.DependsOn = <factory>, description: str = '', columns:
 * Dict[str, dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] =
 * <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str,
 * NoneType] = None, compiled_path: Union[str, NoneType] = None, build_path: Union[str,
 * NoneType] = None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>,
 * created_at: float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
 *
 * ParsedSnapshotNode(raw_sql: str, database: Union[str, NoneType], schema: str, fqn:
 * List[str], unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, resource_type: dbt.node_types.NodeType, alias: str,
 * checksum: dbt.contracts.files.FileHash, config:
 * dbt.contracts.graph.model_config.SnapshotConfig, _event_status: Dict[str, Any] =
 * <factory>, tags: List[str] = <factory>, refs: List[List[str]] = <factory>, sources:
 * List[List[str]] = <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn =
 * <factory>, description: str = '', columns: Dict[str,
 * dbt.contracts.graph.parsed.ColumnInfo] = <factory>, meta: Dict[str, Any] = <factory>,
 * docs: dbt.contracts.graph.unparsed.Docs = <factory>, patch_path: Union[str, NoneType] =
 * None, compiled_path: Union[str, NoneType] = None, build_path: Union[str, NoneType] =
 * None, deferred: bool = False, unrendered_config: Dict[str, Any] = <factory>, created_at:
 * float = <factory>, config_call_dict: Dict[str, Any] = <factory>)
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

    @get:JsonProperty("compiled_path")@field:JsonProperty("compiled_path")
    val compiledPath: String? = null,

    @get:JsonProperty("compiled_sql")@field:JsonProperty("compiled_sql")
    val compiledSql: String? = null,

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

    @get:JsonProperty("raw_sql", required=true)@field:JsonProperty("raw_sql", required=true)
    val rawSql: String,

    val refs: List<List<String>>? = null,

    @get:JsonProperty("relation_name")@field:JsonProperty("relation_name")
    val relationName: String? = null,

    @get:JsonProperty("resource_type", required=true)@field:JsonProperty("resource_type", required=true)
    val resourceType: NodeResourceType,

    @get:JsonProperty("root_path", required=true)@field:JsonProperty("root_path", required=true)
    val rootPath: String,

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
    val testMetadata: TestMetadata? = null
)

/**
 * NodeConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str, NoneType] =
 * None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>,
 * materialized: str = 'view', persist_docs: Dict[str, Any] = <factory>, post_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Union[str,
 * NoneType] = 'ignore')
 *
 * TestConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = 'dbt_test__audit', database: Union[str,
 * NoneType] = None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] =
 * <factory>, materialized: str = 'test', severity:
 * dbt.contracts.graph.model_config.Severity = 'ERROR', store_failures: Union[bool,
 * NoneType] = None, where: Union[str, NoneType] = None, limit: Union[int, NoneType] = None,
 * fail_calc: str = 'count(*)', warn_if: str = '!= 0', error_if: str = '!= 0')
 *
 * SeedConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias: Union[str,
 * NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str, NoneType] =
 * None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] = <factory>,
 * materialized: str = 'seed', persist_docs: Dict[str, Any] = <factory>, post_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, List[str], NoneType] = None, on_schema_change: Union[str,
 * NoneType] = 'ignore', quote_columns: Union[bool, NoneType] = None)
 *
 * SnapshotConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, alias:
 * Union[str, NoneType] = None, schema: Union[str, NoneType] = None, database: Union[str,
 * NoneType] = None, tags: Union[List[str], str] = <factory>, meta: Dict[str, Any] =
 * <factory>, materialized: str = 'snapshot', persist_docs: Dict[str, Any] = <factory>,
 * post_hook: List[dbt.contracts.graph.model_config.Hook] = <factory>, pre_hook:
 * List[dbt.contracts.graph.model_config.Hook] = <factory>, quoting: Dict[str, Any] =
 * <factory>, column_types: Dict[str, Any] = <factory>, full_refresh: Union[bool, NoneType]
 * = None, unique_key: Union[str, NoneType] = None, on_schema_change: Union[str, NoneType] =
 * 'ignore', strategy: Union[str, NoneType] = None, target_schema: Union[str, NoneType] =
 * None, target_database: Union[str, NoneType] = None, updated_at: Union[str, NoneType] =
 * None, check_cols: Union[str, List[str], NoneType] = None)
 */
data class NodeConfig (
    val alias: String? = null,

    @get:JsonProperty("column_types")@field:JsonProperty("column_types")
    val columnTypes: Map<String, Any?>? = null,

    val database: String? = null,
    val enabled: Boolean? = null,

    @get:JsonProperty("full_refresh")@field:JsonProperty("full_refresh")
    val fullRefresh: Boolean? = null,

    val materialized: String? = null,
    val meta: Map<String, Any?>? = null,

    @get:JsonProperty("on_schema_change")@field:JsonProperty("on_schema_change")
    val onSchemaChange: String? = null,

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

    @get:JsonProperty("quote_columns")@field:JsonProperty("quote_columns")
    val quoteColumns: Boolean? = null,

    @get:JsonProperty("check_cols")@field:JsonProperty("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @get:JsonProperty("target_database")@field:JsonProperty("target_database")
    val targetDatabase: String? = null,

    @get:JsonProperty("target_schema")@field:JsonProperty("target_schema")
    val targetSchema: String? = null,

    @get:JsonProperty("updated_at")@field:JsonProperty("updated_at")
    val updatedAt: String? = null
)

enum class NodeResourceType(val value: String) {
    Analysis("analysis"),
    Model("model"),
    Operation("operation"),
    RPC("rpc"),
    SQL("sql"),
    Seed("seed"),
    Snapshot("snapshot"),
    Test("test");

    companion object {
        fun fromValue(value: String): NodeResourceType = when (value) {
            "analysis"  -> Analysis
            "model"     -> Model
            "operation" -> Operation
            "rpc"       -> RPC
            "sql"       -> SQL
            "seed"      -> Seed
            "snapshot"  -> Snapshot
            "test"      -> Test
            else        -> throw IllegalArgumentException()
        }
    }
}

/**
 * ParsedSourceDefinition(fqn: List[str], database: Union[str, NoneType], schema: str,
 * unique_id: str, package_name: str, root_path: str, path: str, original_file_path: str,
 * name: str, source_name: str, source_description: str, loader: str, identifier: str,
 * resource_type: dbt.node_types.NodeType, _event_status: Dict[str, Any] = <factory>,
 * quoting: dbt.contracts.graph.unparsed.Quoting = <factory>, loaded_at_field: Union[str,
 * NoneType] = None, freshness: Union[dbt.contracts.graph.unparsed.FreshnessThreshold,
 * NoneType] = None, external: Union[dbt.contracts.graph.unparsed.ExternalTable, NoneType] =
 * None, description: str = '', columns: Dict[str, dbt.contracts.graph.parsed.ColumnInfo] =
 * <factory>, meta: Dict[str, Any] = <factory>, source_meta: Dict[str, Any] = <factory>,
 * tags: List[str] = <factory>, config: dbt.contracts.graph.model_config.SourceConfig =
 * <factory>, patch_path: Union[pathlib.Path, NoneType] = None, unrendered_config: Dict[str,
 * Any] = <factory>, relation_name: Union[str, NoneType] = None, created_at: float =
 * <factory>)
 */
data class ParsedSourceDefinition (
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

    @get:JsonProperty("root_path", required=true)@field:JsonProperty("root_path", required=true)
    val rootPath: String,

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

