package com.github.rinchinov.ijdbtplugin.completion

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.openapi.components.service
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.jetbrains.jinja2.psi.Jinja2StringLiteral

interface DbtCompletionProvider {

    fun getManifestService(parameters: CompletionParameters): ManifestService{
        return parameters.originalFile.project.service<ManifestService>()
    }

    fun ancientIsMacroName(startElement: PsiElement, text: String) : Boolean {
        if (startElement.parent.parent.text.startsWith("$text(")) return true
        if (startElement.parent.parent.text.replace("\\s".toRegex(), "").startsWith("{{$text(")) return true
        return false
    }

    fun findClosestPrevStringSibling(startElement: PsiElement): PsiElement? {
        var currentElement = startElement.prevSibling
        while (currentElement != null) {
            if (currentElement is PsiWhiteSpace || currentElement is PsiComment) {
                currentElement = currentElement.prevSibling
                continue
            }
            if (currentElement is Jinja2StringLiteral &&
                (currentElement.text.startsWith("\"") || currentElement.text.startsWith("'") )) {
                return currentElement
            }
            currentElement = currentElement.prevSibling
        }
        return null
    }
}