# ij-dbt-plugin

![Build](https://github.com/rinchinov/ij-dbt-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.rinchinov.ijdbtplugin.svg)](https://plugins.jetbrains.com/plugin/com.github.rinchinov.ijdbtplugin)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.rinchinov.ijdbtplugin.svg)](https://plugins.jetbrains.com/plugin/com.github.rinchinov.ijdbtplugin)

![Alt text](https://s9.gifyu.com/images/SUcFi.md.gif "Usage")
# Table of contents
1. [Key features](#key-features)
2. [Quick start](#quick-start)
3. [Full Setup instructions](#full-setup-instructions)
4. [Configuration options reference](#configuration-options-reference)
5. [Query execution mechanism explanation](#query-execution-mechanism)
<!-- Plugin description -->
# DBT for idea IDEs

**Elevate Your Data Engineering Workflow**

The [DBT](https://docs.getdbt.com/docs/introduction) plugin for PyCharm revolutionizes the interaction with DBT projects for data engineers and analysts. This indispensable tool integrates advanced DBT navigation and documentation preview directly into your IDE.

## Key Features

- **Synchronized Navigation**: Navigate through DBT projects with enhanced capabilities. The plugin intelligently highlights corresponding entries in DBT documentation as you explore models, sources, macros and tests within PyCharm. This seamless integration streamlines workflows and enriches project comprehension.

- **Autocompletion**: Improve efficiency with the ability to write code with ability to autocomplete jinja macros and arguments for ref and source macros.

- **Copy/Paste Feature**: Improve efficiency with the ability to copy and paste queries, seamlessly replacing refs/sources for database operations.

- **Run selected query**: Improve efficiency with the ability to run query parts right inside the IDE.

- **Integrated DBT Documentation Preview**: Access DBT documentation without leaving PyCharm. A dedicated plugin window provides instant insights and information relevant to your current context, enriching your development experience with immediate access to your project's data documentation.

- **Annotations**: Annotations for macros, models, and sources

Leverage these features to transform your navigation and documentation review processes, boosting productivity and understanding of DBT projects.

<!-- Plugin description end -->

## Quick start

* Install the plugin
* Open some dbt project
* Make sure that in IDE [set python interpreter with dbt](#configure-python-sdk)
* If your `dbt_project.yml` not in root directory of project
  * set project file path in [plugins settings](#main-settings)
* If your profiles not in `~/.dbt`
  * set path to profiles directory in [plugins settings](#main-settings)

## Full Setup Instructions

### Prerequisites

Ensure you have a Python SDK configured in IDE with DBT installed. This setup is essential for the correct functioning of the plugin.

### Install the DBT Plugin

- **Install** the DBT plugin directly from the PyCharm Marketplace to get started.

### Configure Python SDK

1. **Navigate**: Go to `File` > `Project Structure` > `Project`.
2. **Set SDK**: Choose a Python interpreter where DBT is installed. If DBT is not installed, [install DBT](https://docs.getdbt.com/dbt-cli/installation) accordingly.

### Specify DBT Project Paths

- **DBT Project Settings**: Access `Preferences` > `DBT Project Settings` to set both the `DBT interpreter path` (your DBT project directory) and the `DBT profile path` (your DBT profile location).

### Open and Navigate Projects

- Open any DBT project in PyCharm to automatically unlock advanced navigation features, making every DBT component effortlessly accessible.

### Optional: Restart PyCharm/IntelliJ IDEA

- A restart may be necessary to fully integrate the new settings, optimizing your plugin experience.

## Configuration options reference

### Main settings
| Settings name             | Description                                                                                                                                                              | By default                         | Examples                                   |
|:--------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------|:-------------------------------------------|
| dbt_project.yml File Path | Path to dbt project **file**(dbt_project.yml). <br/>[DBT docs link](https://docs.getdbt.com/reference/dbt_project.yml)                                                   | dbt_project.yml                    |                                            |
| dbt Profile Directory     | Path to **directory** where dbt profile file is placed. <br/>[DBT docs link](https://docs.getdbt.com/docs/core/connect-data-platform/profiles.yml)                       | ~/.dbt                             |                                            |
| DBT runner import         | Import string, will be used to call dbt commands [see programmatic invocations](https://docs.getdbt.com/reference/programmatic-invocations)                              | from dbt.cli.main import dbtRunner | from custom_package import customDbtRunner |
| DBT interpreter path      | Python interpreter SDK where DBT is installed, if empty it takes current project's sdk                                                                                   |                                    |                                            |
| DBT environment variables | Environment variables that will be used for dbt calls                                                                                                                    |                                    | DB_PASS=secret1@;DB_USER=dbt               |
| Collect usage statistic   | Statistics help to analyze which features most used, which adapters and environments are most popular to prioritize plugin development. [privacy policy](PRIVACY_POLICY) | unchecked                          |                                            |

### Query execution settings

String templates are used for query formatting during execution, `%s` -- selected text. See section with query execution description

| Settings name             | Description                              | By default                  | 
|:--------------------------|:-----------------------------------------|:----------------------------|
| Query paginated template  | for _pagination_ to get paginated result | `%s LIMIT ? OFFSET ?`       |
| Query count template      | for _pagination_ to get count of pages   | `SELECT COUNT(*) FROM (%s)` |
| Query plan template       | for getting query plan                   | `EXPLAIN %s`                |
| Query dry run template    | for dry run query, if adapter supports   | `%s LIMIT 10`               |


## Query execution mechanism

Probably query execution is the most difficult and not obvious part of the plugin. It is adapter specific, so it can be different in different adapters.

Now there is two ways how to execute query
* With IDEA's database tools
* With plugins owns query execution logic

### How to use query execution with IDEA's database mechanism

1. Set up datasource to with name `{project name}__{target}`, e.x. `jaffle_shop__dev`, plugin will use it.
2. Select query in editor
3. Choose `Run Selected Query With DBT` and then desired target
4. Plugin should replace refs and sources and pass query text to IDEA database console

### How to use query execution with plugins owns query execution mechanism

1. If your adapter is `postgres` then set up datasource to with name `{project name}__{target}`, e.x. `jaffle_shop__dev`, plugin will use it.
2. Select query in editor
3. Choose `Run Selected Query With DBT` then choose
   * `Run query for {your desired target}` to run query with pagination
   * `Get query plan for {your desired target}` to get query plan
   * `Dry run query for {your desired target}` to dry run query
4. Plugin should replace refs and sources and show query execution results in plugins tool window

Note: It formats queries with query templates from [plugin's settings.](#query-execution-settings)

If your adapter is `postgres` then plugin takes JDBC connection from IDEA's database tools, and runs query with JDBC.

For the rest adapters it uses dbt some tricky logic to run query with dbt cli(it works much slower than JDBC).

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
