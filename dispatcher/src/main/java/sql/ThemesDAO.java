package sql;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Log4j
@Data
public class ThemesDAO {
    public DataSource dataSource;
    public ThemesDAO(DataSource dataSource){
        log.info("Создал объект ThemesDao");
        this.dataSource = dataSource;
    }
    public LinkedHashMap<Integer,String> getThemes() throws SQLException {
        Statement statement = dataSource.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT id,theme FROM anekdot_themes ORDER BY id ASC");
        LinkedHashMap<Integer,String> themes = SqlConverter.convertSqlQueryToHashMap(resultSet,"theme");
//        ArrayList<String> themes = SqlConverter.convertSqlQueryToStringArrayList(resultSet,"theme");
        return themes;
    }



}
