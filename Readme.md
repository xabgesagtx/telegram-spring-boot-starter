# Telegram Spring Boot Starter

[![Build Status](https://travis-ci.org/xabgesagtx/telegram-spring-boot-starter.svg?branch=master)](https://travis-ci.org/xabgesagtx/telegram-spring-boot-starter) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.xabgesagtx/telegram-spring-boot-starter/badge.svg)](https://mvnrepository.com/artifact/com.github.xabgesagtx/telegram-spring-boot-starter) [![Jitpack](https://jitpack.io/v/xabgesagtx/telegram-spring-boot-starter.svg)](https://jitpack.io/#xabgesagtx/telegram-spring-boot-starter)

This is a starter pom for a spring boot application with the [TelegramBots Java API](https://github.com/rubenlagus/TelegramBots).


The starter is available at maven central. Just add the following dependency to your pom:

```xml
<dependency>
	<groupId>com.github.xabgesagtx</groupId>
	<artifactId>telegram-spring-boot-starter</artifactId>
	<version>0.22</version>
</dependency>
```

For gradle users just add this to your dependencies:
```groovy
compile('com.github.xabgesagtx:telegram-spring-boot-starter:0.22')
```

The only thing you need to do after adding the dependency is to create a bean for a bot. E.g.:

```java
@Component
public class Bot extends TelegramLongPollingBot {
...
} 
```

or for a webhook bot:

```java
@Component
public class Bot extends TelegramWebhookBotService {
... 
}
```

The bot will then be registered for you automatically on startup.

## Configuration
 
The following properties can be configured (none are mandatory):

| property | description | available since |
| -------- | ----------- | --------------- |
| telegram.external-url | external base url for the webhook | 0.15 |
| telegram.internal-url | internal base url for the webhook | 0.15 |
| telegram.key-store | keystore for the server | 0.15 |
| telegram.key-store-password | keystore password for the server | 0.15 |
| telegram.path-to-certificate | full path for .pem public certificate keys | 0.15 |
| telegram.proxy.type | type of proxy (NO_PROXY, HTTP, SOCKS4, SOCKS5) | 0.22 |
| telegram.proxy.host | host of the proxy | 0.22 |
| telegram.proxy.port | port of the proxy | 0.22 |
| telegram.proxy.user | username for proxy authentication | 0.22 |
| telegram.proxy.password | password for proxy authentication | 0.22 |

### Webhook support

You need to configure at least `telegram.external-url` and `telegram.internal-url` for webhook support. This configuration leaves the HTTPS handling to a proxy.

If you configure `telegram.key-store` for your HTTPS webhook bot, you need to configure `telegram.key-store-password` as well.

Also, `telegram.path-to-certificate` will only be used, if you configure the keystore.

### Proxy support

For proxy support you need to set all of the following properties: 
* `telegram.proxy.type`
* `telegram.proxy.host`
* `telegram.proxy.port`

To enable authentication for a proxy your need to set `telegram.proxy.user` and `telegram.proxy.password`.

### General

In a regular spring boot setting these properties can be set in your `application.properties` or `application.yml`.

For more information on how these configuration options work, please refer to the [TelegramBots Java API](https://github.com/rubenlagus/TelegramBots).

## Example

An [implementation example](https://github.com/xabgesagtx/telegram-spring-boot-starter-example) is available too.
