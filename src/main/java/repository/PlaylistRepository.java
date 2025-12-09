package repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Playlist;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing playlists stored in MongoDB.
 * Supports creating playlists and adding/removing songs.
 */
public class PlaylistRepository {

    private final MongoCollection<Document> playlists;

    public PlaylistRepository(MongoDatabase db) {
        playlists = db.getCollection("playlists");
        System.out.println("[repo] PlaylistRepository initialized (collection='playlists')");
    }

    public List<Playlist> getAllPlaylists() {
        List<Playlist> list = new ArrayList<>();
        for (Document d : playlists.find()) {
            list.add(new Playlist(
                    d.getObjectId("_id").toHexString(),
                    d.getString("name"),
                    d.getList("songs", String.class)));
        }
        return list;
    }

    public String createPlaylist(String name) {
        Document doc = new Document("name", name)
                .append("songs", new ArrayList<>());
        playlists.insertOne(doc);
        System.out.println("[repo] Created playlist '" + name + "' with id " + doc.getObjectId("_id").toHexString());
        return doc.getObjectId("_id").toHexString();
    }

    public void addSong(String playlistId, String songId) {
        System.out.println("[repo] Adding song '" + songId + "' to playlist '" + playlistId + "'");
        playlists.updateOne(
                new Document("_id", new ObjectId(playlistId)),
                new Document("$addToSet", new Document("songs", songId)));
    }

    public void removeSong(String playlistId, String songId) {
        System.out.println("[repo] Removing song '" + songId + "' from playlist '" + playlistId + "'");
        playlists.updateOne(
                new Document("_id", new ObjectId(playlistId)),
                new Document("$pull", new Document("songs", songId)));
    }

    public Playlist getPlaylist(String playlistId) {
        Document d = playlists.find(new Document("_id", new ObjectId(playlistId))).first();
        if (d == null)
            return null;

        return new Playlist(
                playlistId,
                d.getString("name"),
                d.getList("songs", String.class));
    }
}
