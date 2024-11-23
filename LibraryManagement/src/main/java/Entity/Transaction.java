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
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private Person user;

    @Column(name = "borrow_time", nullable = false)
    private Timestamp borrow_time;

    @Column(name = "return_time")
    private Timestamp return_time;

    public Transaction(Book book, Person user) {
        this.book = book;
        this.user = user;
        borrow_time = new Timestamp(System.currentTimeMillis());
    }

    public Transaction() {
    }

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

    public Timestamp getBorrow_time() {
        return borrow_time;
    }

    public void setBorrow_time(Timestamp borrow_time) {
        this.borrow_time = borrow_time;
    }

    public Timestamp getReturn_time() {
        return return_time;
    }

    public void setReturn_time(Timestamp return_time) {
        this.return_time = return_time;
    }

}


