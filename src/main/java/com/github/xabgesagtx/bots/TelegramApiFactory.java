package com.github.xabgesagtx.bots;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

/**
 * Factory to create a telegram API.
 */
@Component
@ConditionalOnMissingBean(TelegramApiFactory.class)
@RequiredArgsConstructor
@Slf4j
class TelegramApiFactory {

    private final TelegramProperties properties;

    /**
     * Creates API without or with webhook support depending on properties set.
     * At least the internal URL needs to be set to have webhook support enabled.
     * @return api
     * @throws TelegramApiException when the API is misconfigured
     */
    TelegramBotsApi create() throws TelegramApiException {
        TelegramBotsApi result;
        if (properties.hasInternalUrl()) {
            log.info("Initializing API with webhook support");
            result = new TelegramBotsApi(DefaultBotSession.class, createDefaultWebhook());
        } else {
            log.info("Initializing API without webhook support");
            result = new TelegramBotsApi(DefaultBotSession.class);
        }
        return result;
    }

    private DefaultWebhook createDefaultWebhook() throws TelegramApiException {
        DefaultWebhook webhook = new DefaultWebhook();
        webhook.setInternalUrl(properties.getInternalUrl());
        if (properties.hasKeyStore()) {
            webhook.setKeyStore(properties.getKeyStore(), properties.getKeyStorePassword());
        }
        return webhook;
    }

}
