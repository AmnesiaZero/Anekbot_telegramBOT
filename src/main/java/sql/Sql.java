package sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import  org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
@Configuration
@PropertySource("application.properties")
public class Sql {
    @Value("${sql.url")
    static String url;
    @Value("${sql.username}")
    static String username;
    @Value("${sql.password}")
    static String password;
    public Connection connection;
    public Sql() throws SQLException {
        connection =  DriverManager.getConnection(url,username,password);
    }
    public String convertSqlToString(ResultSet resultSet) throws SQLException {
        resultSet.next();
        return resultSet.getString(1);
    }


}
