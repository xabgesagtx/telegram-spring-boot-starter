package org.github.xabgesagtx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="config")
public class MainConfig {

	private BotConfig bot;
	
	public BotConfig getBot() {
		return bot;
	}
	
	public void setBot(BotConfig bot) {
		this.bot = bot;
	}
	
}
