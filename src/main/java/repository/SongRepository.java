package repository;

import com.mongodb.client.model.Filters;
import model.Song;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Repository providing access to the `songs` collection in MongoDB.
 * Offers search, retrieval and update operations for `Song` documents.
 */
public class SongRepository {

    private final MongoCollection<Document> songs;

    public SongRepository(MongoDatabase db) {
        this.songs = db.getCollection("songs");
    }

    // Informational logging when repository is created
    public SongRepository() {
        this.songs = null;
        System.out.println("[repo] SongRepository class loaded");
    }

    // Convert a Mongo document into a Song object
    private Song mapToSong(Document doc) {

        String id = String.valueOf(doc.get("_id"));
        String title = String.valueOf(doc.get("track_name"));
        String artist = String.valueOf(doc.get("artists"));

        // FIX: your field is album_name (NOT track_album_name)
        String album = String.valueOf(doc.get("album_name"));

        String genre = String.valueOf(doc.get("track_genre"));

        int duration = 0;
        Object d = doc.get("duration_ms");
        if (d instanceof Number) {
            duration = ((Number) d).intValue() / 1000;
        }

        // THE ONLY CORRECT ORDER
        return new Song(id, title, artist, album, genre, duration);
    }

    // Search songs by title
    public List<Song> searchSongs(String keyword) {
        System.out.println("[search] Searching songs by title with keyword '" + keyword + "'");
        List<Document> docs = songs.find(Filters.regex("track_name", keyword, "i"))
                .into(new ArrayList<>());

        List<Song> results = new ArrayList<>();
        for (Document d : docs) {
            results.add(mapToSong(d));
        }
        return results;
    }

    // Search by artist
    public List<Song> searchByArtist(String artist) {
        System.out.println("[search] Searching songs by artist with keyword '" + artist + "'");
        List<Document> docs = songs.find(Filters.regex("artists", artist, "i"))
                .into(new ArrayList<>());

        List<Song> results = new ArrayList<>();
        for (Document d : docs) {
            results.add(mapToSong(d));
        }
        return results;
    }

    // Get song by ID
    public Song findById(String id) {
        System.out.println("[repo] Retrieving song by id: " + id);
        Document doc = songs.find(eq("_id", id)).first();
        if (doc == null)
            return null;
        return mapToSong(doc);
    }

    // Get all songs (use with care on large collections)
    public List<Song> getAll() {
        List<Document> docs = songs.find().into(new ArrayList<>());
        List<Song> results = new ArrayList<>();
        for (Document d : docs)
            results.add(mapToSong(d));
        return results;
    }

    // Update a song
    public void updateSong(Song song) {
        System.out.println("[update] Updating song id=" + song.getId());
        songs.updateOne(
                eq("_id", song.getId()),
                new Document("$set", new Document()
                        .append("track_name", song.getTitle())
                        .append("artists", song.getArtist())
                        .append("album_name", song.getAlbum())
                        .append("track_genre", song.getGenre())
                        .append("duration_ms", song.getDuration() * 1000)));
    }

    public void updateField(String id, String mongoField, Object value) {
        songs.updateOne(eq("_id", id),
                new Document("$set", new Document(mongoField, value)));
    }
}
