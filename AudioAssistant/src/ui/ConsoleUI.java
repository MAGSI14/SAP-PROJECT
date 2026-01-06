package ui;
import exceptions.*;
import model.*;
import persistence.CatalogFileRepo;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleUI {

    private final AudioCatalog catalog;
    private final CatalogFileRepo fileRepo;
    private final Scanner scanner;
    public ConsoleUI(AudioCatalog catalog) {
        this.catalog = catalog;
        this.fileRepo = new CatalogFileRepo();
        this.scanner = new Scanner(System.in);
    }
    public void start() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        addNewObject();
                        break;
                    case "2":
                        deleteObject();
                        break;
                    case "3":
                        searchObjects();
                        break;
                    case "4":
                        createPlaylist();
                        break;
                    case "5":
                        addObjectToPlaylist();
                        break;
                    case "6":
                        removeObjectFromPlaylist();
                        break;
                    case "7":
                        printInfo();
                        break;
                    case "8":
                        filterObjects();
                        break;
                    case "9":
                        sortAndPrint();
                        break;
                    case "10":
                        saveCatalog();
                        break;
                    case "11":
                        loadCatalog();
                        break;
                    case "12":
                        saveSinglePlaylist();
                        break;
                    case "13":
                        loadSinglePlaylist();
                        break;
                    case "0":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    private void printMenu() {
        System.out.println("\n=== PERSONAL AUDIO CATALOG ===");
        System.out.println("1. Add new object (song/podcast/audiobook/album)");
        System.out.println("2. Delete object");
        System.out.println("3. Search object (title/author/genre/year...)");
        System.out.println("4. Create playlist");
        System.out.println("5. Add object to playlist");
        System.out.println("6. Remove object from playlist");
        System.out.println("7. Show info (playlist/song/album/item)");
        System.out.println("8. Filter objects (genre/author/year/category...)");
        System.out.println("9. Sort objects (all or inside playlist)");
        System.out.println("10. Save catalog to file");
        System.out.println("11. Load catalog from file");
        System.out.println("12. Save single playlist to file");
        System.out.println("13. Load single playlist from file");
        System.out.println("0. Exit");
        System.out.print("> ");
    }

    private void addNewObject() throws Exception {
        System.out.println("\nAdd type:");
        System.out.println("1. Song");
        System.out.println("2. Podcast");
        System.out.println("3. Audiobook");
        System.out.println("4. Album");
        System.out.print("> ");
        String type = scanner.nextLine().trim();
        if (type.equals("1")) {
            AudioItem item = createAudioItem("song");
            catalog.addItem(item);
            System.out.println("Added: " + item.printInfo());
            return;
        }
        if (type.equals("2")) {
            AudioItem item = createAudioItem("podcast");
            catalog.addItem(item);
            System.out.println("Added: " + item.printInfo());
            return;
        }
        if (type.equals("3")) {
            AudioItem item = createAudioItem("audiobook");
            catalog.addItem(item);
            System.out.println("Added: " + item.printInfo());
            return;
        }
        if (type.equals("4")) {
            Album album = createAlbum();
            catalog.addAlbum(album);
            System.out.println("Added album: " + album.printAlbum());
            return;
        }
        System.out.println("Unknown type.");
    }
    private AudioItem createAudioItem(String type) throws Exception {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Duration (mm:ss or hh:mm:ss): ");
        Duration duration = AudioItem.formatDuration(scanner.nextLine());
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Author/Performer: ");
        String author = scanner.nextLine();
        System.out.print("Year: ");
        int year = Integer.parseInt(scanner.nextLine());
        if (type.equals("song")) {
            return new Song(title, genre, duration, category, author, year);
        } else if (type.equals("podcast")) {
            return new Podcast(title, genre, duration, category, author, year);
        } else if (type.equals("audiobook")) {
            return new Audiobook(title, genre, duration, category, author, year);
        }
        throw new Exception("Invalid audio item type.");
    }
    private Album createAlbum() throws Exception {
        System.out.print("Album name: ");
        String name = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Author/Performer: ");
        String author = scanner.nextLine();
        System.out.print("Year: ");
        int year = Integer.parseInt(scanner.nextLine());
        if (year < 1860 || year > java.time.Year.now().getValue()) {
            throw new InvalidYearException("Invalid album year.");
        }
        Album album = new Album(name, null, genre, category, author, year);
        System.out.print("How many songs in this album? ");
        int count = Integer.parseInt(scanner.nextLine());
        if (count <= 0) {
            throw new Exception("Album must contain at least 1 song.");
        }
        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Song " + (i + 1) + "/" + count + " ---");
            Song song = createSongFromInput();
            try {
                album.addSong(song);
            } catch (DuplicateItemException ex) {
                System.out.println(ex.getMessage());
                i--;
                continue;
            }
            if (!catalog.getItems().contains(song)) {
                catalog.addItem(song);
            }
        }
        return album;
    }

    private Song createSongFromInput() throws Exception {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Duration (mm:ss or hh:mm:ss): ");
        Duration duration = AudioItem.formatDuration(scanner.nextLine());
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Author/Performer: ");
        String author = scanner.nextLine();
        System.out.print("Year: ");
        int year = Integer.parseInt(scanner.nextLine());
        return new Song(title, genre, duration, category, author, year);
    }


    private void deleteObject() throws Exception {
        System.out.println("\nDelete type:");
        System.out.println("1. Audio item (song/podcast/audiobook) by title");
        System.out.println("2. Album by name");
        System.out.println("3. Playlist by name");
        System.out.print("> ");
        String type = scanner.nextLine().trim();
        if (type.equals("1")) {
            AudioItem item = findItemByTitle(ask("Title: "));
            catalog.removeItem(item);
            System.out.println("Deleted item.");
            return;
        }
        if (type.equals("2")) {
            Album album = findAlbumByName(ask("Album name: "));
            catalog.removeAlbum(album);
            System.out.println("Deleted album.");
            return;
        }
        if (type.equals("3")) {
            Playlist playlist = findPlaylistByName(ask("Playlist name: "));
            catalog.removePlaylist(playlist);
            System.out.println("Deleted playlist.");
            return;
        }
        System.out.println("Invalid option.");
    }

    private void searchObjects() {
        System.out.print("Search phrase: ");
        String phrase = scanner.nextLine();
        ArrayList<AudioItem> results = catalog.search(phrase);
        if (results.isEmpty()) {
            System.out.println("No results.");
            return;
        }
        System.out.println("\n--- RESULTS (AudioItems) ---");
        for (AudioItem item : results) {
            System.out.println(item.printInfo());
        }
    }
    private void createPlaylist() throws DuplicateItemException {
        String name = ask("Playlist name: ");
        Playlist playlist = new Playlist(name);
        catalog.addPlaylist(playlist);
        System.out.println("Playlist created.");
    }

    private void addObjectToPlaylist() throws Exception {
        Playlist playlist = findPlaylistByName(ask("Playlist name: "));
        AudioItem item = findItemByTitle(ask("Item title: "));
        playlist.addItem(item);
        System.out.println("Item added to playlist.");
    }

    private void removeObjectFromPlaylist() throws Exception {
        Playlist playlist = findPlaylistByName(ask("Playlist name: "));
        AudioItem item = findItemByTitle(ask("Item title: "));
        playlist.removeItem(item);
        System.out.println("Item removed from playlist.");
    }

    private void printInfo() throws Exception {
        System.out.println("\nInfo for:");
        System.out.println("1. Audio item (by title)");
        System.out.println("2. Album (by name)");
        System.out.println("3. Playlist (by name)");
        System.out.print("> ");
        String type = scanner.nextLine().trim();
        if (type.equals("1")) {
            AudioItem item = findItemByTitle(ask("Title: "));
            System.out.println(item.printInfo());
            return;
        }
        if (type.equals("2")) {
            Album album = findAlbumByName(ask("Album name: "));
            System.out.println(album.printAlbum());
            return;
        }
        if (type.equals("3")) {
            Playlist playlist = findPlaylistByName(ask("Playlist name: "));
            System.out.println(playlist.printPlaylist());
            return;
        }
        System.out.println("Invalid option.");
    }

    private void filterObjects() {
        System.out.println("\nFilter AudioItems by:");
        System.out.println("1. Name");
        System.out.println("2. Genre");
        System.out.println("3. Author");
        System.out.println("4. Year");
        System.out.println("5. Category");
        System.out.println("6. Duration");
        System.out.print("> ");
        String choice = scanner.nextLine().trim();
        ArrayList<AudioItem> results = new ArrayList<>();
        if(choice.equals("1")){
            results = catalog.filterByName(ask("Name:"));
        } else if (choice.equals("2")) {
            results = catalog.filterByGenre(ask("Genre: "));
        } else if (choice.equals("3")) {
            results = catalog.filterByAuthor(ask("Author: "));
        } else if (choice.equals("4")) {
            results = catalog.filterByYear(Integer.parseInt(ask("Year: ")));
        } else if (choice.equals("5")) {
            results = catalog.filterByCategory(ask("Category: "));
        } else if(choice.equals("6")) {
            results = catalog.filterByDuration(ask("Duration: "));
        }else{
            System.out.println("Invalid option.");
            return;
        }
        if (results.isEmpty()) {
            System.out.println("No results.");
            return;
        }
        for (AudioItem item : results) {
            System.out.println(item.printInfo());
        }
    }

    private void sortAndPrint() throws Exception {
        System.out.println("\nSort:");
        System.out.println("1. All AudioItems (catalog)");
        System.out.println("2. Albums (catalog)");
        System.out.println("3. Playlists (catalog)");
        System.out.println("4. Items inside a playlist (by title)");
        System.out.println("5. Songs inside an album (by title)");
        System.out.print("> ");
        String choice = scanner.nextLine().trim();
        if (choice.equals("1")) {
            ArrayList<AudioItem> sorted = catalog.getSortedItems();
            for (AudioItem item : sorted) {
                System.out.println(item.printInfo());
            }
            return;
        }
        if (choice.equals("2")) {
            ArrayList<Album> sorted = catalog.getSortedAlbums();
            for (Album album : sorted) {
                System.out.println(album.printAlbum());
            }
            return;
        }
        if (choice.equals("3")) {
            ArrayList<Playlist> sorted = catalog.getSortedPlaylists();
            for (Playlist playlist : sorted) {
                System.out.println(playlist.printPlaylist());
            }
            return;
        }
        if (choice.equals("4")) {
            Playlist playlist = findPlaylistByName(ask("Playlist name: "));
            ArrayList<AudioItem> sorted = playlist.getSortedPlaylist();
            for (AudioItem item : sorted) {
                System.out.println(item.printInfo());
            }
            return;
        }
        if (choice.equals("5")) {
            Album album = findAlbumByName(ask("Album name: "));
            ArrayList<Song> sorted = album.getSortedAlbum();
            for (Song item : sorted) {
                System.out.println(item.printInfo());
            }
            return;
        }
        System.out.println("Invalid option.");
    }

    private void saveCatalog() throws IOException {
        fileRepo.saveCatalog(catalog);
        System.out.println("Catalog saved.");
    }
    private void loadCatalog() throws IOException {
        AudioCatalog loaded = fileRepo.loadCatalog();
        catalog.getItems().clear();
        catalog.getAlbums().clear();
        catalog.getPlaylists().clear();
        catalog.getItems().addAll(loaded.getItems());
        catalog.getAlbums().addAll(loaded.getAlbums());
        catalog.getPlaylists().addAll(loaded.getPlaylists());
        System.out.println("Catalog loaded.");
    }

    private void saveSinglePlaylist() throws Exception {
        Playlist playlist = findPlaylistByName(ask("Playlist name: "));
        Path path = Path.of(ask("File path (e.g. data/playlist.json): "));
        fileRepo.savePlaylist(playlist, path);
        System.out.println("Playlist saved to file.");
    }
    private void loadSinglePlaylist() throws Exception {
        Path path = Path.of(ask("File path: "));
        Playlist playlist = fileRepo.loadPlaylist(path);
        catalog.addPlaylist(playlist);
        System.out.println("Playlist loaded and added to catalog.");
    }
    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private AudioItem findItemByTitle(String title) throws UnavailableItemException {
        for (AudioItem item : catalog.getItems()) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                return item;
            }
        }
        throw new UnavailableItemException("Item not found");
    }
    private Playlist findPlaylistByName(String name) throws UnavailableItemException {
        for (Playlist playlist : catalog.getPlaylists()) {
            if (playlist.getNameOfPlaylist().equalsIgnoreCase(name)) {
                return playlist;
            }
        }
        throw new UnavailableItemException("Playlist not found");
    }
    private Album findAlbumByName(String name) throws UnavailableItemException {
        for (Album album : catalog.getAlbums()) {
            if (album.getNameOfAlbum().equalsIgnoreCase(name)) {
                return album;
            }
        }
        throw new UnavailableItemException("Album not found");
    }
}
