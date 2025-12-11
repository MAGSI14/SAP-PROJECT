import java.time.Duration;

public class Podcast extends AudioItem{

    public Podcast(String title, String genre, Duration duration, String category, String author, int year) throws InvalidYearException, DurationException {
        super(title, genre, duration, category, author, year);
    }

    @Override public String printInfo(){
        return String.format("The podcast is called %s with duration of %s, by %s and published in %d", getTitle(), formatDuration(getDuration()), getAuthor(), getYear());
    }
}
