package entities;

import javax.persistence.*;
import java.sql.Timestamp;

    @Entity
    @Table(name = "account")
public class Account {

    @Id
    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "account_role", nullable = false)
    private boolean typeAccount;

    @Column(name = "joined_date", nullable = false)
    private Timestamp joined_date;

    private Account(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.joined_date = builder.joined_date;
        this.typeAccount = builder.typeAccount;
    }

    public Account() {}

    public static boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 60) {
            return false;
        }
        if (password.contains(" ")) {
            return false;
        }
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            }
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        return hasUpperCase && hasDigit && hasLowerCase;
    }

    public static class Builder {
        private User user;
        private String username;
        private String password;
        private boolean typeAccount;
        private Timestamp joined_date;

        public Builder() {
        }

        public Builder username(String username) {
            if (isValidUsername(username)) {
                this.username = username;
            } else {
                throw new IllegalArgumentException("Invalid username");
            }
            return this;
        }

        public Builder password(String password) {
            if (isValidPassword(password)) {
                this.password = password;
            } else {
                throw new IllegalArgumentException("Invalid password");
            }
            return this;
        }

        public Builder typeAccount(boolean typeAccount) {
            this.typeAccount = typeAccount;
            return this;
        }

        public Builder joined_date(Timestamp joined_date) {
            this.joined_date = joined_date;
            return this;
        }

        public Builder user_ID(User user_ID) {
            this.user = user_ID;
            return this;
        }

        public Account build() {
            return new Account(this);
        }

        private boolean isValidUsername(String username) {
            if (username.length() < 3 || username.length() > 30) {
                return false;
            }
            if (!username.matches("^[a-zA-Z0-9_]+$")) {
                return false;
            }
            return !username.contains(" ");
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(boolean typeAccount) {
        this.typeAccount = typeAccount;
    }

    public Timestamp getJoined_date() {
        return joined_date;
    }
    public void setJoined_date(Timestamp joined_date) {
        this.joined_date = joined_date;
    }

    @Override
    public String toString() {
        return "Account [username=" + username + ", password=" + password + ", typeAccount=" + typeAccount + "]";
    }

}
