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
		MessageReceiver messageReceiver = new MessageReceiver(telegramBot);
		MessageSender messageSender = new MessageSender(telegramBot);
		Thread receiveThread = new Thread(messageReceiver);
		receiveThread.setDaemon(true);
		receiveThread.setName("receiveThread");
		receiveThread.start();
		Thread sendThread = new Thread(messageSender);
		sendThread.setDaemon(true);
		sendThread.setName("sendThread");
		sendThread.start();
	}
}
