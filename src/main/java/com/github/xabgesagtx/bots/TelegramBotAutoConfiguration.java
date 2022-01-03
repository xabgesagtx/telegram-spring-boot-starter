package com.github.xabgesagtx.bots;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Auto configuration for telegram bots to automatically start bots that are configured as beans.
 */
@Configuration
@Slf4j
@ConditionalOnClass(TelegramBotsApi.class)
@RequiredArgsConstructor
@Import({TelegramProperties.class, TelegramApiFactory.class, SetWebhookBuilderFactory.class, TelegramBotStarter.class})
public class TelegramBotAutoConfiguration {

    private final TelegramApiFactory apiFactory;

    /**
     * Get API object depending on configured properties.
     * If no properties are configured, the API object won't support webhooks.
     *
     * @return api object
     * @throws TelegramApiException in case the creation of the API object fails
     */
    @Bean
    @ConditionalOnMissingBean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return apiFactory.create();
    }

}
