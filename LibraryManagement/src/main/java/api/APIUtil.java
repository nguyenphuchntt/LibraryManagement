package api;

import Entity.Book;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class APIUtil {

    private static final String API_KEY = "AIzaSyBHhrP5SG8DmlOWYzttGGYwJkxxVldz5qs";
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
        String authors = volumeInfo.has("authors") ? refactorNames(volumeInfo.getAsJsonArray("authors").toString()) : "Unknown";
        String categories = volumeInfo.has("categories") ? refactorNames(volumeInfo.getAsJsonArray("categories").toString()) : "No categories";
        String thumbnail = volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("smallThumbnail")
                ? volumeInfo.getAsJsonObject("imageLinks").get("smallThumbnail").getAsString()
                : "No thumbnail available";

        Book book = new Book.Builder(isbn)
                .title(title)
                .author(authors)
                .description(description)
                .year(Integer.parseInt(extractYear(publishedDate).equalsIgnoreCase("unknown") ? "0" : extractYear(publishedDate)))
                .category(categories)
                .thumbnailLink(thumbnail)
                .build();

    return book;
    }

    private static String extractYear(String publishedDate) {
        if (publishedDate == null || publishedDate.isEmpty()) {
            return "Unknown";
        }
        return publishedDate.split("-")[0];
    }

    private static String refactorNames(String namesString) {
        String[] names = namesString.replaceAll("[\\[\\]\"]", "").split(",\\s*");

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            String firstName = names[i].split(" ")[0];
            result.append(firstName);

            if (i < names.length - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String query = "9781491910740";
        if (!query.isEmpty()) {
            try {
                APIUtil api = new APIUtil();
                Book books = api.searchBooks(query);

                System.out.println(books.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
