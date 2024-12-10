package entities;

public class UserFactory {

    private UserFactory() {}

    public static final User createUser(boolean isAdmin, String username) {
        if (isAdmin) {
            return new Manager.Builder().username(username).build();
        }
        return new Reader.Builder().username(username).build();
    }

}
