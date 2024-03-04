package com.github.rinchinov.ijdbtplugin.ref
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern.Capture
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import com.jetbrains.jinja2.tags.Jinja2FunctionCall
import com.jetbrains.jinja2.template.psi.impl.DjangoLiteral
import com.jetbrains.jinja2.template.psi.impl.Jinja2MemberExpressionImpl
import com.jetbrains.jinja2.template.psi.impl.Jinja2MemberNameImpl
import com.jetbrains.jinja2.template.psi.impl.Jinja2VariableReferenceImpl


class DbtPsiReferenceContributor : PsiReferenceContributor() {
    private fun nextSiblingIsLPar() = object : PatternCondition<PsiElement>("nextSiblingIsLPar") {
        override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
            return t.nextSibling != null && t.nextSibling.text == "("
        }
    }

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        val macroPattern = StandardPatterns.or(
            PlatformPatterns.psiElement(Jinja2MemberNameImpl::class.java).withParent(
                PlatformPatterns.psiElement(Jinja2MemberExpressionImpl::class.java)
                    .with(nextSiblingIsLPar())
            ),
            PlatformPatterns.psiElement(Jinja2VariableReferenceImpl::class.java)
                .with(nextSiblingIsLPar())
        )
        val refOrSourcePattern: Capture<DjangoLiteral> = PlatformPatterns.psiElement(DjangoLiteral::class.java)
            .withParent(
                PlatformPatterns.psiElement(Jinja2FunctionCall::class.java)
                    .with(object : PatternCondition<Jinja2FunctionCall>("FunctionNameCondition") {
                        override fun accepts(jinja2FunctionCall: Jinja2FunctionCall, context: ProcessingContext?): Boolean {
                            val functionName = jinja2FunctionCall.firstChild.text
                            return functionName == "source" || functionName == "ref"
                        }
                    })

            )
        registrar.registerReferenceProvider(
            refOrSourcePattern,
            RefOrSourceReferenceProvider()
        )
        registrar.registerReferenceProvider(
            macroPattern,
            MacroReferenceProvider()
        )
    }
}
