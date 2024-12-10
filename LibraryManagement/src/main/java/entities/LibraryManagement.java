package entities;

public class LibraryManagement {

    Account currentAccount = null;
    User currentUser = null;

    private static LibraryManagement libraryManagement;

    public static LibraryManagement getInstance() {
        if (libraryManagement == null) {
            libraryManagement = new LibraryManagement();
        }
        return libraryManagement;
    }

    public void logout() {
        if (currentAccount != null) {
            currentAccount = null;
            currentUser = null;
            System.out.println("Logout successful.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
