package App.Features;

import java.util.List;

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

    @Override
    public void setPerson_ID(String person_ID) {
        this.person_ID = person_ID;
    }

}
