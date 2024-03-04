package com.github.rinchinov.ijdbtplugin.extensions
import com.github.rinchinov.ijdbtplugin.MyBundle
import com.github.rinchinov.ijdbtplugin.services.ProjectConfigurations
import com.github.rinchinov.ijdbtplugin.services.ProjectSettings
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.options.Configurable
import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*
import com.intellij.ui.components.JBScrollPane


class PluginProjectConfigurable(private val project: Project) : Configurable {
    private val settings = project.service<ProjectSettings>()
    private val projectConfigurations = project.service<ProjectConfigurations>()
    private val mainPanel = JPanel(GridBagLayout())
    private val projectPath: JTextField = JTextField()
    private val dbtProfileDir: JTextField = JTextField()
    private val dbtRunnerImport: JTextField = JTextField()
    private val dbtInterpreterPath: JTextField = JTextField()
    private val dbtTargetList: JTextField = JTextField()
    private val dbtDefaultTarget: JTextField = JTextField()
    private val dbtAdapter: JTextField = JTextField()

    init {
        val gbc = GridBagConstraints().apply {
            gridx = 0
            gridy = GridBagConstraints.RELATIVE
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
            insets = JBUI.insets(4)
        }

        addLabeledField("Project path", projectPath, mainPanel, gbc)
        addLabeledField("DBT profile path", dbtProfileDir, mainPanel, gbc)
        addLabeledField("DBT runner import", dbtRunnerImport, mainPanel, gbc)
        addLabeledField("DBT interpreter path", dbtInterpreterPath, mainPanel, gbc)
        addLabeledField("DBT target lists", dbtTargetList, mainPanel, gbc)
        addLabeledField("DBT plugin default target", dbtDefaultTarget, mainPanel, gbc)
        addLabeledField("DBT plugin default adapter", dbtAdapter, mainPanel, gbc)

        gbc.weighty = 1.0
        mainPanel.add(Box.createVerticalGlue(), gbc)
    }

    private fun addLabeledField(labelText: String, field: JTextField, panel: JPanel, gbc: GridBagConstraints) {// Clone the GridBagConstraints to avoid side effects
        val labelGbc = gbc.clone() as GridBagConstraints
        labelGbc.gridx = 0
        labelGbc.weightx = 0.0
        panel.add(JLabel(labelText), labelGbc)

        val fieldGbc = gbc.clone() as GridBagConstraints
        fieldGbc.gridx = 1
        fieldGbc.weightx = 1.0
        panel.add(field, fieldGbc)
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
                || settings.getDbtDefaultTarget() != dbtDefaultTarget.text
                || settings.getDbtAdapter() != dbtAdapter.text
                || settings.getDbtTargetList() != dbtTargetList.text.split(",")
    }

    override fun apply() {
        settings.setDbtProjectPath(projectPath.text)
        settings.setDbtProfileDir(dbtProfileDir.text)
        settings.setDbtRunnerImport(dbtRunnerImport.text)
        settings.setDbtInterpreterPath(dbtInterpreterPath.text)
        settings.setDbtDefaultTarget(dbtDefaultTarget.text)
        settings.setDbtAdapter(dbtAdapter.text)
        settings.setDbtTargetList(dbtTargetList.text)
        projectConfigurations.reloadDbtProjectSettings()
    }

    override fun reset() {
        projectPath.text = settings.getDbtProjectPath()
        dbtProfileDir.text = settings.getDbtProfileDir()
        dbtRunnerImport.text = settings.getDbtRunnerImport()
        dbtInterpreterPath.text = settings.getDbtInterpreterPath()
        dbtDefaultTarget.text = settings.getDbtDefaultTarget()
        dbtAdapter.text = settings.getDbtAdapter()
        dbtTargetList.text = settings.getDbtTargetList().joinToString(separator = ",")
    }

    override fun getDisplayName(): String = MyBundle.message("settingWindowName")
    override fun disposeUIResources() {

    }
}
