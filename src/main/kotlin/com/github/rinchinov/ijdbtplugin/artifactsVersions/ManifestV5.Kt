// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json       = Json { allowStructuredMapKeys = true }
// val manifestV5 = json.parse(ManifestV5.serializer(), jsonString)

package com.github.rinchinov.ijdbtplugin.artifactsVersions

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

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
@Serializable
data class ManifestV5 (
    /**
     * A mapping from parent nodes to their dependents
     */
    @SerialName("child_map")
    val childMap: Map<String, List<String>>? = null,

    /**
     * A mapping of the disabled nodes in the target
     */
    val disabled: Map<String, List<CompiledAnalysisNode>>? = null,

    /**
     * The docs defined in the dbt project and its dependencies
     */
    val docs: Map<String, ParsedDocumentation>,

    /**
     * The exposures defined in the dbt project and its dependencies
     */
    val exposures: Map<String, ParsedExposure>,

    /**
     * The macros defined in the dbt project and its dependencies
     */
    val macros: Map<String, ParsedMacro>,

    /**
     * Metadata about the manifest
     */
    val metadata: ManifestMetadata,

    /**
     * The metrics defined in the dbt project and its dependencies
     */
    val metrics: Map<String, ParsedMetric>,

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
    val sources: Map<String, ParsedSourceDefinition>
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
@Serializable
data class CompiledAnalysisNode (
    val alias: String? = null,

    @SerialName("build_path")
    val buildPath: String? = null,

    val checksum: FileHash? = null,
    val columns: Map<String, ColumnInfo>? = null,
    val compiled: Boolean? = null,

    @SerialName("compiled_path")
    val compiledPath: String? = null,

    @SerialName("compiled_sql")
    val compiledSql: String? = null,

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
    val meta: JsonObject? = null,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    @SerialName("patch_path")
    val patchPath: String? = null,

    val path: String,

    @SerialName("raw_sql")
    val rawSql: String? = null,

    val refs: List<List<String>>? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    @SerialName("resource_type")
    val resourceType: DisabledResourceType,

    @SerialName("root_path")
    val rootPath: String,

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
 * ColumnInfo(name: str, description: str = '', meta: Dict[str, Any] = <factory>, data_type:
 * Union[str, NoneType] = None, quote: Union[bool, NoneType] = None, tags: List[str] =
 * <factory>, _extra: Dict[str, Any] = <factory>)
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
@Serializable
data class DisabledConfig (
    val alias: String? = null,

    @SerialName("column_types")
    val columnTypes: JsonObject? = null,

    val database: String? = null,
    val enabled: Boolean? = null,

    @SerialName("full_refresh")
    val fullRefresh: Boolean? = null,

    val materialized: String? = null,
    val meta: JsonObject? = null,

    @SerialName("on_schema_change")
    val onSchemaChange: String? = null,

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

    @SerialName("quote_columns")
    val quoteColumns: Boolean? = null,

    @SerialName("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @SerialName("target_database")
    val targetDatabase: String? = null,

    @SerialName("target_schema")
    val targetSchema: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
sealed class UniqueKey {
    class StringArrayValue(val value: List<String>) : UniqueKey()
    class StringValue(val value: String)            : UniqueKey()
    class NullValue()                               : UniqueKey()
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
 * DependsOn(macros: List[str] = <factory>, nodes: List[str] = <factory>)
 */
@Serializable
data class DependsOn (
    val macros: List<String>? = null,
    val nodes: List<String>? = null
)

/**
 * Docs(show: bool = True)
 */
@Serializable
data class Docs (
    val show: Boolean? = null
)

/**
 * ExternalTable(_extra: Dict[str, Any] = <factory>, location: Union[str, NoneType] = None,
 * file_format: Union[str, NoneType] = None, row_format: Union[str, NoneType] = None,
 * tbl_properties: Union[str, NoneType] = None, partitions:
 * Union[List[dbt.contracts.graph.unparsed.ExternalPartition], NoneType] = None)
 */
@Serializable
data class ExternalTable (
    @SerialName("file_format")
    val fileFormat: String? = null,

    val location: String? = null,
    val partitions: List<ExternalPartition>? = null,

    @SerialName("row_format")
    val rowFormat: String? = null,

    @SerialName("tbl_properties")
    val tblProperties: String? = null
)

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
 * InjectedCTE(id: str, sql: str)
 */
@Serializable
data class InjectedCte (
    val id: String,
    val sql: String
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

@Serializable
enum class DisabledResourceType(val value: String) {
    @SerialName("analysis") Analysis("analysis"),
    @SerialName("model") Model("model"),
    @SerialName("operation") Operation("operation"),
    @SerialName("rpc") RPC("rpc"),
    @SerialName("sql") SQL("sql"),
    @SerialName("seed") Seed("seed"),
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

/**
 * ParsedDocumentation(unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, block_contents: str)
 */
@Serializable
data class ParsedDocumentation (
    @SerialName("block_contents")
    val blockContents: String,

    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    val path: String,

    @SerialName("root_path")
    val rootPath: String,

    @SerialName("unique_id")
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
@Serializable
data class ParsedExposure (
    @SerialName("created_at")
    val createdAt: Double? = null,

    @SerialName("depends_on")
    val dependsOn: DependsOn? = null,

    val description: String? = null,
    val fqn: List<String>,
    val maturity: Maturity? = null,
    val meta: JsonObject? = null,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    val owner: ExposureOwner,

    @SerialName("package_name")
    val packageName: String,

    val path: String,
    val refs: List<List<String>>? = null,

    @SerialName("resource_type")
    val resourceType: ExposureResourceType? = null,

    @SerialName("root_path")
    val rootPath: String,

    val sources: List<List<String>>? = null,
    val tags: List<String>? = null,
    val type: Type,

    @SerialName("unique_id")
    val uniqueId: String,

    val url: String? = null
)

@Serializable
enum class Maturity(val value: String) {
    @SerialName("high") High("high"),
    @SerialName("low") Low("low"),
    @SerialName("medium") Medium("medium");
}

/**
 * ExposureOwner(email: str, name: Union[str, NoneType] = None)
 */
@Serializable
data class ExposureOwner (
    val email: String,
    val name: String? = null
)

@Serializable
enum class ExposureResourceType(val value: String) {
    @SerialName("analysis") Analysis("analysis"),
    @SerialName("docs") Docs("docs"),
    @SerialName("exposure") Exposure("exposure"),
    @SerialName("macro") Macro("macro"),
    @SerialName("metric") Metric("metric"),
    @SerialName("model") Model("model"),
    @SerialName("operation") Operation("operation"),
    @SerialName("rpc") RPC("rpc"),
    @SerialName("sql") SQL("sql"),
    @SerialName("seed") Seed("seed"),
    @SerialName("snapshot") Snapshot("snapshot"),
    @SerialName("source") Source("source"),
    @SerialName("test") Test("test");
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
 * ParsedMacro(unique_id: str, package_name: str, root_path: str, path: str,
 * original_file_path: str, name: str, macro_sql: str, resource_type:
 * dbt.node_types.NodeType, tags: List[str] = <factory>, depends_on:
 * dbt.contracts.graph.parsed.MacroDependsOn = <factory>, description: str = '', meta:
 * Dict[str, Any] = <factory>, docs: dbt.contracts.graph.unparsed.Docs = <factory>,
 * patch_path: Union[str, NoneType] = None, arguments:
 * List[dbt.contracts.graph.unparsed.MacroArgument] = <factory>, created_at: float =
 * <factory>)
 */
@Serializable
data class ParsedMacro (
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

    @SerialName("root_path")
    val rootPath: String,

    val tags: List<String>? = null,

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
 * MacroDependsOn(macros: List[str] = <factory>)
 */
@Serializable
data class MacroDependsOn (
    val macros: List<String>? = null
)

@Serializable
enum class MacroResourceType(val value: String) {
    @SerialName("macro") Macro("macro");
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
 * ParsedMetric(fqn: List[str], unique_id: str, package_name: str, root_path: str, path:
 * str, original_file_path: str, model: str, name: str, description: str, label: str, type:
 * str, sql: Union[str, NoneType], timestamp: Union[str, NoneType], filters:
 * List[dbt.contracts.graph.unparsed.MetricFilter], time_grains: List[str], dimensions:
 * List[str], resource_type: dbt.node_types.NodeType = <NodeType.Metric: 'metric'>, meta:
 * Dict[str, Any] = <factory>, tags: List[str] = <factory>, sources: List[List[str]] =
 * <factory>, depends_on: dbt.contracts.graph.parsed.DependsOn = <factory>, refs:
 * List[List[str]] = <factory>, created_at: float = <factory>)
 */
@Serializable
data class ParsedMetric (
    @SerialName("created_at")
    val createdAt: Double? = null,

    @SerialName("depends_on")
    val dependsOn: DependsOn? = null,

    val description: String,
    val dimensions: List<String>,
    val filters: List<MetricFilter>,
    val fqn: List<String>,
    val label: String,
    val meta: JsonObject? = null,
    val model: String,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    val path: String,
    val refs: List<List<String>>? = null,

    @SerialName("resource_type")
    val resourceType: ExposureResourceType? = null,

    @SerialName("root_path")
    val rootPath: String,

    val sources: List<List<String>>? = null,
    val sql: String? = null,
    val tags: List<String>? = null,

    @SerialName("time_grains")
    val timeGrains: List<String>,

    val timestamp: String? = null,
    val type: String,

    @SerialName("unique_id")
    val uniqueId: String
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
@Serializable
data class Node (
    val alias: String,

    @SerialName("build_path")
    val buildPath: String? = null,

    val checksum: FileHash,
    val columns: Map<String, ColumnInfo>? = null,
    val compiled: Boolean? = null,

    @SerialName("compiled_path")
    val compiledPath: String? = null,

    @SerialName("compiled_sql")
    val compiledSql: String? = null,

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
    val meta: JsonObject? = null,
    val name: String,

    @SerialName("original_file_path")
    val originalFilePath: String,

    @SerialName("package_name")
    val packageName: String,

    @SerialName("patch_path")
    val patchPath: String? = null,

    val path: String,

    @SerialName("raw_sql")
    val rawSql: String,

    val refs: List<List<String>>? = null,

    @SerialName("relation_name")
    val relationName: String? = null,

    @SerialName("resource_type")
    val resourceType: NodeResourceType,

    @SerialName("root_path")
    val rootPath: String,

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
@Serializable
data class NodeConfig (
    val alias: String? = null,

    @SerialName("column_types")
    val columnTypes: JsonObject? = null,

    val database: String? = null,
    val enabled: Boolean? = null,

    @SerialName("full_refresh")
    val fullRefresh: Boolean? = null,

    val materialized: String? = null,
    val meta: JsonObject? = null,

    @SerialName("on_schema_change")
    val onSchemaChange: String? = null,

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

    @SerialName("quote_columns")
    val quoteColumns: Boolean? = null,

    @SerialName("check_cols")
    val checkCols: UniqueKey? = null,

    val strategy: String? = null,

    @SerialName("target_database")
    val targetDatabase: String? = null,

    @SerialName("target_schema")
    val targetSchema: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
enum class NodeResourceType(val value: String) {
    @SerialName("analysis") Analysis("analysis"),
    @SerialName("model") Model("model"),
    @SerialName("operation") Operation("operation"),
    @SerialName("rpc") RPC("rpc"),
    @SerialName("sql") SQL("sql"),
    @SerialName("seed") Seed("seed"),
    @SerialName("snapshot") Snapshot("snapshot"),
    @SerialName("test") Test("test");
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
@Serializable
data class ParsedSourceDefinition (
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

    @SerialName("root_path")
    val rootPath: String,

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
