import java.time.Duration;

public class Song extends AudioItem{

    public Song(String title, String genre, Duration duration, String category, String author, int year) throws InvalidYearException, DurationException {
        super(title, genre, duration, category, author, year);
    }

    @Override
    public String printInfo(){
        return String.format("The song %s, with duration of %s, is written by %s and published in %d", getTitle(), formatDuration(getDuration()), getAuthor(), getYear());
    }

}
