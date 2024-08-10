package il.sce.scecafe.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

public class DatabaseConfigInitializer {
    private static final String PROPERTIES_FILE_PATH = "src/main/resources/application.properties";

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE_PATH)) {
            properties.load(input);
        } catch (IOException ex) {
            System.out.println("Properties file not found, creating new one...");
        }

        if (properties.containsKey("spring.datasource.url") &&
                properties.containsKey("spring.datasource.username") &&
                properties.containsKey("spring.datasource.password")) {
            System.out.println("Database configuration already exists. Skipping input.");
            return;
        }
        Scanner scanner = new Scanner(System.in);

        String defaultHost = "localhost";
        String defaultPort = "5432";
        String defaultDatabaseName = "postgres";
        String defaultUsername = "postgres";
        String defaultPassword = "postgres";

        System.out.printf("Please enter the database host (default: %s): ", defaultHost);
        String host = scanner.nextLine();
        if (host.isEmpty()) {
            host = defaultHost;
        }

        System.out.printf("Please enter the database port (default: %s): ", defaultPort);
        String port = scanner.nextLine();
        if (port.isEmpty()) {
            port = defaultPort;
        }

        System.out.printf("Please enter the database name (default: %s): ", defaultDatabaseName);
        String databaseName = scanner.nextLine();
        if (databaseName.isEmpty()) {
            databaseName = defaultDatabaseName;
        }

        System.out.printf("Please enter the database username (default: %s): ", defaultUsername);
        String username = scanner.nextLine();
        if (username.isEmpty()) {
            username = defaultUsername;
        }

        System.out.printf("Please enter the database password (default: %s): ", defaultPassword);
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            password = defaultPassword;
        }

        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, databaseName);

        properties.setProperty("spring.datasource.url", url);
        properties.setProperty("spring.datasource.username", username);
        properties.setProperty("spring.datasource.password", password);

        try (OutputStream output = new FileOutputStream("src/main/resources/application.properties")) {
            properties.store(output, null);
            System.out.println("Database configuration saved successfully!");
        } catch (IOException io) {
            io.printStackTrace();
        }

        scanner.close();
    }
}
