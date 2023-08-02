//package initialisers;
//
//import controller.TelegramBot;
//import controller.UpdateController;
//import lombok.extern.log4j.Log4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.meta.generics.LongPollingBot;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//@Log4j
//@Component
//public class BotInitializer {
//    @Autowired
//    TelegramBot bot;
//    UpdateController updateController;
//
////    @EventListener({ContextRefreshedEvent.class})
////    public void init() {
////        log.debug("Бот инициализирован");
////        try {
////            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
////            telegramBotsApi.registerBot((LongPollingBot) bot);
////            updateController.registerBot(bot);
////        } catch (TelegramApiException e) {
////            log.error(e.getMessage());
////        }
////    }
//}