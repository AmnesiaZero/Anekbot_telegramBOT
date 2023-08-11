package com.telegrambots.Anekbot;
import controller.TelegramBot;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.MessageReceiver;
import service.MessageSender;

import java.sql.SQLException;

//@SpringBootApplication
@Log4j
public class AnekbotApplication {
	public static void main(String[] args) throws SQLException, TelegramApiException {
		TelegramBot telegramBot = new TelegramBot();
		telegramBot.getUpdateController().registerBot(telegramBot);
//		telegramBot.getSqlController().getBotStateDAO().setDefaultStateForAll();
		MessageReceiver messageReceiver = new MessageReceiver(telegramBot);
		MessageSender messageSender = new MessageSender(telegramBot);
		telegramBot.loadMessageHandlers(messageReceiver,messageSender);
		Thread receiver = new Thread(messageReceiver);
		receiver.setDaemon(true);
		receiver.setName("MsgReciever");
		receiver.start();

		Thread sender = new Thread(messageSender);
		sender.setDaemon(true);
		sender.setName("MsgSender");
		sender.start();

	}
}
