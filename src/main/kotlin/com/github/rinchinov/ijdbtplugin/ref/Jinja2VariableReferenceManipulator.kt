package com.github.rinchinov.ijdbtplugin.ref

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.util.IncorrectOperationException
import com.jetbrains.jinja2.template.psi.impl.Jinja2VariableReferenceImpl


class Jinja2VariableReferenceManipulator : AbstractElementManipulator<Jinja2VariableReferenceImpl>() {
    @Throws(IncorrectOperationException::class)
    override fun handleContentChange(
        element: Jinja2VariableReferenceImpl,
        range: TextRange,
        newContent: String
    ): Jinja2VariableReferenceImpl {
        return element
    }

    override fun getRangeInElement(element: Jinja2VariableReferenceImpl): TextRange {
        return TextRange(0, element.textLength)
    }
}