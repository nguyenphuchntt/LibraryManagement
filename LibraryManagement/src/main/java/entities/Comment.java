package entities;

import utils.BookUtils;

import javax.persistence.*;

@Entity
@Table(name = "book_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private String comment_id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "book_comment", columnDefinition = "TEXT")
    private String content;

    @Column(name = "rate", nullable = false)
    private Double rate;

    public Comment() {}

    public Comment(Book book, String username, String book_comment, Double rate) {
        this.book = book;
        this.username = username;
        this.content = book_comment;
        this.rate = rate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String book_comment) {
        this.content = book_comment;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getUser_id() {
        return username;
    }

    public void setUser_id(String username) {
        this.username = username;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public boolean isWrittenComment(String username, String bookIsbn) {
        return BookUtils.hadComment(username, bookIsbn);
    }

    public String toString() {
        return "Author: " + username + "\n" + content + " Star: " + rate;
    }

}
