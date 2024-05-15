package com.github.rinchinov.ijdbtplugin.run
import com.github.rinchinov.ijdbtplugin.queryExecution.IdeQueryExecutionBackend
import com.github.rinchinov.ijdbtplugin.queryExecution.QueryExecutionBackend
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.ExecutionResult
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.Executor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager

class DbtQueryRunProfileState(private val environment: ExecutionEnvironment, private val options: DbtQueryRunConfiguration.Options): RunProfileState {

    override fun execute(executor: Executor?, runner: ProgramRunner<*>): ExecutionResult? {
        FileEditorManager.getInstance(environment.project).selectedTextEditor?.let {
            if (options.queryType == null){
                environment.project.service<IdeQueryExecutionBackend>().runQuery(
                    it,
                    options.target,
                    options.dbtCompile
                )
            }
            else {
                environment.project.service<QueryExecutionBackend>().runQuery(
                    it,
                    options.target,
                    options.queryType,
                    options.dbtCompile
                )
            }
        }
        return null
    }
}