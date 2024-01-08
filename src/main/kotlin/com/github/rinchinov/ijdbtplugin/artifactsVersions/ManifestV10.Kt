// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json        = Json { allowStructuredMapKeys = true }
// val manifestV10 = json.parse(ManifestV10.serializer(), jsonString)

package com.github.rinchinov.ijdbtplugin.artifactsVersions

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

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
@Serializable
data class ManifestV10 (
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
@Serializable
data class AnalysisNode (
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
    val filter: WhereFilter? = null,
    val metadata: SourceFileMetadata? = null,

    @SerialName("type_params")
    val typeParams: MetricTypeParams? = null,

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

/**
 * FileHash(name: str, checksum: str)
 */
@Serializable
data class FileHash (
    val checksum: String,
    val name: String
)

/**
 * Used in all ManifestNodes and SourceDefinition
 */
@Serializable
data class ColumnInfo (
    val constraints: List<ColumnLevelConstraint>? = null,

    @SerialName("data_type")
    val dataType: String? = null,

    val description: String? = null,
    val meta: JsonObject? = null,
    val name: String,
    val quote: Boolean? = null,
    val tags: List<String>? = null
)

/**
 * ColumnLevelConstraint(type: dbt.contracts.graph.nodes.ConstraintType, name: Union[str,
 * NoneType] = None, expression: Union[str, NoneType] = None, warn_unenforced: bool = True,
 * warn_unsupported: bool = True)
 */
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
@Serializable
data class DisabledConfig (
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

    @SerialName("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @SerialName("target_database")
    val targetDatabase: String? = null,

    @SerialName("target_schema")
    val targetSchema: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    @SerialName("quote_columns")
    val quoteColumns: Boolean? = null
)

@Serializable
sealed class UniqueKey {
    class StringArrayValue(val value: List<String>) : UniqueKey()
    class StringValue(val value: String)            : UniqueKey()
    class NullValue()                               : UniqueKey()
}

/**
 * ContractConfig(enforced: bool = False)
 */
@Serializable
data class ContractConfig (
    val enforced: Boolean? = null
)

/**
 * Docs(show: bool = True, node_color: Union[str, NoneType] = None)
 */
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

/**
 * Hook(sql: str, transaction: bool = True, index: Union[int, NoneType] = None)
 */
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

/**
 * ModelLevelConstraint(type: dbt.contracts.graph.nodes.ConstraintType, name: Union[str,
 * NoneType] = None, expression: Union[str, NoneType] = None, warn_unenforced: bool = True,
 * warn_unsupported: bool = True, columns: List[str] = <factory>)
 */
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

/**
 * Contract(enforced: bool = False, checksum: Union[str, NoneType] = None)
 */
@Serializable
data class Contract (
    val checksum: String? = null,
    val enforced: Boolean? = null
)

/**
 * Defaults(agg_time_dimension: Union[str, NoneType] = None)
 */
@Serializable
data class Defaults (
    @SerialName("agg_time_dimension")
    val aggTimeDimension: String? = null
)

/**
 * DeferRelation(database: Union[str, NoneType], schema: str, alias: str, relation_name:
 * Union[str, NoneType])
 */
@Serializable
data class DeferRelation (
    val alias: String,
    val database: String? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    val schema: String
)

/**
 * DependsOn(macros: List[str] = <factory>, nodes: List[str] = <factory>)
 *
 * Used only in the Macro class
 */
@Serializable
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

/**
 * Provides file context about what something was created from.
 *
 * Implementation of the dbt-semantic-interfaces `Metadata` protocol
 */
@Serializable
data class SourceFileMetadata (
    @SerialName("file_slice")
    val fileSlice: FileSlice,

    @SerialName("repo_file_path")
    val repoFilePath: String
)

/**
 * Provides file slice level context about what something was created from.
 *
 * Implementation of the dbt-semantic-interfaces `FileSlice` protocol
 */
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

/**
 * DimensionTypeParams(time_granularity:
 * dbt_semantic_interfaces.type_enums.time_granularity.TimeGranularity, validity_params:
 * Union[dbt.contracts.graph.semantic_models.DimensionValidityParams, NoneType] = None)
 */
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

/**
 * DimensionValidityParams(is_start: bool = False, is_end: bool = False)
 */
@Serializable
data class DimensionValidityParams (
    @SerialName("is_end")
    val isEnd: Boolean? = null,

    @SerialName("is_start")
    val isStart: Boolean? = null
)

/**
 * Entity(name: str, type: dbt_semantic_interfaces.type_enums.entity_type.EntityType,
 * description: Union[str, NoneType] = None, label: Union[str, NoneType] = None, role:
 * Union[str, NoneType] = None, expr: Union[str, NoneType] = None)
 */
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

/**
 * ExternalTable(_extra: Dict[str, Any] = <factory>, location: Union[str, NoneType] = None,
 * file_format: Union[str, NoneType] = None, row_format: Union[str, NoneType] = None,
 * tbl_properties: Union[str, NoneType] = None, partitions: Union[List[str],
 * List[dbt.contracts.graph.unparsed.ExternalPartition], NoneType] = None)
 */
@Serializable
data class ExternalTable (
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

/**
 * ExternalPartition(_extra: Dict[str, Any] = <factory>, name: str = '', description: str =
 * '', data_type: str = '', meta: Dict[str, Any] = <factory>)
 */
@Serializable
data class ExternalPartition (
    @SerialName("data_type")
    val dataType: String? = null,

    val description: String? = null,
    val meta: JsonObject? = null,
    val name: String? = null
)

/**
 * Used in CompiledNodes as part of ephemeral model processing
 */
@Serializable
data class InjectedCte (
    val id: String,
    val sql: String
)

/**
 * WhereFilter(where_sql_template: str)
 */
@Serializable
data class WhereFilter (
    @SerialName("where_sql_template")
    val whereSqlTemplate: String
)

/**
 * FreshnessThreshold(warn_after: Union[dbt.contracts.graph.unparsed.Time, NoneType] =
 * <factory>, error_after: Union[dbt.contracts.graph.unparsed.Time, NoneType] = <factory>,
 * filter: Union[str, NoneType] = None)
 */
@Serializable
data class FreshnessThreshold (
    @SerialName("error_after")
    val errorAfter: Time? = null,

    val filter: String? = null,

    @SerialName("warn_after")
    val warnAfter: Time? = null
)

/**
 * Time(count: Union[int, NoneType] = None, period:
 * Union[dbt.contracts.graph.unparsed.TimePeriod, NoneType] = None)
 */
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

/**
 * Measure(name: str, agg:
 * dbt_semantic_interfaces.type_enums.aggregation_type.AggregationType, description:
 * Union[str, NoneType] = None, label: Union[str, NoneType] = None, create_metric: bool =
 * False, expr: Union[str, NoneType] = None, agg_params:
 * Union[dbt.contracts.graph.semantic_models.MeasureAggregationParameters, NoneType] = None,
 * non_additive_dimension: Union[dbt.contracts.graph.semantic_models.NonAdditiveDimension,
 * NoneType] = None, agg_time_dimension: Union[str, NoneType] = None)
 */
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

/**
 * MeasureAggregationParameters(percentile: Union[float, NoneType] = None,
 * use_discrete_percentile: bool = False, use_approximate_percentile: bool = False)
 */
@Serializable
data class MeasureAggregationParameters (
    val percentile: Double? = null,

    @SerialName("use_approximate_percentile")
    val useApproximatePercentile: Boolean? = null,

    @SerialName("use_discrete_percentile")
    val useDiscretePercentile: Boolean? = null
)

/**
 * NonAdditiveDimension(name: str, window_choice:
 * dbt_semantic_interfaces.type_enums.aggregation_type.AggregationType, window_groupings:
 * List[str])
 */
@Serializable
data class NonAdditiveDimension (
    val name: String,

    @SerialName("window_choice")
    val windowChoice: Agg,

    @SerialName("window_groupings")
    val windowGroupings: List<String>
)

/**
 * NodeRelation(alias: str, schema_name: str, database: Union[str, NoneType] = None,
 * relation_name: Union[str, NoneType] = None)
 */
@Serializable
data class NodeRelation (
    val alias: String,
    val database: String? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    @SerialName("schema_name")
    val schemaName: String
)

/**
 * Owner(_extra: Dict[str, Any] = <factory>, email: Union[str, NoneType] = None, name:
 * Union[str, NoneType] = None)
 */
@Serializable
data class Owner (
    val email: String? = null,
    val name: String? = null
)

/**
 * Quoting(database: Union[bool, NoneType] = None, schema: Union[bool, NoneType] = None,
 * identifier: Union[bool, NoneType] = None, column: Union[bool, NoneType] = None)
 */
@Serializable
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
    @SerialName("seed") Seed("seed"),
    @SerialName("semantic_model") SemanticModel("semantic_model"),
    @SerialName("snapshot") Snapshot("snapshot"),
    @SerialName("source") Source("source"),
    @SerialName("test") Test("test");
}

/**
 * TestMetadata(name: str, kwargs: Dict[str, Any] = <factory>, namespace: Union[str,
 * NoneType] = None)
 */
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

/**
 * MetricInput(name: str, filter: Union[dbt.contracts.graph.nodes.WhereFilter, NoneType] =
 * None, alias: Union[str, NoneType] = None, offset_window:
 * Union[dbt.contracts.graph.nodes.MetricTimeWindow, NoneType] = None, offset_to_grain:
 * Union[dbt_semantic_interfaces.type_enums.time_granularity.TimeGranularity, NoneType] =
 * None)
 */
@Serializable
data class MetricInput (
    val alias: String? = null,
    val filter: WhereFilter? = null,
    val name: String,

    @SerialName("offset_to_grain")
    val offsetToGrain: Granularity? = null,

    @SerialName("offset_window")
    val offsetWindow: MetricTimeWindow? = null
)

/**
 * MetricTimeWindow(count: int, granularity:
 * dbt_semantic_interfaces.type_enums.time_granularity.TimeGranularity)
 */
@Serializable
data class MetricTimeWindow (
    val count: Long,
    val granularity: Granularity
)

/**
 * MetricInputMeasure(name: str, filter: Union[dbt.contracts.graph.nodes.WhereFilter,
 * NoneType] = None, alias: Union[str, NoneType] = None, join_to_timespine: bool = False,
 * fill_nulls_with: Union[int, NoneType] = None)
 */
@Serializable
data class MetricInputMeasure (
    val alias: String? = null,

    @SerialName("fill_nulls_with")
    val fillNullsWith: Long? = null,

    val filter: WhereFilter? = null,

    @SerialName("join_to_timespine")
    val joinToTimespine: Boolean? = null,

    val name: String
)

/**
 * Documentation(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path:
 * str, original_file_path: str, unique_id: str, block_contents: str)
 */
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

/**
 * ExposureConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
@Serializable
data class ExposureConfig (
    val enabled: Boolean? = null
)

/**
 * DependsOn(macros: List[str] = <factory>, nodes: List[str] = <factory>)
 */
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

/**
 * Group(name: str, resource_type: dbt.node_types.NodeType, package_name: str, path: str,
 * original_file_path: str, unique_id: str, owner: dbt.contracts.graph.unparsed.Owner)
 */
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

/**
 * MacroArgument(name: str, type: Union[str, NoneType] = None, description: str = '')
 */
@Serializable
data class MacroArgument (
    val description: String? = null,
    val name: String,
    val type: String? = null
)

/**
 * Used only in the Macro class
 */
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
 *
 * Metadata for the manifest.
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
@Serializable
data class Metric (
    val config: MetricConfig? = null,

    @SerialName("created_at")
    val createdAt: Double? = null,

    @SerialName("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    val description: String,
    val filter: WhereFilter? = null,
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

/**
 * MetricConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True, group: Union[str,
 * NoneType] = None)
 */
@Serializable
data class MetricConfig (
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
@Serializable
data class Node (
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
@Serializable
data class NodeConfig (
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

    @SerialName("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @SerialName("target_database")
    val targetDatabase: String? = null,

    @SerialName("target_schema")
    val targetSchema: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

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
    val uniqueId: String
)

/**
 * SemanticModelConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
@Serializable
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
@Serializable
data class SourceDefinition (
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

/**
 * SourceConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
@Serializable
data class SourceConfig (
    val enabled: Boolean? = null
)

@Serializable
enum class SourceResourceType(val value: String) {
    @SerialName("source") Source("source");
}
