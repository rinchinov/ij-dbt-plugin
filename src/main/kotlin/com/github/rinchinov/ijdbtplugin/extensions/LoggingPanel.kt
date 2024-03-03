package com.github.rinchinov.ijdbtplugin.extensions

import com.github.rinchinov.ijdbtplugin.LoggingInterface
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.SwingUtilities


class LoggingPanel : JPanel(), LoggingInterface {
    private val logTextArea = JTextArea()

    init {
        layout = BorderLayout()
        logTextArea.lineWrap = true
        logTextArea.isEditable = false

        val scrollPane = JScrollPane(logTextArea)
        add(scrollPane, BorderLayout.CENTER)
    }

    @Synchronized
    override fun appendLog(message: String, logType: String) {
        SwingUtilities.invokeLater {
            logTextArea.append("$message\n")
            ensureLimit()
            // Scroll to the bottom
            logTextArea.caretPosition = logTextArea.document.length
        }
    }

    private fun ensureLimit() {
        val maxRows = 1000
        val lines = logTextArea.text.split("\n")
        if (lines.size > maxRows) {
            val startIndex = lines.size - maxRows
            val newText = lines.subList(startIndex, lines.size).joinToString("\n")
            logTextArea.text = newText
        }
    }
}
