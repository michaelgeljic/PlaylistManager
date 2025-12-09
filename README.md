# PlaylistManager  
A JavaFX desktop application for browsing, managing, and analyzing songs stored in MongoDB, with recommendation features powered by Neo4j.

This project provides:  
- Song search by title or artist  
- Song metadata editing  
- Playlist creation and editing  
- Neo4j-based related-artist recommendations  
- Import and population of artists and genres from MongoDB into Neo4j  
- A GUI built with JavaFX

---

## Features

### Search
Search for songs by title keyword or artist name. Results are shown in a table, and selecting one displays details below.

### Update Songs
Edit a song’s title, artist, album, genre, or duration.  
Changes are written directly to MongoDB.

### Related Artists (Neo4j)
Shows artists similar to the selected song’s artist based on Neo4j genre relationships.

### Playlists
- Create playlists  
- Add and remove songs  
- View playlist contents  

Playlists are stored in MongoDB.

### Neo4j Population
Imports all artists and genres from MongoDB, normalizes names, and creates:
- Artist nodes  
- Genre nodes  
- IN_GENRE relationships  
- SIMILAR_TO relationships  

---

## Technology Stack

**Frontend**  
- JavaFX  
- FXML layouts

**Backend**  
- Java 17  
- MongoDB Java Driver  
- Neo4j Java Driver

**Databases**  
- MongoDB Atlas or local MongoDB  
- Neo4j (Docker or local)

---

## Running the Project

### 1. JavaFX Setup
Ensure JavaFX SDK is downloaded.

Typical VM args:
--module-path /path/to/javafx-sdk/lib
--add-modules javafx.controls,javafx.fxml


### 2. MongoDB Configuration
Update connection string in:

src/main/java/config/MongoConfig.java



Collections expected:
- `songs`
- `playlists`

Example document:

json
{
  "_id": "5SuOikwiRyPMVoIQDJUgSV",
  "artists": "Gen Hoshino",
  "album_name": "Comedy",
  "track_name": "Comedy",
  "duration_ms": 230666,
  "track_genre": "acoustic"
}

3. Neo4j Setup

Recommended Docker command:

docker run -p 7687:7687 -p 7474:7474 \
  -e NEO4J_AUTH=neo4j/password \
  -e NEO4J_server_memory_heap_initial__size=2G \
  -e NEO4J_server_memory_heap_max__size=2G \
  -e NEO4J_server_memory_pagecache_size=2G \
  neo4j:latest

Configure credentials in:

src/main/java/config/Neo4jConfig.java

Build and run it!
