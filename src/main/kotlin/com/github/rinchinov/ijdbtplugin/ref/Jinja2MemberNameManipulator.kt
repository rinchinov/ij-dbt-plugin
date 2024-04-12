package com.github.rinchinov.ijdbtplugin.ref

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.util.IncorrectOperationException
import com.intellij.jinja.template.psi.impl.Jinja2MemberNameImpl


class Jinja2MemberNameManipulator : AbstractElementManipulator<Jinja2MemberNameImpl>() {
    @Throws(IncorrectOperationException::class)
    override fun handleContentChange(
        element: Jinja2MemberNameImpl,
        range: TextRange,
        newContent: String
    ): Jinja2MemberNameImpl {
        return element
    }

    override fun getRangeInElement(element: Jinja2MemberNameImpl): TextRange {
        return TextRange(0, element.textLength)
    }
}