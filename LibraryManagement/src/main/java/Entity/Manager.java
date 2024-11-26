package Entity;

public class Manager extends Person {

    private Manager(Builder builder) {
        super(builder);
    }

    public static class Builder extends Person.Builder<Builder> {

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
