package com.github.rinchinov.ijdbtplugin

interface CompletionInterface {
    fun getMacrosList(): List<String>
    fun getSourcesNamesList(): List<String>
    fun getSourcesNamesList(packageName: String): List<String>
    fun getRefNamesList(): List<String>
    fun getRefNamesList(packageName: String): List<String>
}