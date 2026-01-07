package persistence;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class CatalogFileRepo {

    private final Gson gson;
    private static final Path CATALOG_PATH = Path.of("data", "catalog.json");
    private static final Path DATA_DIR = Path.of("data");

    public CatalogFileRepo() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(AudioItem.class, new AudioItemAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    private void ensureCatalogFileExists() throws IOException {
        if (Files.notExists(CATALOG_PATH)) {
            Files.createDirectories(CATALOG_PATH.getParent());
            Files.writeString(CATALOG_PATH, gson.toJson(new CatalogDTO()));
        }
    }

    public void saveCatalog(AudioCatalog catalog) throws IOException {
        ensureCatalogFileExists();
        CatalogDTO dto = new CatalogDTO();
        dto.items.addAll(catalog.getItems());
        dto.albums.addAll(catalog.getAlbums());
        dto.playlists.addAll(catalog.getPlaylists());
        String json = gson.toJson(dto);
        Files.writeString(CATALOG_PATH, json);
    }

    public AudioCatalog loadCatalog() throws IOException {
        ensureCatalogFileExists();
        String json = Files.readString(CATALOG_PATH);
        if (json.isBlank() || json.equals("{}")) {
            return new AudioCatalog();
        }
        CatalogDTO dto = gson.fromJson(json, CatalogDTO.class);
        AudioCatalog catalog = new AudioCatalog();
        if (dto != null) {
            if (dto.items != null) catalog.getItems().addAll(dto.items);
            if (dto.albums != null) catalog.getAlbums().addAll(dto.albums);
            if (dto.playlists != null) catalog.getPlaylists().addAll(dto.playlists);
        }
        return catalog;
    }

    public void savePlaylist(Playlist playlist) throws IOException {
        Files.createDirectories(DATA_DIR);
        Path path = DATA_DIR.resolve(playlist.getNameOfPlaylist() + ".json");
        String json = gson.toJson(playlist);
        Files.writeString(path, json);
    }


    public Playlist loadPlaylist(String playlistName) throws IOException {
        Path path = DATA_DIR.resolve(playlistName + ".json");
        String json = Files.readString(path);
        return gson.fromJson(json, Playlist.class);
    }

    public boolean deletePlaylistFile(String playlistName) throws IOException {
        Files.createDirectories(DATA_DIR); // safe, ако папката не съществува
        Path path = DATA_DIR.resolve(playlistName + ".json");
        return Files.deleteIfExists(path);
    }

}
