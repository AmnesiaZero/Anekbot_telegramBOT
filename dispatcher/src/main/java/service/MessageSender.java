package service;

import controller.TelegramBot;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.MessageUtils;

@Log4j
@Data
public class MessageSender implements Runnable {
    private TelegramBot telegramBot;
    public MessageSender(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    @Override
    public void run() {
        while (true){
            SendMessage sendMessage = telegramBot.getSendQueue().poll();
            if(sendMessage==null) continue;
            telegramBot.sendAnswerMessage(sendMessage);
        }

    }
}
