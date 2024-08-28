# Java Spring Masterclass - Learn Spring Framework 5

## Section 1: Course Introduction

## Section 2: Install and Setup

## Section 3: NEW Spring 5 - Maven and your first project

[14. Creating a Maven Project](014_hello-maven/)
- Created [pom.xml](014_hello-maven/pom.xml) and [HelloMaven.java](014_hello-maven/src/main/java/academy/learnprogramming/HelloMaven.java)

15\. Importing Maven Projects

- In this lesson we closed the project, removed, and re-imported by IntelliJ

[16. Maven Lifecycle Plugin and Goals](016_maven-lifecycle.md)

[17. Fix our Maven Project](014_hello-maven/pom.xml)

- We updated pom by specifying the `maven-compiler-plugin`

## Section 4: New Spring 5 - Logging with SLF4J & Logback

[18. What is Logging?](018_logging.md)

19\. Using Logging with Logback
- Add Logback to [pom.xml](014_hello-maven/pom.xml)
- Create [class](014_hello-maven/src/main/java/academy/learnprogramming/L19Logback.java) for demonstration

20\. Logback Configuration
- [Notes](020_logback_config.md)
- Create [logback.xml](014_hello-maven/src/main/resources/logback.xml)

## Section 5: New Spring 5 - Multi module Spring Project

22\. Create a Multi Module Project
- Create a new project `022-guess-the-number-game`
    - Remove src in the main module, since codes will be in sub-modules
    - Specify ```<packaging>pom</packaging>``` in the main module's [pom.xml](022-guess-the-number-game/pom.xml)
    - Create `core` module as sub-module
    - Note that Intellij automatically added the hierarchy in the main module's pom as well as sub-module (core)'s [pom.xml](022-guess-the-number-game/core/pom.xml)

23\. Project Setup
- Update pom.xml in both main module & sub-module
- Create [Main](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/Main.java) class in the core module to see if everything is working

24\. Using a Spring Container
- [IOC Container Documentation](https://docs.spring.io/spring-framework/reference/core/beans.html)
    - Following the doc's container overview, we copy and create [beans.xml](022-guess-the-number-game/core/src/main/resources/beans.xml)
    - We add `<bean id="numberGenerator" ...` based on later steps
- Create NumberGenerator [Interface](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/NumberGenerator.java) and [Implementation](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/NumberGeneratorImpl.java)
- Update [Main](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/Main.java) class to test the container
- Optional: We can see more detailed logging inside the container by editing sub-module's [logback.xml](022-guess-the-number-game/core/src/main/resources/logback.xml)

25\. Implementing the Game
- Create [Game Interface](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/Game.java)
- Create [Game Implementation](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/GameImpl.java)

26\. Constructor Based Dependency Injection
- Update [Main](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/Main.java) to include the game
- Add constructor to [GameImpl](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/GameImpl.java) using _Constructor Based Dependency Injection_
- Add the game to [beans.xml](022-guess-the-number-game/core/src/main/resources/beans.xml)

27\. Setter Based Dependency Injection
- We updated GameImpl and beans.xml

## Section 6: New Spring 5 - Lombok Introduction

## Section 7: New Spring 5 - Spring MVC

## Section 8: New Spring 5 - Spring Boot 2 Introduction

## Section 9: New Spring 5 - Spring Boot 2 And Thymeleaf 3

## Section 10: New Spring 5 - Gradle Introduction

## Section 11: New Spring 5 - Gradle Multi Module Project Setup