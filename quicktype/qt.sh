
QT() {
  out_dir=$1
  out=$2
  schema=$3
  prefix=src/main/kotlin/com/github/rinchinov/ijdbtplugin/artifactsVersions
  mkdir ${prefix}/${out_dir}
  quicktype --lang=kotlin --src-lang=schema --framework=jackson --acronym-style=camel \
            --package com.github.rinchinov.ijdbtplugin.artifactsVersions.${out_dir}   \
            --out ${prefix}/${out_dir}/${out}.kt1                                     \
            src/main/resources/schemas/${schema}.json
  python3 quicktype/mc.py $4 ${prefix}/${out_dir}/${out}.kt1 > ${prefix}/${out_dir}/${out}.kt
  rm ${prefix}/${out_dir}/${out}.kt1
}
QT manifestV4 ManifestV4 manifest/v4 ManifestInterface
QT manifestV5 ManifestV5 manifest/v5 ManifestInterface
QT manifestV6 ManifestV6 manifest/v6 ManifestInterface
QT manifestV7 ManifestV7 manifest/v7 ManifestInterface
QT manifestV8 ManifestV8 manifest/v8 ManifestInterface
QT manifestV9 ManifestV9 manifest/v9 ManifestInterface
QT manifestV10 ManifestV10 manifest/v10 ManifestInterface
QT manifestV11 ManifestV11 manifest/v11 ManifestInterface
QT runResultsV1 RunResultsV1 runResults/v1 RunResultsInterface
QT runResultsV2 RunResultsV2 runResults/v2 RunResultsInterface
QT runResultsV3 RunResultsV3 runResults/v3 RunResultsInterface
QT runResultsV4 RunResultsV4 runResults/v4 RunResultsInterface
QT runResultsV5 RunResultsV5 runResults/v5 RunResultsInterface
