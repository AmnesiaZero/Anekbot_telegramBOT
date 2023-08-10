package service;

import Buttons.BotCommands;
import Buttons.HelpButtons;
import Buttons.StartButtons;
import controller.TelegramBot;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import utils.MessageUtils;

import java.sql.SQLException;


@Log4j
@Data
public class MessageSender implements Runnable, BotCommands {
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
    public void sendMessage(Update update, String text, InlineKeyboardMarkup inlineKeyboardMarkup){
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update,text);
        if(inlineKeyboardMarkup!=null)
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.getSendQueue().offer(sendMessage);
    }



}
