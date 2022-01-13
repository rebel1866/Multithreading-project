package by.emaptc.stanislavmelnikov.multithreading.outputinput;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
    private final static String CONFIG_PATH = "src/main/resources/config.properties";

    public static String getProperty(String key) {
        Properties property = new Properties();
        String values;
        try {
            FileInputStream fileInputStream = new FileInputStream(CONFIG_PATH);
            property.load(fileInputStream);
        } catch (IOException e) {
            Logger logger = LogManager.getLogger(PropertyLoader.class);
            logger.error("Cannot load init parameters", e);
            System.exit(0);
        }
        values = property.getProperty(key);
        return values;
    }
}
