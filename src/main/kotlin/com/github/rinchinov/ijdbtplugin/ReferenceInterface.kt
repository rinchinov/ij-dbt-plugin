package com.github.rinchinov.ijdbtplugin

import com.github.rinchinov.ijdbtplugin.artifactsVersions.Macro
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase

interface ReferenceInterface: PsiLookupInterface {
    fun getPackageDir(packageName: String): String

    fun macroReference(element: PsiElement): PsiReferenceBase<PsiElement> {
        return object : PsiReferenceBase<PsiElement>(element) {
            override fun resolve(): PsiElement? {
                val macro: Macro? = findMacroByElement(element)
                val path = if (macro != null){
                    getPackageDir(macro.packageName) + "/" + macro.originalFilePath
                } else {
                    ""
                }
                val virtualFile = LocalFileSystem.getInstance().findFileByPath(
                    path
                ) ?: return null
                return PsiManager.getInstance(element.project).findFile(virtualFile)
            }
        }
    }

    fun refOrSourceReference(element: PsiElement): PsiReferenceBase<PsiElement> {
        return object : PsiReferenceBase<PsiElement>(element) {
            override fun resolve(): PsiElement? {
                val path: String = when {
                    element.parent.text.startsWith("ref(") -> {
                        val node = findNodeByElement(element)
                        if (node != null){
                            getPackageDir(node.packageName) + "/" + node.originalFilePath
                        } else {
                            ""
                        }
                    }
                    element.parent.text.startsWith("source(") -> {
                        val source = findSourceDefinitionByElement(element)
                        if (source != null){
                            getPackageDir(source.packageName) + "/" + source.originalFilePath
                        } else {
                            ""
                        }
                    }
                    else -> ""
                }
                val virtualFile = LocalFileSystem.getInstance().findFileByPath(
                    path
                ) ?: return null
                return PsiManager.getInstance(element.project).findFile(virtualFile)
            }
        }
    }
}