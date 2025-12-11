import java.time.Duration;

public class Audiobook extends AudioItem{
    public Audiobook(String title, String genre, Duration duration, String category, String author, int year) throws InvalidYearException, DurationException {
        super(title, genre, duration, category, author, year);
    }
    @Override public String printInfo(){
        return String.format("The audiobook %s by %s is published in %d with duration of %s", getTitle(), getAuthor(), getYear(), formatDuration(getDuration()));
    }
}
