package com.cydeo.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {

    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream file = new FileInputStream("configuration.properties");
            properties.load(file);
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found, Please make sure \"configuration.properties\" exists");
        } catch (IOException e) {
            System.out.println("failed to read from \"configuration.properties\"");
        }
    }

    public static String getProperty(String keyword) {
        return properties.getProperty(keyword);
    }
}
