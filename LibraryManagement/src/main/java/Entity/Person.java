package Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "user")
public class Person {

    @Id
    @Column(name = "username")
    protected String username;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    protected Account account;

    @Column(name = "name", nullable = false, length = 50)
    protected String name;

    @Column(name = "yearOfBirth")
    protected Integer yearOfBirth;

    @Column(name = "gender")
    protected String gender;

    @Column(name = "department", length = 100)
    protected String department;


//    protected Account account;
//    protected List<Book> borrowedBooks;

    protected Person(Builder<?> builder) {
        this.name = builder.name;
        this.yearOfBirth = builder.yearOfBirth;
        this.gender = builder.gender;
        this.department = builder.department;
        this.account = builder.account;
        this.username = builder.username;
//        this.account = builder.account;
//        this.borrowedBooks = builder.borrowedBooks != null ? builder.borrowedBooks : new ArrayList<>();
    }

    public Person() {}

    public static class Builder<T extends Builder<T>> {
        protected String username;
        protected Account account;
        protected String name;
        protected Integer yearOfBirth;
        protected String gender;
        protected String department;
        protected boolean role;
//        protected Account account;
//        protected List<Book> borrowedBooks;

        public T username(String username) {
            this.username = username;
            return self();
        }

        public T account(Account account) {
            this.account = account;
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
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getYearOfBirth() { return yearOfBirth; }
    public void setYearOfBirth(int yearOfBirth) { this.yearOfBirth = yearOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String toString() {
        return "name: " + name + "\nbirth: " + yearOfBirth + "\ngender: " + gender + "\ndepartment: " + department;
    }
}
