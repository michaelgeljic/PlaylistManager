package service;

import model.Song;
import repository.SongRepository;

import java.util.List;

public class SongService {

    private final SongRepository repo;

    public SongService(SongRepository repo) {
        this.repo = repo;
    }

    public List<Song> searchSongs(String keyword) {
        return repo.searchSongs(keyword);
    }

    public Song getSong(String id) {
        return repo.findById(id);
    }

    // Update logical fields (user-friendly fields)
    public void updateSong(String id, String field, Object value) {
        String mongoField = switch (field) {
            case "title" -> "track_name";
            case "artist" -> "artists";
            case "album" -> "album_name";
            case "genre" -> "track_genre";
            case "duration" -> "duration_ms";
            default -> null;
        };

        if (mongoField != null) {
            repo.updateField(id, mongoField, value);
        } else {
            System.out.println("Invalid field: " + field);
        }
    }
}
