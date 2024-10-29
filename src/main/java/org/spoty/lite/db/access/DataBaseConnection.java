package org.spoty.lite.db.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String db_url = "jdbc:mysql://localhost:3306/spotylite";
    private static final String dbe_user = "root";
    private static final String db_password = "root";

    public DataBaseConnection() throws SQLException {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(db_url, dbe_user, db_password);
    }
}
