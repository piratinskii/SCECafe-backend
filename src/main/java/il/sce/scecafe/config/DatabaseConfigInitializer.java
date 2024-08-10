package il.sce.scecafe.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

public class DatabaseConfigInitializer {
    private static final String PROPERTIES_FILE_PATH = "src/main/resources/application.properties";

    public static void initializeDatabaseConfig() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE_PATH)) {
            properties.load(input);
        } catch (IOException ex) {
            System.out.println("Properties file not found, creating new one...");
        }

        Scanner scanner = new Scanner(System.in);

        String host = System.getProperty("db.host", "localhost");
        String port = System.getProperty("db.port", "5432");
        String databaseName = System.getProperty("db.name", "postgres");
        String username = System.getProperty("db.username", "postgres");
        String password = System.getProperty("db.password", "postgres");

        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, databaseName);

        properties.setProperty("spring.datasource.url", url);
        properties.setProperty("spring.datasource.username", username);
        properties.setProperty("spring.datasource.password", password);

        try (OutputStream output = new FileOutputStream(PROPERTIES_FILE_PATH)) {
            properties.store(output, null);
            System.out.println("Database configuration saved successfully!");
        } catch (IOException io) {
            io.printStackTrace();
        }

        scanner.close();
    }
}
