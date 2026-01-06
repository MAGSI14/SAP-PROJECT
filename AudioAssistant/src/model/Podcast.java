package model;

import exceptions.DurationException;
import exceptions.InvalidYearException;

import java.time.Duration;

public class Podcast extends AudioItem{

    public Podcast(String title, String genre, Duration duration, String category, String author, int year) throws InvalidYearException, DurationException {
        super(title, genre, duration, category, author, year);
    }

    @Override public String printInfo(){
        return String.format(
                "Podcast: %s | Author: %s | Year: %d | Genre: %s | Category: %s | Duration: %s",
                getTitle(),
                getAuthor(),
                getYear(),
                getGenre(),
                getCategory(),
                AudioItem.formatDuration(getDuration())
        );
    }
}
