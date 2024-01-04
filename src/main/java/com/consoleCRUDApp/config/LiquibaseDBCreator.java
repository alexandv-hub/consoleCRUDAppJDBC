package com.consoleCRUDApp.config;

import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import static com.consoleCRUDApp.config.DBConnection.*;

public class LiquibaseDBCreator {

    private static Properties liquibaseProperties;

    static {
        loadLiquibaseProperties();
    }

    private LiquibaseDBCreator() {

    }

    private static void loadLiquibaseProperties() {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("liquibase.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find liquibase.properties");
                return;
            }
            liquibaseProperties = new Properties();
            liquibaseProperties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public static void createDbIfNotExist() {
        System.out.println("Connecting to MySQL Server...");
        try (Connection connection = getServerConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SHOW DATABASES;");
            boolean dbExists = false;
            while (resultSet.next()) {
                if (getProperties().getProperty("database.name").equals(resultSet.getString(1))) {
                    dbExists = true;
                    break;
                }
            }

            if (!dbExists) {
                System.out.println("Creating database...");
                statement.executeUpdate("CREATE DATABASE " + getProperties().getProperty("database.name"));
                System.out.println("Database successfully created...");
                applyLiquibaseUpdate();
            }
        }  catch (Exception e) {
            System.out.println("Database creation failed: " + e.getMessage());
            e.printStackTrace(System.out);
            System.exit(1);
        }
    }

    private static void applyLiquibaseUpdate() throws LiquibaseException {
        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(getConnection()));
            Map<String, Object> scopeObjects = Map.of(
                    Scope.Attr.database.name(), database,
                    Scope.Attr.resourceAccessor.name(), new ClassLoaderResourceAccessor()
            );

            CommandScope commandScope = new CommandScope("update")
                    .addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, liquibaseProperties.getProperty("changeLogFile"))
                    .addArgumentValue("url", liquibaseProperties.getProperty("url"))
                    .addArgumentValue("username", liquibaseProperties.getProperty("username"))
                    .addArgumentValue("password", liquibaseProperties.getProperty("password"));

            Scope.child(scopeObjects, () -> commandScope.execute());
        }
        catch (Exception e) {
            throw new LiquibaseException("Liquibase database tables creation failed: ", e);
        }
    }
}
