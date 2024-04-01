package com.github.rinchinov.ijdbtplugin.extensions;

import com.github.rinchinov.ijdbtplugin.queryExecution.QueryChangeListener
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import javax.swing.*
import java.awt.BorderLayout
import javax.swing.table.DefaultTableModel


class QueryRunResultsPanel(toolWindow: ToolWindow): QueryChangeListener {
    private val queryExecutionBackend = toolWindow.project.service<QueryExecutionBackend>()
    private val querySelector = ComboBox<QueryBoxItem>()
    private val resultsTable = object : DefaultTableModel() {
        override fun isCellEditable(row: Int, column: Int): Boolean {
            return false
        }
        fun clear(){
            for (i in rowCount - 1 downTo 0) {
                removeRow(i)
            }
            columnCount = 0
        }
    }
    private val statusLabel = JLabel()
    private val paginationPanel = JPanel()
    class QueryBoxItem(val id: String, val text: String) {
        override fun toString(): String {
            return text.trim().take(60)
        }
    }
    init {
        queryExecutionBackend.addQueryChangeListener(this)
    }
    fun getContent(): JComponent {
        val mainPanel = JPanel(BorderLayout())
        val scrollPane = JBScrollPane(
            JBTable(resultsTable).apply {
                fillsViewportHeight = true
            }
        )
        val northPanel = JPanel()
        northPanel.add(statusLabel)
        northPanel.add(querySelector)
        northPanel.add(
            JButton("Clear executions").apply {
                addActionListener {
                    clearQueryPanel()
                }
            }
        )
        querySelector.addActionListener {
            val selectedItem = querySelector.selectedItem as QueryBoxItem
            val queryExecution = queryExecutionBackend.getExecutionById(selectedItem.id)
            if (queryExecution != null) {
                displayExecutedQuery(queryExecution, 1)
            }
        }
        mainPanel.add(northPanel, BorderLayout.NORTH)
        mainPanel.add(scrollPane, BorderLayout.CENTER)
        mainPanel.add(paginationPanel, BorderLayout.SOUTH)
        return mainPanel
    }

    private fun displayTable(rows: List<List<String>>, queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int){
        resultsTable.clear()
        rows.forEachIndexed { index, row ->
            if (index == 0) {
                row.forEach {
                    resultsTable.addColumn(it)
                }
            } else {
                resultsTable.addRow(row.toTypedArray())
            }
        }
        displayPagination(queryExecution, pageNumber)
    }

    private fun paginatedButton(text: String, queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int, enabled: Boolean): JButton {
        return JButton(text).apply {
            addActionListener {
                if (enabled){
                    displayExecutedQuery(queryExecution, pageNumber)
                }
            }
        }
    }
    private fun displayPagination(queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int) {
        paginationPanel.removeAll()
        if (queryExecution.totalPages > 1){
            paginationPanel.add(
                JLabel("Total pages ${queryExecution.totalPages}")
            )
            arrayOf(
                paginatedButton("<<", queryExecution, 1, pageNumber != 1),
                paginatedButton("<", queryExecution, pageNumber - 1, pageNumber > 1),
                paginatedButton(pageNumber.toString(), queryExecution, pageNumber, false),
                paginatedButton(">", queryExecution, pageNumber + 1, pageNumber < queryExecution.totalPages),
                paginatedButton(">>", queryExecution, queryExecution.totalPages, pageNumber != queryExecution.totalPages),
            ).forEach {
                paginationPanel.add(it)
            }
        }
    }
    override fun displayExecutedQuery(queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int){
        statusLabel.text = "Query status: ${queryExecution.status}"
        val rows = queryExecutionBackend.getExecutionResultPageNumber(
            queryExecution,
            pageNumber
        )
        if (rows != null) {
            displayTable(rows, queryExecution, pageNumber)
        }
    }
    override fun clearQueryPanel() {
        queryExecutionBackend.clearExecutions()
        resultsTable.clear()
        paginationPanel.removeAll()
        statusLabel.text = ""
        querySelector.removeAllItems()
    }
    override fun onQueryAdd(queryExecution: QueryExecutionBackend.QueryExecution, keep: Int) {
        querySelector.insertItemAt(QueryBoxItem(queryExecution.executionId, queryExecution.query), 0)
        if (querySelector.itemCount > QueryExecutionBackend.MAX_EXECUTIONS) {
            querySelector.removeItemAt(querySelector.itemCount - 1)
        }
    }
}
