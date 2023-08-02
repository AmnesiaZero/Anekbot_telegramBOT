package com.telegrambots.Anekbot;
import config.BotConfig;
import controller.TelegramBot;
import controller.UpdateController;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.MessageUtils;

//@SpringBootApplication
@Log4j
public class AnekbotApplication {
	public static void main(String[] args) throws TelegramApiException {
//		SpringApplication.run(AnekbotApplication.class);
		MessageUtils messageUtils = new MessageUtils();
		UpdateController updateController = new UpdateController(messageUtils);
		BotConfig botConfig = new BotConfig();
		TelegramBot telegramBot = new TelegramBot(botConfig,updateController);
		updateController.registerBot(telegramBot);
		telegramBot.botConnect();
	}
}
