package service;

import model.Song;
import repository.SongRepository;

import java.util.*;

/**
 * Service layer for song operations.
 * Provides methods for searching, retrieving and updating songs via the
 * repository.
 */
public class SongService {

    private final SongRepository repo;

    public SongService(SongRepository repo) {
        this.repo = repo;
    }

    public List<Song> searchSongs(String keyword) {
        keyword = keyword.trim();
        if (keyword.isEmpty())
            return new ArrayList<>();

        System.out.println("[service] Performing search for: '" + keyword + "'");

        // If text contains words, try both title and artist
        List<Song> byTitle = repo.searchSongs(keyword);
        List<Song> byArtist = repo.searchByArtist(keyword);

        // Merge without duplicates
        Set<String> ids = new HashSet<>();
        List<Song> merged = new ArrayList<>();

        for (Song s : byTitle) {
            if (ids.add(s.getId()))
                merged.add(s);
        }
        for (Song s : byArtist) {
            if (ids.add(s.getId()))
                merged.add(s);
        }

        return merged;
    }

    public Song getSong(String id) {
        System.out.println("[service] getSong: " + id);
        return repo.findById(id);
    }

    public List<Song> getAllSongs() {
        return repo.getAll();
    }

    public void updateSong(Song song) {
        System.out.println("[service] updateSong: " + song.getId());
        repo.updateSong(song);
    }

}
