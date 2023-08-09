
package service;

import controller.TelegramBot;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;

@Log4j
@Data
public class MessageReceiver implements Runnable{
    private TelegramBot telegramBot;
    @Override
    public void run() {
        while (true){
            Update update = telegramBot.getReceiveQueue().poll();
            if(update==null) continue;
            try {
                telegramBot.getUpdateController().processUpdate(update);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public MessageReceiver(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }
}
