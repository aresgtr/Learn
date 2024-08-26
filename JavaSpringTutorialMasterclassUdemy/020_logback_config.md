# Logback Configuration

## Logback Initialization Steps

1. Logback tries to find a file called `logback-text.xml` in the classpath.

2. If no such file is found, logback tries to find a file called `logback.groovy` in the classpath.

3. If no such file is found, it checks for the file `logback.xml` in the classpath.

4. If no such file is found, service-provider loading facility (introduced in JDK 1.6) is used to resolve the implementation of a Configurator interface.

5. If none of the above succeeds, logback configures itself automatically using the BasicConfigurator which will cause logging output to be directed to the console.

The last step is meant as last-ditch effort to provide a default (but very basic) logging functionality in the absense of a configuration file.

## Loggers

- Loggers are objects with names. These names are case-sensitive and follow the hierarchical naming rule. When we say hierarchy, it means there can be parent and child relationship among loggers.

- The logback official documentation says:"A logger is said to be an ancestor of another logger if its name followed by a dot is a prefix of the descendant logger name. A logger is said to be a parent of a child logger if there are no ancestors between itself and the descendant logger."

- This type of naming should be familiar to developers since it follows the same pattern as that for a Java package name. For instance, the logger named "com.timbuchalka" is a parent of the logger named "com.timbuchalka.springdemo". Now "com.timbuchalka" is a parent of "com.timbuchalka.springdemo" but an ancestor of "com.timbuchalka.springdemo.logdemo".

- The "root logger" sits at the top of the logger hierarchy. Keep in mind the following...
    - The root logger cannot be retrieved by name as it can be done for other loggers.
    - The root logger always exists by default in a log4j system.
    - We don't really need to know much more about logging hierarchy right now.

## Pattern Conversion

|__PATTERN__|%date|[%thread]|[%-5level]|[%logger{40} -]|[%message]|%n|
|--|--|--|--|--|--|--|
|__EXAMPLE__|2017-12-12 13:51:20,066|[main]|[INFO]|academy.learnprogramming.HelloMaven -|Hello Info| |
| |2017-12-12 13:51:20,069|[main]|[DEBUG]|academy.learnprogramming.HelloMaven -|Hello Debug| |

See official document: [Conversion Word](https://logback.qos.ch/manual/layouts.html#conversionWord)