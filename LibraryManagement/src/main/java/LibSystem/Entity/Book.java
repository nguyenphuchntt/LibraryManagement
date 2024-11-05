package App.Features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book {
    private final String id;
    private String title;
    private String author;
    private String publisher;
    private int amount;
    private String type;
    private boolean available;
    private int rating; // Rating tổng hợp
    private Map<Account, Integer> userRatings;
    private Map<Account, List<String>> userComments;

    private Book(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.author = builder.author;
        this.publisher = builder.publisher;
        this.amount = builder.amount;
        this.type = builder.type;
        this.available = builder.available;
        this.rating = builder.rating;
        this.userRatings = new HashMap<>();
        this.userComments = new HashMap<>();
    }

    public static class Builder {
        private String id;
        private String title;
        private String author;
        private String publisher;
        private int amount;
        private String type;
        private boolean available;
        private int rating;

        public Builder(String id) {
            this.id = id;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder amount(int amount) {
            if (amount >= 0) {
                this.amount = amount;
                this.available = amount > 0;
            } else {
                System.out.println("Amount must be greater than 0");
            }
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder rating(int rating) {
            this.rating = rating;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if(amount >= 0) {
            this.amount = amount;
            this.available = amount > 0;
        } else {
            System.out.println("Amount must be greater than 0");
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getRating() {
        return rating;
    }

    public void addRating(Account account, int rating) {
        userRatings.put(account, rating);
    }

    @Override
    public String toString() {
        String typeAvailable = available ? "Yes" : "No";
        return "Book [id=" + id + ", title=" + title + ", author=" + author
                + ", publisher=" + publisher + ", amount=" + amount
                + ", type=" + type + ", available=" + typeAvailable + ", rating=" + rating + "]";
    }
}
