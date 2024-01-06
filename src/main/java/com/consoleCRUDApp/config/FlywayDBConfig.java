package com.consoleCRUDApp.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import java.util.Properties;

import static com.consoleCRUDApp.config.DBConnection.getProperties;
import static com.consoleCRUDApp.config.DBCreator.createDbIfNotExist;

public class FlywayDBConfig {

    private FlywayDBConfig() {
    }

    public static void applyFlywayMigrations() {

        createDbIfNotExist();

        try {
            Properties dbConnectionProperties = getProperties();
            String serverUrl = dbConnectionProperties.getProperty("database.url") +
                               dbConnectionProperties.getProperty("database.name");

            Flyway flyway = Flyway.configure().dataSource(
                            serverUrl,
                            dbConnectionProperties.getProperty("database.user"),
                            dbConnectionProperties.getProperty("database.password"))
                    .load();

            flyway.migrate();
        } catch (FlywayException fwe) {
            System.out.println("Flyway migration update failed!");
        }
    }
}
