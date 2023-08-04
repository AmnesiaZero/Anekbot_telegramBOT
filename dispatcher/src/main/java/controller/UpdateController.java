package controller;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.Buttons;
import sql.AnekdotDAO;
import utils.MessageUtils;
import java.util.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static service.BotCommands.HELP_TEXT;


@Log4j
@Data
public class UpdateController  {
    private TelegramBot telegramBot;
    private String userName = "Приколист";
    private Long chatId;
    private final int chooseAnekdotState = 1;
    private final int defualtState = 0;
    private int currentState = defualtState;
    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }
    public void processUpdate(@NotNull Update update) throws SQLException {
        log.debug("Вошёл в функцкию processUpdate");
        if(update.getMessage()!=null) {
            if (update.getMessage().getChatId() != null)
                 chatId = update.getMessage().getChatId();
             else
                 log.error("Чат id не найден");
            if (update.getMessage().getFrom().getUserName() != null)
                userName = update.getMessage().getFrom().getUserName();
             else
                 log.error("Username не найден");

            distributeMessageByType(update);
        }
        else
            log.error("Message is null");
    }

    private void distributeMessageByType(Update update) throws SQLException {
        log.debug("Вошёл в функцкию distributeMessageByType");
        var message = update.getMessage();
        if(message.getText()!=null){
            processTextMessage(update);
        } else if (message.getDocument()!=null) {
            processDocumentMessage(update);
        } else if (message.getPhoto()!=null) {
            processPhotoMessage(update);
        }
        else{
            setUnsupportedMessageTypeView(update);
        }


    }

    private void setUnsupportedMessageTypeView(Update update) {
        var recievedMessage = update.getMessage();
        log.info("Получено неподдерживаемое сообщение");
        log.info(recievedMessage.getText());
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update,"Скинь что-то более понятное");
        sendMessage.setChatId(recievedMessage.getChatId());
        setView(sendMessage);
    }

    private void setView(SendMessage message) {
        //тут должна быть доп. обёртка для вьюхи,но её пока нет
        telegramBot.sendAnswerMessage(message);
    }

    private void processPhotoMessage(Update update) {
        Message recievedMessage = update.getMessage();
        log.info("Получено фото");
        log.info(recievedMessage.getText());
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update,"Спасибо за фото,хочешь анекдот?");
        sendMessage.setChatId(recievedMessage.getChatId());
        setView(sendMessage);
    }

    private void processDocumentMessage(Update update) {
        Message recievedMessage = update.getMessage();
        log.info("Получен документ");
        log.info(recievedMessage.getText());
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update,"зачем мне документ............");
        sendMessage.setChatId(recievedMessage.getChatId());
        setView(sendMessage);
    }

    private void processTextMessage(Update update) throws SQLException {
        log.debug("Вошёл в функцкию processTextMessage");
        Message receivedMessage = update.getMessage();
        String receivedMessageText = receivedMessage.getText();
        log.debug(receivedMessageText);
        if(currentState==chooseAnekdotState&&receivedMessageText!="/back")
        switch (receivedMessageText){
            case "/start":
                sendStartText(update);
                break;
            case "/help":
                sendHelpText(update);
                break;
            case "/theme":
                displayThemes(update);
                break;
            case "/back":
                currentState = defualtState;
                break;
            default:
                log.info("Была введена не команда,текст сообщения - " + receivedMessageText);
                break;
        }
    }
    private void sendStartText(Update update){
        SendMessage message = MessageUtils.generateSendMessageWithText(update,"Привет,приколист! Хочешь анекдотов?");
        message.setReplyMarkup(Buttons.inlineMarkup());
        setView(message);
    }
    private void sendHelpText(Update update){
        SendMessage message = MessageUtils.generateSendMessageWithText(update,HELP_TEXT);
        setView(message);
    }
    private void displayThemes(Update update) throws SQLException {
        log.debug("Вошёл в фукнцию choseAnekdot");
        String replyText = "Выберите тему анекдота:\n";
        ArrayList<String> themes = telegramBot.getSqlController().getThemesDAO().getThemes();
        for(int i =0;i<themes.size();i++) {
            int themeNumber = i + 1;
            replyText += themeNumber + ")" + themes.get(i) + "\n";
        }
        SendMessage anekdotMessage = MessageUtils.generateSendMessageWithText(update,replyText);
        setView(anekdotMessage);
        currentState = chooseAnekdotState;
    }
}
