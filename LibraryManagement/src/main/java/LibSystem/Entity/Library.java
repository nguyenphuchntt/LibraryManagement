package App.Features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private Map<String, Book> booksById;
    private List<Reader> readers;
    private List<Manager> managers;
    private int totalBooks;
    private int returnedBooks;
    private int borrowedBooks;

    private Library(Builder builder) {
        this.booksById = builder.booksById;
        this.readers = builder.readers;
        this.managers = builder.managers;
        this.totalBooks = builder.totalBooks;
        this.returnedBooks = builder.returnedBooks;
        this.borrowedBooks = builder.borrowedBooks;
    }

    public static class Builder {
        private Map<String, Book> booksById = new HashMap<>();
        private List<Reader> readers = new ArrayList<>();
        private List<Manager> managers = new ArrayList<>();
        private int totalBooks;
        private int returnedBooks;
        private int borrowedBooks;

        public Builder booksById(Map<String, Book> booksById) {
            this.booksById = booksById;
            return this;
        }

        public Builder readers(List<Reader> readers) {
            this.readers = readers;
            return this;
        }

        public Builder managers(List<Manager> managers) {
            this.managers = managers;
            return this;
        }

        public Builder totalBooks(int totalBooks) {
            this.totalBooks = totalBooks;
            return this;
        }

        public Builder returnedBooks(int returnedBooks) {
            this.returnedBooks = returnedBooks;
            return this;
        }

        public Builder borrowedBooks(int borrowedBooks) {
            this.borrowedBooks = borrowedBooks;
            return this;
        }

        public Library build() {
            return new Library(this);
        }
    }

    public Map<String, Book> getBooks() {
        return booksById;
    }

    public void incrementTotalBooks() {
        totalBooks++;
    }

    public void decrementTotalBooks() {
        totalBooks--;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public int getReturnedBooks() {
        return returnedBooks;
    }

    public int getBorrowedBooks() {
        return borrowedBooks;
    }

}
