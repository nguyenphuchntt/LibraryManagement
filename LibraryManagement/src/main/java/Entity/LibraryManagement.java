package Entity;

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
}
