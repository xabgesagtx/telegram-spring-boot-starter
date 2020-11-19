package com.github.xabgesagtx.bots;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import javax.validation.constraints.NotNull;

public abstract class TelegramWebhookBotService extends TelegramWebhookBot {
	/**
	 * Override this to configure the webhook for a single service here.
	 * A set of configurations like "certificate" and "external Url" will be preconfigured
	 *
	 * @param webhook never null
	 * @return the modified webhook
	 */
	public SetWebhook.SetWebhookBuilder getInitializingWebhookRequest(@NotNull SetWebhook.SetWebhookBuilder webhook) {
		return webhook;
	}

}
