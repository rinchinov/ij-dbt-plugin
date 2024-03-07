package com.github.rinchinov.ijdbtplugin


fun replaceJinjaWithEnvOrDefault(input: String): String {
    val regexPattern = "\\{\\{\\s*env_var\\(\\s*[\"\'](.*?)[\"\']\\s*(,\\s*[\"\'](.*?)[\"\'])?\\s*\\)\\s*}}".toRegex()
    return regexPattern.replace(input) { matchResult ->
        val envVarName = matchResult.groupValues[1]
        val defaultValue = matchResult.groupValues[3] // Might be empty if no default value is provided
        System.getenv(envVarName) ?: defaultValue // Replace with env var value or default value
    }
}