# ij-dbt-plugin

![Build](https://github.com/rinchinov/ij-dbt-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [x] Get familiar with the [template documentation][template].
- [x] Adjust the [pluginGroup](./gradle.properties), [plugin ID](./src/main/resources/META-INF/plugin.xml) and [sources package](./src/main/kotlin).
- [x] Adjust the plugin description in `README` (see [Tips][docs:plugin-description])
- [x] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html?from=IJPluginTemplate).
- [x] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the `PLUGIN_ID` in the above README badges.
- [ ] Set the [Plugin Signing](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html?from=IJPluginTemplate) related [secrets](https://github.com/JetBrains/intellij-platform-plugin-template#environment-variables).
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html?from=IJPluginTemplate).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.

<!-- Plugin description -->
# DBT Navigator for PyCharm

**Navigate Your dbt Projects with Unprecedented Ease**

The DBT Navigator is a specialized PyCharm plugin designed exclusively for data engineers and analysts working with dbt. It simplifies navigating through the intricate structure of dbt projects, allowing you to focus on what truly matters - your data.

## Key Feature:

- **Advanced dbt Navigation:** Instantly jump to the definitions and references of macros, models, sources, and seeds within your dbt project. Our intuitive navigation system eliminates the hassle of manually searching through files, making your workflow seamless and efficient.

## Prerequisites:

Before using the DBT Navigator, ensure you have a Python SDK configured in PyCharm with dbt installed. This is essential for the plugin to function correctly.

## Setup Instructions:

1. **Install** the DBT Navigator directly from the PyCharm Marketplace.
2. **Configure Python SDK:**
   - Go to `File` > `Project Structure` > `Project`.
   - Set the Project SDK to a Python interpreter where dbt is installed.
   - If dbt is not installed in any Python interpreter, [install dbt](https://docs.getdbt.com/dbt-cli/installation) in your desired environment.
3. **Specify dbt Project Path:**
   - In PyCharm, navigate to `Preferences` > `DBT Project Settings` > 
     - Navigate to `DBT interpreter path`.
       - Enter the path to your dbt project directory.
     - Navigate to `DBT profile path`.
       - Enter the path to your dbt profile location.
4. **Open** any dbt project in PyCharm to automatically activate advanced navigation features.
5. **Navigate** your dbt project effortlessly, with all your dbt components just a click away.

## How to Get Started:

After setting up the Python SDK with dbt installed and specifying your dbt project path, you're ready to take full advantage of the DBT Navigator's capabilities.

Elevate your dbt project experience in PyCharm with DBT Navigator. Install now and transform the way you work with dbt.
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
