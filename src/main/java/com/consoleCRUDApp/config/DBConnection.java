package com.consoleCRUDApp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static com.consoleCRUDApp.config.PropertiesLoader.loadProperties;

public class DBConnection {
    private static final Properties properties;

    static {
        properties = loadProperties("application.properties");
    }

    private DBConnection() {
    }

    static synchronized Connection getServerConnection() throws SQLException {
        String serverUrl = properties.getProperty("database.url");
        return DriverManager.getConnection(
                serverUrl,
                properties.getProperty("database.user"),
                properties.getProperty("database.password"));
    }

    static synchronized Properties getProperties() {
        return properties;
    }
}
