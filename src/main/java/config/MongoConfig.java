package config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConfig {

    // Your Atlas URI with username and password included
    private static final String URI =
            "mongodb+srv://USERNAME:PASSWORD@CHANGEME.mongodb.net/";


    private static final String DB_NAME = "musicdb";

    public static MongoDatabase getDatabase() {
        MongoClient client = MongoClients.create(URI);
        return client.getDatabase(DB_NAME);
    }
}
