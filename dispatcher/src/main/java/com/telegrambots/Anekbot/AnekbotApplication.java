package com.telegrambots.Anekbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class AnekbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnekbotApplication.class, args);
	}

}
