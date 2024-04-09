package com.github.rinchinov.ijdbtplugin.artifactsServices

import com.github.rinchinov.ijdbtplugin.CopyPasteActionsInterface
import com.github.rinchinov.ijdbtplugin.artifactsVersions.Manifest
import com.github.rinchinov.ijdbtplugin.extensions.FocusLogsTabAction
import com.github.rinchinov.ijdbtplugin.services.Notifications
import com.github.rinchinov.ijdbtplugin.utils.renderJinjaRef
import com.github.rinchinov.ijdbtplugin.utils.renderJinjaSource
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

interface ManifestCopyPasteActions: CopyPasteActionsInterface {
    val manifests: MutableMap<String, Manifest?>
    val dbtNotifications: Notifications
    var project: Project
    fun defaultProjectName(): String
    override fun replaceRefsAndSourcesFromJinja2(query: String, target: String): String {
        val manifest = manifests[target]
        if (manifest == null) {
    //        val result = executor.dbtCompileInline(target, query)
    //        val jsonNode = ObjectMapper().readTree(result)
    //        val compiledCode = jsonNode.at("/results/0/node/compiled_code").asText()
    //        if (compiledCode == null) {
            dbtNotifications.sendNotification(
                "Failed to replace ref/source for copying!",
                "",
                NotificationType.ERROR,
                FocusLogsTabAction(project)
            )
            return query
        } else {
            val step1 = renderJinjaSource(query, manifest.sourceMap)
            return renderJinjaRef(step1, manifest.refMap)
        }
    }

    override fun replaceRefsAndSourcesToJinja2(query: String, target: String): String {
        var replaced = query
        manifests[target]?.relationMap?.forEach{
            replaced = replaced.replace(it.key, it.value)
        }
        return replaced.replace("'${defaultProjectName()}', ", "")
    }
}