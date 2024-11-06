package Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Person {
    protected String person_ID;
    protected String name;
    protected int yearOfBirth;
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
        protected int yearOfBirth;
        protected String gender;
        protected String department;
        protected Account account;
        protected List<Book> borrowedBooks;

        public T person_ID(String person_ID) {
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

        public T account(Account account) {
            this.account = account;
            return self();
        }

        public T borrowedBooks(List<Book> borrowedBooks) {
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

    public String getPerson_ID() { return person_ID; }
    public void setPerson_ID(String person_ID) { this.person_ID = person_ID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getYearOfBirth() { return yearOfBirth; }
    public void setYearOfBirth(int yearOfBirth) { this.yearOfBirth = yearOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }
}
