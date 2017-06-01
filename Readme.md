# Telegram Spring Boot Starter

[![Build Status](https://travis-ci.org/xabgesagtx/telegram-spring-boot-starter.svg?branch=master)](https://travis-ci.org/xabgesagtx/telegram-spring-boot-starter) [![Jitpack](https://jitpack.io/v/xabgesagtx/telegram-spring-boot-starter.svg)](https://jitpack.io/#xabgesagtx/telegram-spring-boot-starter)

This is a starter pom for a spring boot application with the [TelegramBots Java API](https://github.com/rubenlagus/TelegramBots).

First, you need to add jitpack as a repository to your pom of your spring boot app

```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

Then you have to add the following dependency to the pom

```xml
<dependency>
	<groupId>com.github.xabgesagtx</groupId>
	<artifactId>telegram-spring-boot-starter</artifactId>
	<version>0.6</version>
</dependency>
```

The only thing you need to do now is to create a bean for a bot. E.g.:

```java
@Component
public class Bot extends TelegramLongPollingBot {
...
} 
```

The bot will then be registered for you automatically on startup.
 

# Example

An [implementation example](https://github.com/xabgesagtx/telegram-spring-boot-starter-example) is available too.
