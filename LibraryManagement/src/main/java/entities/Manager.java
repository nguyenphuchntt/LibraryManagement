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

}
