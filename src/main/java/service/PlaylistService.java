package service;

import model.Playlist;
import repository.PlaylistRepository;

import java.util.List;

/**
 * Service layer for playlist operations.
 * Provides methods for creating, retrieving and managing playlists.
 */
public class PlaylistService {

    private final PlaylistRepository repo;

    public PlaylistService(PlaylistRepository repo) {
        this.repo = repo;
    }

    public List<Playlist> getAll() {
        return repo.getAllPlaylists();
    }

    public String create(String name) {
        return repo.createPlaylist(name);
    }

    public void addSong(String playlistId, String songId) {
        repo.addSong(playlistId, songId);
    }

    public void removeSong(String playlistId, String songId) {
        repo.removeSong(playlistId, songId);
    }

    public Playlist get(String id) {
        return repo.getPlaylist(id);
    }
}
