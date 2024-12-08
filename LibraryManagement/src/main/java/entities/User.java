package entities;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "username")
    protected String username;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    protected Account account;

    @Column(name = "name", nullable = false, length = 50)
    protected String name;

    @Column(name = "age")
    protected Integer age;

    @Column(name = "gender")
    protected String gender;

    @Column(name = "department", length = 100)
    protected String department;


    protected User(Builder<?> builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.gender = builder.gender;
        this.department = builder.department;
        this.account = builder.account;
        this.username = builder.username;
    }

    public User() {}

    public static class Builder<T extends Builder<T>> {
        protected String username;
        protected Account account;
        protected String name;
        protected Integer age;
        protected String gender;
        protected String department;
        protected boolean role;

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

        public T age(int age) {
            this.age = age;
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

        protected T self() {
            return (T) this;
        }

        public User build() {
            return new User(this);
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
    public Integer getYearOfBirth() { return age; }
    public void setYearOfBirth(int age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String toString() {
        return "name: " + name + "\nbirth: " + age + "\ngender: " + gender + "\ndepartment: " + department;
    }
}
