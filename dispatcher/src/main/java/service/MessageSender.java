package service;

import controller.TelegramBot;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import utils.MessageUtils;

import java.sql.SQLException;


@Log4j
@Data
public class MessageSender implements Runnable {
    private TelegramBot telegramBot;
    private boolean runningFlag = true;
    public MessageSender(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }
    @Override
    public void run() {
        log.debug("Запустилась функция run у MessageSender");
        while (runningFlag){
                SendMessage sendMessage = telegramBot.getSendQueue().poll();
                if(sendMessage==null) continue;
                log.debug("Вызвана отправка сообщения");
                telegramBot.sendAnswerMessage(sendMessage);
            }
    }

}
