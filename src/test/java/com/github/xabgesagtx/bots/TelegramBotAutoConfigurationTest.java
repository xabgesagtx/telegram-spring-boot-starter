package com.github.xabgesagtx.bots;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook.SetWebhookBuilder;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotAutoConfigurationTest {

    @Mock
    private static TelegramApiFactory apiFactory;

    @Mock
    private static SetWebhookBuilderFactory webhookBuilderFactory;

    @Mock
    private static TelegramLongPollingBot longPollingBot;

    @Mock
    private static TelegramWebhookBot webhookBot;

    @Mock
    private static CustomizableTelegramWebhookBot customizableWebhookBot;

    @Mock
    private TelegramBotsApi telegramBotsApi;

    @Mock
    private SetWebhook webhook;

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TelegramBotAutoConfiguration.class));

    @BeforeEach
    void setUp() throws TelegramApiException {
        when(apiFactory.create()).thenReturn(telegramBotsApi);
    }

    @Test
    @DisplayName("Test api gets created even when there are no bots")
    void noBotAtAll() {
        contextRunner.withUserConfiguration(TestNoBotConfiguration.class)
                .run(context -> {
                    context.getBean(TelegramBotsApi.class);
                    verify(telegramBotsApi, never()).registerBot(any());
                    verify(telegramBotsApi, never()).registerBot(any(), any());
                });
    }

    @Test
    @DisplayName("Api gets created and long polling bot is registered")
    void singleLongPollingBot() {
        contextRunner.withUserConfiguration(TestSingleLongPollingBotConfiguration.class)
                .run(context -> {
                    context.getBean(TelegramBotsApi.class);
                    verify(telegramBotsApi).registerBot(longPollingBot);
                    verify(telegramBotsApi, never()).registerBot(any(), any());
                });
    }

    @Test
    @DisplayName("Api gets created and simple web hook bot is registered")
    void simpleWebHookBot() {
        var webhookBuilder = mock(SetWebhookBuilder.class);
        when(webhookBuilderFactory.create()).thenReturn(webhookBuilder);
        when(webhookBuilder.build()).thenReturn(webhook);
        contextRunner.withUserConfiguration(TestSimpleWebhookBot.class)
                .run(context -> {
                    context.getBean(TelegramBotsApi.class);

                    verify(telegramBotsApi, never()).registerBot(any());
                    verify(telegramBotsApi).registerBot(webhookBot, webhook);
                });
    }

    @Test
    @DisplayName("Api gets created and customizable web hook bot is registered")
    void customizableWebHookBot() {
        var webhookBuilder = mock(SetWebhookBuilder.class);
        when(webhookBuilderFactory.create()).thenReturn(webhookBuilder);
        when(webhookBuilder.build()).thenReturn(webhook);
        contextRunner.withUserConfiguration(TestCustomizableWebhookBot.class)
                .run(context -> {
                    context.getBean(TelegramBotsApi.class);

                    verify(customizableWebhookBot).customizeWebHook(webhookBuilder);

                    verify(telegramBotsApi, never()).registerBot(any());
                    verify(telegramBotsApi).registerBot(customizableWebhookBot, webhook);
                });
    }

    @Configuration
    static class TestNoBotConfiguration extends BaseConfiguration {

    }

    @Configuration
    static class TestSingleLongPollingBotConfiguration extends BaseConfiguration {

        @Bean
        public TelegramLongPollingBot longPollingBot() {
            return longPollingBot;
        }

    }

    @Configuration
    static class TestSimpleWebhookBot extends BaseConfiguration {

        @Bean
        public TelegramWebhookBot webhookBot() {
            return webhookBot;
        }

    }

    @Configuration
    static class TestCustomizableWebhookBot extends BaseConfiguration {

        @Bean
        public CustomizableTelegramWebhookBot webhookBot() {
            return customizableWebhookBot;
        }

    }

    static class BaseConfiguration {

        @Bean
        public TelegramApiFactory apiFactory() {
            return apiFactory;
        }

        @Bean
        public SetWebhookBuilderFactory webhookBuilderFactory() {
            return webhookBuilderFactory;
        }

    }

}