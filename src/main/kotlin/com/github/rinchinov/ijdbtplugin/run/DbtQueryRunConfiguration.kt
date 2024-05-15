package com.github.rinchinov.ijdbtplugin.run
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import javax.swing.JPanel

class DbtQueryRunConfiguration(
    private val project: Project,
    private val factory: ConfigurationFactory,
    private val name: String,
    val options: Options
): RunConfigurationBase<Any>(project, factory, name) {

    data class Options(
        val target: String,
        val queryType: QueryExecutionBackend.QueryTypes?,
        val dbtCompile: Boolean
    )

    override fun clone(): RunConfiguration {
        return DbtQueryRunConfiguration(project, factory, name, options)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return DbtQueryRunProfileState(environment, options)
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return object: SettingsEditor<DbtQueryRunConfiguration>() {
            override fun createEditor() = JPanel()

            override fun resetEditorFrom(s: DbtQueryRunConfiguration) {}

            override fun applyEditorTo(s: DbtQueryRunConfiguration) {}

        }
    }
}
