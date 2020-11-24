package com.github.xabgesagtx.bots;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

/**
 * This class is a blueprint to generate a Webhook Bot.
 */
public abstract class TelegramWebhookBotService extends TelegramWebhookBot {

    public TelegramWebhookBotService() {
        super();
    }

    public TelegramWebhookBotService(DefaultBotOptions options) {
        super(options);
    }

    /**
     * Override this to configure the webhook for a single service here.
     * A set of configurations like "certificate" and "external Url" will be preconfigured
     *
     * @param webhook never null
     * @return the modified webhook
     */
    public SetWebhook.SetWebhookBuilder customizeWebHook(SetWebhook.SetWebhookBuilder webhook) {
        return webhook;
    }

}