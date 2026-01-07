import model.AudioCatalog;
import persistence.CatalogFileRepo;
import ui.ConsoleUI;
public class Main {
    public static void main(String[] args) {
        CatalogFileRepo repo = new CatalogFileRepo();
        AudioCatalog catalog;
        try {
            catalog = repo.loadCatalog();
        } catch (Exception e) {
            System.out.println("Starting with empty catalog (cannot load file).");
            catalog = new AudioCatalog();
        }
        ConsoleUI ui = new ConsoleUI(catalog);
        ui.start();
    }
}
