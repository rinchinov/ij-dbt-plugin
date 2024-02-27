package com.github.rinchinov.ijdbtplugin.ref

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.*
import com.jetbrains.jinja2.template.psi.impl.Jinja2MemberNameImpl


interface ReferencesProviderInterface {

    fun modelReferenceFileByElement(packageName: String?, uniqueId: String, currentVersion: Int?, element: PsiElement): String
    fun sourceReferenceFileByElement(uniqueId: String, element: PsiElement): String
    fun macroReferenceFileByElement(packageName: String, macroName: String, element: PsiElement): String

    fun macroReference(element: PsiElement): PsiReferenceBase<PsiElement> {
        return object : PsiReferenceBase<PsiElement>(element) {
            override fun resolve(): PsiElement? {
                val packageName = if (element is Jinja2MemberNameImpl) element.prevSibling.prevSibling.text else ""
                val macroName = element.text
                val path: String = macroReferenceFileByElement(packageName, macroName, element)
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
                        val modelVersionRegex = """(version|v)=["']?(\d+)["']?""".toRegex()
                        val modelProjectRegex = """ref\((['"]([^'"]*)['"],\s*)?["']([^"']*)['"]""".toRegex()
                        val modelMatch = modelProjectRegex.find(element.parent.text)?.groups
                        val packageName = modelMatch?.get(2)?.value
                        val uniqueId = modelMatch?.get(3)?.value
                        val currentVersion = modelVersionRegex.find(element.parent.text)?.groupValues?.get(2)?.toInt()
                        if (uniqueId != null) {
                            modelReferenceFileByElement(packageName, uniqueId, currentVersion, element)
                        }
                        else {
                            ""
                        }
                    }
                    element.parent.text.startsWith("source(") -> {
                        val uniqueId = """source\(['"]([^'"]*)['"],\s*["']([^"']*)['"]\)""".toRegex().replace(element.parent.text,
                            fun(matchResult: MatchResult): CharSequence {
                                return "${matchResult.groupValues[1]}.${matchResult.groupValues[2]}"
                            })
                        sourceReferenceFileByElement(uniqueId, element)
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