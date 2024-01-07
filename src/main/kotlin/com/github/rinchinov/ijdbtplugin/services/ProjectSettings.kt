package com.github.rinchinov.ijdbtplugin.services
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project


@Service(Service.Level.PROJECT)
@State(
        name = "DBTSettings",
        storages = [Storage("dbtSettings.xml")]
)
class ProjectSettings(private val project: Project) : PersistentStateComponent<ProjectSettings.State> {
    private var myState = State()

    data class State(
        var projectPath: String = PROJECT_PATH,
        var dbtProfilePath: String = DBT_PROFILE_PATH,
        var dbtRunnerImport: String = DBT_RUNNER_IMPORT,
        var dbtInterpreterPath: String = DBT_INTERPRETER_PATH,
    ){
        companion object {
            const val PROJECT_PATH = "dbt_project.yml"
            const val DBT_PROFILE_PATH = "~/.dbt/profile.yml"
            const val DBT_RUNNER_IMPORT = "from dbt.cli.main import dbtRunner"
            const val DBT_INTERPRETER_PATH = ""
            fun defaultState() = State() // Returns a state with default values
        }
    }
//    fun resetToDefaults() {
//        myState = State.defaultState() // Resets the state to default values
//    }
    companion object {
        fun getInstance(project: Project): ProjectSettings {
            return project.service()
        }
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState = state
    }

    fun getProjectPath(): String = myState.projectPath

    fun setProjectPath(projectPath: String) {
        myState.projectPath = projectPath
    }

    fun getDbtProfilePath(): String = myState.dbtProfilePath
    fun setDbtProfilePath(dbtProfilePath: String) {
        myState.dbtProfilePath = dbtProfilePath
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
