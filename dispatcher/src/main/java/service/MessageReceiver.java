package service;

import controller.TelegramBot;
import controller.UpdateController;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.LinkedHashMap;

@Log4j
@Data
public class MessageReceiver implements Runnable {
    private boolean runningFlag = true;
    private TelegramBot telegramBot;
    public MessageReceiver(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }
    @Override
    public void run() {
        log.debug("Запустилась очередь приема сообщений");
        log.debug("Очередь приёма - " + telegramBot.getReceiveQueue());
        while (runningFlag){
            Update update = telegramBot.getReceiveQueue().poll();
            if(update==null) continue;
            log.debug("Update обработан");
            try {
                telegramBot.getUpdateController().processUpdate(update);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
