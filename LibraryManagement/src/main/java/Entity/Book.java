package Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book {
    private final String isbn;
    private String title;
    private String author;
    private String publisher;
    private int amount;
    private String category;
    private boolean available;
    private String description;
    private double rating; // Rating tổng hợp
    private Map<Account, Integer> userRatings;
    private Map<Account, String> userComments;
    private int year;

    private Book(Builder builder) {
        this.isbn = builder.isbn;
        this.title = builder.title;
        this.author = builder.author;
        this.publisher = builder.publisher;
        this.amount = builder.amount;
        this.category = builder.category;
        this.available = builder.available;
        this.rating = builder.rating;
        this.description = builder.description;
        this.userRatings = new HashMap<>();
        this.userComments = new HashMap<>();
        this.year = builder.year;
    }

    public static class Builder {
        private String isbn;
        private String title;
        private String author;
        private String publisher;
        private int amount;
        private String category;
        private boolean available;
        private double rating;
        private String description;
        private int year;

        public Builder(String id) {
            this.isbn = id;
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

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder rating(double rating) {
            this.rating = rating;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }

    public String getIsbn() {
        return isbn;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return amount > 0;
    }

    public double getRating() {
        return rating;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void addRating(Account account, int rating) {
        userRatings.put(account, rating);
        this.rating = (double) (rating * (userRatings.size() - 1) + rating) / userRatings.size();
    }

    public void addComment(Account account, String comment) {
        userComments.put(account, comment);
    }

    @Override
    public String toString() {
        String typeAvailable = available ? "Yes" : "No";
        return "Book [id=" + isbn + ", title=" + title + ", author=" + author
                + ", publisher=" + publisher + ", amount=" + amount
                + ", category=" + category + ", available=" + typeAvailable + ",rating=" + rating + "]";
    }
}
