package service;

import repository.Neo4jRepository;
import java.util.List;

public class Neo4jService {

    private final Neo4jRepository repo;

    public Neo4jService(Neo4jRepository repo) {
        this.repo = repo;
    }

    // Create artist node if missing
    public void addArtist(String artist) {
        repo.createArtist(artist);
    }

    // Connect two artists
    public void relateArtists(String a1, String a2) {
        repo.createRelation(a1, a2);
    }

    // Get a list of related artists
    public List<String> getRelated(String artist) {
        return repo.findRelatedArtists(artist);
    }
}