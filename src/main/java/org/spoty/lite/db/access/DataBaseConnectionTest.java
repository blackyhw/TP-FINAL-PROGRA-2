package org.spoty.lite.db.access;

import java.sql.Connection;
import java.sql.SQLException;


public class DataBaseConnectionTest {

    public static void test() {
        try {
            Connection conn = DataBaseConnection.getConnection();
            if (conn != null) {
                System.out.println("1");
                conn.close();
            } else {
                System.out.println("0");
            }
        } catch (SQLException e) {
            System.out.println("-1" + e.getMessage());
        }
    }
}


