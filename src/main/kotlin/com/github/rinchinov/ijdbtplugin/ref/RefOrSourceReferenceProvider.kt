package com.github.rinchinov.ijdbtplugin.ref

import com.github.rinchinov.ijdbtplugin.artifactsServices.ManifestService
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

class RefOrSourceReferenceProvider: PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        return arrayOf(element.project.service<ManifestService>().refOrSourceReference(element))
    }
}