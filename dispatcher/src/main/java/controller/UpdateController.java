package controller;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.GenericButtons;
import utils.MessageUtils;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import static utils.BotCommands.HELP_TEXT;


@Log4j
@Data
public class UpdateController  {
    private TelegramBot telegramBot;
    private final int defaultState = 0;
    private final int chooseThemeState = 1;
    private final int chooseAnekdotState = 2;
    private final int setAutoModeTimeState = 3;
    private boolean autoState = false;
    private LinkedHashMap<Integer,String> neededThemes;
    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }
    public void processUpdate(Update update) throws SQLException {
        log.debug("Вошёл в функцкию processUpdate");
        if(!telegramBot.getSqlController().getBotStateDAO().chatIdExist(MessageUtils.getChatId(update)))
           setBotState(update,defaultState);
        distributeMessageByType(update);
    }



    private void distributeMessageByType(Update update) throws SQLException {
        log.debug("Вошёл в функцкию distributeMessageByType");
        Message message = MessageUtils.getMessage(update);
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
        String receivedMessageText = MessageUtils.getMessageText(update);
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
                int botState = getBotState(update);
                if(botState==chooseThemeState)
                    displayThemes(update,receivedMessageText);
                else if(botState==chooseAnekdotState)
                    displayAnekdot(update,receivedMessageText);
                else
                    log.debug("Была введна не команда,текст - " + receivedMessageText);
                break;
        }

    }
    private void sendHelpText(Update update){
        setView(update,HELP_TEXT);;
    }
    private void sendStartText(Update update){
        String username = MessageUtils.getUsername(update);
        setView(update,"Привет," + username + "!Хочешь анекдотов?");
    }
    private void setAutoMod(Update update){
        log.debug("Был включён автомод");
//        chooseThemeLetter(update);
//        autoState = true;
    }

    private void setAutoModeTime(Update update, String receivedMessageText){
        int receivedTime;
        try {
             receivedTime = Integer.parseInt(receivedMessageText);
        }
        catch (Exception e){
            log.debug(e);
            setView(update,"Введите корректный номер");
//            telegramBot.getSqlController().getBotStateDAO().setBotState();
            return;
        }
    }
    private void setView(Update update,String text){
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update,text);
        sendMessage.setReplyMarkup(GenericButtons.inlineMarkup());
        telegramBot.getSendQueue().offer(sendMessage);
    }
    private void addMessageToSendQueue(Update update,String text){
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update,text);
        telegramBot.getSendQueue().offer(sendMessage);
    }

    private void runAutoMode(Update update){
//        Long sleepTime = (long) receivedTime * 60000;
//        boolean flag = true;
//        while (flag){
//            try {
//                log.info("Вошёл в сон");
//                Thread.sleep(sleepTime);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//        }

    }
    private void chooseThemeLetter(Update update) throws SQLException {
        addMessageToSendQueue(update,"Выберите первый символ темы[А-Я] [1-100]\n Для продолжения нажмите /theme");
        setBotState(update,chooseThemeState);
    }
    private void displayThemes(Update update, String receivedMessageText) throws SQLException {
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
        if(neededThemes.size()==0){
            log.debug("Количество тем = 0");
            setView(update,"К сожалению,таких тем нет");
            return;
        }
        for (Integer key: neededThemes.keySet()) {
            String themeText = count + ")" + neededThemes.get(key) + "\n";
            replyText +=themeText;
            count++;
        }
        log.debug(replyText);
        addMessageToSendQueue(update,replyText);
        setBotState(update,chooseAnekdotState);
    }
    private void setBotState(Update update,int botState) throws SQLException {
         telegramBot.getSqlController().getBotStateDAO().setBotState(MessageUtils.getChatId(update),botState);
    }
    private int getBotState(Update update) throws SQLException {
        return telegramBot.getSqlController().getBotStateDAO().getBotState(MessageUtils.getChatId(update));
    }

    private void displayAnekdot(Update update, String receivedMessageText) throws SQLException {
        log.debug("Вошёл в функцию sendAnekdotText");
        receivedMessageText = receivedMessageText.replace("/anekdot","");
        receivedMessageText = receivedMessageText.trim();
        int chosenThemeId;
        try{
            chosenThemeId = Integer.parseInt(receivedMessageText);
        }
        catch (Exception e){
            log.debug(e);
            setView(update,"Введите корректный номер");
            setBotState(update,defaultState);
            return;
        }
        if(neededThemes==null){
            log.error("Нет списка нужных тем");
            setView(update,"Ошибка:нет нужных тем");
            setBotState(update,defaultState);
            return;
        }
        if(chosenThemeId>neededThemes.size()){
            log.debug("Выбрана тема больше максимальной");
            setBotState(update,defaultState);
            return;
        }
        log.debug("Id темы - " +neededThemes.values().toArray()[chosenThemeId-1]);
        int themeId =(int)neededThemes.keySet().toArray()[chosenThemeId-1];//получение id темы из выбранного номера
        if(autoState){
            while (autoState){
                sendAnekdotText(update,themeId);
            }
        }
        else
            sendAnekdotText(update,themeId);
        setBotState(update,defaultState);
    }
    private void sendAnekdotText(Update update,int themeId) throws SQLException {
        String anekdotText = telegramBot.getSqlController().getAnekdotDAO().getAnekdot(themeId);
        setView(update,anekdotText);
    }



}
