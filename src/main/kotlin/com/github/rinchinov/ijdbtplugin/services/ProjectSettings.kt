package com.github.rinchinov.ijdbtplugin.services
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import java.nio.file.Path
import java.nio.file.Paths


@Service(Service.Level.PROJECT)
@State(
        name = "DBTSettings",
        storages = [Storage("dbtSettings.xml")]
)
class ProjectSettings(private val project: Project) : PersistentStateComponent<ProjectSettings.State> {
    private var myState = State()

    data class State(
        var dbtProjectPath: String = PROJECT_PATH,
        var dbtProfileDir: String = DBT_PROFILE_DIR,
        var dbtRunnerImport: String = DBT_RUNNER_IMPORT,
        var dbtInterpreterPath: String = DBT_INTERPRETER_PATH,
        var dbtDefaultTarget: String = DBT_DEFAULT_TARGET,
        var dbtAdapter: String = DBT_DEFAULT_ADAPTER,
        var dbtTargetList: List<String> = DBT_TARGET_LIST,
    ){
        companion object {
            const val PROJECT_PATH = "dbt_project.yml"
            const val DBT_PROFILE_DIR = "~/.dbt"
            const val DBT_RUNNER_IMPORT = "from dbt.cli.main import dbtRunner"
            const val DBT_INTERPRETER_PATH = ""
            const val DBT_DEFAULT_TARGET = "dev"
            const val DBT_DEFAULT_ADAPTER = "dbt_postgres"
            val DBT_TARGET_LIST = listOf("dev,prod")
            fun defaultState() = State() // Returns a state with default values
        }
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
    fun getDbtInterpreterPath(): String = myState.dbtInterpreterPath
    fun setDbtInterpreterPath(dbtInterpreterPath: String) {
        myState.dbtInterpreterPath = dbtInterpreterPath
    }
    fun getDbtDefaultTarget(): String = myState.dbtDefaultTarget
    fun setDbtDefaultTarget(dbtDefaultTarget: String) {
        myState.dbtDefaultTarget = dbtDefaultTarget
    }
    fun getDbtAdapter(): String = myState.dbtAdapter
    fun setDbtAdapter(dbtAdapter: String) {
        myState.dbtAdapter = dbtAdapter
    }
    fun getDbtTargetList(): List<String> = myState.dbtTargetList
    fun setDbtTargetList(dbtTargetList: String) {
        myState.dbtTargetList = dbtTargetList.split(",")
    }

}
