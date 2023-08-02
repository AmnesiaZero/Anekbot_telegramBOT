package sql;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlConverter {
    public static String convertSqlToString(ResultSet resultSet) throws SQLException {
        resultSet.next();
        return resultSet.getString(1);
    }
    public static ArrayList<String> convertSqlQueryToStringArray(ResultSet resultSet) throws SQLException{
        ArrayList<String> themes = new ArrayList<>();
        while (resultSet.next()){
            String theme = resultSet.getString("theme");
            themes.add(theme);
        }
        return themes;
    }
}
