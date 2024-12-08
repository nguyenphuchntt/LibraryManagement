package entities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book extends LibraryItem {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "author", length = 200)
    private String author;

    @Column(name = "averageRating", nullable = false)
    private double averageRate;

    @Column(name = "category", nullable = false, length = 150)
    private String category;

    @Column(name = "thumbnailLink")
    private String thumbnailLink;

    @Transient
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public boolean getSelected() {
        return selected.get();
    }

    public void setSelected(boolean value) {
        selected.set(value);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    private Book(Builder builder) {
        super(builder);
        this.id = builder.id;
        this.author = builder.author;
        this.category = builder.category;
        this.averageRate = builder.averageRate;
        this.thumbnailLink = builder.thumbnailLink;
    }

    public Book() {}

    public static class Builder extends LibraryItem.Builder<Builder> {
        private String id;
        private String author;
        private String category;
        private double averageRate;
        private String thumbnailLink;

        public Builder() {}

        public Builder(String id) {
            this.id = id;
        }

        public Builder author(String author) {
            this.author = author;
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

        public Builder thumbnailLink(String thumbnailLink) {
            this.thumbnailLink = thumbnailLink;
            return this;
        }

        @Override
        public Book build() {
            return new Book(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + ", author=" + author
                + ", publisher=" + ", quantity="
                + ", category=" + category  + ",averageRate=" + averageRate + ",thumbnailLink=" + thumbnailLink + ",description=" + "]";
    }
}
