package controller;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.MessageUtils;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import static utils.BotCommands.HELP_TEXT;


@Log4j
@Data
public class UpdateController  {
    private TelegramBot telegramBot;
    private String userName = "Приколист";
    private Long chatId;
    private final int defaultState = 0;
    private final int chooseThemeState = 1;
    private final int chooseAnekdotState = 2;
    private final int setAutoModeTimeState = 3;
    private int currentState = defaultState;
    private boolean autoState = false;
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
    private void addTextMessageToQueue(Update update, String messageText) {
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update,messageText);
        telegramBot.getSendQueue().offer(sendMessage);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var recievedMessage = update.getMessage();
        log.info("Получено неподдерживаемое сообщение");
        log.info(recievedMessage.getText());
        addTextMessageToQueue(update,"Скинь что-то более понятное");
    }



    private void processPhotoMessage(Update update) {
        Message recievedMessage = update.getMessage();
        log.info("Получено фото");
        log.info(recievedMessage.getText());
        addTextMessageToQueue(update,"Спасибо за фото,хочешь анекдот?");
    }

    private void processDocumentMessage(Update update) {
        Message recievedMessage = update.getMessage();
        log.info("Получен документ");
        log.info(recievedMessage.getText());
        addTextMessageToQueue(update,"зачем мне документ............");
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
            case "/auto":
                setAutoMod(update);
                break;
            default:
                if(currentState==chooseThemeState)
                    displayThemes(update,receivedMessageText);
                else if (currentState==chooseAnekdotState)
                   sendAnekdotText(update,receivedMessageText);
                else if (currentState== setAutoModeTimeState) {
                     setAutoModeTime(update,receivedMessageText);

                } else
                   log.debug("Была введна не команда,текст - " + receivedMessageText);
               break;
        }
    }
    private void setAutoMod(Update update){
        addTextMessageToQueue(update,"Выберите время,через которое бот будет присылать анекдот(в минутах)");
        currentState = setAutoModeTimeState;
        autoState = true;
    }

    private void setAutoModeTime(Update update, String receivedMessageText){
        int receivedTime;
        try {
             receivedTime = Integer.parseInt(receivedMessageText);
        }
        catch (Exception e){
            log.debug(e);
            addTextMessageToQueue(update,"Введите корректный номер");
            currentState = defaultState;
            return;
        }
        chooseThemeLetter(update);
        Long sleepTime = (long) receivedTime * 60000;
        boolean flag = true;
        while (flag){
            try {
                log.info("Вошёл в сон");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
    private void sendStartText(Update update){
        String userName = update.getMessage().getFrom().getUserName();
        addTextMessageToQueue(update,"Привет," + userName + "!Хочешь анекдотов?");
    }

    private void sendAnekdotText(Update update, String receivedMessageText) throws SQLException {
        log.debug("Вошёл в функцию sendAnekdotText");
        int chosenThemeId;
        try{
            chosenThemeId = Integer.parseInt(receivedMessageText);
        }
        catch (Exception e){
            log.debug(e);
            addTextMessageToQueue(update,"Введите корректный номер");
            currentState = defaultState;
            return;
        }
        if(neededThemes==null){
            log.error("Нет списка нужных тем");
            addTextMessageToQueue(update,"Ошибка:нет нужных тем");
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
        addTextMessageToQueue(update,anekdotText);
        currentState = defaultState;
    }
    private void sendHelpText(Update update){
        addTextMessageToQueue(update,HELP_TEXT);;
    }
    private void chooseThemeLetter(Update update){
        addTextMessageToQueue(update,"Выберите первую букву темы");
        currentState = chooseThemeState;
    }
    private void displayThemes(Update update, String receivedMessageText) throws SQLException {
        log.debug("Вошёл в фукнцию displayThemes");
        if(receivedMessageText.length()>1){
            addTextMessageToQueue(update,"Пожалуйста,введите 1 символ");
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
        if(neededThemes.size()==0){
            log.debug("Количество тем = 0");
            addTextMessageToQueue(update,"К сожалению,таких тем нет");
            return;
        }
        for (Integer key: neededThemes.keySet()) {
            String themeText = count + ")" + neededThemes.get(key) + "\n";
            replyText +=themeText;
            count++;
        }
        log.debug(replyText);
        addTextMessageToQueue(update,replyText);
        currentState = chooseAnekdotState;
    }
}
