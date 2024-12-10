package entities;

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

    public boolean addBooks(Book book) {
        return false;
    }

    public boolean removeBooks(Book book) {
        return false;
    }

    public boolean AlterBooks(Book book) {
        return false;
    }

    public boolean postAnnouncement(Announcement announcement) {
        return false;
    }
}
