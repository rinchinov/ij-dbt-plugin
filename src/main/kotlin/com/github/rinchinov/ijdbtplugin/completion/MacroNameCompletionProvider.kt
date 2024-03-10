package com.github.rinchinov.ijdbtplugin.completion

import com.github.rinchinov.ijdbtplugin.annotations.DbtJinjaFunctions
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

class MacroNameCompletionProvider: DbtCompletionProvider, CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        var priority = 100.0
        if (ancientIsMacroName(parameters.position,"ref")) priority = 50.0
        if (ancientIsMacroName(parameters.position,"source")) priority = 50.0
        val packageName = if (parameters.position.parent.prevSibling.text == ".") parameters.position.parent.prevSibling.prevSibling.text else null
        val suggestions = if (packageName == null) {
            getManifestService(parameters).getMacrosList()
        } else {
            getManifestService(parameters).getMacrosList(
                packageName
            )
        }
        (DbtJinjaFunctions().getCompletions() + suggestions).forEach{macroName ->
            resultSet.addElement(
                LookupElementBuilder.create(macroName)
                    .withInsertHandler(MyFunctionInsertHandler())
                    .let { PrioritizedLookupElement.withPriority(it, priority) }
            )
        }
    }
}
