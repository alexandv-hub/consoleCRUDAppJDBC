package com.consoleCRUDApp.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
    private static Properties properties;
    private static Connection connectionInstance;

    static {
        loadProperties();
    }

    private DBConnection() {

    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
       return getConnection().prepareStatement(sql);
    }

    public static PreparedStatement getPreparedStatement(String sql, int intConst) throws SQLException {
       return getConnection().prepareStatement(sql, intConst);
    }

    private static void loadProperties() {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return;
            }
            properties = new Properties();
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    static synchronized Connection getServerConnection() throws SQLException {
        String serverUrl = properties.getProperty("database.url");
        return DriverManager.getConnection(serverUrl, properties.getProperty("database.user"), properties.getProperty("database.password"));
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (connectionInstance == null || connectionInstance.isClosed()) {
            String fullDatabaseUrl = properties.getProperty("database.url") + properties.getProperty("database.name");
            connectionInstance = DriverManager.getConnection(fullDatabaseUrl, properties.getProperty("database.user"), properties.getProperty("database.password"));
        }
        return connectionInstance;
    }

    public static synchronized Connection getConnectionNoAutoCommit() throws SQLException {
        if (connectionInstance == null || connectionInstance.isClosed()) {
            connectionInstance = getConnection();
        }
        connectionInstance.setAutoCommit(false);
        return connectionInstance;
    }

    static synchronized Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
        }
        return properties;
    }
}
