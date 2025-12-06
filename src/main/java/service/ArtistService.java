package service;



import repository.ArtistRepository;

import java.util.List;

public class ArtistService {

    private final ArtistRepository repo;

    public ArtistService(ArtistRepository repo) {
        this.repo = repo;
    }

    public List<String> getRelatedArtists(String name) {
        return repo.findRelatedArtists(name);
    }
}
