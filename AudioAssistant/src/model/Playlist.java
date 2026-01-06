package model;

import exceptions.DuplicateItemException;
import exceptions.UnavailableItemException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;
public class Playlist {
    private String nameOfPlaylist;
    private ArrayList<AudioItem> items;

    public Playlist(String nameOfPlaylist) {
        this.nameOfPlaylist = nameOfPlaylist;
        this.items = new ArrayList<>();
    }

    public String getNameOfPlaylist() {
        return nameOfPlaylist;
    }

    public void addItem(AudioItem item) throws DuplicateItemException {
        if(!items.contains(item)){
            items.add(item);
        }else{
            throw new DuplicateItemException(String.format("The %s is already in the playlist!", item.getGenre()));
        }
    }
    public void removeItem(AudioItem item) throws UnavailableItemException {
        if(items.contains(item)){
            items.remove(item);
        }else{
            throw new UnavailableItemException(String.format("There is no such %s in the playlist!", item.getGenre()));
        }
    }
    public Duration getTotalDuration(){
        Duration total = Duration.ZERO;
        for (AudioItem item: items) {
            total = total.plus(item.getDuration());
        }
        return total;
    }

    public ArrayList<AudioItem> getSortedPlaylist(){
        ArrayList<AudioItem> sorted = new ArrayList<>(items);
        sorted.sort((a,b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        return sorted;
    }

    public String printPlaylist() {
        return String.format(
                "Playlist: %s | Duration: %s | Number of content: %d",
                nameOfPlaylist,
                AudioItem.formatDuration(getTotalDuration()),
                items.size()
        );
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Playlist)) return false;
        Playlist p = (Playlist) o;
        return nameOfPlaylist.equalsIgnoreCase(p.nameOfPlaylist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfPlaylist.toLowerCase());
    }

}
