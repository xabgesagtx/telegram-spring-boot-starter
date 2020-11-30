package com.github.xabgesagtx.bots;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ConfigurationProperties("telegram")
@Data
class TelegramProperties {
    private String externalUrl;
    private String internalUrl;
    private String keyStore;
    private String keyStorePassword;
    private String pathToCertificate;

    boolean hasKeyStore() {
        return !StringUtils.isEmpty(keyStore) && !StringUtils.isEmpty(keyStorePassword);
    }

    boolean hasInternalUrl() {
        return StringUtils.hasText(internalUrl);
    }

    public boolean hasPathToCertificate() {
        return StringUtils.hasText(pathToCertificate);
    }

    public boolean hasExternalUrl() {
        return StringUtils.hasText(externalUrl);
    }
}
