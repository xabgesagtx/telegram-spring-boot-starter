package com.github.xabgesagtx.bots;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import static java.util.Objects.nonNull;

@Component
@ConfigurationProperties("telegram.proxy")
@Data
class TelegramProxyProperties {

    private DefaultBotOptions.ProxyType type;
    private String host;
    private Integer port;
    private String user;
    private String password;

    boolean hasProxy() {
        return nonNull(type) && !StringUtils.isEmpty(host) && nonNull(port);
    }

    boolean hasAuthData() {
        return !StringUtils.isEmpty(user) && !StringUtils.isEmpty(password);
    }
}
