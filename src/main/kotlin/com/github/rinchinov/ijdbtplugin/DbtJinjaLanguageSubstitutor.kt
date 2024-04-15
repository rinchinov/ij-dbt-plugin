package com.github.rinchinov.ijdbtplugin

import com.intellij.jinja.Jinja2Language
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutor

class DbtJinjaLanguageSubstitutor : LanguageSubstitutor() {
    override fun getLanguage(file: VirtualFile, project: Project): Language? {
        if (file.extension == "sql") {
            return Jinja2Language.INSTANCE
        }

        return null;
    }
}
