package com.github.rinchinov.ijdbtplugin.completion

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder

class MyFunctionInsertHandler : InsertHandler<LookupElement> {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val editor = context.editor
        val caretModel = editor.caretModel
        val tailOffset = context.tailOffset
        caretModel.moveToOffset(tailOffset - 1)
    }
}

fun LookupElementBuilder.withPriority(priority: Double): LookupElement {
    return this.withLookupString(this.lookupString).withInsertHandler { context, item ->
    }.let { PrioritizedLookupElement.withPriority(it, priority) }
}