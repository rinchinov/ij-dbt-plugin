package com.github.rinchinov.ijdbtplugin.extentions
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
    private val dbtProfilePath: JTextField = JTextField()
    private val dbtRunnerImport: JTextField = JTextField()
    private val dbtInterpreterPath: JTextField = JTextField()
    private val shellPathField = JTextField(20)
    private val defaultTabNameField = JTextField(20)
    private val audibleBellCheckBox = JCheckBox("Audible bell")

    init {
        val gbc = GridBagConstraints()
        gbc.gridx = 0 // First column
        gbc.gridy = 0 // Start from first row
        gbc.gridwidth = 2 // Span two columns
        gbc.fill = GridBagConstraints.HORIZONTAL // Fill horizontally
        gbc.insets = JBUI.insets(4)
        // Project Settings Section
        mainPanel.add(JLabel("Project Settings"), gbc)
        gbc.gridy++
        gbc.gridwidth = 1 // Reset to one column
        gbc.weightx = 0.0 // Do not expand labels horizontally
        mainPanel.add(JLabel("Project path"), gbc)
        gbc.gridx = 1 // Second column for input fields
        gbc.weightx = 1.0 // Expand fields horizontally
        mainPanel.add(projectPath, gbc)
        gbc.gridy++
        gbc.gridx = 0
        gbc.gridwidth = 1 // Reset to one column
        gbc.weightx = 0.0 // Do not expand labels horizontally
        mainPanel.add(JLabel("DBT profile path"), gbc)
        gbc.gridx = 1 // Second column for input fields
        gbc.weightx = 1.0 // Expand fields horizontally
        mainPanel.add(dbtProfilePath, gbc)
        gbc.gridy++
        gbc.gridx = 0
        gbc.gridwidth = 1 // Reset to one column
        gbc.weightx = 0.0 // Do not expand labels horizontally
        mainPanel.add(JLabel("DBT runner import"), gbc)
        gbc.gridx = 1 // Second column for input fields
        gbc.weightx = 1.0 // Expand fields horizontally
        mainPanel.add(dbtRunnerImport, gbc)
        gbc.gridy++
        gbc.gridx = 0
        gbc.gridwidth = 1 // Reset to one column
        gbc.weightx = 0.0 // Do not expand labels horizontally
        mainPanel.add(JLabel("DBT interpreter path"), gbc)
        gbc.gridx = 1 // Second column for input fields
        gbc.weightx = 1.0 // Expand fields horizontally
        mainPanel.add(dbtInterpreterPath, gbc)

//        // Application Settings Section
//        gbc.gridx = 0 // Reset to first column
//        gbc.gridy++
//        gbc.gridwidth = 2 // Span two columns for section label
//        gbc.weightx = 0.0 // Do not expand section label horizontally
//        mainPanel.add(JLabel("Application Settings"), gbc)
//
//        gbc.gridy++
//        gbc.gridwidth = 1 // Reset to one column for labels
//        gbc.weightx = 0.0 // Do not expand labels horizontally
//        mainPanel.add(JLabel("Shell path:"), gbc)
//        gbc.gridx = 1 // Second column for input fields
//        gbc.weightx = 1.0 // Expand fields horizontally
//        mainPanel.add(shellPathField, gbc)
//
//        gbc.gridx = 0
//        gbc.gridy++
//        mainPanel.add(JLabel("Default Tab name:"), gbc)
//        gbc.gridx = 1
//        mainPanel.add(defaultTabNameField, gbc)
//
//        gbc.gridx = 0
//        gbc.gridy++
//        gbc.gridwidth = 2 // Checkbox to span two columns
//        mainPanel.add(audibleBellCheckBox, gbc)
//
//        // Filler at the end to push everything to the top
//        gbc.gridx = 0
//        gbc.gridy++
//        gbc.gridwidth = 2
//        gbc.weighty = 1.0 // Extra vertical space assigned to the filler
//        gbc.fill = GridBagConstraints.BOTH // Fill both horizontally and vertically
        mainPanel.add(Box.createGlue(), gbc)
    }
    override fun createComponent(): JComponent {
        return JBScrollPane(mainPanel) // Wrap the mainPanel in a JScrollPane
    }

    override fun isModified(): Boolean {
        return settings.getDbtProjectPath() != projectPath.text
                || settings.getDbtProfilePath() != dbtProfilePath.text
                || settings.getDbtRunnerImport() != dbtRunnerImport.text
                || settings.getDbtInterpreterPath() != dbtInterpreterPath.text
    }

    override fun apply() {
        settings.setDbtProjectPath(projectPath.text)
        settings.setDbtProfilePath(dbtProfilePath.text)
        settings.setDbtRunnerImport(dbtRunnerImport.text)
        settings.setDbtInterpreterPath(dbtInterpreterPath.text)
        projectConfigurations.reloadDbtProjectSettings()
    }

    override fun reset() {
        projectPath.text = settings.getDbtProjectPath()
        dbtProfilePath.text = settings.getDbtProfilePath()
        dbtRunnerImport.text = settings.getDbtRunnerImport()
        dbtInterpreterPath.text = settings.getDbtInterpreterPath()
    }

    override fun getDisplayName(): String = MyBundle.message("settingWindowName")
    override fun disposeUIResources() {

    }
}
