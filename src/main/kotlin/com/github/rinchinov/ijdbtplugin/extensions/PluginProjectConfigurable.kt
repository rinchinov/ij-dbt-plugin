package com.github.rinchinov.ijdbtplugin.extensions
import com.github.rinchinov.ijdbtplugin.MyBundle
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.github.rinchinov.ijdbtplugin.services.ProjectSettings
import com.github.rinchinov.ijdbtplugin.services.Statistics
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.HyperlinkLabel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.Box
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
class PluginProjectConfigurable(project: Project) : Configurable {
    private val statistics =  Statistics.getInstance()
    private val settings = project.service<ProjectSettings>()
    private val projectConfigurations = project.service<ProjectConfigurations>()
    private val mainPanel = JPanel(GridBagLayout())
    private val projectPath: JBTextField = JBTextField()
    private val dbtProfileDir: JBTextField = JBTextField()
    private val dbtRunnerImport: JBTextField = JBTextField()
    private val dbtInterpreterPath: JBTextField = JBTextField()
    private val dbtEnvVariables: JBTextField = JBTextField()
    private val dbtQueryRunPaginationTemplate: JBTextField = JBTextField()
    private val dbtQueryRunCountTemplate: JBTextField = JBTextField()
    private val dbtQueryRunDryTemplate: JBTextField = JBTextField()
    private val dbtQueryRunPlanTemplate: JBTextField = JBTextField()
    private val userConsent: JBCheckBox = JBCheckBox("Allow to collect anonymous statistics")
    init {
        val gbc = GridBagConstraints().apply {
            gridx = 0
            gridy = GridBagConstraints.RELATIVE
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            insets = JBUI.insets(4)
        }

        addLabeledField("dbt_project.yml File Path", projectPath, mainPanel, gbc)
        addLabeledField(
            "dbt Profile Directory",
            "It should be a path to directory where profiles are stored. Click to go to dbt docs",
            "https://docs.getdbt.com/docs/core/connect-data-platform/profiles.yml",
            dbtProfileDir,
            mainPanel,
            gbc
        )
        addLabeledField(
            "DBT runner import",
            dbtRunnerImport,
            mainPanel,
            gbc
        )
        addLabeledField(
            "DBT interpreter path",
            "If this option is empty, then plugin will get active python SDK from IDE",
            dbtInterpreterPath,
            mainPanel,
            gbc
        )
        addLabeledField("DBT environment variables", dbtEnvVariables, mainPanel, gbc)

        addLabeledField("Query run SQL templates:", null, mainPanel, gbc)
        addLabeledField("Query paginated template", dbtQueryRunPaginationTemplate, mainPanel, gbc)
        addLabeledField("Query count template", dbtQueryRunCountTemplate, mainPanel, gbc)
        addLabeledField("Query plan template", dbtQueryRunPlanTemplate, mainPanel, gbc)
        addLabeledField("Query dry run template", dbtQueryRunDryTemplate, mainPanel, gbc)
        addLabeledField(
            "Collect usage statistic",
            "Click to learn more about how we use collected data",
            "https://github.com/rinchinov/ij-dbt-plugin/blob/main/PRIVACY_POLICY",
            userConsent,
            mainPanel,
            gbc
        )
        gbc.weighty = 1.0
        mainPanel.add(Box.createVerticalGlue(), gbc)
    }

    private fun simpleLabel(text: String) = JLabel(text)
    private fun simpleLabel(text: String, toolTipText: String) = JLabel(text).apply {
        this.toolTipText = toolTipText
    }

    private fun hyperLinkLabel(labelText: String, toolTipText: String, labelLink: String) = HyperlinkLabel().apply {
        this.setHyperlinkText(labelText)
        this.setToolTipText(toolTipText)
        this.addHyperlinkListener {
            BrowserUtil.browse(labelLink)
        }
    }

    private fun addLabeledField(labelText: String, toolTipText: String, labelLink: String, field: JComponent?, panel: JPanel, gbc: GridBagConstraints) {
        addLabeledField(
            hyperLinkLabel(labelText, toolTipText, labelLink),
            field,
            panel,
            gbc
        )
    }

    private fun addLabeledField(labelText: String, toolTipText: String, field: JComponent?, panel: JPanel, gbc: GridBagConstraints) {
        addLabeledField(
            simpleLabel(labelText, toolTipText),
            field,
            panel,
            gbc
        )
    }

    private fun addLabeledField(labelText: String, field: JComponent?, panel: JPanel, gbc: GridBagConstraints) {
        addLabeledField(
            simpleLabel(labelText),
            field,
            panel,
            gbc
        )
    }

    private fun addLabeledField(label: JComponent, field: JComponent?, panel: JPanel, gbc: GridBagConstraints) {// Clone the GridBagConstraints to avoid side effects
        val labelGbc = gbc.clone() as GridBagConstraints
        labelGbc.gridx = 0
        labelGbc.weightx = 0.0
        panel.add(label, labelGbc)
        if (field!=null){
            val fieldGbc = gbc.clone() as GridBagConstraints
            fieldGbc.gridx = 1
            fieldGbc.weightx = 1.0
            panel.add(field, fieldGbc)
        }
        gbc.gridy += 2
    }

    override fun createComponent(): JComponent {
        return JBScrollPane(mainPanel) // Wrap the mainPanel in a JScrollPane
    }

    override fun isModified(): Boolean {
        return settings.getDbtProjectPath() != projectPath.text
                || settings.getDbtProfileDir() != dbtProfileDir.text
                || settings.getDbtRunnerImport() != dbtRunnerImport.text
                || settings.getDbtInterpreterPath() != dbtInterpreterPath.text
                || settings.getDbtEnvVariablesText() != dbtEnvVariables.text
                || settings.getDbtQueryRunPlanTemplate() != dbtQueryRunPaginationTemplate.text
                || settings.getDbtQueryRunCountTemplate() != dbtQueryRunCountTemplate.text
                || settings.getDbtQueryRunPlanTemplate() != dbtQueryRunPlanTemplate.text
                || settings.getDbtQueryRunDryTemplate() != dbtQueryRunDryTemplate.text
                || statistics.getUserConsent() != userConsent.isSelected
    }

    override fun apply() {
        settings.setDbtProjectPath(projectPath.text)
        settings.setDbtProfileDir(dbtProfileDir.text)
        settings.setDbtRunnerImport(dbtRunnerImport.text)
        settings.setDbtInterpreterPath(dbtInterpreterPath.text)
        settings.setDbtEnvVariables(dbtEnvVariables.text)
        settings.setDbtQueryRunPaginationTemplate(dbtQueryRunPaginationTemplate.text)
        settings.setDbtQueryRunCountTemplate(dbtQueryRunCountTemplate.text)
        settings.setDbtQueryRunDryTemplate(dbtQueryRunDryTemplate.text)
        settings.setDbtQueryRunPlanTemplate(dbtQueryRunPlanTemplate.text)
        statistics.setUserConsent(userConsent.isSelected)
        projectConfigurations.reloadDbtProjectSettings()
    }

    override fun reset() {
        projectPath.text = settings.getDbtProjectPath()
        dbtProfileDir.text = settings.getDbtProfileDir()
        dbtRunnerImport.text = settings.getDbtRunnerImport()
        dbtInterpreterPath.text = settings.getDbtInterpreterPath()
        dbtEnvVariables.text = settings.getDbtEnvVariablesText()
        dbtQueryRunPaginationTemplate.text = settings.getDbtQueryRunPaginationTemplate()
        dbtQueryRunCountTemplate.text = settings.getDbtQueryRunCountTemplate()
        dbtQueryRunDryTemplate.text = settings.getDbtQueryRunDryTemplate()
        dbtQueryRunPlanTemplate.text = settings.getDbtQueryRunPlanTemplate()
        userConsent.isSelected = statistics.getUserConsent() ?: false
    }

    override fun getDisplayName(): String = MyBundle.message("settingWindowName")
    override fun disposeUIResources() {}
}
