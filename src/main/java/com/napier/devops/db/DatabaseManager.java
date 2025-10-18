package com.napier.devops.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://db:3306/world?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "P@ssw0rd!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
