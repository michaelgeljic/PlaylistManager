package repository;


import model.Song;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class SongRepository {

    private final MongoCollection<Document> songs;

    public SongRepository(MongoDatabase db) {
        this.songs = db.getCollection("songs");
    }

    // Convert a Mongo document into a Song object
    private Song mapToSong(Document doc) {
        String id = String.valueOf(doc.get("_id"));
        String artist = String.valueOf(doc.get("artists"));
        String album = String.valueOf(doc.get("album_name"));
        String title = String.valueOf(doc.get("track_name"));
        String genre = String.valueOf(doc.get("track_genre"));

        int durationSeconds = 0;
        Object durationObj = doc.get("duration_ms");
        if (durationObj instanceof Number) {
            durationSeconds = ((Number) durationObj).intValue() / 1000;
        }

        return new Song(id, artist, album, title, genre, durationSeconds);
    }


    // Search songs by title
    public List<Song> searchSongs(String keyword) {
        List<Document> docs = songs.find(regex("track_name", keyword, "i")).into(new ArrayList<>());
        List<Song> results = new ArrayList<>();
        for (Document d : docs) {
            results.add(mapToSong(d));
        }
        return results;
    }

    // Get a single song by ID
    public Song findById(String id) {
        Document doc = songs.find(eq("_id", id)).first();
        if (doc == null) return null;
        return mapToSong(doc);
    }

    // Update a field (you will map logical name -> CSV name in service layer)
    public void updateField(String id, String mongoField, Object value) {
        songs.updateOne(eq("_id", id),
                new Document("$set", new Document(mongoField, value)));
    }

    public void updateSong(Song song) {
    songs.updateOne(
        new Document("_id", song.getId()),
        new Document("$set", new Document()
            .append("track_name", song.getTitle())
            .append("artists", song.getArtist())
            .append("album_name", song.getAlbum())
            .append("track_genre", song.getGenre())
            .append("duration_ms", song.getDuration() * 1000)
        )
    );
}
}