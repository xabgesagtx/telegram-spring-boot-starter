package com.github.xabgesagtx.bots;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@Configuration
@RequiredArgsConstructor
@Import({TelegramProxyProperties.class,TelegramProperties.class})
public class TelegramBotOptionsAutoConfiguration {

    private final TelegramProxyProperties properties;
    private final TelegramProperties plainProperties;

    @ConditionalOnProperty(value = {"host", "port", "type"}, prefix = "telegram.proxy")
    @Bean
    public DefaultBotOptions defaultBotOptions() {
        if (properties.hasAuthData()) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(properties.getUser(), properties.getPassword().toCharArray());
                }
            });
        }
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setProxyHost(properties.getHost());
        botOptions.setProxyPort(properties.getPort());
        botOptions.setProxyType(properties.getType());
        addLocalBotUrlToOption(botOptions);
        return botOptions;
    }

    @Bean
    @ConditionalOnMissingBean(DefaultBotOptions.class)
    public DefaultBotOptions localBotApiOptions(){
        DefaultBotOptions botOptions = new DefaultBotOptions();
        addLocalBotUrlToOption(botOptions);
        return botOptions;
    }

    private void addLocalBotUrlToOption(DefaultBotOptions botOptions)
    {
        if(plainProperties.hasLocalBotUrl())
        {
            botOptions.setBaseUrl(plainProperties.getLocalBotUrl());
        }
    }


}
