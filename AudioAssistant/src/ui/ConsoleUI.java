package ui;
import exceptions.*;
import model.*;
import persistence.CatalogFileRepo;
import java.io.IOException;
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
                        addObjectToPlaylist();
                        break;
                    case "5":
                        removeObjectFromPlaylist();
                        break;
                    case "6":
                        printInfo();
                        break;
                    case "7":
                        filterObjects();
                        break;
                    case "8":
                        sortAndPrint();
                        break;
                    case "9":
                        saveCatalog();
                        break;
                    case "10":
                        loadCatalog();
                        break;
                    case "11":
                        saveSinglePlaylist();
                        break;
                    case "12":
                        loadSinglePlaylist();
                        break;
                    case "X":
                        printEntireCatalog();
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
        System.out.println("1. Add new object (song/podcast/audiobook/album/playlist)");
        System.out.println("2. Delete object");
        System.out.println("3. Search object (title/author/genre/year...)");
        System.out.println("4. Add object to playlist");
        System.out.println("5. Remove object from playlist");
        System.out.println("6. Show info about a certain object");
        System.out.println("7. Filter objects (genre/author/year/category...)");
        System.out.println("8. Sort objects (all or inside playlist/album)");
        System.out.println("9. Save catalog to file");
        System.out.println("10. Load catalog from file");
        System.out.println("11. Save single playlist to file");
        System.out.println("12. Load single playlist from file");
        System.out.println("X. Print entire catalog");
        System.out.println("0. Exit");
        System.out.print("> ");
    }

    private void addNewObject() throws Exception {
        System.out.println("\nAdd type:");
        System.out.println("1. Song");
        System.out.println("2. Podcast");
        System.out.println("3. Audiobook");
        System.out.println("4. Album");
        System.out.println("5. Playlist");
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
        if (type.equals("5")) {
            Playlist playlist = createPlaylist();
            catalog.addPlaylist(playlist);
            System.out.println("Playlist created.");
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
    private Playlist createPlaylist() {
        String name = ask("Playlist name: ");
        return new Playlist(name);
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
            String answer = ask("Are you sure? Y/N:");
            if(answer.equalsIgnoreCase("Y")) {
                catalog.removeItem(item);
                System.out.println("Deleted item.");
            }
            return;
        }
        if (type.equals("2")) {
            Album album = findAlbumByName(ask("Album name: "));
            String answer = ask("Are you sure? Y/N:");
            if(answer.equalsIgnoreCase("Y")) {
                catalog.removeAlbum(album);
                System.out.println("Deleted album.");
            }
            return;
        }
        if (type.equals("3")) {
            Playlist playlist = findPlaylistByName(ask("Playlist name: "));
            String answer = ask("Are you sure? Y/N:");
            if(answer.equalsIgnoreCase("Y")) {
                catalog.removePlaylist(playlist);
                try {
                    boolean deleted = fileRepo.deletePlaylistFile(playlist.getNameOfPlaylist());
                    if (deleted) {
                        System.out.println("Playlist deleted (and its file was deleted).");
                    } else {
                        System.out.println("Playlist deleted (no separate file found).");
                    }
                } catch (IOException e) {
                    System.out.println("Playlist deleted, but file could not be deleted: " + e.getMessage());
                }
            }
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
    private void printEntireCatalog() {
        System.out.println("\n=== FULL CATALOG ===");
        System.out.println("\n--- AUDIO ITEMS ---");
        if (catalog.getItems().isEmpty()) {
            System.out.println("No audio items.");
        } else {
            for (AudioItem item : catalog.getItems()) {
                System.out.println(item.printInfo());
            }
        }
        System.out.println("\n--- ALBUMS ---");
        if (catalog.getAlbums().isEmpty()) {
            System.out.println("No albums.");
        } else {
            for (Album album : catalog.getAlbums()) {
                System.out.println(album.printAlbum());
            }
        }
        System.out.println("\n--- PLAYLISTS ---");
        if (catalog.getPlaylists().isEmpty()) {
            System.out.println("No playlists.");
        } else {
            for (Playlist playlist : catalog.getPlaylists()) {
                System.out.println(playlist.printPlaylist());
            }
        }
    }
    private void filterObjects() {
        System.out.println("\nFilter AudioItems by:");
        System.out.println("1. Name/ Title");
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
        fileRepo.savePlaylist(playlist);
        System.out.println("Playlist saved.");
    }

    private void loadSinglePlaylist() throws Exception {
        String name = ask("Playlist name: ");
        Playlist playlist = fileRepo.loadPlaylist(name);
        catalog.addPlaylist(playlist);
        System.out.println("Playlist loaded and added to catalog.");
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private AudioItem findItemByTitle(String title) throws UnavailableItemException {
        for (AudioItem item : catalog.getItems()) {
            if (TextMatchUtil.equalsCrossScript(title, item.getTitle())) {
                return item;
            }
        }
        throw new UnavailableItemException("Item not found");
    }
    private Playlist findPlaylistByName(String name) throws UnavailableItemException {
        for (Playlist playlist : catalog.getPlaylists()) {
            if (TextMatchUtil.equalsCrossScript(name, playlist.getNameOfPlaylist())) {
                return playlist;
            }
        }
        throw new UnavailableItemException("Playlist not found");
    }
    private Album findAlbumByName(String name) throws UnavailableItemException {
        for (Album album : catalog.getAlbums()) {
            if (TextMatchUtil.equalsCrossScript(name, album.getNameOfAlbum())) {
                return album;
            }
        }
        throw new UnavailableItemException("Album not found");
    }
}
