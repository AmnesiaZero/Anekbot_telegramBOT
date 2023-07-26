package service;

import model.AnekdotModel;
import sql.Sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class AnekdotData {
    static Sql sql;

    static {
        try {
            sql = new Sql();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AnekdotData() throws SQLException {
    }

    public static String getAnekdot(int themeId) throws SQLException {
        Statement statement = sql.connection.createStatement();
        String text = "default";
        switch (themeId){
            case 1:
                text = sql.convertSqlToString(statement.executeQuery("SELECT text FROM anekdot_store WHERE theme=1"));
            case 2:
                text = sql.convertSqlToString(statement.executeQuery("SELECT text FROM anekdot_store WHERE theme=2"));
            case 3:
                text = sql.convertSqlToString(statement.executeQuery("SELECT text FROM anekdot_store WHERE theme=3"));
            default:
                text = "Введите текст";
            }

             return text;

    }


}
