package controller;
import config.BotConfig;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import service.MessageReceiver;
import service.MessageSender;
import utils.BotCommands;
import sql.AnekdotDAO;
import sql.DataSource;
import sql.ThemesDAO;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

@Log4j
@Data
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {
    final int RECONNECT_PAUSE =10000;
    final BotConfig config;
    private UpdateController updateController;
    private SqlController sqlController;
    private MessageReceiver messageReceiver;
    private MessageSender messageSender;
    private LinkedBlockingQueue<Update> receiveQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<SendMessage> sendQueue = new LinkedBlockingQueue<>();
    public TelegramBot() throws SQLException, TelegramApiException {
        this.config = new BotConfig();
        this.updateController = new UpdateController();
        DataSource dataSource = new DataSource();
        loadSqlController(dataSource);
        botConnect();
    }
    private void loadSqlController(DataSource dataSource) throws SQLException {
        sqlController = new SqlController();
        sqlController.setAnekdotDAO(new AnekdotDAO(dataSource));
        sqlController.setThemesDAO(new ThemesDAO(dataSource));
    }
    public void loadMessageHandlers(MessageReceiver messageReceiver,MessageSender messageSender){
        this.messageReceiver = messageReceiver;
        this.messageSender = messageSender;
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
        log.debug(update);
        if(getReceiveQueue().offer(update)){
            log.debug("Update успешно добавлен");
        }
        else
           log.error("Объект не был добавлен");

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