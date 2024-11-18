package Entity;

public class Manager extends Person {
//    private String managerId;

    private Manager(Builder builder) {
        super(builder);
//        this.managerId = builder.managerId;
    }

    public static class Builder extends Person.Builder<Builder> {
//        private String managerId;

        public Builder() {}

//        public Builder managerId(String managerId) {
//            this.managerId = managerId;
//            return this;
//        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Manager build() {
            return new Manager(this);
        }
    }

//    public String getManagerId() {
//        return managerId;
//    }
//
//    public void setManagerId(String managerId) {
//        this.managerId = managerId;
//    }

    public void addBook(Library library, Book book) {
        library.getBooks().put(book.getIsbn(), book);
        library.incrementTotalBooks();
        System.out.println("Book added successfully.");
    }

    public void removeBook(Library library, String bookId) {
        library.getBooks().remove(bookId);
        library.decrementTotalBooks();
        System.out.println("Book removed successfully.");
    }

    public void editBook(Library library, String id, String title, int amount, String type, String author, String publisher) {
        Book book = library.getBooks().get(id);
        if (book != null) {
            book.setTitle(title);
            book.setAmount(amount);
            book.setCategory(type);
            book.setAuthor(author);
            book.setPublisher(publisher);
            System.out.println("Book edited successfully.");
        } else {
            System.out.println("Book not found with id " + id);
        }
    }
}
