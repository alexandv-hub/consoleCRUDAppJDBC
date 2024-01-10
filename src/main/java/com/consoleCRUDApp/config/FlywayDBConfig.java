package com.consoleCRUDApp.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import java.util.Properties;

import static com.consoleCRUDApp.config.DBConnection.*;
import static com.consoleCRUDApp.config.DBCreator.createDbIfNotExist;
import static com.consoleCRUDApp.view.messages.ErrorMessages.Database.FLYWAY_MIGRATION_UPDATE_FAILED;

public class FlywayDBConfig {

    private FlywayDBConfig() {
    }

    public static void applyFlywayMigrations() {

        createDbIfNotExist();

        try {
            Properties dbConnectionProperties = getProperties();
            String serverUrl = dbConnectionProperties.getProperty(PROPERTY_NAME_DATABASE_URL)
                                + dbConnectionProperties.getProperty(PROPERTY_NAME_DATABASE_NAME);

            Flyway flyway = Flyway.configure().dataSource(
                            serverUrl,
                            dbConnectionProperties.getProperty(PROPERTY_NAME_DATABASE_USER),
                            dbConnectionProperties.getProperty(PROPERTY_NAME_DATABASE_PASSWORD))
                    .load();

            flyway.migrate();
        } catch (FlywayException fwe) {
            System.out.println(FLYWAY_MIGRATION_UPDATE_FAILED);
            fwe.printStackTrace(System.out);
            throw fwe;
        }
    }
}
