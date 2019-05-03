package com.github.xabgesagtx.bots;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * Auto configuration for telegram bots to automatically start bots that are configured as beans.
 */
@Configuration
@Slf4j
@ConditionalOnClass(TelegramBotsApi.class)
@RequiredArgsConstructor
@Import(TelegramProperties.class)
public class TelegramBotAutoConfiguration {

	private List<BotSession> sessions = new ArrayList<>();
	
	private final List<TelegramLongPollingBot> pollingBots;
	private final List<TelegramWebhookBot> webHookBots;
	private final TelegramProperties properties;

	static {
		ApiContextInitializer.init();
	}
	
	@PostConstruct
	public void start() throws TelegramApiRequestException {
		log.info("Starting auto config for telegram bots");
		TelegramBotsApi api = telegramBotsApi();
		pollingBots.forEach(bot -> {
			try {
				log.info("Registering polling bot: {}", bot.getBotUsername());
				sessions.add(api.registerBot(bot));
			} catch (TelegramApiException e) {
				log.error("Failed to register bot {} due to error", bot.getBotUsername(), e);
			}			
		});
		webHookBots.forEach(bot -> {
			try {
				log.info("Registering web hook bot: {}", bot.getBotUsername());
				api.registerBot(bot);
			} catch (TelegramApiException e) {
				log.error("Failed to register bot {} due to error", bot.getBotUsername(), e);
			}			
		});
	}

	/**
	 * Get API object depending on configured properties.
	 * If no properties are configured, the API object won't support webhooks.
	 * @return api object
	 * @throws TelegramApiRequestException in case the creation of the API object fails
	 */
	@Bean
	@ConditionalOnMissingBean
	public TelegramBotsApi telegramBotsApi() throws TelegramApiRequestException {
		TelegramBotsApi result;
		if (properties.hasKeyStoreWithPath()) {
			log.info("Initializing API with webhook support and configured keystore and path to certificate");
			result = new TelegramBotsApi(properties.getKeyStore(), properties.getKeyStorePassword(), properties.getExternalUrl(), properties.getInternalUrl(), properties.getPathToCertificate());
		} else if (properties.hasKeyStore()) {
			log.info("Initializing API with webhook support and configured keystore");
			result = new TelegramBotsApi(properties.getKeyStore(), properties.getKeyStorePassword(), properties.getExternalUrl(), properties.getInternalUrl());
		} else if (properties.hasUrls()) {
			log.info("Initializing API with webhook support");
			result = new TelegramBotsApi(properties.getExternalUrl(), properties.getInternalUrl());
		} else {
			log.info("Initializing API without webhook support");
			result = new TelegramBotsApi();
		}
		return result;
	}

	@PreDestroy
	public void stop() {
		sessions.forEach(session -> {
			if (session != null) {
				session.stop();
			}	
		});
	}
	
}
