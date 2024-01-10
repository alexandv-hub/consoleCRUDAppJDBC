package com.consoleCRUDApp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.consoleCRUDApp.view.messages.ErrorMessages.Database.SORRY_UNABLE_TO_FIND_FILE;

public class PropertiesLoader {

    static Properties loadProperties(String propertiesFileName) {
        Properties properties = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (input == null) {
                System.out.println(SORRY_UNABLE_TO_FIND_FILE + propertiesFileName);
                return null;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            return null;
        }
        return properties;
    }
}
