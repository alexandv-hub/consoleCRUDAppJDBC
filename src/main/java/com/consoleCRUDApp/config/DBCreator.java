package com.consoleCRUDApp.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import static com.consoleCRUDApp.config.DBConnection.getProperties;
import static com.consoleCRUDApp.config.DBConnection.getServerConnection;

public class DBCreator {

    private DBCreator() {
    }

    static void createDbIfNotExist() {
        System.out.println("Connecting to MySQL Server...");
        try (Connection connection = getServerConnection();
             Statement statement = connection.createStatement()) {

            Properties dbConnectionProperties = getProperties();

            ResultSet resultSet = statement.executeQuery("SHOW DATABASES;");
            boolean dbExists = false;
            while (resultSet.next()) {
                if (dbConnectionProperties.getProperty("database.name").equals(resultSet.getString(1))) {
                    dbExists = true;
                    System.out.println("Connected to MySQL Server successfully...");
                    break;
                }
            }

            if (!dbExists) {
                System.out.println("No database found!");
                System.out.println("Creating database...");
                statement.executeUpdate("CREATE DATABASE " + dbConnectionProperties.getProperty("database.name"));
                System.out.println("Database successfully created...");
            }
        } catch (Exception e) {
            System.out.println("Database creation failed!");
            e.printStackTrace(System.out);
            System.exit(1);
        }
    }
}
