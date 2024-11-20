package Controllers;

import Entity.Book;
import Entity.LibraryManagement;
import Entity.Transaction;
import Entity.TransactionDTO;
import database.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;


public class BookReturnController {

    private String star = null;

    @FXML
    private TextField status_TextField;

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
    private Label postCommentMessage_Label;

    @FXML
    private Button post_Button;

    @FXML
    private TextField bookID_TextField;

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
        handleSearch();
    }

    private void assignColumnValue() {
        bookTitle_Column.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        bookID_Column.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        category_Column.setCellValueFactory(new PropertyValueFactory<>("category"));
        borrowDate_Column.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        status_Column.setCellValueFactory(new PropertyValueFactory<>("status"));
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
        List<Transaction> transactionList = DatabaseController.getFilteredBorrowTransactions(
                title, author, category, isbn, currentUsername
        );
        searchTable_TableView.setItems(TransactionDTO.loadTransactions(transactionList));
    }
}
