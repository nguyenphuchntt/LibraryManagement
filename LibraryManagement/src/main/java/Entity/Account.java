package Entity;

public class Account {
    private String account_ID;
    private String user_ID;
    private String username;
    private String password;
    private String typeAccount;
    private String joined_date;
//    private Person Owner;

    private Account(Builder builder) {
        this.account_ID = builder.account_ID;
        this.username = builder.username;
        this.password = builder.password;
        this.typeAccount = builder.typeAccount;
        this.joined_date = builder.joined_date;
        this.user_ID = builder.user_ID;
//        this.Owner = builder.Owner;

    }

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
        private String account_ID;
        private String user_ID;
        private String username;
        private String password;
        private String typeAccount;
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

        public Builder typeAccount(String typeAccount) {
            this.typeAccount = typeAccount;
            return this;
        }

        public Builder account_ID(String account_ID) {
            this.account_ID = account_ID;
            return this;
        }

//        public Builder owner(Person Owner) {
//            this.Owner = Owner;
//            return this;
//        }

        public Builder joined_date(String joined_date) {
            this.joined_date = joined_date;
            return this;
        }

        public Builder user_ID(String user_ID) {
            this.user_ID = user_ID;
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

    public String getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(String typeAccount) {
        this.typeAccount = typeAccount;
    }

//    public void setOwner(Person Owner) {
//        this.Owner = Owner;
//    }
//
//    public Person getOwner() {
//        return Owner;
//    }

    public void setAccount_ID(String account_ID) {
        this.account_ID = account_ID;
    }
    public String getAccount_ID() {
        return account_ID;
    }
    public String getJoined_date() {
        return joined_date;
    }
    public void setJoined_date(String joined_date) {
        this.joined_date = joined_date;
    }
    public String getUser_ID() {
        return user_ID;
    }
    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    @Override
    public String toString() {
        return "Account [username=" + username + ", password=" + password + ", typeAccount=" + typeAccount + "]";
    }

}
