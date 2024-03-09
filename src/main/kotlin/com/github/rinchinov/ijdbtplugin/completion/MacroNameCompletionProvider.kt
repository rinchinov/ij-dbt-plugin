package com.github.rinchinov.ijdbtplugin.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

class MacroNameCompletionProvider: DbtCompletionProvider, CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        getManifestService(parameters).getMacrosList().forEach { macroName ->
            resultSet.addElement(
                LookupElementBuilder.create(macroName)
                    .withInsertHandler(MyFunctionInsertHandler())
            )
        }
    }
}
