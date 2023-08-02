package sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSource {
    static String url = "jdbc:mysql://localhost/anekbot_db";

    static String username = "root";

    static String password = "root";
    public Connection connection;
    public DataSource() throws SQLException {
        connection =  DriverManager.getConnection(url,username,password);
    }



}
