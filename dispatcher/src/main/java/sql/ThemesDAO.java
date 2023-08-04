package sql;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Log4j
@Data
public class ThemesDAO {
    public DataSource dataSource;
    public ThemesDAO(DataSource dataSource){
        log.info("Создал объект ThemesDao");
        this.dataSource = dataSource;
    }
    public ArrayList<String> getThemes() throws SQLException {
        Statement statement = dataSource.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT theme FROM anekdot_themes ORDER BY id ASC");
        ArrayList<String> themes = SqlConverter.convertSqlQueryToStringArrayList(resultSet,"theme");
        return themes;
    }



}
