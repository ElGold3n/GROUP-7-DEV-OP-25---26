package com.napier.devops.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;

    // Default values (can be overridden by environment variables)
    private static final String DEFAULT_URL =
            "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "worlduser";
    private static final String DEFAULT_PASS = "_ty5g09^tt";

    /**
     * Connect using environment variables or defaults.
     */
    public void connect() {
        String url = System.getenv().getOrDefault("DB_URL", DEFAULT_URL);
        String user = System.getenv().getOrDefault("DB_USER", DEFAULT_USER);
        String pass = System.getenv().getOrDefault("DB_PASS", DEFAULT_PASS);

        connect(url, user, pass, 15, 5000);
    }

    /**
     * Overloaded connect method with full parameters.
     *
     * @param location host:port string (e.g. "localhost:33060")
     * @param timeoutMillis total time to keep retrying (ms)
     * @param database database name
     * @param username database username
     * @param password database password
     */
    public void connect(String location, int timeoutMillis, String database, String username, String password) {
        String url = "jdbc:mysql://" + location + "/" + database
                + "?allowPublicKeyRetrieval=true&useSSL=false";

        connect(url, username, password, timeoutMillis / 5000, 5000);
    }

    /**
     * Internal connect helper with retries.
     */
    private void connect(String url, String user, String pass, int retries, int delayMillis) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            return;
        }

        for (int i = 0; i < retries; i++) {
            try {
                System.out.println("Attempting DB connection (" + (i + 1) + "/" + retries + ")...");
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Connected to database");
                break;
            } catch (SQLException e) {
                System.out.println("Failed: " + e.getMessage());
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException ignored) {}
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
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
