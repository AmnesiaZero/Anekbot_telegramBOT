package sql;

import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Log4j
@Data
public class AnekdotDAO {
    public DataSource dataSource;
    public AnekdotDAO(DataSource dataSource) throws SQLException {
             log.info("Создал объект AnekdotDAO");
              this.dataSource = dataSource;
    }
    public String getAnekdot(int themeId) throws SQLException {
        Statement statement = dataSource.connection.createStatement();
        String sqlQuery = "SELECT text FROM anekdot_store WHERE theme_id="+ themeId +" LIMIT 1";
        log.debug(sqlQuery);
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        String anekdotText = SqlConverter.convertSqlQueryToString(resultSet);
        return anekdotText;
    }



}
