package oleborn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                LoadProperties.loadData("url"),
                LoadProperties.loadData("user"),
                LoadProperties.loadData("password")
        );
    }
}
