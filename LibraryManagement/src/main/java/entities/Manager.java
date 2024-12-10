package entities;

import database.DatabaseController;
import utils.BookUtils;

public class Manager extends User {

    private Manager(Builder builder) {
        super(builder);
    }

    public static class Builder extends User.Builder<Builder> {

        public Builder() {}

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Manager build() {
            return new Manager(this);
        }
    }

    public void addBooks(Book book) {
        DatabaseController.saveEntity(book);
    }

    public void removeBooks(String book) {
        BookUtils.removeBookByISBN(book);
    }

    public void alterBooks(Book book) {
        BookUtils.alterBook(book);
    }

    public void postAnnouncement(Announcement announcement) {
        DatabaseController.saveEntity(announcement);
    }

    public void sendQuickMessage(Message message) {
        DatabaseController.saveEntity(message);
    }
}
