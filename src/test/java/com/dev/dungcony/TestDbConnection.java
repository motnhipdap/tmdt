package com.dev.dungcony;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDbConnection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3307/db1";
        String user = "root";
        String pass = "123456";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("✅ DB Connected");
        }
    }
}
