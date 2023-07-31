package controller;
import config.BotConfig;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.BotCommands;
import service.Buttons;

import javax.annotation.PostConstruct;

@Log4j
@Component
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {
    final BotConfig config;
    private UpdateController updateController;

    public TelegramBot(BotConfig config,UpdateController updateController) {
        this.config = config;
        this.updateController = updateController;
    }
    @PostConstruct
    public void init(){
        updateController.registerBot(this);
    }


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
         var message = update.getMessage();
         log.debug(message.getText());
         var response = new SendMessage();
         response.setChatId(message.getChatId());
         response.setText("Hi,i'm anekbot");
         sendAnswerMessage(response);
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


}