package org.github.xabgesagtx;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.github.xabgesagtx.bot.SimpleBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.BotSession;

@SpringBootApplication
public class TelegramBotStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotStarterApplication.class, args);
	}
	
	private final Logger logger = LoggerFactory.getLogger(TelegramBotStarterApplication.class);
	
	@Autowired
	private SimpleBot bot;
	
	private BotSession session;
	
	@PostConstruct
	public void start() {
		TelegramBotsApi api = new TelegramBotsApi();
		try {
			session = api.registerBot(bot);
		} catch (TelegramApiException e) {
			logger.error("Failed to register bot {} due to error {}: {}", bot.getBotUsername(), e.getMessage(), e.getApiResponse());
		}
	}
	
	@PreDestroy
	public void stop() {
		if (session != null) {
			session.close();
		}
	}
	
}
