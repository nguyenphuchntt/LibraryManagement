package Entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transaction_id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private Person user;

    @Column(name = "type", nullable = false)
    private boolean type;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    @Column(name = "amount", nullable = false)
    private int amount;

    public Transaction(int id, Book book, Person user, boolean type, int amount) {
        this.transaction_id = id;
        this.book = book;
        this.user = user;
        this.type = type;
        this.amount = amount;
    }

    public Transaction() {}

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
