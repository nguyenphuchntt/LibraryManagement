package App.Features;

public class Manager extends Person {
    private String managerId;

    private Manager(Builder builder) {
        super(builder);
        this.managerId = builder.managerId;
    }

    public static class Builder extends Person.Builder<Builder> {
        private String managerId;

        public Builder() {}

        public Builder managerId(String managerId) {
            this.managerId = managerId;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Manager build() {
            return new Manager(this);
        }
    }

    @Override
    public void setPerson_ID(String person_ID) {
        this.person_ID = person_ID;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

}
