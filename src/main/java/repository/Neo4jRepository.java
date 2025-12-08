package repository;

import org.neo4j.driver.*;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class Neo4jRepository {

    private final Driver driver;

    public Neo4jRepository(Driver driver) {
        this.driver = driver;
    }

    // Create an artist node if it doesn't exist
    public void createArtist(String name) {
        try (Session session = driver.session()) {
            session.run(
                "MERGE (:Artist {name: $name})",
                parameters("name", name)
            );
        }
    }

    // Create SIMILAR_TO relationship
    public void createRelation(String a1, String a2) {
        try (Session session = driver.session()) {
            session.run(
                "MATCH (a:Artist {name: $a1}), (b:Artist {name: $a2}) " +
                "MERGE (a)-[:SIMILAR_TO]->(b)",
                parameters("a1", a1, "a2", a2)
            );
        }
    }

    // Get related artists
    public List<String> findRelatedArtists(String name) {
        try (Session session = driver.session()) {
            return session.run(
                "MATCH (:Artist {name: $name})-[:SIMILAR_TO]->(other) " +
                "RETURN other.name AS name",
                parameters("name", name)
            ).list(record -> record.get("name").asString());
        }
    }
}