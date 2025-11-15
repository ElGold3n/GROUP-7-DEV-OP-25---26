package com.napier.devops.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd!";

    public static Connection getConnection() throws SQLException {
        String url = System.getenv().getOrDefault("DB_URL", URL);
        String user = System.getenv().getOrDefault("DB_USER", USER);
        String pass = System.getenv().getOrDefault("DB_PASS", PASSWORD);

        int attempts = 0;
        while (attempts < 15) {
            try {
                return DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                attempts++;
                System.out.println("Attempting DB connection (" + attempts + "/15)... " +  e.getMessage());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }
        throw new SQLException("Unable to connect to database after 15 attempts.");
    }

}
