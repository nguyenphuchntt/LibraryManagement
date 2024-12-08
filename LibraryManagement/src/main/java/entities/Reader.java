package entities;

public class Reader extends User {

    private Reader(Builder builder) {
        super(builder);
    }

    public static class Builder extends User.Builder<Builder> {

        public Builder() {}

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Reader build() {
            return new Reader(this);
        }
    }

}
