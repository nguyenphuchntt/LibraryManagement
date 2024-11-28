package Controllers;

import DTO.TransactionDTO;
import Entity.*;
import Utils.AccountUserUtils;
import Utils.BookUtils;
import Utils.PopupUtils;
import Utils.TransactionUtils;
import database.DatabaseController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BookReturnController {

    private String star = null;

    private List<Transaction> transactionList;

    private ObservableList<TransactionDTO> transactionDTOObservableList;

    @FXML
    private TextField category_TextField;

    @FXML
    private TextField author_TextField;

    @FXML
    private TextField isbn_TextField;

    @FXML
    private TextField searchBar_TextField;

    @FXML
    private TableView<TransactionDTO> searchTable_TableView;

    @FXML
    private TableColumn<TransactionDTO, Boolean> amount_Column;

    @FXML
    private TableColumn<TransactionDTO, String> bookTitle_Column;

    @FXML
    private TableColumn<TransactionDTO, String> bookID_Column;

    @FXML
    private TableColumn<TransactionDTO, String> category_Column;

    @FXML
    private TableColumn<TransactionDTO, String> borrowDate_Column;

    @FXML
    private TableColumn<TransactionDTO, String> status_Column;

    @FXML
    private Button search_Button;

    @FXML
    private Button return_Button;

    @FXML
    private TextField bookIDInComment_TextField;

    @FXML
    private Label postCommentMessage_Label;

    @FXML
    private Button post_Button;

    @FXML
    private TextArea comment_TextArea;

    @FXML
    private Button oneStar_Button;

    @FXML
    private Button twoStar_Button;

    @FXML
    private Button threeStar_Button;

    @FXML
    private Button fourStar_Button;

    @FXML
    private Button fiveStar_Button;

    @FXML
    public void initialize() {
        assignColumnValue();

        amount_Column.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        amount_Column.setCellFactory(CheckBoxTableCell.forTableColumn(amount_Column));

        handleSearch();
    }

    private void assignColumnValue() {
        bookTitle_Column.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        bookID_Column.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        category_Column.setCellValueFactory(new PropertyValueFactory<>("category"));
        borrowDate_Column.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        status_Column.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public static List<TransactionDTO> getSelectedTransactions(ObservableList<TransactionDTO> transactions) {
        if (transactions == null) {
            return null;
        }
        return transactions.stream()
                .filter(TransactionDTO::getSelected)
                .collect(Collectors.toList());
    }

    @FXML
    private void handleOneStar() {
        star = "1";
    }
    @FXML
    private void handleTwoStar() {
        star = "2";
    }
    @FXML
    private void handleThreeStar() {
        star = "3";
    }
    @FXML
    private void handleFourStar() {
        star = "4";
    }
    @FXML
    private void handleFiveStar() {
        star = "5";
    }

    @FXML
    private void handleSearch() {
        String title = searchBar_TextField.getText().isEmpty() ? null : searchBar_TextField.getText();
        String isbn = isbn_TextField.getText().isEmpty() ? null : isbn_TextField.getText();
        String author = author_TextField.getText().isEmpty() ? null : author_TextField.getText();
        String category = category_TextField.getText().isEmpty() ? null : category_TextField.getText();

        String currentUsername = LibraryManagement.getInstance().getCurrentAccount();
        transactionList = TransactionUtils.getFilteredBorrowTransactions(
                title, author, category, isbn, currentUsername
        );

        transactionDTOObservableList = TransactionDTO.loadTransactionsForReturn(transactionList);
        searchTable_TableView.setItems(transactionDTOObservableList);
    }

    @FXML
    private void handleReturn() {
        List<TransactionDTO> transactions = getSelectedTransactions(transactionDTOObservableList);
        if (transactions == null) {
            return;
        }
        if (transactions.isEmpty()) {
            PopupUtils.showAlert("No books are selected");
        }
        List<String> toReturnBookID = new ArrayList<>();
        Person currentUser = AccountUserUtils.getCurrentUser();
        StringBuilder alert = new StringBuilder();
        if (currentUser == null) {
            PopupUtils.showAlert("User is null");
        }
        for (TransactionDTO transactionDTO : transactions) {
            if (transactionDTO.getStatus().equalsIgnoreCase("Returned")) {
                alert.append(transactionDTO.getBookTitle()).append("\n");
            }
        }
        if (alert.isEmpty()) {
            for (TransactionDTO transactionDTO : transactions) {
                toReturnBookID.add(transactionDTO.getBookId());
            }
            TransactionUtils.addReturnTransactions(transactions);
            BookUtils.updateBookAmountAfterBorrowed(toReturnBookID, true);
            PopupUtils.showAlert("Returned " + transactions.size() + " books successfully");
            handleSearch();
            cleanUp();
        } else {
            PopupUtils.showAlert(alert.append(" had been returned!").toString());
        }
    }
    
    @FXML
    private void handlePostComment() {
        if (bookIDInComment_TextField.getText().isEmpty()) {
            postCommentMessage_Label.setText("Please enter a valid book ID");
            cleanUp();
            return;
        }
        if (comment_TextArea.getText().isEmpty()) {
            postCommentMessage_Label.setText("Please enter a valid comment");
            cleanUp();
            return;
        }
        if (star == null) {
            postCommentMessage_Label.setText("Please enter a valid rating star");
            cleanUp();
            return;
        }
        isbn_TextField.setText(bookIDInComment_TextField.getText());
        handleSearch();
        if (transactionList.isEmpty()) {
            cleanUp();
            postCommentMessage_Label.setText("You haven't been read this book!");
            return;
        }
        Book book = transactionList.get(0).getBook();
        String username = LibraryManagement.getInstance().getCurrentAccount();
        Double rate = Double.parseDouble(star);
        String commentContent = comment_TextArea.getText();
        Comment comment = new Comment(book, username, commentContent, rate);
        if (comment.isWrittenComment(username, bookIDInComment_TextField.getText())) {
            cleanUp();
            postCommentMessage_Label.setText("You have been written this book's comment!");
            comment = null;
            return;
        }
        DatabaseController.saveEntity(comment);
        PopupUtils.showAlert("Posted comment successfully");
        cleanUp();
    }

    private void cleanUp() {
        bookIDInComment_TextField.clear();
        comment_TextArea.clear();
        searchBar_TextField.clear();
        isbn_TextField.clear();
        category_TextField.clear();
        author_TextField.clear();
        star = null;
    }
}
