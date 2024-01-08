// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json       = Json { allowStructuredMapKeys = true }
// val manifestV8 = json.parse(ManifestV8.serializer(), jsonString)

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
 * dbt.contracts.graph.nodes.Metric], selectors: Mapping[str, Any], disabled:
 * Optional[Mapping[str, List[Union[dbt.contracts.graph.nodes.AnalysisNode,
 * dbt.contracts.graph.nodes.SingularTestNode, dbt.contracts.graph.nodes.HookNode,
 * dbt.contracts.graph.nodes.ModelNode, dbt.contracts.graph.nodes.RPCNode,
 * dbt.contracts.graph.nodes.SqlNode, dbt.contracts.graph.nodes.GenericTestNode,
 * dbt.contracts.graph.nodes.SnapshotNode, dbt.contracts.graph.nodes.SeedNode,
 * dbt.contracts.graph.nodes.SourceDefinition]]]], parent_map: Optional[Dict[str,
 * List[str]]], child_map: Optional[Dict[str, List[str]]])
 */
@Serializable
data class ManifestV8 (
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
     * The sources defined in the dbt project and its dependencies
     */
    val sources: Map<String, SourceDefinition>
)

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

    val refs: List<List<String>>? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    @SerialName("resource_type")
    val resourceType: DisabledResourceType,

    val schema: String,
    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @SerialName("unique_id")
    val uniqueId: String,

    @SerialName("unrendered_config")
    val unrenderedConfig: JsonObject? = null,

    val index: Long? = null,

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
    val sourceName: String? = null
)

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
    @SerialName("data_type")
    val dataType: String? = null,

    val description: String? = null,
    val meta: JsonObject? = null,
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
@Serializable
data class DisabledConfig (
    val alias: String? = null,

    @SerialName("column_types")
    val columnTypes: JsonObject? = null,

    val database: String? = null,
    val docs: Docs? = null,
    val enabled: Boolean? = null,

    @SerialName("full_refresh")
    val fullRefresh: Boolean? = null,

    val grants: JsonObject? = null,

    @SerialName("incremental_strategy")
    val incrementalStrategy: String? = null,

    val materialized: String? = null,
    val meta: JsonObject? = null,

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
 * Docs(show: bool = True, node_color: Optional[str] = None)
 */
@Serializable
data class Docs (
    @SerialName("node_color")
    val nodeColor: String? = null,

    val show: Boolean? = null
)

/**
 * Hook(sql: str, transaction: bool = True, index: Optional[int] = None)
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
 * ExternalTable(_extra: Dict[str, Any] = <factory>, location: Optional[str] = None,
 * file_format: Optional[str] = None, row_format: Optional[str] = None, tbl_properties:
 * Optional[str] = None, partitions: Union[List[str],
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
 * FreshnessThreshold(warn_after: Optional[dbt.contracts.graph.unparsed.Time] = <factory>,
 * error_after: Optional[dbt.contracts.graph.unparsed.Time] = <factory>, filter:
 * Optional[str] = None)
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
 * Time(count: Optional[int] = None, period:
 * Optional[dbt.contracts.graph.unparsed.TimePeriod] = None)
 */
@Serializable
data class Time (
    val count: Long? = null,
    val period: TimePeriod? = null
)

@Serializable
enum class TimePeriod(val value: String) {
    @SerialName("day") Day("day"),
    @SerialName("hour") Hour("hour"),
    @SerialName("minute") Minute("minute");
}

/**
 * Quoting(database: Optional[bool] = None, schema: Optional[bool] = None, identifier:
 * Optional[bool] = None, column: Optional[bool] = None)
 */
@Serializable
data class Quoting (
    val column: Boolean? = null,
    val database: Boolean? = null,
    val identifier: Boolean? = null,
    val schema: Boolean? = null
)

@Serializable
enum class DisabledResourceType(val value: String) {
    @SerialName("analysis") Analysis("analysis"),
    @SerialName("model") Model("model"),
    @SerialName("operation") Operation("operation"),
    @SerialName("rpc") RPC("rpc"),
    @SerialName("sql operation") SQLOperation("sql operation"),
    @SerialName("seed") Seed("seed"),
    @SerialName("snapshot") Snapshot("snapshot"),
    @SerialName("source") Source("source"),
    @SerialName("test") Test("test");
}

/**
 * TestMetadata(name: str, kwargs: Dict[str, Any] = <factory>, namespace: Optional[str] =
 * None)
 */
@Serializable
data class TestMetadata (
    val kwargs: JsonObject? = null,
    val name: String,
    val namespace: String? = null
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

    val owner: ExposureOwner,

    @SerialName("package_name")
    val packageName: String,

    val path: String,
    val refs: List<List<String>>? = null,

    @SerialName("resource_type")
    val resourceType: ExposureResourceType,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,
    val type: Type,

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
enum class Maturity(val value: String) {
    @SerialName("high") High("high"),
    @SerialName("low") Low("low"),
    @SerialName("medium") Medium("medium");
}

/**
 * ExposureOwner(email: str, name: Optional[str] = None)
 */
@Serializable
data class ExposureOwner (
    val email: String,
    val name: String? = null
)

@Serializable
enum class ExposureResourceType(val value: String) {
    @SerialName("exposure") Exposure("exposure");
}

@Serializable
enum class Type(val value: String) {
    @SerialName("analysis") Analysis("analysis"),
    @SerialName("application") Application("application"),
    @SerialName("dashboard") Dashboard("dashboard"),
    @SerialName("ml") Ml("ml"),
    @SerialName("notebook") Notebook("notebook");
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
 * MacroArgument(name: str, type: Optional[str] = None, description: str = '')
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
     * A unique identifier for the project
     */
    @SerialName("project_id")
    val projectId: String? = null,

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
@Serializable
data class Metric (
    @SerialName("calculation_method")
    val calculationMethod: String,

    val config: MetricConfig? = null,

    @SerialName("created_at")
    val createdAt: Double? = null,

    @SerialName("depends_on")
    val dependsOn: ExposureDependsOn? = null,

    val description: String,
    val dimensions: List<String>,
    val expression: String,
    val filters: List<MetricFilter>,
    val fqn: List<String>,
    val label: String,
    val meta: JsonObject? = null,
    val metrics: List<List<String>>? = null,
    val model: String? = null,

    @SerialName("model_unique_id")
    val modelUniqueId: String? = null,

    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    val path: String,
    val refs: List<List<String>>? = null,

    @SerialName("resource_type")
    val resourceType: MetricResourceType,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,

    @SerialName("time_grains")
    val timeGrains: List<String>,

    val timestamp: String? = null,

    @SerialName("unique_id")
    val uniqueId: String,

    @SerialName("unrendered_config")
    val unrenderedConfig: JsonObject? = null,

    val window: MetricTime? = null
)

/**
 * MetricConfig(_extra: Dict[str, Any] = <factory>, enabled: bool = True)
 */
@Serializable
data class MetricConfig (
    val enabled: Boolean? = null
)

/**
 * MetricFilter(field: str, operator: str, value: str)
 */
@Serializable
data class MetricFilter (
    val field: String,
    val operator: String,
    val value: String
)

@Serializable
enum class MetricResourceType(val value: String) {
    @SerialName("metric") Metric("metric");
}

/**
 * MetricTime(count: Optional[int] = None, period:
 * Optional[dbt.contracts.graph.unparsed.MetricTimePeriod] = None)
 */
@Serializable
data class MetricTime (
    val count: Long? = null,
    val period: MetricTimePeriod? = null
)

@Serializable
enum class MetricTimePeriod(val value: String) {
    @SerialName("day") Day("day"),
    @SerialName("month") Month("month"),
    @SerialName("week") Week("week"),
    @SerialName("year") Year("year");
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

    val refs: List<List<String>>? = null,

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
@Serializable
data class NodeConfig (
    val alias: String? = null,

    @SerialName("column_types")
    val columnTypes: JsonObject? = null,

    val database: String? = null,
    val docs: Docs? = null,
    val enabled: Boolean? = null,

    @SerialName("full_refresh")
    val fullRefresh: Boolean? = null,

    val grants: JsonObject? = null,

    @SerialName("incremental_strategy")
    val incrementalStrategy: String? = null,

    val materialized: String? = null,
    val meta: JsonObject? = null,

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
    @SerialName("sql operation") SQLOperation("sql operation"),
    @SerialName("seed") Seed("seed"),
    @SerialName("snapshot") Snapshot("snapshot"),
    @SerialName("test") Test("test");
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
