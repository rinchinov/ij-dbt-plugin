
QT() {
  out=$1
  schema=$2
  quicktype --lang=kotlin --src-lang=schema --framework=kotlinx --acronym-style=camel --package com.github.rinchinov.ijdbtplugin.artifactsVersions --out src/main/kotlin/com/github/rinchinov/ijdbtplugin/artifactsVersions/${out}.Kt src/main/resources/schemas/${schema}.json
}
QT ManifestV4 manifest/v4
QT ManifestV5 manifest/v5
QT ManifestV6 manifest/v6
QT ManifestV7 manifest/v7
QT ManifestV8 manifest/v8
QT ManifestV9 manifest/v9
QT ManifestV10 manifest/v10
QT ManifestV11 manifest/v11
QT RunResultsV1 runResults/v1
QT RunResultsV2 runResults/v2
QT RunResultsV3 runResults/v3
QT RunResultsV4 runResults/v4
QT RunResultsV5 runResults/v5