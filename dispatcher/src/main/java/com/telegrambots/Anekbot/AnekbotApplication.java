package com.telegrambots.Anekbot;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import service.TelegramBot;

@SpringBootApplication
@Log4j
public class AnekbotApplication {
	public static void main(String[] args) {

		SpringApplication.run(AnekbotApplication.class);


	}

}
