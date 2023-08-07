package controller;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.Buttons;
import utils.MessageUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatConversionException;
import java.util.LinkedHashMap;

import static service.BotCommands.HELP_TEXT;


@Log4j
@Data
public class UpdateController  {
    private TelegramBot telegramBot;
    private String userName = "Приколист";
    private Long chatId;
    private final int defaultState = 0;
    private final int chooseThemeState = 1;
    private final int chooseAnekdotState = 2;
    private int currentState = defaultState;
    private LinkedHashMap<Integer,String> neededThemes;
    private String themesReplyMessage;
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
        setView(update,"Скинь что-то более понятное");
    }

    private void setView(Update update,String messageText) {
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update,messageText);
//        sendMessage.setReplyMarkup(Buttons.inlineMarkup());
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        Message recievedMessage = update.getMessage();
        log.info("Получено фото");
        log.info(recievedMessage.getText());
        setView(update,"Спасибо за фото,хочешь анекдот?");
    }

    private void processDocumentMessage(Update update) {
        Message recievedMessage = update.getMessage();
        log.info("Получен документ");
        log.info(recievedMessage.getText());
        setView(update,"зачем мне документ............");
    }

    private void processTextMessage(Update update) throws SQLException {
        log.debug("Вошёл в функцкию processTextMessage");
        Message receivedMessage = update.getMessage();
        String receivedMessageText = receivedMessage.getText();
        log.debug(receivedMessageText);
        switch (receivedMessageText){
            case "/start":
                sendStartText(update);
                break;
            case "/help":
                sendHelpText(update);
                break;
            case "/theme":
                chooseThemeLetter(update);
                break;
            default:
                if(currentState==chooseThemeState)
                    displayThemes(update,receivedMessageText);
                else if (currentState==chooseAnekdotState)
                   sendAnekdotText(update,receivedMessageText);
               else
                   log.debug("Была введна не команда,текст - " + receivedMessageText);
               break;
        }
    }
    private void sendStartText(Update update){
        String userName = update.getMessage().getFrom().getUserName();
        setView(update,"Привет," + userName + "!Хочешь анекдотов?");
    }

    private void sendAnekdotText(Update update, String receivedMessageText) throws SQLException {
        log.debug("Вошёл в функцию sendAnekdotText");
        int chosenThemeId;
        try{
            chosenThemeId = Integer.parseInt(receivedMessageText);
        }
        catch (Exception e){
            log.debug(e);
            setView(update,"Введите корректный номер");
            currentState = defaultState;
            return;
        }
        if(neededThemes==null){
            log.error("Нет списка нужных тем");
            setView(update,"Ошибка:нет нужных тем");
            currentState = defaultState;
            return;
        }
        if(chosenThemeId>neededThemes.size()){
            log.debug("Выбрана тема больше максимальной");
            currentState = defaultState;
            return;
        }
        log.debug("Id темы - " +neededThemes.values().toArray()[chosenThemeId-1]);
        int themeId =(int)neededThemes.keySet().toArray()[chosenThemeId-1];//получение id темы из выбранного номера
        String anekdotText = telegramBot.getSqlController().getAnekdotDAO().getAnekdot(themeId);
        setView(update,anekdotText);
        currentState = defaultState;
    }
    private void sendHelpText(Update update){
        setView(update,HELP_TEXT);;
    }
    private void chooseThemeLetter(Update update){
        setView(update,"Выберите первую букву темы");
        currentState = chooseThemeState;
    }
    private void displayThemes(Update update, String receivedMessageText) throws SQLException {
        log.debug("Вошёл в фукнцию displayThemes");
        if(receivedMessageText.length()>1){
            setView(update,"Пожалуйста,введите 1 символ");
            return;
        }
        char themeLetter = receivedMessageText.charAt(0);
        String replyText = "Выберите тему анекдота:\n";
        LinkedHashMap<Integer,String> allThemes = telegramBot.getSqlController().getThemesDAO().getThemes();
        neededThemes = new LinkedHashMap<>();
        allThemes.forEach((key, value) -> {
            String shortTheme = value.replace("Анекдоты про ","");
            if(shortTheme.charAt(0)==themeLetter)
                neededThemes.put(key,value);
        });
        int count=1;
        for (Integer key: neededThemes.keySet()) {
            String themeText = count + ")" + neededThemes.get(key) + "\n";
            replyText +=themeText;
            count++;
        }
        log.debug(replyText);
        setView(update,replyText);
        currentState = chooseAnekdotState;
    }
}
