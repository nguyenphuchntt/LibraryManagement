package Entity;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @Column(name = "account_id")
    private int account_ID;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private Person user;

    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "account_type", nullable = false)
    private boolean typeAccount;

    @Column(name = "joined_date", nullable = false)
    private String joined_date;

    private Account(Builder builder) {
        this.account_ID = builder.account_ID;
        this.username = builder.username;
        this.password = builder.password;
        this.joined_date = builder.joined_date;
        this.typeAccount = builder.typeAccount;
        this.user = builder.user;
    }

    public Account() {}

    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
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
        private int account_ID;
        private Person user;
        private String username;
        private String password;
        private boolean typeAccount;
        private String joined_date;

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

        public Builder account_ID(int account_ID) {
            this.account_ID = account_ID;
            return this;
        }

        public Builder joined_date(String joined_date) {
            this.joined_date = joined_date;
            return this;
        }

        public Builder user_ID(Person user_ID) {
            this.user = user_ID;
            return this;
        }

        public Account build() {
            return new Account(this);
        }

        private boolean isValidUsername(String username) {
            if (username.length() < 3 || username.length() > 20) {
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

    public void setAccount_ID(int account_ID) {
        this.account_ID = account_ID;
    }
    public int getAccount_ID() {
        return account_ID;
    }
    public String getJoined_date() {
        return joined_date;
    }
    public void setJoined_date(String joined_date) {
        this.joined_date = joined_date;
    }
    public Person getUser() {
        return user;
    }
    public void setUser(Person user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Account [username=" + username + ", password=" + password + ", typeAccount=" + typeAccount + "]";
    }

}
