package com.github.rinchinov.ijdbtplugin.completion

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement

class MyFunctionInsertHandler : InsertHandler<LookupElement> {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val editor = context.editor
        val caretModel = editor.caretModel
        val tailOffset = context.tailOffset
        caretModel.moveToOffset(tailOffset - 1)
    }
}
