package fr.univ_lyon1.info.m1.microblog.config;

import fr.univ_lyon1.info.m1.microblog.model.Y;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Config file for Y. */
public class YConfiguration {
    private static final String CONFIG_FILE = "config/app-config.properties";
    private final Y model;

    /** Default constructor. */
    public YConfiguration(final Y model) {
        this.model = model;
    }

    /** Method to initialize the app with some users and messages. */
    public void initialize() throws IOException {
        Properties config = loadProperties();
        initializeUsers(config);
        initializeMessages(config);
    }

    private Properties loadProperties() throws IOException {
        Properties config = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            config.load(in);
        } catch (IOException e) {
            System.err.println("Warning: could not load " + CONFIG_FILE);
            config.setProperty("users", "foo,bar");
            config.setProperty("messages.file", "messages/example-messages.txt");
        }
        return config;
    }

    private void initializeUsers(final Properties config) {
        String usersStr = config.getProperty("users", "");
        String[] users = usersStr.split(",");
        for (String user : users) {
            if (!user.trim().isEmpty()) {
                model.createUser(user.trim(), user.trim());
            }
        }
    }

    private void initializeMessages(final Properties config) throws IOException {
        String messagesFile = config.getProperty("messages.file");
        if (messagesFile != null && !messagesFile.isEmpty()) {
            model.loadMessagesFromFile(messagesFile);
        }
    }
}
