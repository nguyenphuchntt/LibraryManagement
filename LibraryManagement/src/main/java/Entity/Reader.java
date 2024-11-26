package Entity;

public class Reader extends Person {

    private Reader(Builder builder) {
        super(builder);
    }

    public static class Builder extends Person.Builder<Builder> {

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
