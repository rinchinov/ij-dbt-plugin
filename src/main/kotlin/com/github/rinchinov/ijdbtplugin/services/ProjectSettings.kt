package com.github.rinchinov.ijdbtplugin.services
import com.intellij.openapi.components.*


@Service(Service.Level.PROJECT)
@State(
        name = "DBTSettings",
        storages = [Storage("dbtSettings.xml")]
)
class ProjectSettings() : PersistentStateComponent<ProjectSettings.State> {
    private var myState = State()

    data class State(
        var dbtProjectPath: String = PROJECT_PATH,
        var dbtProfileDir: String = DBT_PROFILE_DIR,
        var dbtRunnerImport: String = DBT_RUNNER_IMPORT,
        var dbtInterpreterPath: String = DBT_INTERPRETER_PATH
    ){
        companion object {
            const val PROJECT_PATH = "dbt_project.yml"
            const val DBT_PROFILE_DIR = "~/.dbt"
            const val DBT_RUNNER_IMPORT = "from dbt.cli.main import dbtRunner"
            const val DBT_INTERPRETER_PATH = ""
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
}
