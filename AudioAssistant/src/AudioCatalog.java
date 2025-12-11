import java.util.*;
import java.time.Duration;

public class AudioCatalog {
    private ArrayList<AudioItem> items;
    private ArrayList<Album> albums;
    private ArrayList<Playlist> playlists;

    public AudioCatalog() {
        this.items = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }

    public ArrayList<AudioItem> getItems() {
        return items;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void addItem(AudioItem item) throws DuplicateItemException{
        if(!items.contains(item)) {
            items.add(item);
        }else{
            throw new DuplicateItemException("This item is already in your catalog!");
        }
    }

    public void addAlbum(Album album) throws DuplicateItemException{
        if(!albums.contains(album)){
            albums.add(album);
        }else{
            throw new DuplicateItemException("This album is already in your catalog!");
        }
    }

    public void addPlaylist(Playlist playlist) throws DuplicateItemException{
        if(!playlists.contains(playlist)){
            playlists.add(playlist);
        }else{
            throw new DuplicateItemException("This playlist is already in your catalog!");
        }
    }

    public ArrayList<AudioItem> searchByName(String name) throws UnavailableItemException{
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if(i.getTitle().equals(name)){
                found.add(i);
            }
        }
        if(found.size()>0){
            return found;
        }else{
            throw new UnavailableItemException("There is no such item in your catalog!");
        }
    }

    public ArrayList<AudioItem> searchByAuthor(String author)throws UnavailableItemException{
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if(i.getAuthor().equals(author)){
                found.add(i);
            }
        }
        if(found.size()>0){
            return found;
        }else{
            throw new UnavailableItemException("There is no such item in your catalog!");
        }
    }

    public ArrayList<AudioItem> searchByGenre(String genre) throws UnavailableItemException{
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if(i.getGenre().equals(genre)){
                found.add(i);
            }
        }
        if(found.size()>0){
            return found;
        }else{
            throw new UnavailableItemException("There is no such item in your catalog!");
        }
    }
    public ArrayList<AudioItem> searchByDuration(String duration) throws UnavailableItemException{
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if(AudioItem.formatDuration(i.getDuration()).equals(duration)){
                found.add(i);
            }
        }
        if(found.size()>0){
            return found;
        }else{
            throw new UnavailableItemException("There is no such item in your catalog!");
        }
    }

    public ArrayList<AudioItem> searchByCategory(String category) throws UnavailableItemException{
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if(i.getCategory().equals(category)){
                found.add(i);
            }
        }
        if(found.size()>0){
            return found;
        }else{
            throw new UnavailableItemException("There is no such item in your catalog!");
        }
    }

    public ArrayList<AudioItem> searchByYear(int year) throws UnavailableItemException{
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if(i.getYear() == year){
                found.add(i);
            }
        }
        if(found.size()>0){
            return found;
        }else{
            throw new UnavailableItemException("There is no such item in your catalog!");
        }
    }
}
