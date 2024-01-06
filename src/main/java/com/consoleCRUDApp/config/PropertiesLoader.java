package com.consoleCRUDApp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    static Properties loadProperties(String propertiesFileName) {
        Properties properties = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (input == null) {
                System.out.println("Sorry, unable to find file: " + propertiesFileName);
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
