package sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
@Configuration
@PropertySource("application.properties")
public class DataSource {
    @Value("${sql.url")
    static String url;
    @Value("${sql.username}")
    static String username;
    @Value("${sql.password}")
    static String password;
    public Connection connection;
    public DataSource() throws SQLException {
        connection =  DriverManager.getConnection(url,username,password);
    }



}
