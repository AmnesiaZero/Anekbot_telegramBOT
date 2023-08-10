package utils;

import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
@Log4j
public class MessageUtils {
    public static SendMessage generateSendMessageWithText(Update update,String text){
        Message message = getMessage(update);
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(text);
        return sendMessage;
    }
    public static Message getMessage(Update update){
        if(update.getMessage()!=null)
            return update.getMessage();
        else
            return update.getCallbackQuery().getMessage();
    }
    public static String getUsername(Update update){
        if(update.getMessage()!=null)
            return update.getMessage().getFrom().getFirstName();
        else
            return update.getCallbackQuery().getFrom().getFirstName();
    }
    public static String getMessageText(Update update){
        if(update.getMessage()!=null)
            return update.getMessage().getText();
        else
            return update.getCallbackQuery().getData();
    }
    public static Long getChatId(Update update){
        if(update.getMessage()!=null)
            return update.getMessage().getChatId();
        else
            return update.getCallbackQuery().getMessage().getChatId();
    }
}
