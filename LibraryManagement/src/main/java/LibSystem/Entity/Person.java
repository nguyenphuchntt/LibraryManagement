package LibSystem.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Person {
    protected String person_ID;
    protected String name;
    protected String yearOfBirth;
    protected String gender;
    protected String department;
    protected Account account;
    protected List<Book> borrowedBooks;

    protected Person(Builder<?> builder) {
        this.person_ID = builder.person_ID;
        this.name = builder.name;
        this.yearOfBirth = builder.yearOfBirth;
        this.gender = builder.gender;
        this.department = builder.department;
        this.account = builder.account;
        this.borrowedBooks = builder.borrowedBooks != null ? builder.borrowedBooks : new ArrayList<>();
    }

    public static class Builder<T extends Builder<T>> {
        protected String person_ID;
        protected String name;
        protected String yearOfBirth;
        protected String gender;
        protected String department;
        protected Account account;
        protected List<Book> borrowedBooks;

        public T person_ID(String person_ID) {
            if (person_ID == null) {
                System.out.println("Function person_ID: null parameter");
                return self();
            }
            this.person_ID = person_ID;
            return self();
        }

        public T name(String name) {
            if (name == null) {
                System.out.println("Function name: null parameter");
                return self();
            }
            this.name = name;
            return self();
        }

        public T yearOfBirth(String yearOfBirth) {
            if (yearOfBirth == null) {
                System.out.println("Function yearOfBirth: null parameter");
                return self();
            }
            this.yearOfBirth = yearOfBirth;
            return self();
        }

        public T gender(String gender) {
            if (gender == null) {
                System.out.println("Function gender: null parameter");
                return self();
            }
            this.gender = gender;
            return self();
        }

        public T department(String department) {
            if (department == null) {
                System.out.println("Function department: null parameter");
                return self();
            }
            this.department = department;
            return self();
        }

        public T account(Account account) {
            if (account == null) {
                System.out.println("Function account: null parameter");
                return self();
            }
            this.account = account;
            return self();
        }

        public T borrowedBooks(List<Book> borrowedBooks) {
            if (borrowedBooks == null) {
                System.out.println("Function borrowedBooks: null parameter");
                return self();
            }
            this.borrowedBooks = borrowedBooks;
            return self();
        }

        protected T self() {
            return (T) this;
        }

        public Person build() {
            return new Person(this);
        }
    }

    public String getPerson_ID() {
        return person_ID;
    }

    public void setPerson_ID(String person_ID) {
        if (person_ID == null) {
            System.out.println("Function setPerson_ID: null parameter");
            return;
        }
        this.person_ID = person_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            System.out.println("Function setName: null parameter");
            return;
        }
        this.name = name;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        if (yearOfBirth == null) {
            System.out.println("Function setYearOfBirth: null parameter");
            return;
        }
        try {
            Integer.parseInt(yearOfBirth);
            this.yearOfBirth = yearOfBirth;
        } catch (NumberFormatException e) {
            System.out.println("Invalid year of birth: must be a numeric value.");
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender == null) {
            System.out.println("Function setGender: null parameter");
            return;
        }
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        if (department == null) {
            System.out.println("Function setDepartment: null parameter");
            return;
        }
        this.department = department;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        if (account == null) {
            System.out.println("Function setAccount: null parameter");
            return;
        }
        this.account = account;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Library Lib, String bookID) {
        if (Lib == null || bookID == null) {
            System.out.println("Function borrowBook: null parameter(s)");
            return;
        }
        Book book = Lib.searchByID(bookID);
        if (book != null && book.isAvailable()) {
            borrowedBooks.add(book);
            book.setAmount(book.getAmount() - 1);
        } else {
            System.out.println("Đã hết sách có thể cho mượn");
        }
    }

    public void returnBook(Library Lib, String bookID) {
        if (Lib == null || bookID == null) {
            System.out.println("Function returnBook: null parameter(s)");
            return;
        }
        Book book = Lib.searchByID(bookID);
        if (book != null) {
            borrowedBooks.remove(book);
            book.setAmount(book.getAmount() + 1);
        }
    }

    @Override
    public String toString() {
        return "Person [person_ID=" + person_ID + ", name=" + name + ", yearOfBirth=" + yearOfBirth + ", gender=" + gender
                + ", department=" + department + "]";
    }

    public void editInformation(String name, String yearOfBirth, String gender, String department) {
        if (name != null && !name.isEmpty()) { this.name = name; }
        if (yearOfBirth != null && !yearOfBirth.isEmpty()) { setYearOfBirth(yearOfBirth); }
        if (gender != null && !gender.isEmpty()) { this.gender = gender; }
        if (department != null && !department.isEmpty()) { this.department = department; }
    }

    public boolean changePassword(String oldPassword, String newPassword, String confirmPassword) {
        if (!this.account.getPassword().equals(oldPassword)) {
            System.out.println("Old password does not match");
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("New password and confirm password do not match");
            return false;
        }
        this.account.setPassword(newPassword);
        System.out.println("Password changed successfully");
        return true;
    }

    public List<Book> searchBook(Library library, String id, String title, String author) {
        List<Book> result = new ArrayList<>();
        for (Map.Entry<String, Book> entry : library.getBooks().entrySet()) {
            boolean match = true;
            Book book = entry.getValue();
            if (id != null && book.getId().equals(id)) { match = false; }
            if (title != null && !book.getTitle().toLowerCase().contains(title.toLowerCase())) { match = false; }
            if (author != null && !book.getAuthor().toLowerCase().contains(author.toLowerCase())) { match = false; }
            if (match) { result.add(book); }
        }
        return result;
    }
}
