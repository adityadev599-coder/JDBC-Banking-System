package com.aditya.bank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/banking_system";

    private static final String USER = getRequiredEnvironmentVariable("DB_USER");
    private static final String PASSWORD = getRequiredEnvironmentVariable("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String getRequiredEnvironmentVariable(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Set the " + name + " environment variable before running the application."
            );
        }

        return value;
    }
}
