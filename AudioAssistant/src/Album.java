import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;
public class Album {
    private String nameOfAlbum;
    private String authorOfAlbum;
    private ArrayList <Song> songs;

    public Album(String nameOfAlbum, String authorOfAlbum, ArrayList<Song> songs) {
        this.nameOfAlbum = nameOfAlbum;
        this.authorOfAlbum = authorOfAlbum;
        this.songs = songs;
    }

    public String getNameOfAlbum() {
        return nameOfAlbum;
    }

    public void setNameOfAlbum(String nameOfAlbum) {
        this.nameOfAlbum = nameOfAlbum;
    }

    public String getAuthorOfAlbum() {
        return authorOfAlbum;
    }

    public void setAuthorOfAlbum(String authorOfAlbum) {
        this.authorOfAlbum = authorOfAlbum;
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
                Objects.equals(authorOfAlbum, album.authorOfAlbum) &&
                Objects.equals(songs, album.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfAlbum, authorOfAlbum, songs);
    }

}
