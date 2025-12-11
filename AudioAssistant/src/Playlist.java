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

    public void setNameOfPlaylist(String nameOfPlaylist) {
        this.nameOfPlaylist = nameOfPlaylist;
    }

    public void addItem(AudioItem item) throws DuplicateItemException{
        if(!items.contains(item)){
            items.add(item);
        }else{
            throw new DuplicateItemException(String.format("The %s is already in the playlist!", item.getGenre()));
        }
    }
    public void removeItem(AudioItem item) throws UnavailableItemException{
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

    public void printPlaylist() {
        System.out.println("Playlist: " + nameOfPlaylist);
        for (AudioItem item : items) {
            System.out.println(" - " + item.printInfo());
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Playlist playlist = (Playlist) o;

        return Objects.equals(nameOfPlaylist, playlist.nameOfPlaylist) &&
                Objects.equals(items, playlist.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfPlaylist, items);
    }

}
