package com.github.rinchinov.ijdbtplugin.completion
import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.jetbrains.jinja2.template.DjangoTemplateLanguage

class DbtJinja2CompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PsiElement::class.java).withLanguage(DjangoTemplateLanguage.INSTANCE),
            MacroNameCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PsiElement::class.java).withLanguage(DjangoTemplateLanguage.INSTANCE),
            SourceArgsCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PsiElement::class.java).withLanguage(DjangoTemplateLanguage.INSTANCE),
            RefArgsCompletionProvider()
        )
    }
}
