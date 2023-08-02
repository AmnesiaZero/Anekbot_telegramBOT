package sql;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import service.RandomGenerator;
import sql.DataSource;
import sql.SqlConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@Log4j
@Data
public class AnekdotDAO {
    public DataSource dataSource;
    public AnekdotDAO() throws SQLException {
        log.info("Вошёл в dataSource");
        try {
            dataSource = new DataSource();
        } catch (SQLException e) {
            log.error(e);
        }
    }
    public String getAnekdot(int themeId) throws SQLException {
        Statement statement = dataSource.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT text FROM anekdot_store WHERE theme=" + themeId + "ORDER BY RAND ()LIMIT 1");
        String anekdotText = SqlConverter.convertSqlToString(resultSet);
        return anekdotText;
    }


}
