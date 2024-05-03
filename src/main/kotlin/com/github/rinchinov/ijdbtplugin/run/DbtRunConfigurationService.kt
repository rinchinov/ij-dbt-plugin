package com.github.rinchinov.ijdbtplugin.run

import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.UnknownConfigurationType
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class DbtRunConfigurationService(val project: Project) {

    fun updateConfigurations(configurations: ProjectConfigurations) {
        val runManager = RunManager.getInstance(project) as RunManagerImpl
        createQueryRunConfigurations(configurations, runManager)
    }

    private fun createQueryRunConfigurations(configurations: ProjectConfigurations, runManager: RunManagerImpl) {
        runManager.removeConfigurations(runManager.getConfigurationSettingsList(UnknownConfigurationType.getInstance()))
        runManager.removeConfigurations(runManager.getConfigurationSettingsList(DbtQueryRunConfigurationType()))
        configurations.dbtProjectConfig.targets.forEach {target ->
            createQueryRunConfiguration(runManager, "Run Selected With Database Tools: $target", DbtQueryRunConfiguration.Options(target, null))
            createQueryRunConfiguration(runManager, "Run Selected: $target", DbtQueryRunConfiguration.Options(target, QueryExecutionBackend.QueryTypes.PAGINATED))
            createQueryRunConfiguration(runManager, "Dry Run Selected: $target", DbtQueryRunConfiguration.Options(target, QueryExecutionBackend.QueryTypes.DRY))
            createQueryRunConfiguration(runManager, "Query Plan Selected: $target", DbtQueryRunConfiguration.Options(target, QueryExecutionBackend.QueryTypes.PLAN))
        }
    }

    private fun createQueryRunConfiguration(runManager: RunManagerImpl, name: String, options: DbtQueryRunConfiguration.Options) {
        val factory = object: ConfigurationFactory(DbtQueryRunConfigurationType()) {
            private val options = options
            override fun createTemplateConfiguration(project: Project): RunConfiguration {
                return DbtQueryRunConfiguration(project, this, name, this.options)
            }
            override fun getId(): String {
                return "DBT_RUN_QUERY_CONFIGURATION_${options.queryType}_${options.target.uppercase()}"
            }
        }
        val settings = runManager.createConfiguration(name, factory)
        runManager.addConfiguration(settings)
    }
}
