package com.github.rinchinov.ijdbtplugin

interface CompletionInterface {
    fun getMacrosList(): List<String>
    fun getMacrosList(packageName: String): List<String>
    fun getSourcesNamesList(): List<String>
    fun getSourcesNamesList(sourceName: String): List<String>
    fun getRefNamesList(): List<String>
    fun getRefNamesList(packageName: String): List<String>
}
