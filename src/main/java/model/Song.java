package model;

/**
 * Domain model representing a music track / song.
 * Contains metadata such as title, artist, album, genre and duration.
 */
public class Song {
    private String id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int duration; // in seconds

    public Song() {
    }

    // FIXED constructor order
    public Song(String id, String title, String artist, String album, String genre, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
