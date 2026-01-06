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


    public void savePlaylist(Playlist playlist, Path path) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        String json = gson.toJson(playlist);
        Files.writeString(path, json);
    }


    public Playlist loadPlaylist(Path path) throws IOException {
        String json = Files.readString(path);
        return gson.fromJson(json, Playlist.class);
    }

}
