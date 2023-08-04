package sql;

import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
        ResultSet resultSet = statement.executeQuery("SELECT text FROM anekdot_store WHERE theme_id=" + themeId + "ORDER BY RAND ()LIMIT 1");
        String anekdotText = SqlConverter.convertSqlToString(resultSet);
        return anekdotText;
    }



}
