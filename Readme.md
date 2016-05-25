# Spring Boot Telegram Starter

This repository provides an example for a simple bot with the [Java Telegram Bot API](https://github.com/rubenlagus/TelegramBots) using Spring Boot-

It can be used to create your own bot without any further knowledge of Spring or Spring Boot.

To run the bot in eclipse just set these two environment variables which provide the token of your bot and its name:

```
config.bot.token
config.bot.name
```

If you want to run the bot from command line, you can run it with:

```bash
mvn spring-boot:run -Dconfig.bot.token=YOUR_TOKEN -Dconfig.bot.name=YOUR_BOT_NAME 
```

Alternatively, you can provide an application.yml file and the variables there.


# Requirements

* Java 8
* Maven
 