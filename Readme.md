# Spring Boot Telegram Starter

This is a starter pom for a spring boot application.

You have to add the following dependency to the pom of your spring boot app

```xml
<dependency>
	<groupId>com.github.xabgesagtx</groupId>
	<artifactId>telegram-spring-boot-starter</artifactId>
	<version>0.1-SNAPSHOT</version>
</dependency>
```

The only thing you need to do is create a bean for a bot. E.g.:

```java
@Component
public class Bot extends TelegramLongPollingBot {
...
} 
```

The bot will be registered for you automatically on startup.


# Requirements

* Java 8
* Maven
 
