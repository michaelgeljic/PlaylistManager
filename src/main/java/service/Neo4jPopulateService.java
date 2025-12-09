package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.Neo4jConfig;
import org.bson.Document;
import org.neo4j.driver.Session;

import java.util.*;

public class Neo4jPopulateService {

    private final MongoDatabase mongoDb;

    public Neo4jPopulateService(MongoDatabase mongoDb) {
        this.mongoDb = mongoDb;
    }

    public void populate() {

        MongoCollection<Document> songs = mongoDb.getCollection("songs");

        Map<String, String> artistGenre = new HashMap<>();

        // Extract and normalize
        for (Document d : songs.find()) {

            String rawArtist = String.valueOf(d.get("artists"));
            String rawGenre = String.valueOf(d.get("track_genre"));

            String artist = ArtistNormalizer.extractPrimaryArtist(rawArtist);
            String genre = GenreNormalizer.normalize(rawGenre);
            System.out.println("IMPORTING ARTIST RAW: " + rawArtist);
            System.out.println("IMPORTING ARTIST NORMALIZED: " + artist);
            System.out.println("IMPORTING GENRE: " + genre);

            if (!artist.isBlank() && !genre.isBlank()) {
                artistGenre.put(artist, genre);
            }
        }

        System.out.println("Normalized artists: " + artistGenre.size());

        // Insert into Neo4j
        try (Session session = Neo4jConfig.getDriver().session()) {

            // Insert artists + genres
            for (var entry : artistGenre.entrySet()) {
                session.run("""
                        MERGE (a:Artist {name: $artist})
                        MERGE (g:Genre {name: $genre})
                        MERGE (a)-[:IN_GENRE]->(g)
                        """,
                        Map.of("artist", entry.getKey(), "genre", entry.getValue()));
            }

            // Connect similar artists
            session.run("""
                    MATCH (a1:Artist)-[:IN_GENRE]->(g)<-[:IN_GENRE]-(a2:Artist)
                    WHERE a1 <> a2
                    MERGE (a1)-[:SIMILAR_TO]->(a2)
                    """);

            System.out.println("Neo4j population complete.");
        }
    }
}