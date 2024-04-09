package com.github.rinchinov.ijdbtplugin.extensions

import com.github.rinchinov.ijdbtplugin.queryExecution.QueryChangeListener
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import javax.swing.*
import javax.swing.table.DefaultTableModel


class QueryRunResultsPanel(toolWindow: ToolWindow): QueryChangeListener {
    private val queryExecutionBackend = toolWindow.project.service<QueryExecutionBackend>()
    private val loadingPanel = object: JPanel(){
        init {
            add(JLabel("Querying..."))

        }
    }
    private val resultsTable = object : DefaultTableModel() {
        override fun isCellEditable(row: Int, column: Int): Boolean {
            return false
        }
        fun displayTable(rows: List<List<String>>){
            clear()
            rows.forEachIndexed { index, row ->
                if (index == 0) {
                    row.forEach {
                        addColumn(it)
                    }
                } else {
                    addRow(row.toTypedArray())
                }
            }
        }
        private fun clear(){
            for (i in rowCount - 1 downTo 0) {
                removeRow(i)
            }
            columnCount = 0
        }
    }
    private val tableScroll = JBScrollPane(
        JBTable(resultsTable).apply {
            fillsViewportHeight = true
        }
    )

    private val topPanel = object: JPanel(){
        private val querySelector = object: ComboBox<QueryExecutionBackend.QueryExecution>() {
            init {
                addActionListener {
                    val queryExecution = selectedItem as QueryExecutionBackend.QueryExecution
                    displayExecutedQuery(queryExecution, 1)
                }
            }
        }
        private val clearButton = object: JButton("Clear executions"){
            init {
                addActionListener {
                    if (isEnabled) {
                        clearQueryPanel()
                    }
                }
            }
        }
        init {
            add(querySelector)
            add(clearButton)
        }
        fun disableElements(){
            querySelector.isEnabled = false
            clearButton.isEnabled = false
        }

        fun enableElements(){
            querySelector.isEnabled = true
            clearButton.isEnabled = true
        }
        fun pushExecution(queryExecution: QueryExecutionBackend.QueryExecution, keep: Int){
            querySelector.insertItemAt(queryExecution, 0)
            querySelector.selectedIndex = 0
            if (querySelector.itemCount > keep) {
                querySelector.removeItemAt(querySelector.itemCount - 1)
            }
        }
        fun clearPanel(){
            querySelector.removeAllItems()
        }
    }

    private val mainPanel = JPanel(BorderLayout())

    private val paginationPanel = object : JPanel() {
        private var totalPages = 1
        private var currentPage = 1
        private var currentQueryExecution: QueryExecutionBackend.QueryExecution? = null
        val label = JLabel("Total pages: ")
        init {
            isVisible = false
        }
        private fun createPageButton(label: String, action: () -> Unit): JButton {
            return JButton(label).apply {
                addActionListener {
                    if (this.isEnabled) {
                        action()
                    }
                }
            }
        }

        val firstPageButton = createPageButton("<<") {
            queryExecutionBackend.getExecutionResultPageNumber(currentQueryExecution!!, 1)
        }

        val prevPageButton = createPageButton("<") {
            queryExecutionBackend.getExecutionResultPageNumber(currentQueryExecution!!, currentPage - 1)
        }

        val currentPageButton = JButton("").apply { isEnabled = false }

        val nextPageButton = createPageButton(">") {
            queryExecutionBackend.getExecutionResultPageNumber(currentQueryExecution!!, currentPage + 1)
        }

        val lastPageButton = createPageButton(">>") {
            queryExecutionBackend.getExecutionResultPageNumber(currentQueryExecution!!, totalPages)
        }

        init {
            add(firstPageButton)
            add(prevPageButton)
            add(currentPageButton)
            add(nextPageButton)
            add(lastPageButton)
        }
        fun setPagination(queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int) {
            totalPages = queryExecution.totalPages
            currentPage = pageNumber
            currentPageButton.text = pageNumber.toString()
            currentQueryExecution = queryExecution
            if (totalPages > 1){
                this.isVisible = true
                label.text = "Total pages: $totalPages"
                when (currentPage) {
                    1 -> {
                        firstPageButton.isEnabled = false
                        prevPageButton.isEnabled = false
                        nextPageButton.isEnabled = true
                        lastPageButton.isEnabled = true
                    }
                    totalPages -> {
                        firstPageButton.isEnabled = true
                        prevPageButton.isEnabled = true
                        nextPageButton.isEnabled = false
                        lastPageButton.isEnabled = false
                    }
                    else -> {
                        firstPageButton.isEnabled = true
                        prevPageButton.isEnabled = true
                        nextPageButton.isEnabled = true
                        lastPageButton.isEnabled = true
                    }
                }
            }
        }
        fun disablePaginationButtons(){
            firstPageButton.isEnabled = false
            prevPageButton.isEnabled = false
            nextPageButton.isEnabled = false
            lastPageButton.isEnabled = false
        }
    }
    init {
        queryExecutionBackend.addQueryChangeListener(this)
    }
    fun getContent(): JComponent {
        mainPanel.add(topPanel, BorderLayout.NORTH)
        mainPanel.add(tableScroll, BorderLayout.CENTER)
        mainPanel.add(paginationPanel, BorderLayout.SOUTH)
        return mainPanel
    }
    override fun onQueryAdd(queryExecution: QueryExecutionBackend.QueryExecution, keep: Int) {
        topPanel.pushExecution(queryExecution, keep)
    }

    override fun displayQueryLoading(){
        paginationPanel.disablePaginationButtons()
        topPanel.disableElements()
        mainPanel.remove(tableScroll)
        mainPanel.add(loadingPanel, BorderLayout.CENTER)
    }

    override fun displayExecutedQuery(queryExecution: QueryExecutionBackend.QueryExecution, pageNumber: Int){
        val rows = queryExecution.getPage(pageNumber)
        resultsTable.displayTable(rows)
        paginationPanel.setPagination(queryExecution, pageNumber)
        topPanel.enableElements()
        mainPanel.remove(loadingPanel)
        mainPanel.add(tableScroll, BorderLayout.CENTER)
    }

    override fun clearQueryPanel() {
        queryExecutionBackend.clearExecutions()
        resultsTable.displayTable(emptyList())
        paginationPanel.isVisible = false
        topPanel.clearPanel()
    }
}
