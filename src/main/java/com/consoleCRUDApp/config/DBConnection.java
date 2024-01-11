package com.consoleCRUDApp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static com.consoleCRUDApp.config.PropertiesLoader.loadProperties;
import static com.consoleCRUDApp.view.messages.ErrorMessages.Database.DB_CONNECTION_FAILED;

public class DBConnection {
    private static final Properties properties;

    static final String APPLICATION_PROPERTIES_FILE_NAME = "application.properties";

    static final String PROPERTY_NAME_DATABASE_URL = "database.url";
    static final String PROPERTY_NAME_DATABASE_NAME = "database.name";
    static final String PROPERTY_NAME_DATABASE_USER = "database.user";
    static final String PROPERTY_NAME_DATABASE_PASSWORD = "database.password";

    static {
        properties = loadProperties(APPLICATION_PROPERTIES_FILE_NAME);
    }

    private DBConnection() {
    }

    static synchronized Connection getServerConnection() throws SQLException {
        try {
            String serverUrl = properties.getProperty(PROPERTY_NAME_DATABASE_URL);
            return DriverManager.getConnection(
                    serverUrl,
                    properties.getProperty(PROPERTY_NAME_DATABASE_USER),
                    properties.getProperty(PROPERTY_NAME_DATABASE_PASSWORD));
        } catch (Exception e) {
            System.out.println(DB_CONNECTION_FAILED);
            throw e;
        }
    }

    static synchronized Properties getProperties() {
        return properties;
    }
}
