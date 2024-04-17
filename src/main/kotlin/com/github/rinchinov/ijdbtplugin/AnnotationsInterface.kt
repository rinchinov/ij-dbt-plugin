package com.github.rinchinov.ijdbtplugin

import com.github.rinchinov.ijdbtplugin.services.Statistics
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

interface AnnotationsInterface: PsiLookupInterface {
    val statistics: Statistics
    fun getSourceAnnotation(element: PsiElement): List<Pair<String, HighlightSeverity>> {
        val source = findSourceDefinitionByElement(element)
        if (source != null){
            val result = mutableListOf<Pair<String, HighlightSeverity>>()
            mapOf(
                "DB name" to source.relationName,
                "Description" to source.description.toString(),
                "Tags" to source.tags?.joinToString(separator = ","),
                "Package name" to source.packageName,
            ).forEach { t ->
                if (!t.value.isNullOrEmpty() ){
                    result.add(Pair("${t.key}: ${t.value}", HighlightSeverity.INFORMATION))
                }
            }
            return result
        }
        else {
            return listOf(Pair("Source not found!", HighlightSeverity.ERROR))
        }
    }
    fun getModelAnnotation(element: PsiElement): List<Pair<String, HighlightSeverity>>{
        val node = findNodeByElement(element)
        if (node != null){
            val result = mutableListOf<Pair<String, HighlightSeverity>>()
            mapOf(
                "DB name" to node.relationName,
                "Description" to node.description.toString(),
                "Tags" to node.tags?.joinToString(separator = ","),
                "Package name" to node.packageName,
            ).forEach { t ->
                if (!t.value.isNullOrEmpty() ){
                    result.add(Pair("${t.key}: ${t.value}", HighlightSeverity.INFORMATION))
                }
            }
            return result
        }
        else {
            return listOf(Pair("Model not found!", HighlightSeverity.ERROR))
        }
    }
    fun getMacroAnnotation(element: PsiElement): List<Pair<String, HighlightSeverity>>{
        val macro = findMacroByElement(element)
        if (macro != null){
            val result = mutableListOf<Pair<String, HighlightSeverity>>()
            mapOf(
                "Arguments" to macro.arguments.toString(),
                "Description" to macro.description.toString(),
                "Package name" to macro.packageName,
                "Macro sql" to macro.macroSql,
            ).forEach { t ->
                if (!t.value.isNullOrEmpty() ){
                    result.add(Pair("${t.key}: ${t.value}", HighlightSeverity.INFORMATION))
                }
            }
            return result
        }
        else {
            return listOf(Pair("Macro not found!", HighlightSeverity.ERROR))
        }
    }
}
