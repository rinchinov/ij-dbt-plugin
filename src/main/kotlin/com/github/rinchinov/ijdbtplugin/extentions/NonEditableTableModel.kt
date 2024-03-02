package com.github.rinchinov.ijdbtplugin.extentions

import javax.swing.table.DefaultTableModel

class NonEditableTableModel : DefaultTableModel() {
    private var keys: MutableMap<String, Int> = mutableMapOf()
    private var index: Int = 0
    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }
    fun addRow(id: String, name: String, value: String) {
        super.addRow(arrayOf(name, value))
        keys[id] = index
        index += 1
    }
    fun setValue(id: String, value: String) {
        keys[id]?.let { setValueAt(value, it, 1) }
    }
}
