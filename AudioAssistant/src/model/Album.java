package model;
import exceptions.DuplicateItemException;
import exceptions.UnavailableItemException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;
public class Album {
    private String nameOfAlbum;
    private ArrayList <Song> songs;
    private String genre;
    private String category;
    private String author;
    private int year;

    public Album(String nameOfAlbum, ArrayList<Song> songs, String genre, String category, String author, int year) {
        this.nameOfAlbum = nameOfAlbum;
        if (songs == null){
            this.songs = new ArrayList<>();
        }else {
            this.songs = songs;
        }
        this.genre = genre;
        this.category = category;
        this.author = author;
        this.year = year;
    }

    public String getNameOfAlbum() {
        return nameOfAlbum;
    }

    public void addSong(Song song) throws DuplicateItemException {
        if(!songs.contains(song)) {
            songs.add(song);
        }else{
            throw new DuplicateItemException("The song is in thе album already!");
        }
    }
    public void removeSong(Song song) throws UnavailableItemException {
        if (songs.contains(song)) {
            songs.remove(song);
        } else {
            throw new UnavailableItemException("There is no such song in the album.");
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
    public String printAlbum() {
        return String.format(
                "Album: %s | Author: %s | Year: %d | Genre: %s | Category: %s | Duration: %s | Songs: %d",
                nameOfAlbum,
                author,
                year,
                genre,
                category,
                AudioItem.formatDuration(getTotalDuration()),
                songs.size()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        Album a = (Album) o;
        return nameOfAlbum.equalsIgnoreCase(a.nameOfAlbum)
                && author.equalsIgnoreCase(a.author)
                && year == a.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfAlbum.toLowerCase(), author.toLowerCase(), year);
    }

}
