# What is Logging?
- Logging is keeping a record of specific information from a programs execution.
- Logging is one of the most powerful tools in a programmer's toolbox.
- Logging gives you data showing what your code is doing and allows you to rapidly develop and debug software. Approprite logging helps answer nearly any question about what a program is doing.
- There are 2 aspects related to increasing visibility into programs you run:
    - visibility into what your code is doing after it's running and people are using the software
    - visibility into your code during development
    
    Visibility into the behavior of shipped code is equally important as visibility into code behavior during develoment.

- With good logging in place, it's easier for other developers to see exactly what your code is doing.
- The first and foremost advantage of any logging API over plain `System.out.println`'s resides in its ability of filter. You can choose what to show and what not to show.
- The great thing about logging is that it is possible to categorize logging statements according to specific developer-chosen criteria.
- Logging will also help you see how Spring handles everything internally. In other words how the spring container starts up, etc.

## Logging Use Cases
- Debugging software during development
- Help to diagnose bugs during production - when your application is live
- Tracing access for security purposes
- Creating data for statistical use
- Whatever the use case is, logs should be __detailed, configurable and reliable__.

> ## History (Not so important 🍿)
> - Historically, Java logging was done with `System.out.println()`, `System.err.println()` or `e.printStackTrace()` statements.
> - Debug logs were shown in `System.out` and error logs in `System.err`
> - In production, both were redirected:
>   - `System.out` was usually discarded
>   - `System.err` usually sent to an error log file
> - Log records before the advent of modern logging were useful enough but suffered from one big drawback: a lack of configurability. The choice was simple. Log or don't log.

## Slf4j - The Logging API
- There are 2 aspects to a well configured logging system:
    - Logging API - what will be used at the application layer directly
        - In other words, what you will code.
        - Is the facade or interface that a developer interact with.
    - Logging Implementation - what your Logging API will use internally and make calls to get logging working.
- There are a lot of benefits to use slf4j library.
- You can think of slf4j as an Java interface or facade that would require an implementation (ONLY ONE) at runtime to provide that actual logging details, such as writing to STDOUT (console) or to a file etc. In other words, not a lot of work for the developer to get up and working.

## Logging Implementation - Using logback
- Each logging implementation (also called a binding) has their own way of configuring the log output, but your application will remain agnostic and always use the same org.slf4j.Logger API
- Logback is intended as a successor to the popular log4j project, picking up where log4j left off
- Logback is a reliable, fast and flexible logging implemtation written in Java
- Logback is highly configutable through external configuration files at runtime. It views the logging process in terms of levels of priorities and offers mechanisms to direct logging information to a great variety of destinations including a database, file, console, email, etc.
- Logback is built using Maven
- Logback has 3 main components:
    1. loggers: responsible for capturing logging information
    2. appenders: responsible for publishing logging information to various preferred destinations
    3. layouts: responsible for formatting logging information in differnt styles
- Logback also has filters: based on ternary logic allowing them to be assebled or chained together to compose an arbitrarily complex filtering policy. They are largely inspired by Linuxs iptables.

## Advantages of logback
- Logging behavior can be set at runtime using a configuration file.
- It uses multiple levels, namely TRACE, DEBUG, INFO, WARN, ERROR which controls the granularity, severity and verbosity of information.
- The format of the log output can be easily changed by implementing the Layout interface.
- The target or destination of log output as well as the writing strategy can be modified by switching implementations of the Appender interface.
- Logback can automatically reload its configuration file upon modification.
- Logback supports sml and groovy configurations.

## Why logback and not log4j
- Logback continues where log4j leaves off.
- Large number of improvements over log4j.
- Both logback and log4j were developed by the same author.
- Logback has a faster implementation.
- Logback Logger class implements slf4j API (zero overhead).
- Logback has extensive documentation.
- Configuration can be done via XML or groovy.
- Automatic reloading of configuration files.
- Automatic removal of old archives.
- Filters.
- And much more.