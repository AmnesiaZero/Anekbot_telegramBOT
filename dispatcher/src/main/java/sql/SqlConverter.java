package sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SqlConverter {
    public static String convertSqlQueryToString(ResultSet resultSet) throws SQLException {
        resultSet.next();
        return resultSet.getString(1);
    }
    public static ArrayList<String> convertSqlQueryToStringArrayList(ResultSet resultSet,String columnLabel) throws SQLException{
        ArrayList<String> stringArrayList = new ArrayList<>();
        while (resultSet.next()){
            String string = resultSet.getString(columnLabel);
            stringArrayList.add(string);
        }
        return stringArrayList;
    }
    public static LinkedHashMap<Integer,String> convertSqlQueryToHashMap(ResultSet resultSet, String columnLabel) throws SQLException {
        LinkedHashMap<Integer,String> hashMap = new LinkedHashMap<>();
        while (resultSet.next()){
            int integer = resultSet.getInt("id");
            String string = resultSet.getString(columnLabel);
            hashMap.put(integer,string);
        }
        return hashMap;
    }
    public static int convertSqlQueryToInt(ResultSet resultSet, String label) throws SQLException {
        resultSet.next();
        return resultSet.getInt(label);
    }
    public static char convertSqlToChar(ResultSet resultSet,String label) throws SQLException {
        resultSet.next();
        return resultSet.getString(label).charAt(0);
    }
}
