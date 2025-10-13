package com.napier.devops.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;

    // Default values (can be overridden by environment variables)
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "P@ssw0rd!";

    public void connect() {
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String pass = System.getenv().getOrDefault("DB_PASS", "P@ssw0rd!");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found.");
            return;
        }

        int retries = 15;
        for (int i = 0; i < retries; i++) {
            try {
                System.out.println("Attempting DB connection (" + (i+1) + "/" + retries + ")...");
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("✅ Connected to database");
                break;
            } catch (SQLException e) {
                System.out.println("❌ Failed: " + e.getMessage());
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔌 Database connection closed.");
            } catch (SQLException e) {
                System.err.println("⚠️ Error closing connection: " + e.getMessage());
            }
        }
    }
}
