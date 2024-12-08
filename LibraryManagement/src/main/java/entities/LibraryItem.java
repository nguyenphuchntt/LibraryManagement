package entities;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class LibraryItem {

    protected String title;

    protected String publisher;

    protected int year;

    protected int quantity;

    protected String description;

    protected LibraryItem(Builder<?> builder) {
        this.title = builder.title;
        this.publisher = builder.publisher;
        this.year = builder.year;
        this.quantity = builder.quantity;
        this.description = builder.description;
    }

    public LibraryItem() {}

    public static class Builder<T extends Builder<T>> {
        protected String title;
        protected String publisher;
        protected int year;
        protected int quantity;
        protected String description;

        public T title(String title) {
            this.title = title;
            return self();
        }

        public T publisher(String publisher) {
            this.publisher = publisher;
            return self();
        }

        public T year(int year) {
            this.year = year;
            return self();
        }

        public T quantity(int quantity) {
            this.quantity = quantity;
            return self();
        }

        public T description(String description) {
            this.description = description;
            return self();
        }

        protected T self() {
            return (T) this;
        }

        public LibraryItem build() {
            return new LibraryItem(this);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
