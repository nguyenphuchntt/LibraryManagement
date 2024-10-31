package App.Features;

public class Account {
    protected String username;
    protected String password;
    protected String typeAccount;

    private Account(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.typeAccount = builder.typeAccount;
    }

    public static class Builder {
        private String username;
        private String password;
        private String typeAccount;

        public Builder() {
        }

        public Builder username(String username) {
            if (username == null) {
                System.out.println("Function username: null parameter");
                return this;
            }
            if (isValidUsername(username)) {
                this.username = username;
            } else {
                throw new IllegalArgumentException("Username không hợp lệ.\n" +
                        "Username phải có tối thiểu 3 kí tự và nhiều nhất 20 kí tự, các kí tự thuộc 0-9, a-z, A-Z và không có khoảng trắng giữa các kí tự");
            }
            return this;
        }

        public Builder password(String password) {
            if (password == null) {
                System.out.println("Function password: null parameter");
                return this;
            }
            if (isValidPassword(password)) {
                this.password = password;
            } else {
                throw new IllegalArgumentException("Mật khẩu không hợp lệ.\n" +
                        "Mật khẩu phải có tối thiểu 8 kí tự gồm chữ in hoa, chữ thường và số");
            }
            return this;
        }

        public Builder typeAccount(String typeAccount) {
            if (typeAccount == null) {
                System.out.println("Function typeAccount: null parameter");
                return this;
            }
            this.typeAccount = typeAccount;
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

        private boolean isValidPassword(String password) {
            if (password.length() < 8) {
                return false;
            }
            boolean hasUpperCase = false;
            boolean hasLowerCase = false;
            boolean hasDigit = false;
            for (char c : password.toCharArray()) {
                if (Character.isUpperCase(c)) hasUpperCase = true;
                if (Character.isLowerCase(c)) hasLowerCase = true;
                if (Character.isDigit(c)) hasDigit = true;
            }
            return hasUpperCase && hasLowerCase && hasDigit;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null) {
            System.out.println("Function setUsername: null parameter");
            return;
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null) {
            System.out.println("Function setPassword: null parameter");
            return;
        }
        this.password = password;
    }

    public String getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(String typeAccount) {
        if (typeAccount == null) {
            System.out.println("Function setTypeAccount: null parameter");
            return;
        }
        this.typeAccount = typeAccount;
    }

    @Override
    public String toString() {
        return "Account [username=" + username + ", password=" + password + ", typeAccount=" + typeAccount + "]";
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}
