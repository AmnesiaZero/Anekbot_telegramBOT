package service;

import config.BotConfig;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import model.AnekdotModel;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static service.BotCommands.HELP_TEXT;
import static service.BotCommands.LIST_OF_COMMANDS;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    Logger log = LogManager.getLogger(LoggingLog4j.class);

    public TelegramBot(BotConfig config) {
        this.config = config;
        System.out.println("Токен = " + config.getToken()+ " имя = " + config.getBotName());
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.log(Level.WARNING,e.getMessage(),e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived( Update update) {
        var message = update.getMessage();
        log.info(message.getText());
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
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.log(Level.WARNING,e.getMessage(),e);
        }
    }

    private void sendHelpText(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.log(Level.WARNING,e.getMessage(),e);
        }
    }


}