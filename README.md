# ij-dbt-plugin

![Build](https://github.com/rinchinov/ij-dbt-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.rinchinov.ijdbtplugin.svg)](https://plugins.jetbrains.com/plugin/com.github.rinchinov.ijdbtplugin)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.rinchinov.ijdbtplugin.svg)](https://plugins.jetbrains.com/plugin/com.github.rinchinov.ijdbtplugin)

![Alt text](https://s9.gifyu.com/images/SUcFi.md.gif "Usage")

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

## Prerequisites

Ensure you have a Python SDK configured in IDE with DBT installed. This setup is essential for the correct functioning of the plugin.

## Setup Instructions

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

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
