# ij-dbt-plugin

![Build](https://github.com/rinchinov/ij-dbt-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.rinchinov.ijdbtplugin.svg)](https://plugins.jetbrains.com/plugin/com.github.rinchinov.ijdbtplugin)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.rinchinov.ijdbtplugin.svg)](https://plugins.jetbrains.com/plugin/com.github.rinchinov.ijdbtplugin)

<!-- Plugin description -->
# DBT for PyCharm

**Elevate Your Data Engineering Workflow**

The [DBT](https://docs.getdbt.com/docs/introduction) plugin for PyCharm revolutionizes the interaction with DBT projects for data engineers and analysts. This indispensable tool integrates advanced DBT navigation and documentation preview directly into your IDE.

## Key Features

- **Synchronized Navigation**: Navigate through DBT projects with enhanced capabilities. The plugin intelligently highlights corresponding entries in DBT documentation as you explore models, sources, and tests within PyCharm. This seamless integration streamlines workflows and enriches project comprehension.

- **Integrated DBT Documentation Preview**: Access DBT documentation without leaving PyCharm. A dedicated plugin window provides instant insights and information relevant to your current context, enriching your development experience with immediate access to your project's data documentation.

- **Copy/Paste Feature**: Improve efficiency with the ability to copy and paste queries, seamlessly replacing refs/sources for database operations.

Leverage these features to transform your navigation and documentation review processes, boosting productivity and understanding of DBT projects.

## Prerequisites

Ensure you have a Python SDK configured in PyCharm with DBT installed. This setup is essential for the correct functioning of the plugin.

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

### Configure Jinja2 for `.sql` and `.yml` Files

1. **Access Settings**: Go to `File` > `Settings` > `Languages & Frameworks` > `Template Languages`.
2. **Select Template Language**: Choose `Jinja2` and include `sql` and `yml` in `Template File Types`.

### Optional: Restart PyCharm/IntelliJ IDEA

- A restart may be necessary to fully integrate the new settings, optimizing your plugin experience.

## Getting Started

With the Python SDK and DBT project path configured, you're ready to explore the full capabilities of the DBT plugin.

Dive into an enhanced DBT project experience with the DBT plugin for PyCharm, transforming how you navigate and understand DBT projects.
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "ij-dbt-plugin"</kbd> >
  <kbd>Install</kbd>
  
- Manually:

  Download the [latest release](https://github.com/rinchinov/ij-dbt-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation

1. plugin initialization -- need to check dbt proj file and only then activates all services
2. test all possible scenarios
3. test cases/test plan?
4. update readme and release notes
5. docs generate freezes ui -- probably reload and etc also
6. issue with plugin


1. listener to lookup dbt_project file
  1. read configs
  2. if jinja render specific jinja
  3. lookup profile
    4. lookup targets
    5. default target
    6. if jinja render specific jinja
  7. init tool windows
  8. parse manifest
  9. register Referense contributors
