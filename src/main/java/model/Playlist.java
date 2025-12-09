package model;

import java.util.List;

/**
 * Domain model representing a playlist which contains a name and
 * an ordered list of song IDs.
 */
public class Playlist {
    private String id;
    private String name;
    private List<String> songIds;

    public Playlist(String id, String name, List<String> songIds) {
        this.id = id;
        this.name = name;
        this.songIds = songIds;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void addSong(String songId) {
        if (!songIds.contains(songId))
            songIds.add(songId);
    }

    public void removeSong(String songId) {
        songIds.remove(songId);
    }
}
