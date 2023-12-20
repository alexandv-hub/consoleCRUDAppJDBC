package com.consoleCRUDApp.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public static Connection getConnection() throws SQLException {
        String fullDatabaseUrl = properties.getProperty("database.url") + properties.getProperty("database.name");
        return DriverManager.getConnection(fullDatabaseUrl, properties.getProperty("database.user"), properties.getProperty("database.password"));
    }

    public static void createDbIfNotExist() {
        System.out.println("Connecting to MySQL Server...");
        try (Connection connection = DriverManager.getConnection(properties.getProperty("database.url"), properties.getProperty("database.user"), properties.getProperty("database.password"));
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SHOW DATABASES;");
            boolean dbExists = false;
            while (resultSet.next()) {
                if (properties.getProperty("database.name").equals(resultSet.getString(1))) {
                    dbExists = true;
                    break;
                }
            }

            if (!dbExists) {
                System.out.println("Creating database...");
                statement.executeUpdate("CREATE DATABASE " + properties.getProperty("database.name"));
                System.out.println("Database successfully created...");
            }
        } catch (SQLException e) {
            System.out.println("Database creation failed: " + e.getMessage());
        }
    }
}
