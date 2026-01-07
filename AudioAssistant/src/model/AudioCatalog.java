package model;
import exceptions.DuplicateItemException;
import exceptions.UnavailableItemException;

import java.util.*;

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

    public void addItem(AudioItem item) throws DuplicateItemException {
        if(!items.contains(item)) {
            items.add(item);
        }else{
            throw new DuplicateItemException("This item is already in your catalog!");
        }
    }

    public void removeItem(AudioItem item) throws UnavailableItemException {
        if(items.contains(item)){
            items.remove(item);
        }else{
            throw new UnavailableItemException("There is no such item in your catalog!");
        }
    }

    public void addAlbum(Album album) throws DuplicateItemException{
        if(!albums.contains(album)){
            albums.add(album);
        }else{
            throw new DuplicateItemException("This album is already in your catalog!");
        }
    }

    public void removeAlbum(Album album) throws UnavailableItemException{
        if(albums.contains(album)){
            albums.remove(album);
        }else {
            throw new UnavailableItemException("There is no such album in your catalog!");
        }
    }

    public void addPlaylist(Playlist playlist) throws DuplicateItemException{
        if(!playlists.contains(playlist)){
            playlists.add(playlist);
        }else{
            throw new DuplicateItemException("This playlist is already in your catalog!");
        }
    }

    public void removePlaylist(Playlist playlist) throws UnavailableItemException{
        if(playlists.contains(playlist)){
            playlists.remove(playlist);
        }else{
            throw new UnavailableItemException("There is no such playlist in your catalog!");
        }
    }
    public ArrayList<AudioItem> filterByName(String name){
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if (TextMatchUtil.equalsCrossScript(name, i.getTitle())){
                found.add(i);
            }
        }
        return found;
    }

    public ArrayList<AudioItem> filterByAuthor(String author){
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if (TextMatchUtil.containsCrossScript(author, i.getAuthor()))
            {
                found.add(i);
            }
        }
        return found;
    }

    public ArrayList<AudioItem> filterByGenre(String genre){
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if (TextMatchUtil.equalsCrossScript(genre, i.getGenre())){
                found.add(i);
            }
        }
        return found;
    }
    public ArrayList<AudioItem> filterByDuration(String duration){
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if(AudioItem.formatDuration(i.getDuration()).equals(duration)){
                found.add(i);
            }
        }
        return found;
    }

    public ArrayList<AudioItem> filterByCategory(String category){
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if (TextMatchUtil.equalsCrossScript(category, i.getCategory())){
                found.add(i);
            }
        }
        return found;
    }

    public ArrayList<AudioItem> filterByYear(int year){
        ArrayList<AudioItem> found = new ArrayList<>();
        for (AudioItem i: items) {
            if(i.getYear() == year){
                found.add(i);
            }
        }
        return found;
    }

    public ArrayList<AudioItem> search(String phrase) {
        ArrayList<AudioItem> found = new ArrayList<>();
        String normalized = TextMatchUtil.normalize(phrase);
        if (normalized.isBlank()) return found;
        String[] tokens = normalized.split("\\s+");
        for (AudioItem item : items) {
            String record = item.getTitle() + " " + item.getAuthor() + " " + item.getYear() + " " + item.getGenre() + " " + item.getCategory();

            boolean allMatch = true;
            for (String token : tokens) {
                if (!TextMatchUtil.containsCrossScript(token, record)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                found.add(item);
            }
        }
        return found;
    }

    public ArrayList<AudioItem> getSortedItems(){
        ArrayList<AudioItem> sorted = new ArrayList<>(items);
        sorted.sort((a, b) -> {
            int byTitle = a.getTitle().compareToIgnoreCase(b.getTitle());
            if (byTitle != 0) {
                return byTitle;
            }
            return a.getAuthor().compareToIgnoreCase(b.getAuthor());
        });
        return sorted;
    }

    public ArrayList<Album> getSortedAlbums(){
        ArrayList<Album> sorted = new ArrayList<>(albums);
        sorted.sort((a, b) -> a.getNameOfAlbum().compareToIgnoreCase(b.getNameOfAlbum()));
        return sorted;
    }

    public ArrayList<Playlist> getSortedPlaylists(){
        ArrayList<Playlist> sorted = new ArrayList<>(playlists);
        sorted.sort((a, b) -> a.getNameOfPlaylist().compareToIgnoreCase(b.getNameOfPlaylist()));
        return sorted;
    }

}
