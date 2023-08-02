package controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.Buttons;
import sql.AnekdotDAO;
import utils.MessageUtils;

import java.util.ArrayList;

@Component
@Log4j
public class UpdateController  {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;

    public UpdateController(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }


    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;

    }
    public void processUpdate(Update update){
        if(update==null){
            log.error("Received update is null");
            return;
        }
        if(update.getMessage()!=null){
            distributeMessageByType(update);
        }
        else
            log.error("Message is null");
    }

    private void distributeMessageByType(Update update) {
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

    private void processTextMessage(Update update) {
        Message receivedMessage = update.getMessage();
        String messageText = receivedMessage.getText();
        Long chatId = receivedMessage.getChatId();
        String userName = update.getCallbackQuery().getFrom().getUserName();
        switch (messageText) {
            case "/start":
                startBot(chatId, userName);
                break;
            case "/анекдот":
                  choseAnekdot(chatId,userName,update);

            default:
                break;
        }
    }
    private void startBot(Long chatId,String userName){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет, " + userName + "! Я - анекбот,что хочешь сделать?'");
        message.setReplyMarkup(Buttons.inlineMarkup());
        setView(message);
    }
    private void choseAnekdot(Long chatId,String userName,Update update){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите тему анекдота");
        ArrayList<String> themes = AnekdotDAO.


    }
}
