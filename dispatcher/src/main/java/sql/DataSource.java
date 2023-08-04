package sql;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Log4j
public class DataSource {
    static String url ;

    static String username;

    static String password;
    public Connection connection;
    public DataSource() throws SQLException {
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("./sql.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            url = properties.getProperty("sql.url");
            username = properties.getProperty("sql.username");
            password = properties.getProperty("sql.password");
        }
        catch (IOException e){
            log.error(e);
        }
        connection =  DriverManager.getConnection(url,username,password);
    }



}
