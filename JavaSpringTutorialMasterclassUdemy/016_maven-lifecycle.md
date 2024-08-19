# Maven Lifecycle, Plugins, and Goals

- Maven Lifecycle.
- Maven Plugins.
- Maven Goals.

## Maven Build Lifecycle
- Maven is based around the central concept of a build lifecycle. It means that the process for building and distributing a particular artifact(project) is clearly defined. For the person building a project, this means that it is only necessary to learn a small set of commands to build any Maven project, and the POM will ensure they get the results they desire.
- There are three build-in build lifecycles: default, clean and site.
  - default - handles your project deployment
  - clean - handles project cleaning
  - site - handles the creation of your project's site documentation
- Each of these build cycles is defined by a different list of build phases
- A build phase represents a stage in the lifecycle

## The Maven Default Lifecycle

The default lifecycle comprises of the following main phases (not all are listed here - just the main ones)

| | |
| -- | -- |
| validate | Validates the project is correct and all necessary information is available |
| compile | Compiles the source code of the project |
| test | Tests the compies source code using a unit testing framework (JUnit for example) |
| package | Packages code in distributable format (e.g. JAR) |
| verify | Run any checks on the results of integration tests |
| install | Install the package to the local repository for use in other projects locally |
| deploy | Copies package to remote repository for sharing with other developers and projects (Done in the build environment) |

## Maven Plugins
- "Maven" is really just a core framework of a collection of Maven Plugins.
- In other words, plugins are where most of the real action occurs, plugins are used to: create jar files, create war files, compile code, unit test code, create project documentation, and so on.
- Almost any action that you can think of performing on a project is implemented as a Maven plugin.
- In Maven, there aare build and reporting plugins:
    - __Build__ plugins will be executed during the build, they should be configured in the <build/\> element.
    - __Reporting__ plugins will be executed during the site generation and they should be configured in the <reporting/\> element.

## Maven Plugins and Goals
- All plugins should have minimal required information: groupId, artifactId, and version.
- Whenever you want to customize the build for a Maven project, this is done by adding or re-configuring plugins.
- Plugins are artifacts that provide Goals to Maven.
- A plugin may have one or more Goals where each Goal represents a capability of that plugin.
    - For example, the Compiler plugin has 2 goals: compile and testCompile
        - compile - compiles the source code fo your main code
        - testCompile - compiles the source of of your test code

## Lifecycle Reference
https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference