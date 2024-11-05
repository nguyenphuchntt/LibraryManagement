package App.Features;

import java.util.HashMap;
import java.util.Map;

public class LibraryManagement {
    private Account currentAccount;
    private Map<String, Account> accounts;
    private Library library;

    public LibraryManagement() {
        this.accounts = new HashMap<>();
        this.library = new Library.Builder().build();
    }

    public boolean login(String username, String password) {
        Account account = accounts.get(username);
        if (account != null && account.getPassword().equals(password)) {
            currentAccount = account;
            System.out.println("Login successful.");
            return true;
        }
        System.out.println("Login failed. Please check your username and password.");
        return false;
    }

    public void logout() {
        if (currentAccount != null) {
            currentAccount = null;
            System.out.println("Logout successful.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

}
