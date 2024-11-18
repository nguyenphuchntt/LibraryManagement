package Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "book_id")
    private String isbn;

    @Column(name = "book_title", nullable = false, length = 100)
    private String title;

    @Column(name = "author", length = 50)
    private String author;

    @Column(name = "publisher", length = 50)
    private String publisher;

    @Column(name = "year")
    private int year;

    @Column(name = "quantity", nullable = false)
    private int amount;

    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(name = "averageRating", nullable = false)
    private double averageRate;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    private Book(Builder builder) {
        this.isbn = builder.isbn;
        this.title = builder.title;
        this.author = builder.author;
        this.publisher = builder.publisher;
        this.amount = builder.amount;
        this.category = builder.category;
        this.averageRate = builder.averageRate;
        this.description = builder.description;
        this.year = builder.year;
    }

    public Book() {

    }

    public static class Builder {
        private String isbn;
        private String title;
        private String author;
        private String publisher;
        private int amount;
        private String category;
        private double averageRate;
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
            } else {
                System.out.println("Amount must be greater than 0");
            }
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder averageRate(double averageRate) {
            this.averageRate = averageRate;
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
        return averageRate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book [id=" + isbn + ", title=" + title + ", author=" + author
                + ", publisher=" + publisher + ", amount=" + amount
                + ", category=" + category  + ",averageRate=" + averageRate + "]";
    }
}
