package model;


public class Song {
    private String id;
    private String artist;
    private String album;
    private String title;
    private String genre;
    private int duration; // in seconds

    public Song() {}

    public Song(String id, String artist, String album, String title, String genre, int duration) {
        this.id = id;
        this.artist = artist;
        this.album = album;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}