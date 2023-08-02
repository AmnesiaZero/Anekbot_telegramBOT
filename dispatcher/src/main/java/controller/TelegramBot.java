package controller;
import config.BotConfig;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import service.BotCommands;
import sql.AnekdotDAO;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@Log4j
@Data
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {
    final int RECONNECT_PAUSE =10000;
    final BotConfig config;
    private UpdateController updateController;
    public AnekdotDAO anekdotDAO;

    public TelegramBot(BotConfig config,UpdateController updateController,AnekdotDAO anekdotDAO) {
        this.config = config;
        this.updateController = updateController;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
        this.anekdotDAO = anekdotDAO;
    }

//    @PostConstruct
//    public void init(){
//        updateController.registerBot(this);
//    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }


    @Override
    public void onUpdateReceived(@NotNull Update update) {
        try {
            updateController.processUpdate(update);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAnswerMessage(SendMessage message) {
        if(message!=null){
            try{
                execute(message);
            }
            catch (TelegramApiException e){
                log.error(e);
            }
        }
    }
    public void botConnect() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(this);
            log.info("TelegramAPI started. Look for messages");
        } catch (TelegramApiRequestException e) {
            log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }
            botConnect();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }



}