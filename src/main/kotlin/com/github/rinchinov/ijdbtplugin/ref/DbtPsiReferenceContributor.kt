package com.github.rinchinov.ijdbtplugin.ref
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import com.intellij.patterns.PsiElementPattern.Capture
import com.jetbrains.jinja2.parsing.Jinja2ElementTypes
import java.util.*
import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.intellij.openapi.components.service


fun parentStartsWithPrefix(prefix: String) = object : PatternCondition<PsiElement>("StartsWith${prefix.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        Locale.getDefault()
    ) else it.toString()
}}") {
    override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
        return t.parent.text.startsWith(prefix)
    }
}


class DbtPsiReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        val basePattern: Capture<PsiElement> = PlatformPatterns.psiElement(PsiElement::class.java)
            .withParent(PlatformPatterns.psiElement(PsiElement::class.java)
                .withElementType(Jinja2ElementTypes.FUNCTION_CALL))

        val refPattern: Capture<PsiElement> = basePattern.with(parentStartsWithPrefix("ref("))
        val sourcePattern: Capture<PsiElement> = basePattern.with(parentStartsWithPrefix("source("))

        registrar.registerReferenceProvider(
            refPattern,
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return arrayOf(element.project.service<ManifestService>().modelReference(element))
                }
            }
        )
        registrar.registerReferenceProvider(
            sourcePattern,
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                    return arrayOf(element.project.service<ManifestService>().sourceReference(element))
                }
            }
        )
    }
}
