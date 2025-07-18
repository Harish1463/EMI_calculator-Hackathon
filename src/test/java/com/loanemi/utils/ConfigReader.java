package com.loanemi.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for reading configuration values from a properties file.
 * Centralizes access to key-value pairs defined in config.properties.
 * Commonly used to retrieve values like base URL, paths, or test parameters.
 */
public class ConfigReader {

    // Logger for reporting config load activity
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);

    // Properties object that holds all loaded config keys and values
    private static Properties properties = new Properties();

    // Static block executes once to load config file at class loading
    static {
        try {
            // Load properties from specified config file path
            FileInputStream fis = new FileInputStream("src/test/resources/test-data/config.properties");
            properties.load(fis);
            fis.close();

            logger.info("Config properties loaded successfully from 'config.properties'");
        } catch (IOException e) {
            logger.error("Error loading config.properties file", e);
        }
    }

    /**
     * Fetches a configuration value using the provided key.
     * Returns null if the key does not exist.
     *
     * @param key The property key to look up
     * @return String value associated with the key, or null if not found
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);

        // Log the result for traceability
        if (value != null) {
            logger.debug("ConfigReader: Retrieved value for key '{}': {}", key, value);
        } else {
            logger.warn("ConfigReader: No value found for key '{}'", key);
        }

        return value;
    }
}
