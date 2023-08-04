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
    public static ArrayList<String> convertSqlQueryToStringArrayList(ResultSet resultSet,String columnLabel) throws SQLException{
        ArrayList<String> themes = new ArrayList<>();
        while (resultSet.next()){
            String theme = resultSet.getString(columnLabel);
            themes.add(theme);
        }
        return themes;
    }
}
