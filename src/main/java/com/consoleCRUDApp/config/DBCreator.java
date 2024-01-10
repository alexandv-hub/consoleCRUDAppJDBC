package com.consoleCRUDApp.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import static com.consoleCRUDApp.config.DBConnection.*;
import static com.consoleCRUDApp.view.messages.ErrorMessages.Database.DATABASE_CREATION_FAILED;
import static com.consoleCRUDApp.view.messages.ErrorMessages.Database.NO_DATABASE_FOUND;
import static com.consoleCRUDApp.repository.hibernate.SQLQueries.SQL_CREATE_DATABASE;
import static com.consoleCRUDApp.repository.hibernate.SQLQueries.POSTGRES_SQL_SELECT_DATNAME_FROM_PG_DATABASE;
import static com.consoleCRUDApp.view.messages.SystemMessages.Database.CONNECTED_TO_POSTGRE_SQL_SERVER_SUCCESSFULLY;
import static com.consoleCRUDApp.view.messages.SystemMessages.Database.DATABASE_SUCCESSFULLY_CREATED;

public class DBCreator {

    private DBCreator() {
    }

    static void createDbIfNotExist() {
        System.out.println("Connecting to PostgreSQL Server...");
        try (Connection connection = getServerConnection();
             Statement statement = connection.createStatement()) {

            Properties dbConnectionProperties = getProperties();

            ResultSet resultSet = statement.executeQuery(POSTGRES_SQL_SELECT_DATNAME_FROM_PG_DATABASE);
            boolean dbExists = false;
            while (resultSet.next()) {
                if (dbConnectionProperties.getProperty(PROPERTY_NAME_DATABASE_NAME).equals(resultSet.getString(1))) {
                    dbExists = true;
                    System.out.println(CONNECTED_TO_POSTGRE_SQL_SERVER_SUCCESSFULLY);
                    break;
                }
            }

            if (!dbExists) {
                System.out.println(NO_DATABASE_FOUND);
                System.out.println("Starting create new database...");
                statement.executeUpdate(SQL_CREATE_DATABASE + dbConnectionProperties.getProperty(PROPERTY_NAME_DATABASE_NAME));
                System.out.println(DATABASE_SUCCESSFULLY_CREATED);
            }
        } catch (Exception e) {
            System.out.println(DATABASE_CREATION_FAILED);
            e.printStackTrace(System.out);
            System.exit(1);
        }
    }
}
