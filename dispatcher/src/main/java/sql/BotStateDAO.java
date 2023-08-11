package sql;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import states.BotStates;
import utils.MessageUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Log4j
@Data
public class BotStateDAO {
    private DataSource dataSource;
    public BotStateDAO(DataSource dataSource){
        log.debug("Создан объект BotStateDAO");
        this.dataSource = dataSource;
    }
    public int getBotState(Update update) throws SQLException {
        Long userId = MessageUtils.getUserId(update);
        Long chatId = MessageUtils.getChatId(update);
        Statement statement = dataSource.getConnection().createStatement();
        String sqlQuery = "SELECT bot_state FROM bot_state_store WHERE chat_id = " + chatId + " AND user_id =" + userId;
        log.debug(sqlQuery);
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        int botState = SqlConverter.convertSqlQueryToInt(resultSet,"bot_state");
        return botState;
    }
    public void setBotState(Update update, int botState) throws SQLException {
        Long userId = MessageUtils.getUserId(update);
        Long chatId = MessageUtils.getChatId(update);
        Statement statement = dataSource.getConnection().createStatement();
        String sqlQuery;
        if (rowExist(chatId, userId))
            sqlQuery = "UPDATE `bot_state_store` SET `bot_state`=" + botState + " WHERE chat_id = " + chatId + " AND user_id =" + userId;
        else
            sqlQuery = "INSERT INTO `bot_state_store`(`id`,`chat_id`,`user_id`, `bot_state`) VALUES (NULL," + chatId + "," + userId + "," + botState + ")";
        log.debug(sqlQuery);
        statement.execute(sqlQuery);
    }
    public boolean rowExist(Long chatId,Long userId) throws SQLException {
        Statement statement = dataSource.getConnection().createStatement();
        String sqlQuery = "SELECT bot_state FROM bot_state_store WHERE chat_id = " + chatId + " AND user_id =" + userId;
        log.debug(sqlQuery);
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        if(resultSet.next()) return true;
        else return false;
    }
    public char getChosenLetter(Update update) throws SQLException{
        Long chatId = MessageUtils.getChatId(update);
        Long userId = MessageUtils.getUserId(update);
        Statement statement = dataSource.getConnection().createStatement();
        String sqlQuery = "SELECT chosen_letter FROM bot_state_store WHERE chat_id = " + chatId + " AND user_id =" + userId;
        log.debug(sqlQuery);
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        char chosenLetter = SqlConverter.convertSqlToChar(resultSet,"chosen_letter");
        log.debug("Получена буква - " + chosenLetter );
        return chosenLetter;
    }
    public void setChosenLetter(Update update,char letter) throws SQLException {
        Long userId = MessageUtils.getUserId(update);
        Long chatId = MessageUtils.getChatId(update);
        Statement statement = dataSource.getConnection().createStatement();
        String sqlQuery = "UPDATE `bot_state_store` SET `chosen_letter`='"+letter +"'WHERE chat_id = " + chatId + " AND user_id =" + userId;
        log.debug(sqlQuery);
        statement.execute(sqlQuery);
    }




}
