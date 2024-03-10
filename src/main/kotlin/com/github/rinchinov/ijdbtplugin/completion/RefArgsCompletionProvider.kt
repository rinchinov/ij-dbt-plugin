package com.github.rinchinov.ijdbtplugin.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

class RefArgsCompletionProvider : DbtCompletionProvider, CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        if (ancientIsMacroName(parameters.position,"ref")) {
            val prevStringSibling= findClosestPrevStringSibling(parameters.position.parent)
            val suggestions = if (prevStringSibling == null) {
                getManifestService(parameters).getRefNamesList()
            } else {
                getManifestService(parameters).getRefNamesList(
                    prevStringSibling.text.replace("'", "").replace("\"", "")
                )
            }
            suggestions.forEach { suggestion ->
                resultSet.addElement(
                    LookupElementBuilder.create(suggestion)
                        .let { PrioritizedLookupElement.withPriority(it, 100.0) }
                )
            }
        }
    }
}
