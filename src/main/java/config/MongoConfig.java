package config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Provides a MongoDB database instance for the application.
 * This class initializes a singleton `MongoClient` and prints
 * simple startup messages to the console for demonstration.
 */
public class MongoConfig {

    // Your Atlas URI with username and password included
    private static final String URI = "PLACEHOLDER";

    private static final String DB_NAME = "musicdb";

    // Keep a singleton client so it stays alive for the lifetime of the app
    private static MongoClient client = null;

    public static MongoDatabase getDatabase() {
        if (client == null) {
            System.out.println("[startup] Connecting to MongoDB server...");
            client = MongoClients.create(URI);

            // Try to trigger a connection and list databases/collections to show progress
            try {
                System.out.println("[startup] Connected to MongoDB server (client created)");
                System.out.println("[startup] Selecting database: " + DB_NAME);
                MongoDatabase db = client.getDatabase(DB_NAME);

                System.out.println("[startup] Listing collections in '" + DB_NAME + "' (may take a moment)...");
                for (String name : db.listCollectionNames()) {
                    System.out.println("[startup]  - collection: " + name);
                }

                System.out.println("[startup] MongoDB initialization complete.");
                return db;
            } catch (Exception ex) {
                System.out.println("[startup] ERROR connecting to MongoDB: " + ex.getMessage());
                throw ex;
            }
        }

        return client.getDatabase(DB_NAME);
    }
}
