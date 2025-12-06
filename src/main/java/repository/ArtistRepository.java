package repository;

import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

public class ArtistRepository {

    private final Driver driver;

    public ArtistRepository(Driver driver) {
        this.driver = driver;
    }

    public List<String> findRelatedArtists(String artistName) {
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> {
                List<String> list = new ArrayList<>();

                var result = tx.run("""
                    MATCH (a:Artist {name: $name})-[:SIMILAR_TO]->(b:Artist)
                    RETURN b.name AS related
                """, Values.parameters("name", artistName));

                while (result.hasNext()) {
                    list.add(result.next().get("related").asString());
                }

                return list;
            });
        }
    }
}
