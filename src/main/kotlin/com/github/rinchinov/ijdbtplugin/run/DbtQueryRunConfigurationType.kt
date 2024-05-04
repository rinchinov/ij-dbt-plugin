package com.github.rinchinov.ijdbtplugin.run

import com.intellij.execution.configurations.ConfigurationFactory

import com.intellij.execution.configurations.ConfigurationType
import com.intellij.icons.AllIcons
import javax.swing.Icon

class DbtQueryRunConfigurationType : ConfigurationType {
    override fun getIcon(): Icon = AllIcons.Actions.Execute

    override fun getConfigurationTypeDescription(): String = "DBT run query configuration"

    override fun getId(): String = "DBT_RUN_QUERY_CONFIGURATION"

    override fun getDisplayName(): String = "DBT Run Query"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf()
    }
}