import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Album {
    private String nameOfAlbum;
    private ArrayList <Song> songs;

    public Album(String nameOfAlbum, ArrayList<Song> songs) {
        this.nameOfAlbum = nameOfAlbum;
        this.songs = songs;
    }

    public String getNameOfAlbum() {
        return nameOfAlbum;
    }

    public void setNameOfAlbum(String nameOfAlbum) {
        this.nameOfAlbum = nameOfAlbum;
    }

    public void addSong(Song song) throws DuplicateItemException{
        if(!songs.contains(song)) {
            songs.add(song);
        }else{
            throw new DuplicateItemException("The song is in thе album already!");
        }
    }

    public void removeSong(Song song) throws UnavailableItemException{
        if(songs.contains(song)){
            songs.remove(song);
        }else{
            throw new UnavailableItemException("There is no such song in the album!");
        }
    }

    public Duration getTotalDuration(){
        Duration total = Duration.ZERO;
        for (Song song: songs) {
            total = total.plus(song.getDuration());
        }
        return total;
    }

    public ArrayList<Song> getSortedAlbum(){
        ArrayList<Song> sorted = new ArrayList<>(songs);
        sorted.sort((a,b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        return sorted;
    }
    public void printAlbum() {
        System.out.println("Album: " + nameOfAlbum);
        for (Song song : songs) {
            System.out.println(" - " + song.printInfo());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        return Objects.equals(nameOfAlbum, album.nameOfAlbum) &&
                Objects.equals(songs, album.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfAlbum, songs);
    }

}
