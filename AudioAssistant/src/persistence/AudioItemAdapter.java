package persistence;

import com.google.gson.*;
import model.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class AudioItemAdapter implements JsonSerializer<AudioItem>, JsonDeserializer<AudioItem> {

    @Override
    public JsonElement serialize(AudioItem src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", src.getClass().getSimpleName());
        obj.addProperty("title", src.getTitle());
        obj.addProperty("genre", src.getGenre());
        obj.addProperty("category", src.getCategory());
        obj.addProperty("author", src.getAuthor());
        obj.addProperty("year", src.getYear());
        obj.addProperty("duration", AudioItem.formatDuration(src.getDuration()));
        return obj;
    }

    @Override
    public AudioItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();
        String title = obj.get("title").getAsString();
        String genre = obj.get("genre").getAsString();
        String category = obj.get("category").getAsString();
        String author = obj.get("author").getAsString();
        int year = obj.get("year").getAsInt();
        Duration duration = AudioItem.formatDuration(obj.get("duration").getAsString());
        AudioItem item;

        try {
            switch (type) {
                case "Song":
                    item = new Song(title, genre, duration, category, author, year);
                    break;
                case "Podcast":
                    item = new Podcast(title, genre, duration, category, author, year);
                    break;
                case "Audiobook":
                    item = new Audiobook(title, genre, duration, category, author, year);
                    break;
                default:
                    throw new JsonParseException("Unknown AudioItem type: " + type);
            }
            return item;
        } catch (Exception e) {
            throw new JsonParseException("Cannot deserialize AudioItem", e);
        }

    }
}
