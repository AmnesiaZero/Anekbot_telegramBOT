package sql;

import lombok.Data;
import lombok.extern.log4j.Log4j;

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
    public int getBotState(Long chatId) throws SQLException {
        Statement statement = dataSource.getConnection().createStatement();
        String sqlQuery = "SELECT bot_state FROM bot_state_store WHERE chat_id = " + chatId;
        log.debug(sqlQuery);
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        int botState = SqlConverter.convertSqlQueryToInt(resultSet,"bot_state");
        return botState;
    }
    public void setBotState(Long chatId,int botState) throws SQLException {
        Statement statement = dataSource.getConnection().createStatement();
        String sqlQuery;
        if(chatIdExist(chatId)){
            sqlQuery = "UPDATE `bot_state_store` SET `bot_state`="+botState +" WHERE `chat_id`="+ chatId;
        }
        else
            sqlQuery = "INSERT INTO `bot_state_store`(`id`,`chat_id`, `bot_state`) VALUES (NULL," + chatId +"," + botState + ")";
        log.debug(sqlQuery);
        statement.execute(sqlQuery);
    }
    public boolean chatIdExist(Long chatId) throws SQLException {
        Statement statement = dataSource.getConnection().createStatement();
        String sqlQuery = "SELECT bot_state FROM bot_state_store WHERE chat_id = " + chatId;
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        if(resultSet.next()) return true;
        else return false;
    }


}
