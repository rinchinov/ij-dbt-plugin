# ij-dbt-plugin

![Build](https://github.com/rinchinov/ij-dbt-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.rinchinov.ijdbtplugin.svg)](https://plugins.jetbrains.com/plugin/com.github.rinchinov.ijdbtplugin)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.rinchinov.ijdbtplugin.svg)](https://plugins.jetbrains.com/plugin/com.github.rinchinov.ijdbtplugin)

<!-- Plugin description -->
# DBT for PyCharm

**Advanced dbt Navigation with Integrated Documentation Preview**

The DBT plugin for PyCharm revolutionizes how you navigate and understand your dbt projects, making it an indispensable tool for data engineers and analysts. With our latest update, navigating through the intricate structure of dbt projects has never been more intuitive or efficient.

## Key Features

- **Synchronized Navigation:** Dive deeper into your dbt project with enhanced navigation capabilities that intelligently sync with your project files. As you explore models, sources, and tests within PyCharm, the plugin dynamically highlights corresponding entries in the dbt documentation. This seamless integration between code and documentation not only streamlines your workflow but also enriches your project comprehension, facilitating effortless navigation through the complexities of dbt projects.

- **Integrated dbt Docs Preview:** Complementing the advanced navigation, the DBT plugin now includes an integrated dbt Docs preview feature. Without leaving the comfort of your IDE, easily access and interact with your project's dbt documentation in a dedicated plugin window. This feature ensures that your project's data documentation is always just a click away, providing you with instant insights and information relevant to your current work context.

Leverage the DBT plugin for PyCharm to transform your dbt project navigation and documentation review process. Experience a harmonious blend of development and documentation that elevates your productivity and project understanding to unprecedented levels.

## Prerequisites:

Before using the DBT, ensure you have a Python SDK configured in PyCharm with dbt installed. This is essential for the plugin to function correctly.

## Setup Instructions:

1. **Install** the DBT directly from the PyCharm Marketplace.
2. **Configure Python SDK:**
    - Go to `File` > `Project Structure` > `Project`.
    - Set the Project SDK to a Python interpreter where dbt is installed.
    - If dbt is not installed in any Python interpreter, [install dbt](https://docs.getdbt.com/dbt-cli/installation) in your desired environment.
3. **Specify dbt Project Path:**
    - In PyCharm, navigate to `Preferences` > `DBT Project Settings`.
        - For `DBT interpreter path`, enter the path to your dbt project directory.
        - For `DBT profile path`, enter the path to your dbt profile location.
4. **Open** any dbt project in PyCharm to automatically activate advanced navigation features.
5. **Navigate** your dbt project effortlessly, with all your dbt components just a click away.
## Setup Instructions for dbt Plugin in PyCharm/IntelliJ IDEA

Follow these steps to install and set up the dbt plugin and configure Jinja2 as the template language for `.sql` and `.yml` files.

### Install the dbt Plugin

1. **Install** the dbt plugin directly from the PyCharm Marketplace.

### Configure Python SDK

2. **Navigate to Project Settings:**
    - Go to `File` > `Project Structure` > `Project`.

3. **Set Project SDK:**
    - Set the Project SDK to a Python interpreter where dbt is installed.
    - If dbt is not installed in any Python interpreter, [install dbt](https://docs.getdbt.com/dbt-cli/installation) in your desired environment.

### Specify dbt Project Paths

4. **DBT Project Settings:**
    - Navigate to `Preferences` > `DBT Project Settings`.
        - For `DBT interpreter path`, enter the path to your dbt project directory.
        - For `DBT profile path`, enter the path to your dbt profile location.

5. **Open and Navigate Projects:**
    - Open any dbt project in PyCharm to activate advanced navigation features.
    - Effortlessly navigate your dbt project, with all components just a click away.

### Configure Jinja2 for `.sql` and `.yml` Files

6. **Open Template Languages Settings:**
    - Open `Preferences` (`Settings` on Windows/Linux) by navigating to `File` > `Settings` (or `PyCharm` > `Preferences` on macOS), then go to `Languages & Frameworks` > `Template Languages`.

7. **Set Template Language Directory:**
    - In the `Template Language` dropdown, select `Jinja2` to apply it as the template language.
    - Add `sql` and `yml` to `Template File Types` via clicking `+` button. 

8. **Apply and Close:**
    - Click `Apply`, then `OK` to save your changes and close the settings dialog.

### Restart PyCharm/IntelliJ IDEA (Optional)

9. **Restart Your IDE (Optional):**
    - It may be necessary to restart your IDE to ensure that the new settings are fully applied.

## How to Get Started:

After setting up the Python SDK with dbt installed and specifying your dbt project path, you're ready to take full advantage of the DBT's capabilities.

Elevate your dbt project experience in PyCharm with DBT. Install now and transform the way you work with dbt.
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
