package config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConfig {

    // Your Atlas URI with username and password included
    private static final String URI =
            "mongodb+srv://mg3178:4321@playlistmanager-cluster.hovcfc8.mongodb.net/?appName=playlistmanager-cluster";



    private static final String DB_NAME = "musicdb";

    public static MongoDatabase getDatabase() {
        MongoClient client = MongoClients.create(URI);
        return client.getDatabase(DB_NAME);
    }
}
