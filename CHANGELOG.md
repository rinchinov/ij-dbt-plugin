<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

All notable changes to the DBT Navigator for PyCharm will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.2.0] - 2024-04-11
### Changed
- Set minimal supported version 241.* (there is incompatible change https://plugins.jetbrains.com/docs/intellij/api-changes-list-2024.html#django-plugin-20241)

## [0.1.5] - 2024-04-11
### Added 
- Loading some parameters from profiles
- MVP for query execution feature
### Changed
- Updated error handling


## [0.1.4] - 2024-03-12
### Changed
- Support only ultimate editions of IDEs
- Updated description, readme

## [0.1.3] - 2024-03-11
### Added
- Idea Ultimate support

## [0.1.2] - 2024-03-10
### Added
- Annotations for models/sources and macros
- Autocompletion for models/sources and macros
### Fixed
- Minor fixes

## [0.1.1] - 2024-03-07
### Added 
- Parse `env_var` for while read project file
### Changed
- Use regexp to copy with replacements(to speedup copying)
- Bumped dependencies
### Fixed
- Minor fixes

## [0.1.0] - 2024-03-07
### Added
- Support of multiple targets
- Navigation to dbt core macros
- Added logic to look up dbt installation(not only from venv)(from settings-> from project Interpreter -> dbt installed on computer)
- Window for logging for run dbt commands
- Added error handling and logging it
- Enhanced Documentation panel: added button to generate docs, selector for targets

## [0.0.1] - 2024-02-27

### added
- Initial release of DBT for PyCharm.
- Features including advanced dbt navigation allowing users to jump to definitions and references of macros, models, sources, and seeds within their dbt project.
- Integrated dbt docs preview feature.
- Support for specifying the path to the dbt project directory within PyCharm settings.
- Prerequisites section in documentation for setting up Python SDK with dbt installed.

## [0.0.1-beta-3] - 2024-02-27
-- manifest loading fixes

## [0.0.1-beta-2] - 2024-02-20

-- Fixed blocking updates of manifest
-- Restrict compatibility to pycharm professional only

## [0.0.1-beta-1] - 2024-02-19

- Bump actions/upload-artifact from 3 to 4 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/2
- Bump org.jetbrains.kotlin.jvm from 1.9.21 to 1.9.22 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/3
- Bump org.gradle.toolchains.foojay-resolver-convention from 0.7.0 to 0.8.0 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/4
- Bump actions/cache from 3 to 4 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/5
- Bump JetBrains/qodana-action from 2023.2.8 to 2023.3.1 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/7
- Bump jtalk/url-health-check-action from 3 to 4 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/8
- Bump gradle/gradle-build-action from 2 to 3 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/9
- Bump org.jetbrains.intellij from 1.16.1 to 1.17.1 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/10
- comment tests by @rinchinov in https://github.com/rinchinov/ij-dbt-plugin/pull/13
- Bump org.yaml:snakeyaml from 2.0 to 2.2 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/11
- Bump codecov/codecov-action from 3 to 4 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/14
- Bump gradle/wrapper-validation-action from 1.1.0 to 2.1.1 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/15
- Bump plugin.serialization from 1.6.10 to 1.9.22 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/16
- Bump org.jetbrains.kotlinx:kotlinx-serialization-json from 1.3.2 to 1.6.3 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/17
- Bump org.jetbrains.kotlinx.kover from 0.7.5 to 0.7.6 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/19
- Bump com.google.code.gson:gson from 2.8.9 to 2.10.1 by @dependabot in https://github.com/rinchinov/ij-dbt-plugin/pull/18
- fix action: allow to create PRs by @rinchinov in https://github.com/rinchinov/ij-dbt-plugin/pull/20
- @dependabot made their first contribution in https://github.com/rinchinov/ij-dbt-plugin/pull/2
- @rinchinov made their first contribution in https://github.com/rinchinov/ij-dbt-plugin/pull/13

### Added

- Initial release of DBT for PyCharm.
- Features including advanced dbt navigation allowing users to jump to definitions and references of macros, models, sources, and seeds within their dbt project.
- Support for specifying the path to the dbt project directory within PyCharm settings.
- Prerequisites section in documentation for setting up Python SDK with dbt installed.

## [0.0.1-beta-0] - 2024-02-25

### Added

- Initial release of DBT for PyCharm.
- Features including advanced dbt navigation allowing users to jump to definitions and references of macros, models, sources, and seeds within their dbt project.
- Support for specifying the path to the dbt project directory within PyCharm settings.
- Prerequisites section in documentation for setting up Python SDK with dbt installed.

[Unreleased]: https://github.com/rinchinov/ij-dbt-plugin/compare/v0.0.1-beta-1...HEAD
[0.0.1-beta-1]: https://github.com/rinchinov/ij-dbt-plugin/compare/v0.0.1-beta-0...v0.0.1-beta-1
[0.0.1-beta-0]: https://github.com/rinchinov/ij-dbt-plugin/commits/v0.0.1-beta-0
