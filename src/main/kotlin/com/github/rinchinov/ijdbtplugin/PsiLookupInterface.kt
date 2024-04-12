package com.github.rinchinov.ijdbtplugin

import com.github.rinchinov.ijdbtplugin.artifactsVersions.Macro
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Node
import com.github.rinchinov.ijdbtplugin.artifactsVersions.SourceDefinition
import com.intellij.psi.PsiElement
import com.intellij.jinja.template.psi.impl.Jinja2MemberNameImpl

interface PsiLookupInterface {
    fun findNode(packageName: String?, uniqueId: String, currentVersion: Int?, target: String?): Node?
    fun findSourceDefinition(uniqueId: String, target: String?): SourceDefinition?
    fun findMacro(packageName: String?, macroName: String, target: String?): Macro?

    fun findNodeByElement(element: PsiElement): Node? {
        if (element.parent.text.startsWith("ref(")){
            val modelVersionRegex = """(version|v)=["']?(\d+)["']?""".toRegex()
            val modelProjectRegex = """ref\((['"]([^'"]*)['"],\s*)?["']([^"']*)['"]""".toRegex()
            val modelMatch = modelProjectRegex.find(element.parent.text)?.groups
            val packageName = modelMatch?.get(2)?.value
            val uniqueId = modelMatch?.get(3)?.value
            val currentVersion = modelVersionRegex.find(element.parent.text)?.groupValues?.get(2)?.toInt()
            if (uniqueId != null) {
                return findNode(packageName, uniqueId, currentVersion, null)
            }
        }
        return null
    }
    fun findSourceDefinitionByElement(element: PsiElement): SourceDefinition? {
        if (element.parent.text.startsWith("source(")){
            val uniqueId = """source\(['"]([^'"]*)['"],\s*["']([^"']*)['"]\)""".toRegex().replace(
                element.parent.text,
                fun(matchResult: MatchResult): CharSequence {
                    return "${matchResult.groupValues[1]}.${matchResult.groupValues[2]}"
                })
            return findSourceDefinition(uniqueId, null)
        }
        return null
    }
    fun findMacroByElement(element: PsiElement): Macro? {
        val packageName = if (element is Jinja2MemberNameImpl) element.prevSibling.prevSibling.text else ""
        val macroName = element.text
        return findMacro(packageName, macroName, null)
    }
}
