package controller;

import Buttons.*;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import states.BotStates;
import utils.MessageUtils;

import java.sql.SQLException;
import java.util.LinkedHashMap;


@Log4j
@Data
public class UpdateController implements BotCommands {
    private TelegramBot telegramBot;
    private BotStates botStates = new BotStates();
    private boolean autoState = false;
    private LinkedHashMap<Integer,String> neededThemes;
    private boolean flag = false;
    private boolean backAnekdotFlag = false;
    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }
    public void processUpdate(Update update) throws SQLException {
        log.debug("Вошёл в функцкию processUpdate");
        if(!telegramBot.getSqlController().getBotStateDAO().chatIdExist(MessageUtils.getChatId(update)))
           setBotState(update,botStates.getStartState());
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
        telegramBot.getMessageSender().sendMessage(update,"Скинь что-то более понятное",GenericButtons.inlineMarkup());
    }



    private void processPhotoMessage(Update update) {
        Message recievedMessage = update.getMessage();
        log.info("Получено фото");
        log.info(recievedMessage.getText());
//        telegramBot.getMessageSender()().sendMessage(update,"Спасибо за фото,хочешь анекдот?");
    }

    private void processDocumentMessage(Update update) {
        Message recievedMessage = update.getMessage();
        log.info("Получен документ");
        log.info(recievedMessage.getText());
//        telegramBot.getMessageSender()().sendMessage(update,"зачем мне документ............");
    }

    private void processTextMessage(Update update) throws SQLException {
        log.debug("Вошёл в функцкию processTextMessage");
        String receivedMessageText = MessageUtils.getMessageText(update);
        switch (receivedMessageText) {
            case "/start":
                setBotState(update, botStates.getStartState());
                break;
            case "/help":
                setBotState(update, botStates.getHelpState());
                break;
            case "/theme":
                setBotState(update, botStates.getChooseThemeLetterState());
                break;
            case "/auto":
                setAutoMod(update);
                break;
            case "/back":
                log.debug("entered /back");
                flag = true;
                goBack(update);
                break;
            default:
                log.debug("Была введна не команда,текст - " + receivedMessageText);
                break;
        }
        int botState = getBotState(update);
        if (botState == botStates.getStartState())
            sendStartMessage(update);
        else if (botState == botStates.getChooseThemeLetterState())
            chooseThemeLetter(update);
        else if (botState == botStates.getChooseThemeState()) {
            if (!flag) setChosenLetter(update, receivedMessageText);
            else displayThemes(update);
        } else if (botState == botStates.getChooseAnekdotState()){
            displayAnekdot(update, receivedMessageText);
        }
        else if(botState==botStates.getHelpState())
            sendHelpMessage(update);
        else
            log.debug("Состояние - " + botState);
        flag = false;
    }

    private void sendHelpMessage(Update update) {
        telegramBot.getMessageSender().sendMessage(update,HELP_TEXT,HelpButtons.inlineMarkup());
    }
    private void sendStartMessage(Update update){
        String username = MessageUtils.getUsername(update);
        telegramBot.getMessageSender().sendMessage(update,"Привет," + username + "! Я - бот,помогающий подобрать анекдот по выбранной теме.Для начала работы используйте /theme Для более подробной информации " +
                "используйте команду /help",StartButtons.inlineMarkup());
    }
    private void goBack(Update update) throws SQLException {
        log.debug("Вошёл в goBack");
        int botState = getBotState(update);
        if (botState == botStates.getChooseThemeLetterState()) {
            setBotState(update, botStates.getStartState());
            log.debug("enter 2");
        } else if (botState == botStates.getChooseThemeState()){
            log.debug("enter 3");
            setBotState(update, botStates.getStartState());
         } else if (botState==botStates.getChooseAnekdotState()) {
            log.debug("enter 4");
            setBotState(update,botStates.getChooseThemeLetterState());
        }
        else if (botState==botStates.getFinishAnekdotState()) {
            log.debug("enter 5");
            setBotState(update,botStates.getChooseThemeState());
        }
        else
            log.debug("Кнопка не сработала,состояние - " + botState);
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
            telegramBot.getMessageSender().sendMessage(update,"Введите корректный номер",GenericButtons.inlineMarkup());
//            telegramBot.getSqlController().getBotStateDAO().setBotState();
            return;
        }
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
        log.debug("Вошёл в функцию chooseThemeLetter");
        telegramBot.getMessageSender().sendMessage(update,THEME_TEXT, GenericButtons.inlineMarkup());
        setBotState(update,botStates.getChooseThemeState());
    }
    private void setChosenLetter(Update update,String receivedMessageText) throws SQLException {
        log.debug("Вошёл в функцию setChosenLetter");
        char themeLetter = receivedMessageText.charAt(0);
        log.debug("Установлена буква " + themeLetter);
        telegramBot.getSqlController().getBotStateDAO().setChosenLetter(MessageUtils.getChatId(update),themeLetter);
        displayThemes(update);
    }
    private void displayThemes(Update update) throws SQLException {
        log.debug("Вошёл в функцию displayThemes");
        char themeLetter = getChosenLetter(update);
        log.debug("Буква - "+ themeLetter);
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
            telegramBot.getMessageSender().sendMessage(update,"К сожалению,таких тем нет",GenericButtons.inlineMarkup());
            return;
        }
        for (Integer key: neededThemes.keySet()) {
            String themeText = count + ")" + neededThemes.get(key) + "\n";
            replyText +=themeText;
            count++;
        }
        log.debug(replyText);
        telegramBot.getMessageSender().sendMessage(update,replyText,GenericButtons.inlineMarkup());
        setBotState(update,botStates.getChooseAnekdotState());
    }
    private void setBotState(Update update,int botState) throws SQLException {
         telegramBot.getSqlController().getBotStateDAO().setBotState(MessageUtils.getChatId(update),botState);
    }
    private int getBotState(Update update) throws SQLException {
        return telegramBot.getSqlController().getBotStateDAO().getBotState(MessageUtils.getChatId(update));
    }
    private char getChosenLetter(Update update) throws SQLException {
        return telegramBot.getSqlController().getBotStateDAO().getChosenLetter(MessageUtils.getChatId(update));
    }
    private int getThemeId(Update update) throws SQLException {
        return telegramBot.getSqlController().getBotStateDAO().getBotState(MessageUtils.getChatId(update));
    }

    private void displayAnekdot(Update update, String receivedMessageText) throws SQLException {
        log.debug("Вошёл в функцию displayAnekdot");
        receivedMessageText = receivedMessageText.trim();
        int chosenThemeId;
        try{
            chosenThemeId = Integer.parseInt(receivedMessageText);
        }
        catch (Exception e){
            log.debug(e);
            telegramBot.getMessageSender().sendMessage(update,"Введите корректный номер",GenericButtons.inlineMarkup());
            goBack(update);
            setBotState(update,botStates.getStartState());
            return;
        }
        if(neededThemes==null){
            log.error("Нет списка нужных тем");
            telegramBot.getMessageSender().sendMessage(update,"Ошибка:нет нужных тем",GenericButtons.inlineMarkup());
            goBack(update);
            setBotState(update,botStates.getStartState());
            return;
        }
        if(chosenThemeId>neededThemes.size()|chosenThemeId<=0){
            log.debug("Выбран неккоректный номер");
            telegramBot.getMessageSender().sendMessage(update,"Выбран неккоректный номер",null);
            goBack(update);
            setBotState(update,botStates.getStartState());
            return;
        }
        log.debug("Id темы - " +neededThemes.values().toArray()[chosenThemeId-1]);
        int themeId =(int)neededThemes.keySet().toArray()[chosenThemeId-1];//получение id темы из выбранного номера
//        if(autoState){
//            while (autoState){
//                sendAnekdotText(update,themeId);
//            }
//        }
//        else
            sendAnekdotText(update,themeId);
        setBotState(update,botStates.getFinishAnekdotState());
    }
    private void sendAnekdotText(Update update,int themeId) throws SQLException {
        String anekdotText = telegramBot.getSqlController().getAnekdotDAO().getAnekdot(themeId);
        telegramBot.getMessageSender().sendMessage(update,anekdotText, GenericButtons.inlineMarkup());
    }



}
