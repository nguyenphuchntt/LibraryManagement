package Entity;

import database.DatabaseController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.Transient;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionDTO {
    private String bookTitle;
    private String bookId;
    private String category;
    private String borrowDate;
    private String status;

    // Constructor
    public TransactionDTO(String bookTitle, String bookId, String category, String borrowDate, String status) {
        this.bookTitle = bookTitle;
        this.bookId = bookId;
        this.category = category;
        this.borrowDate = borrowDate;
        this.status = status;
    }

    // Getters and setters
    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Transient
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public boolean getSelected() {
        return selected.get();
    }

    public void setSelected(boolean value) {
        selected.set(value);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public static ObservableList<TransactionDTO> loadTransactions(List<Transaction> transactionList) {
        ObservableList<TransactionDTO> transactions = FXCollections.observableArrayList();
        for (Transaction transaction : transactionList) {

            Book book = transaction.getBook();
            String bookTitle = book.getTitle();
            String bookId = book.getIsbn();
            String category = book.getCategory();
            String borrowDate = transaction.getBorrow_time().toString();

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            Timestamp transactionTimestamp = transaction.getBorrow_time();

            LocalDateTime currentTime = currentTimestamp.toLocalDateTime();
            LocalDateTime transactionTime = transactionTimestamp.toLocalDateTime();
            long daysDifference = Duration.between(transactionTime, currentTime).toDays();
            String status = null;
            if (transaction.getReturn_time() != null) {
                status = "Returned";
            } else {
                if (daysDifference > 21) {
                    status = "Late";
                } else {
                    long daysRemaining = 21 - daysDifference;
                    status = "Remain " + daysRemaining + " day";
                    if (daysRemaining > 1) {
                        status += 's';
                    }
                }
            }

            transactions.add(new TransactionDTO(bookTitle, bookId, category, borrowDate, status));
        }

        return transactions;
    }

}
