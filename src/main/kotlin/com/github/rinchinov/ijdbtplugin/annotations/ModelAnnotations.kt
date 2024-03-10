package com.github.rinchinov.ijdbtplugin.annotations;

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.jetbrains.jinja2.psi.Jinja2StringLiteral
import com.jetbrains.jinja2.tags.Jinja2FunctionCall
import com.jetbrains.jinja2.template.psi.impl.Jinja2MemberNameImpl
import com.jetbrains.jinja2.template.psi.impl.Jinja2VariableReferenceImpl

class ModelAnnotations : Annotator {

    private fun pushAnnotation(pair: Pair<String, HighlightSeverity>, holder: AnnotationHolder) {
        val (message: String, severity: HighlightSeverity) = pair
        holder.newAnnotation(severity, message).create()
    }

    private fun getManifestService(element: PsiElement): ManifestService = element.project.service<ManifestService>()
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is Jinja2StringLiteral && element.parent is Jinja2FunctionCall) {
            if (element.parent.text.startsWith("source(")) {
                getManifestService(element).getSourceAnnotation(element).forEach {
                    pushAnnotation(
                        it,
                        holder,
                    )
                }
            }
            if (element.parent.text.startsWith("ref(")) {
                getManifestService(element).getModelAnnotation(element).forEach {
                    pushAnnotation(
                        it,
                        holder,
                    )
                }
            }
        }
        val macroElement = when {
            element is Jinja2VariableReferenceImpl && element.nextSibling.text == "(" -> {
                element
            }
            element is Jinja2MemberNameImpl && element.parent.nextSibling.text == "(" -> {
                element.parent
            }
            else -> { null }
        }
        if (macroElement != null) {
            val dbtJinjaFunctionAnnotation: Pair<String, HighlightSeverity>? = DbtJinjaFunctions().getMacroAnnotation(macroElement)
            if (dbtJinjaFunctionAnnotation != null){
                pushAnnotation(
                    dbtJinjaFunctionAnnotation,
                    holder,
                )
            } else {
                getManifestService(element).getMacroAnnotation(element).forEach {
                    pushAnnotation(
                        it,
                        holder,
                    )
                }
            }
        }
    }
}
