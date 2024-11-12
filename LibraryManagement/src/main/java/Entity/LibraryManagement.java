package Entity;

import java.util.HashMap;
import java.util.Map;

public class LibraryManagement {
    private Account currentAccount = null;
    private Map<String, Account> accounts = new HashMap<>();
    private Library library = new Library.Builder().build();
    private static LibraryManagement libraryManagement;

    public static LibraryManagement getInstance() {
        if (libraryManagement == null) {
            libraryManagement = new LibraryManagement();
        }
        return libraryManagement;
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

    public void registerAccount(String username, String password, String typeAccount) {
        if (accounts.containsKey(username)) {
            System.out.println("Username already exists.");
        } else {
            Account newAccount = new Account.Builder()
                    .username(username)
                    .password(password)
                    .typeAccount(typeAccount)
                    .build();
            accounts.put(username, newAccount);
            System.out.println("Account registered successfully.");
        }
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public Library getLibrary() {
        return library;
    }

    public void addBook(Book book) {
        if (currentAccount != null && currentAccount.getTypeAccount().equals("Manager")) {
            library.getBooks().put(book.getIsbn(), book);
            library.incrementTotalBooks();
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Only managers can add books.");
        }
    }

    public void removeBook(String bookId) {
        if (currentAccount != null && currentAccount.getTypeAccount().equals("Manager")) {
            library.getBooks().remove(bookId);
            library.decrementTotalBooks();
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Only managers can remove books.");
        }
    }

    public void editBook(String id, String title, int amount, String type, String author, String publisher) {
        if (currentAccount != null && currentAccount.getTypeAccount().equals("Manager")) {
            Book book = library.getBooks().get(id);
            if (book != null) {
                book.setTitle(title);
                book.setAmount(amount);
                book.setCategory(type);
                book.setAuthor(author);
                book.setPublisher(publisher);
                System.out.println("Book edited successfully.");
            } else {
                System.out.println("Book not found.");
            }
        } else {
            System.out.println("Only managers can edit books.");
        }
    }
}
