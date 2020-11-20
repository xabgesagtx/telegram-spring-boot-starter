package com.github.xabgesagtx.bots;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

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

	private final List<LongPollingBot> pollingBots;
	private final List<TelegramWebhookBotService> webHookBots;
	private final TelegramProperties properties;
	private SetWebhook.SetWebhookBuilder webhookBuilder;

	@PostConstruct
	public void start() throws TelegramApiException {
		log.info("Starting auto config for telegram bots");
		webhookBuilder = SetWebhook.builder();
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
				SetWebhook webhook = bot.customizeWebHook(webhookBuilder).build();
				api.registerBot(bot, webhook);
			} catch (TelegramApiException e) {
				log.error("Failed to register bot {} due to error", bot.getBotUsername(), e);
			}
		});
	}

	/**
	 * Get API object depending on configured properties.
	 * If no properties are configured, the API object won't support webhooks.
	 *
	 * @return api object
	 * @throws TelegramApiRequestException in case the creation of the API object fails
	 */
	@Bean
	@ConditionalOnMissingBean
	public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
		TelegramBotsApi result;
		DefaultWebhook webhook = getWebhookConfig(properties);
		if (properties.hasKeyStoreWithPath()) {
			log.info("Initializing API with webhook support and configured keystore and path to certificate");
			webhookBuilder.certificate(new InputFile(properties.getPathToCertificate()));
			webhookBuilder.url(properties.getExternalUrl());
			result = new TelegramBotsApi(DefaultBotSession.class, webhook);
		} else if (properties.hasKeyStore()) {
			log.info("Initializing API with webhook support and configured keystore");
			webhookBuilder.url(properties.getExternalUrl());
			result = new TelegramBotsApi(DefaultBotSession.class, webhook);
		} else if (properties.hasUrls()) {
			log.info("Initializing API with webhook support");
			webhookBuilder.url(properties.getExternalUrl());
			result = new TelegramBotsApi(DefaultBotSession.class, webhook);
		} else {
			log.info("Initializing API without webhook support");
			result = new TelegramBotsApi(DefaultBotSession.class);
		}
		return result;
	}

	private DefaultWebhook getWebhookConfig(TelegramProperties properties) throws TelegramApiException {
		DefaultWebhook webhook = new DefaultWebhook();
		if (properties.hasKeyStoreWithPath()) {
			webhook.setKeyStore(properties.getKeyStore(), properties.getKeyStorePassword());
		}
		webhook.setInternalUrl(properties.getInternalUrl());
		return webhook;
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
