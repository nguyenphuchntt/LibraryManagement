package Entity;

import java.util.HashMap;
import java.util.Map;

public class LibraryManagement {

    private String currentAccount = null;
    private String currentPassword = null;
    private String role = null;

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
            currentPassword = null;
            role = null;
            System.out.println("Logout successful.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public String  getCurrentAccount() {
        return currentAccount;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentAccount(String currentAccount) {
        this.currentAccount = currentAccount;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
