
import config.MongoConfig;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MongoConnectionTest {

    @Test
    public void testMongoConnection() {
        try {
            MongoDatabase db = MongoConfig.getDatabase();
            assertNotNull(db, "Database should not be null");

            System.out.println("Connected to: " + db.getName());

            MongoCollection<Document> songs = db.getCollection("songs");
            assertNotNull(songs, "Songs collection should not be null");

            long count = songs.countDocuments();
            System.out.println("Number of documents in 'songs': " + count);

            Document first = songs.find().first();
            assertNotNull(first, "At least one document should exist in songs");

            System.out.println("First document:");
            System.out.println(first.toJson());

        } catch (Exception e) {
            fail("Connection to MongoDB failed: " + e.getMessage());
        }
    }


    @Test
    public void testFindArcticMonkeysSongs() {
        MongoDatabase db = MongoConfig.getDatabase();
        MongoCollection<Document> songs = db.getCollection("songs");

        // Search for any song where "artists" contains "Arctic Monkeys"
        List<Document> results = songs.find(
                new Document("artists", new Document("$regex", "Arctic Monkeys").append("$options", "i"))
        ).into(new ArrayList<>());

        System.out.println("Found " + results.size() + " Arctic Monkeys song(s).");

        for (Document doc : results) {
            // SAFE STRING CONVERSION
            String title = String.valueOf(doc.get("track_name"));
            String album = String.valueOf(doc.get("album_name"));
            String genre = String.valueOf(doc.get("track_genre"));

            System.out.println("Title: " + title);
            System.out.println("Album: " + album);
            System.out.println("Genre: " + genre);
            System.out.println("-----------------------");
        }

        assertTrue(results.size() > 0, "There should be at least one Arctic Monkeys song.");
    }




}
