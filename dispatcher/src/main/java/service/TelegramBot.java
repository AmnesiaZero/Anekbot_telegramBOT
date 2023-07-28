package service;

import config.BotConfig;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.logging.Level;
import java.util.logging.Logger;


import static service.BotCommands.HELP_TEXT;
import static service.BotCommands.LIST_OF_COMMANDS;

@Component
@Log4j
public class TelegramBot implements LongPollingBot {
    final BotConfig config;
    @Value("{bot.name}")
    private String botName;
    @Value("{bot.token}")
    private String botToken;

      public TelegramBot(BotConfig config) {
        this.config = config;
        log.debug("Токен = " + config.getToken()+ " имя = " + config.getBotName());
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (Exception e){
            log.error(e);
        }
    }
    private void execute(SetMyCommands setMyCommands) {
    }






    @Override
    public void onUpdateReceived( Update update) {
        var message = update.getMessage();
        System.out.println("Сообщение - " + message.getText() + "/////////////////////////////////////////////////////////////////////////////////////////////////");
//        log.info(message.getText());
//        long chatId = 0;
//        long userId = 0; //это нам понадобится позже
//        String userName = null;
//        String receivedMessage;
//
//        //если получено сообщение текстом
//        if(update.hasMessage()) {
//            chatId = update.getMessage().getChatId();
//            userId = update.getMessage().getFrom().getId();
//            userName = update.getMessage().getFrom().getFirstName();
//
//            if (update.getMessage().hasText()) {
//                receivedMessage = update.getMessage().getText();
//                botAnswerUtils(receivedMessage, chatId, userName);
//            }
//
//            //если нажата одна из кнопок бота
//        } else if (update.hasCallbackQuery()) {
//            chatId = update.getCallbackQuery().getMessage().getChatId();
//            userId = update.getCallbackQuery().getFrom().getId();
//            userName = update.getCallbackQuery().getFrom().getFirstName();
//            receivedMessage = update.getCallbackQuery().getData();
//
//            botAnswerUtils(receivedMessage, chatId, userName);
//        }
    }

    @Override
    public BotOptions getOptions() {
        return null;
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {

    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {
        switch (receivedMessage){
            case "/start":
                startBot(chatId, userName);
                System.out.println("Выбрал старт");
                break;
            case "/help":
                sendHelpText(chatId, HELP_TEXT);
                break;
            default: break;
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hi, " + userName + "! I'm a Telegram bot.'");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            log.info(message);
            log.info("Reply sent");
        } catch (Exception e){
//            log.error(e);
        }
    }

    private void sendHelpText(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        log.info(String.valueOf(message));
        log.info("Reply sent");
    }


    @Override
    public String getBotUsername() {return botName;}

    @Override
    public String getBotToken() {
        return botToken;
    }
}