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

[28. Setter or Constructor](028_setter_or_constructor.md)

29\. Using Bean Lifecycle Callbacks
- We are using `@PostConstruct` and `@PreDestroy` annotations <= best practice for receiving lifecycle callbacks in a Spring application
- Add Annotation API to the root [pom.xml](022-guess-the-number-game/pom.xml) and Core's [pom.xml](022-guess-the-number-game/core/pom.xml)
- Add `init-method` to [beans.xml](022-guess-the-number-game/core/src/main/resources/beans.xml) to initialize container automatically
- Remove `reset()` in [Main](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/Main.java) since it will initialize automatically
- Add annotations to [GameImpl](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/GameImpl.java)

30\. XML or Annotation Configuration
- T.B.D.
- Udemy [link](https://www.udemy.com/course/java-spring-framework-masterclass/learn/lecture/10336420)

31\. Autowiring Beans
- Update [beans.xml](/022-guess-the-number-game/core/src/main/resources/beans.xml)
    - Add `<context:annotation-config/>`
        - Note we update the `<beans xmlns:context... xsi:schemaLocation...` and remove `CommonAnnotationBeanPostProcessor`
        - This tag activates the Spring infrastructure for various annotations
            - e.g. `@Autowire`, `@PostConstruct`, `@PreDestroy`, etc.
- Update [GameImpl](022-guess-the-number-game/core/src/main/java/academy/learnprogramming/GameImpl.java)
    - Remove DI
    - Add `@Autowired` annotation
- By using Autowiring, we don't need a constructor or setter and we don't need the new keyword, since everything is managed by the Spring container

32\. Beans as Components

_We create a new snapshot of the project [032-guess-the-number-game](032-guess-the-number-game/) since there are major changes_
- Update [beans.xml](032-guess-the-number-game/core/src/main/resources/beans.xml) to enable component scanning
- Add `@Component` annotation to [GameImpl](032-guess-the-number-game/core/src/main/java/academy/learnprogramming/GameImpl.java) and [NumberGeneratorImpl](/032-guess-the-number-game/core/src/main/java/academy/learnprogramming/NumberGeneratorImpl.java)
- Remove the bean name in [Main](032-guess-the-number-game/core/src/main/java/academy/learnprogramming/Main.java) in `context.getBean(...)` because the bean name is now auto-assigned
    - Some workarounds: we can manually assign bean name in the `@Component("name")` annotation, but too many hard-coded strings can be problematic. We will see better approaches later.

33\. Using Java Annotation Configuration

_We create a new snapshot of the project [033-guess-the-number-game](033-guess-the-number-game/) since there are major changes_
- Remove beans.xml
- Create [AppConfig.java](033-guess-the-number-game/core/src/main/java/academy/learnprogramming/AppConfig.java)
    - ```java
        @Configuration
        @ComponentScan(basePackages = "academy.learnprogramming")
        public class AppConfig { ...
      ```
    - ⬆️ Note the annotation of the class is equivalent to the previous [beans.xml](032-guess-the-number-game/core/src/main/resources/beans.xml) in Lesson 32
    - Add `@Bean` annotations so we no longer need `@Component` annotations in [GameImpl](033-guess-the-number-game/core/src/main/java/academy/learnprogramming/GameImpl.java) and [NumberGeneratorImpl](033-guess-the-number-game/core/src/main/java/academy/learnprogramming/NumberGeneratorImpl.java)

34\. Message Generator Challenge
- Implement [MessageGenerator](033-guess-the-number-game/core/src/main/java/academy/learnprogramming/MessageGenerator.java) Interface
- Implement [MessageGeneratorImpl](033-guess-the-number-game/core/src/main/java/academy/learnprogramming/MessageGeneratorImpl.java)
- Add to [AppConfig.java](033-guess-the-number-game/core/src/main/java/academy/learnprogramming/AppConfig.java)
- Add to [Main](033-guess-the-number-game/core/src/main/java/academy/learnprogramming/Main.java)

## Section 6: New Spring 5 - Lombok Introduction

## Section 7: New Spring 5 - Spring MVC

## Section 8: New Spring 5 - Spring Boot 2 Introduction

## Section 9: New Spring 5 - Spring Boot 2 And Thymeleaf 3

## Section 10: New Spring 5 - Gradle Introduction

## Section 11: New Spring 5 - Gradle Multi Module Project Setup