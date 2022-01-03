package com.github.xabgesagtx.bots;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBotStarter {

    private final List<BotSession> sessions = new ArrayList<>();
    private final List<LongPollingBot> pollingBots;
    private final List<TelegramWebhookBot> webHookBots;
    private final TelegramBotsApi api;
    private final SetWebhookBuilderFactory setWebhookFactory;

    @PostConstruct
    public void start() {
        log.info("Starting auto config for telegram bots");
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
                SetWebhook.SetWebhookBuilder webhookBuilder = setWebhookFactory.create();
                if (bot instanceof CustomizableTelegramWebhookBot) {
                    ((CustomizableTelegramWebhookBot) bot).customizeWebHook(webhookBuilder);
                }
                api.registerBot(bot, webhookBuilder.build());
            } catch (TelegramApiException e) {
                log.error("Failed to register bot {} due to error", bot.getBotUsername(), e);
            }
        });
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
