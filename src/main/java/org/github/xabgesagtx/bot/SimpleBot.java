package org.github.xabgesagtx.bot;

import org.github.xabgesagtx.config.MainConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * 
 * A simple echo bot
 *
 */
@Component
public class SimpleBot extends TelegramLongPollingBot {
	
	private final Logger logger = LoggerFactory.getLogger(SimpleBot.class);
	
	@Autowired
	private MainConfig config;

	@Override
	public String getBotUsername() {
		return config.getBot().getName();
	}
	
	@Override
	public String getBotToken() {
		return config.getBot().getToken();
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			Message message = update.getMessage();
			SendMessage response = createResponse(message);
			try {
				sendMessage(response);
			} catch (TelegramApiException e) {
				logger.error("Failed to send response due to error {}: {}", e.getMessage(), e.getApiResponse());
			}
		}

	}

	SendMessage createResponse(Message message) {
		Long chatId = message.getChatId();
		String text = message.getText();
		SendMessage response = new SendMessage();
		response.setChatId(chatId.toString());
		response.setText(text);
		return response;
	}



}
