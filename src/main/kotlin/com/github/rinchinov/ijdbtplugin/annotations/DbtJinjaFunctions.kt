package com.github.rinchinov.ijdbtplugin.annotations

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

class DbtJinjaFunctions {
    private val url = "https://docs.getdbt.com/reference/dbt-jinja-functions/"
    private val functionsList = listOf(
        "ref",
        "source",
        "config",
        "debug",
        "doc",
        "env_var",
        "exceptions.raise_compiler_error",
        "exceptions.warn",
        "fromjson",
        "fromyaml",
        "local_md5",
        "log",
        "print",
        "return",
        "run_query",
        "tojson",
        "toyaml",
        "var",
        "zip",
        "adapter.dispatch",
        "adapter.get_missing_columns",
        "adapter.expand_target_column_types",
        "adapter.get_relation",
        "adapter.get_columns_in_relation",
        "adapter.create_schema",
        "adapter.drop_schema",
        "adapter.drop_relation",
        "adapter.rename_relation",
        "adapter.quote",
        "modules.itertools.count",
        "modules.itertools.cycle",
        "modules.itertools.repeat",
        "modules.itertools.accumulate",
        "modules.itertools.chain",
        "modules.itertools.compress",
        "modules.itertools.islice",
        "modules.itertools.starmap",
        "modules.itertools.tee",
        "modules.itertools.zip_longest",
        "modules.itertools.product",
        "modules.itertools.permutations",
        "modules.itertools.combinations",
        "modules.itertools.combinations_with_replacement",
        "modules.datetime.date",
        "modules.datetime.datetime",
        "modules.datetime.time",
        "modules.datetime.timedelta",
        "modules.datetime.tzinfo",
        "modules.pytz.timezone",
        "modules.re.match",
        "modules.re.search",
        "modules.re.compile",
        "modules.re.fullmatch",
        "modules.re.split",
        "modules.re.findall",
        "modules.re.sub",
        "modules.re.subn",
    )
    private val variablesList = listOf(
        "model",
        "dbt_version",
        "flags",
        "flags.FULL_REFRESH",
        "flags.STORE_FAILURES",
        "flags.WHICH",
        "invocation_id",
        "thread_id",
        "invocation_args_dict",
        "dbt_metadata_envs",
        "modules.re",
        "modules.pytz",
        "modules.datetime",
        "modules.itertools",
    )
    private val annotations = functionsList.map { it }.associateWith { "DBT Jinja function: documentation $url${it.replace(".", "#")}" }
    fun getMacroAnnotation(macroElement: PsiElement): Pair<String, HighlightSeverity>? {
        val message = annotations[macroElement.text]
        return if (message != null){
            Pair(message, HighlightSeverity.INFORMATION)
        } else null
    }

    fun getCompletions(): List<String> {
        return functionsList.map { "$it()" } + variablesList
    }
}