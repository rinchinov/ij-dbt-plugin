package com.github.rinchinov.ijdbtplugin.services
import com.intellij.openapi.components.*


@Service(Service.Level.PROJECT)
@State(
    name = "DBTSettings",
    storages = [Storage("dbtSettings.xml")]
)
class ProjectSettings: PersistentStateComponent<ProjectSettings.State> {
    private var myState = State()

    data class State(
        var dbtProjectPath: String = PROJECT_PATH,
        var dbtProfileDir: String = DBT_PROFILE_DIR,
        var dbtRunnerImport: String = DBT_RUNNER_IMPORT,
        var dbtInterpreterPath: String = DBT_INTERPRETER_PATH,
        var dbtEnvVariables: Map<String, String> = emptyMap(),
        var dbtQueryRunPaginationTemplate: String  = DBT_QUERY_RUN_PAGINATION_TEMPLATE,
        var dbtQueryRunCountTemplate: String  = DBT_QUERY_RUN_COUNT_TEMPLATE,
        var dbtQueryRunDryTemplate: String = DBT_QUERY_RUN_DRY_TEMPLATE,
        var dbtQueryRunPlanTemplate: String = DBT_QUERY_RUN_PLAN_TEMPLATE,
    ){
        companion object {
            const val PROJECT_PATH = "dbt_project.yml"
            const val DBT_PROFILE_DIR = "~/.dbt"
            const val DBT_RUNNER_IMPORT = "from dbt.cli.main import dbtRunner"
            const val DBT_INTERPRETER_PATH = ""
            const val DBT_QUERY_RUN_PAGINATION_TEMPLATE = "%s LIMIT ? OFFSET ?"
            const val DBT_QUERY_RUN_COUNT_TEMPLATE = "SELECT COUNT(*) FROM (%s)"
            const val DBT_QUERY_RUN_DRY_TEMPLATE = "%s LIMIT 10"
            const val DBT_QUERY_RUN_PLAN_TEMPLATE = "EXPLAIN %s"
        }
    }

    init {
        Statistics.getInstance().setProjectSettings(this)
    }
    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState = state
    }

    fun getDbtProjectPath(): String = myState.dbtProjectPath

    fun setDbtProjectPath(dbtProjectPath: String) {
        myState.dbtProjectPath = dbtProjectPath
    }

    fun getDbtProfileDir(): String = myState.dbtProfileDir
    fun setDbtProfileDir(dbtProfileDir: String) {
        myState.dbtProfileDir = dbtProfileDir
    }

    fun getDbtRunnerImport(): String = myState.dbtRunnerImport
    fun setDbtRunnerImport(dbtRunnerImport: String) {
        myState.dbtRunnerImport = dbtRunnerImport
    }

    fun getDbtEnvVariables(): Map<String, String> = myState.dbtEnvVariables

    fun setDbtEnvVariables(dbtEnvVariables: String) {
        myState.dbtEnvVariables = dbtEnvVariables.split(',')
            .map { it.split('=') }
            .associate { it.first() to it.last() }
            .filterKeys { it.isNotEmpty() }
    }

    fun getDbtEnvVariablesText(): String {
        return myState.dbtEnvVariables.entries.joinToString(separator = ",") { "${it.key}=${it.value}" }
    }

    fun getDbtInterpreterPath(): String = myState.dbtInterpreterPath

    fun setDbtInterpreterPath(dbtInterpreterPath: String) {
        myState.dbtInterpreterPath = dbtInterpreterPath
    }
    fun getDbtQueryRunPaginationTemplate(): String = myState.dbtQueryRunPaginationTemplate
    fun setDbtQueryRunPaginationTemplate(dbtQueryRunPaginationTemplate: String) {
        myState.dbtQueryRunPaginationTemplate = dbtQueryRunPaginationTemplate
    }
    fun getDbtQueryRunCountTemplate(): String = myState.dbtQueryRunCountTemplate
    fun setDbtQueryRunCountTemplate(dbtQueryRunCountTemplate: String) {
        myState.dbtQueryRunCountTemplate = dbtQueryRunCountTemplate
    }
    fun getDbtQueryRunDryTemplate(): String = myState.dbtQueryRunDryTemplate
    fun setDbtQueryRunDryTemplate(dbtQueryRunDryTemplate: String) {
        myState.dbtQueryRunDryTemplate = dbtQueryRunDryTemplate
    }
    fun getDbtQueryRunPlanTemplate(): String = myState.dbtQueryRunPlanTemplate
    fun setDbtQueryRunPlanTemplate(dbtQueryRunPlanTemplate: String) {
        myState.dbtQueryRunPlanTemplate = dbtQueryRunPlanTemplate
    }

}
