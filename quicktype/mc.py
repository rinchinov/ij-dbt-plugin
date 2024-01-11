import re
import sys

interface_name = sys.argv[1]
# Sample Kotlin data class text
with open(sys.argv[2]) as f:
    text = f.read()

# Define the regex pattern
pattern = re.compile(r'(\))(\s*{\s*fun toJson\(\)\s*=\s*mapper\.writeValueAsString\(this\)\s*companion object\s*{\s*fun fromJson\(json: String\)\s*=\s*mapper\.readValue<\w+\d+>\(json\)\s*}\s*})')

# Perform the replacement
def replace_match(match):
    class_name = match.group(1)
    return f"): {interface_name}{match.group(2)}"

# Replace in the text
result = pattern.sub(replace_match, text)

result = result.replace(
    """import com.fasterxml.jackson.module.kotlin.*""",
    """import com.fasterxml.jackson.module.kotlin.*\nimport com.github.rinchinov.ijdbtplugin.artifactInterfaces.ManifestInterface"""
).replace(
    """setSerializationInclusion(JsonInclude.Include.NON_NULL)""",
    """setSerializationInclusion(JsonInclude.Include.NON_NULL)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)""",
)

print(result)
