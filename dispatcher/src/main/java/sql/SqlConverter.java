package sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlConverter {
    public static String convertSqlToString(ResultSet resultSet) throws SQLException {
        resultSet.next();
        return resultSet.getString(1);
    }
}
