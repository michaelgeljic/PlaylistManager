package service;

import repository.ArtistRepository;

import java.util.List;

/**
 * Service for artist-related operations using the Neo4j graph database.
 */
public class ArtistService {

    private final ArtistRepository repo;

    public ArtistService(ArtistRepository repo) {
        this.repo = repo;
    }

    public List<String> getRelatedArtists(String name) {
        return repo.findRelatedArtists(name);
    }
}
