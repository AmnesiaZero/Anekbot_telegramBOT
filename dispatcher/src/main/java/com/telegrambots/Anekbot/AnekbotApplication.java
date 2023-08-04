package com.telegrambots.Anekbot;
import config.BotConfig;
import controller.TelegramBot;
import controller.UpdateController;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sql.AnekdotDAO;
import sql.DataSource;
import utils.MessageUtils;

import java.sql.SQLException;

//@SpringBootApplication
@Log4j
public class AnekbotApplication {
	public static void main(String[] args) throws SQLException{
		TelegramBot telegramBot = new TelegramBot();
		telegramBot.getUpdateController().registerBot(telegramBot);
	}
}
