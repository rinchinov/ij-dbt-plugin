package com.github.rinchinov.ijdbtplugin.ref

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.*

interface ReferencesProviderInterface {

    fun modelReferenceFileByElement(element: PsiElement): String
    fun sourceReferenceFileByElement(element: PsiElement): String
    fun modelReference(element: PsiElement): PsiReferenceBase<PsiElement> {
        return object : PsiReferenceBase<PsiElement>(element) {
            override fun resolve(): PsiElement? {
                val virtualFile = LocalFileSystem.getInstance().findFileByPath(
                    modelReferenceFileByElement(element)
                ) ?: return null
                return PsiManager.getInstance(element.project).findFile(virtualFile)
            }
        }
    }

    fun sourceReference(element: PsiElement): PsiReferenceBase<PsiElement> {
        return object : PsiReferenceBase<PsiElement>(element) {
            override fun resolve(): PsiElement? {
                val virtualFile = LocalFileSystem.getInstance().findFileByPath(
                    sourceReferenceFileByElement(element)
                ) ?: return null
                return PsiManager.getInstance(element.project).findFile(virtualFile)
            }
        }
    }
}

