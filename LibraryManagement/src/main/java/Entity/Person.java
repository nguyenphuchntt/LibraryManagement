package Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "user", indexes = {@Index(name = "role", columnList = "role")})
public class Person {

    @Id
    @Column(name = "user_id")
    protected int person_ID;

    @Column(name = "name", nullable = false, length = 50)
    protected String name;

    @Column(name = "yearOfBirth")
    protected int yearOfBirth;

    @Column(name = "gender")
    protected String gender;

    @Column(name = "role", nullable = false)
    protected Boolean role;

    @Column(name = "department", length = 100)
    protected String department;


//    protected Account account;
//    protected List<Book> borrowedBooks;

    protected Person(Builder<?> builder) {
        this.person_ID = builder.person_ID;
        this.name = builder.name;
        this.yearOfBirth = builder.yearOfBirth;
        this.gender = builder.gender;
        this.department = builder.department;
        this.role = builder.role;
//        this.account = builder.account;
//        this.borrowedBooks = builder.borrowedBooks != null ? builder.borrowedBooks : new ArrayList<>();
    }

    public Person() {}

    public static class Builder<T extends Builder<T>> {
        protected int person_ID;
        protected String name;
        protected int yearOfBirth;
        protected String gender;
        protected String department;
        protected boolean role;
//        protected Account account;
//        protected List<Book> borrowedBooks;

        public T person_ID(int person_ID) {
            this.person_ID = person_ID;
            return self();
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T yearOfBirth(int yearOfBirth) {
            this.yearOfBirth = yearOfBirth;
            return self();
        }

        public T gender(String gender) {
            this.gender = gender;
            return self();
        }

        public T department(String department) {
            this.department = department;
            return self();
        }

        public T role(boolean role) {
            this.role = role;
            return self();
        }

//        public T account(Account account) {
//            this.account = account;
//            return self();
//        }
//
//        public T borrowedBooks(List<Book> borrowedBooks) {
//            this.borrowedBooks = borrowedBooks;
//            return self();
//        }

        protected T self() {
            return (T) this;
        }

        public Person build() {
            return new Person(this);
        }
    }

    public int getPerson_ID() { return person_ID; }
    public void setPerson_ID(int person_ID) { this.person_ID = person_ID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getYearOfBirth() { return yearOfBirth; }
    public void setYearOfBirth(int yearOfBirth) { this.yearOfBirth = yearOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public boolean getRole() {
        return role;
    }
    public void setRole(boolean role) {
        this.role = role;
    }
//    public Account getAccount() { return account; }
//    public void setAccount(Account account) { this.account = account; }
//    public List<Book> getBorrowedBooks() { return borrowedBooks; }

//    public void borrowBook(Book book) {
//        if (book.isAvailable()) {
//            borrowedBooks.add(book);
//            book.setAmount(book.getAmount() - 1);
//        } else {
//            System.out.println("Đã hết sách có thể cho mượn");
//        }
//    }
//
//    public void returnBook(Book book) {
//        borrowedBooks.remove(book);
//        book.setAmount(book.getAmount() + 1);
//    }

    @Override
    public String toString() {
        return "Person [person_ID=" + person_ID + ", name=" + name + ", yearOfBirth=" + yearOfBirth + ", gender=" + gender
                + ", department=" + department + "]";
    }

//    public void editInformation(String name, int yearOfBirth, String gender, String department) {
//        if (name != null && !name.isEmpty()) { this.name = name; }
//        if (yearOfBirth > 0) { this.yearOfBirth = yearOfBirth; }
//        if (gender != null && !gender.isEmpty()) { this.gender = gender; }
//        if (department != null && !department.isEmpty()) { this.department = department; }
//    }
//
//    public boolean changePassword(String oldPassword, String newPassword, String confirmPassword) {
//        if (!this.account.getPassword().equals(oldPassword)) {
//            System.out.println("Old password does not match");
//            return false;
//        }
//        if (!newPassword.equals(confirmPassword)) {
//            System.out.println("New password and confirm password do not match");
//            return false;
//        }
//        this.account.setPassword(newPassword);
//        System.out.println("Password changed successfully");
//        return true;
//    }
//
//    public List<Book> searchBook(Library library, String id, String title, String author) {
//        List<Book> result = new ArrayList<>();
//        for (Map.Entry<String, Book> entry : library.getBooks().entrySet()) {
//            boolean match = true;
//            Book book = entry.getValue();
//            if (id != null && book.getIsbn().equals(id)) { match = false; }
//            if (title != null && !book.getTitle().toLowerCase().contains(title.toLowerCase())) { match = false; }
//            if (author != null && !book.getAuthor().toLowerCase().contains(author.toLowerCase())) { match = false; }
//            if (match) { result.add(book); }
//        }
//        return result;
//    }
}
