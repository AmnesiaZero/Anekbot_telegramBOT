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


@Log4j
@Data
public class UpdateController  {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;
    private String userName = "Приколист";
    private Long chatId = 0L;

    public UpdateController(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }


    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;

    }
    public void processUpdate(@NotNull Update update) throws SQLException {
        if(update.getMessage().getChatId()!=null){
            chatId = update.getMessage().getChatId();
        }
        else log.error("Чат id не найден");
        if(update.getMessage().getFrom().getUserName()!=null){
            userName = update.getMessage().getFrom().getUserName();
        }
        else log.error("Username не найден");
        if(update.getMessage()!=null){
            distributeMessageByType(update);
        }
        else
            log.error("Message is null");
    }

    private void distributeMessageByType(Update update) throws SQLException {
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
        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update,"Скинь что-то более понятное");
        sendMessage.setChatId(recievedMessage.getChatId());
        setView(sendMessage);
    }

    private void setView(SendMessage message) {
        telegramBot.sendAnswerMessage(message);
    }

    private void processPhotoMessage(Update update) {
        Message recievedMessage = update.getMessage();
        log.info("Получено фото");
        log.info(recievedMessage.getText());
        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update,"Спасибо за фото,хочешь анекдот?");
        sendMessage.setChatId(recievedMessage.getChatId());
        setView(sendMessage);
    }

    private void processDocumentMessage(Update update) {
        Message recievedMessage = update.getMessage();
        log.info("Получен документ");
        log.info(recievedMessage.getText());
        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update,"зачем мне документ............");
        sendMessage.setChatId(recievedMessage.getChatId());
        setView(sendMessage);
    }

    private void processTextMessage(Update update) throws SQLException {
        Message receivedMessage = update.getMessage();
        String receivedMessageText = receivedMessage.getText();
        if(receivedMessageText=="/start") startBot(chatId);
        else if(receivedMessageText=="/анекдот") choseAnekdot(chatId);
        else if(receivedMessageText.toLowerCase().indexOf("/тема")!=-1) getTelegramBot().getAnekdotDAO().getAnekdot(receivedMessageText);
    }
    private void startBot(Long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет,приколист! Я - анекбот,что хочешь сделать?'");
        message.setReplyMarkup(Buttons.inlineMarkup());
        setView(message);
    }
    private void choseAnekdot(Long chatId) throws SQLException {
        SendMessage messageThemeChoose = new SendMessage();
        messageThemeChoose.setChatId(chatId);
        messageThemeChoose.setText("Выберите тему анекдота");
        String replyText = "";
        ArrayList<String> themes = getTelegramBot().getAnekdotDAO().getThemes();
        for(int i =0;i<themes.size();i++) {
            int themeNumber = i + 1;
            replyText += themeNumber + ")" + themes.get(i) + "\n";
        }
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(replyText);
        setView(message);


    }
}
