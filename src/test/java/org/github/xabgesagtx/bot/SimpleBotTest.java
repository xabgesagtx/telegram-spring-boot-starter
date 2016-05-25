package org.github.xabgesagtx.bot;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;

public class SimpleBotTest {

	@Test
	public void testCreateResponse() {
		SimpleBot bot = new SimpleBot();
		Message message = new Message();
		ReflectionTestUtils.setField(message, "text", "echo");
		Chat chat = new Chat();
		ReflectionTestUtils.setField(chat, "id", Long.valueOf(10));
		ReflectionTestUtils.setField(message, "chat", chat);
		SendMessage response = bot.createResponse(message);
		assertEquals("10", response.getChatId());
		assertEquals("echo", response.getText());
	}

}
