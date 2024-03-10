package com.github.rinchinov.ijdbtplugin.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

class SourceArgsCompletionProvider : DbtCompletionProvider, CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {

        if ((ancientIsMacroName(parameters.position, "source")) ) {
            val prevStringSibling= findClosestPrevStringSibling(parameters.position.parent)
            val suggestions = if (prevStringSibling == null) {
                getManifestService(parameters).getSourcesNamesList()
            } else {
                getManifestService(parameters).getSourcesNamesList(
                    prevStringSibling.text.replace("'", "").replace("\"", "")
                )
            }
            suggestions.forEach { suggestion ->
                resultSet.addElement(LookupElementBuilder.create(suggestion).withPriority(100.0))
            }
        }
    }
}
