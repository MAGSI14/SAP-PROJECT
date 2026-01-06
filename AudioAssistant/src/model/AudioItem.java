package model;

import exceptions.DurationException;
import exceptions.InvalidYearException;

import java.time.Duration;
import java.time.Year;
import java.util.Objects;

public abstract class AudioItem {
    private String title;
    private String genre;
    private Duration duration;
    private String category;
    private String author;
    private int year;

    public AudioItem(String title, String genre, Duration duration, String category, String author, int year) throws InvalidYearException, DurationException {
        this.title = title;
        this.genre = genre;
        setDuration(duration);
        this.category = category;
        this.author = author;
        setYear(year);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) throws DurationException{
        if(duration.isNegative() || duration.isZero()){
            throw new DurationException("Invalid audio duration!");
        } else {
            this.duration = duration;
        }
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) throws InvalidYearException{
        if(year >= 1860 && year <= Year.now().getValue()){
            this.year = year;
        }else if (year<1860){
            throw new InvalidYearException("You cannot play ooga booga songs! Digital songs had not been invented back then!");
        }else if(year>Year.now().getValue()){
            throw new InvalidYearException("Oh, doctor Emmet Brown, is this you???");
        }
    }

    public static String formatDuration(Duration d){
        long hours = d.toHours();
        long minutes = d.toMinutesPart();
        long seconds = d.toSecondsPart();
        if(hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public static Duration formatDuration(String s) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException("Duration is empty");
        }
        String[] parts = s.trim().split(":");
        try {
            if (parts.length == 2) {
                long minutes = Long.parseLong(parts[0]);
                long seconds = Long.parseLong(parts[1]);
                return Duration.ofMinutes(minutes).plusSeconds(seconds);
            }
            if (parts.length == 3) {
                long hours = Long.parseLong(parts[0]);
                long minutes = Long.parseLong(parts[1]);
                long seconds = Long.parseLong(parts[2]);
                return Duration.ofHours(hours)
                        .plusMinutes(minutes)
                        .plusSeconds(seconds);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid duration: " + s);
        }
        throw new IllegalArgumentException("Invalid duration format: " + s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AudioItem that = (AudioItem) o;

        return year == that.year &&
                Objects.equals(title, that.title) &&
                Objects.equals(genre, that.genre) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(category, that.category) &&
                Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genre, duration, category, author, year);
    }
    public abstract String printInfo();

}
