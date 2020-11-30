package com.github.xabgesagtx.bots;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook.SetWebhookBuilder;
import org.telegram.telegrambots.meta.api.objects.InputFile;

/**
 * Factory to create webhook builders.
 */
@Component
@ConditionalOnMissingBean(SetWebhookBuilderFactory.class)
@RequiredArgsConstructor
class SetWebhookBuilderFactory {

    private final TelegramProperties properties;

    /**
     * Create webhook builder using properties to set fields to default values (external URL, certificate).
     * @return builder
     */
    SetWebhookBuilder create() {
        SetWebhook.SetWebhookBuilder builder = SetWebhook.builder();
        if (properties.hasExternalUrl()) {
            builder.url(properties.getExternalUrl());
        }
        if (properties.hasPathToCertificate()) {
            InputFile certificatePath = new InputFile(properties.getPathToCertificate());
            builder.certificate(certificatePath);
        }
        return builder;
    }
}
