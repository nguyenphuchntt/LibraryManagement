package Entity;
// thieu phan loai account -> admin vs user
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
            if (isValidUsername(username)) {
                this.username = username;
            } else {
                throw new IllegalArgumentException("Username không hợp lệ.\n" +
                        "Username phải có tối thiểu 3 kí tự và nhiều nhất 20 kí tự, các kí tự thuộc 0-9, a-z, A-Z và không có khoảng trắng giữa các kí tự");
            }
            return this;
        }

        public Builder password(String password) {
            if (isValidPassword(password)) {
                this.password = password;
            } else {
                throw new IllegalArgumentException("Mật khẩu không hợp lệ.\n" +
                        "Mật khẩu phải có tối thiểu 8 kí tự gồm chữ in hoa, chữ thường và số");
            }
            return this;
        }

        public Builder typeAccount(String typeAccount) {
            this.typeAccount = typeAccount;
            return this;
        }

        public Account build() {
            return new Account(this);
        }

        private boolean isValidUsername(String username) {
            // check length
            if (username.length() < 3 || username.length() > 20) {
                return false;
            }
            // check valid character
            if (!username.matches("^[a-zA-Z0-9_]+$")) {
                return false;
            }
            // check space
            if (username.contains(" ")) {
                return false;
            }
            return true;
        }

        private boolean isValidPassword(String password) {
            // minimum length is 8 characters
            if (password.length() < 8) {
                return false;
            }
            // check uppercase, lowercase and number.
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

    @Override
    public String toString() {
        return "Account [username=" + username + ", password=" + password + ", typeAccount=" + typeAccount + "]";
    }

    public boolean login(String username, String password) { // thừa
        return this.username.equals(username) && this.password.equals(password);
    }
}
