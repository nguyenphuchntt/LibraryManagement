package utils;

import entities.Book;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIUtils {

    private static final String API_KEY = DotenvLoader.getDotenv().get("API_KEY");
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    public static Book searchBooks(String query) throws Exception {
        String urlString = API_URL + query + "&fields=items(volumeInfo(title,description,publishedDate,authors,categories,imageLinks/smallThumbnail))&langRestrict=en&key=" + API_KEY;
        System.out.println(urlString);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
            return getBooksFromResponse(response, query);
        } else {
            throw new Exception("Failed to fetch books from API: " + connection.getResponseMessage());
        }
    }

    private static Book getBooksFromResponse(JsonObject response, String isbn) throws Exception {

        if (response == null || !response.has("items") || response.getAsJsonArray("items").isEmpty()) {
            return null;
        }

        JsonArray items = response.getAsJsonArray("items");
        JsonObject volumeInfo = items.get(0).getAsJsonObject()
                .getAsJsonObject("volumeInfo");

        String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Unknown";
        String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description available";
        String publishedDate = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : "Unknown";
        String authors = volumeInfo.has("authors") ? FormatUtils.refactorNames(volumeInfo.getAsJsonArray("authors").toString()) : "Unknown";
        String categories = volumeInfo.has("categories") ? FormatUtils.refactorNames(volumeInfo.getAsJsonArray("categories").toString()) : "No categories";
        String thumbnail = volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("smallThumbnail")
                ? volumeInfo.getAsJsonObject("imageLinks").get("smallThumbnail").getAsString()
                : "No thumbnail available";

        Book book = new Book.Builder(isbn)
                .title(title)
                .author(authors)
                .description(description)
                .year(Integer.parseInt(FormatUtils.extractYear(publishedDate).equalsIgnoreCase("unknown") ? "0" : FormatUtils.extractYear(publishedDate)))
                .category(categories)
                .thumbnailLink(thumbnail)
                .build();

    return book;
    }


}
