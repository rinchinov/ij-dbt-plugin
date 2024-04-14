package com.github.rinchinov.ijdbtplugin.utils.Jinja2Utils


fun renderJinjaEnvVar(input: String): String {
    val regexPattern = "\\{\\{\\s*env_var\\(\\s*[\"\'](.*?)[\"\']\\s*(,\\s*[\"\'](.*?)[\"\'])?\\s*\\)\\s*}}".toRegex()
    return regexPattern.replace(input) { matchResult ->
        val envVarName = matchResult.groupValues[1]
        val defaultValue = matchResult.groupValues[3] // Might be empty if no default value is provided
        System.getenv(envVarName) ?: defaultValue // Replace with env var value or default value
    }
}

fun renderJinjaSource(input: String, mapper: Map<String, String>): String {
    // Pattern to match source function with arguments in single or double quotes
    val regexPattern = """\{\{\s*source\(\s*(["'])(.*?)\1\s*,\s*(["'])(.*?)\3\s*\)\s*}}""".toRegex()
    return regexPattern.replace(input) { matchResult ->
        val arg1 = matchResult.groupValues[2]
        val arg2 = matchResult.groupValues[4]
        mapper["${arg1}.${arg2}"] ?: input
    }
}

fun renderJinjaRef(input: String, mapper: Map<String, String>): String {
    // Pattern to match ref function with two optional positional arguments and an optional keyword argument (version or v)
    val regexPattern = """\{\{\s*ref\(\s*(["']?)(.*?)\1\s*(?:,\s*(["'])(.*?)\3)?\s*(?:,\s*(version|v)=([0-9]+))?\s*\)\s*}}""".toRegex()
//    val exampleInput = """Data: {{ ref("arg1", "arg2", version=2) }} and {{ ref('arg1') }} and {{ ref() }} and {{ ref('arg1', v=4) }}"""

    return regexPattern.replace(input) { matchResult ->
        // Access captured groups
        val arg1 = matchResult.groupValues[2] // Argument 1
        val arg2 = matchResult.groupValues[4] // Argument 2
        val versionValue = matchResult.groupValues[6] // The version number

        val name = if (arg1.isNotEmpty() && arg2.isNotEmpty()){
            "$arg1.$arg2"
        } else {
            "$arg1$arg2"
        }
        val fullname = if (versionValue.isNotEmpty()){ "${name}.v$versionValue"} else { name }
        mapper[fullname] ?: matchResult.value
    }
}
