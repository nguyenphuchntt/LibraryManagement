package Controllers;

import Entity.Book;
import Entity.Person;
import Entity.Transaction;
import database.DatabaseController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookSearchController {

    @FXML
    private TableView<Book> searchTable_TableView;

    @FXML
    private TableColumn<Book, Boolean> amount_Column;

    @FXML
    private TableColumn<Book, String> bookTitle_Column;

    @FXML
    private TableColumn<Book, String> bookID_Column;

    @FXML
    private TableColumn<Book, String> author_Column;

    @FXML
    private TableColumn<Book, String> category_Column;

    @FXML
    private TableColumn<Book, String> description_Column;

    @FXML
    private TableColumn<Book, Integer> quantity_Column;

    @FXML
    private TableColumn<Book, Double> rate_Column;

    @FXML
    private TextField searchBar_TextField;

    @FXML
    private TextField year_TextField;

    @FXML
    private TextField category_TextField;

    @FXML
    private TextField author_TextField;

    @FXML
    private TextField isbn_TextField;

    @FXML
    private Button refresh_Button;

    @FXML
    private Button borrowRecommend_Button;

    @FXML
    private Button borrowSearch_Button;

    @FXML
    private Button search_Button;

    @FXML
    private Label ratingStar_Label;

    @FXML
    private Text name_Text;

    @FXML
    private Text author_Text;

    @FXML
    private Text category_Text;

    @FXML
    private Text description_Text;

    @FXML
    private Text publishedYear_Text;

    private ObservableList<Book> bookList;

    private int priorityOrder = 1;

    @FXML
    public void initialize() {
        mapColumnValue();

        bookList = FXCollections.observableArrayList(DatabaseController.getAllBooks());
        searchTable_TableView.setItems(bookList);

        amount_Column.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        amount_Column.setCellFactory(CheckBoxTableCell.forTableColumn(amount_Column));

    }

    private void mapColumnValue() {
        bookTitle_Column.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookID_Column.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        author_Column.setCellValueFactory(new PropertyValueFactory<>("author"));
        category_Column.setCellValueFactory(new PropertyValueFactory<>("category"));
        description_Column.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantity_Column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        rate_Column.setCellValueFactory(new PropertyValueFactory<>("averageRate"));
    }

    public static List<Book> getSelectedBooks(ObservableList<Book> books) {
        if (books == null) {
            return null;
        }
        return books.stream()
                .filter(Book::getSelected)
                .collect(Collectors.toList());
    }

    @FXML
    private void handleSearchButton() {
        String title = searchBar_TextField.getText().isEmpty() ? null : searchBar_TextField.getText();
        String isbn = isbn_TextField.getText().isEmpty() ? null : isbn_TextField.getText();
        String author = author_TextField.getText().isEmpty() ? null : author_TextField.getText();
        String category = category_TextField.getText().isEmpty() ? null : category_TextField.getText();
        String year = year_TextField.getText().isEmpty() ? null : year_TextField.getText();

        bookList = FXCollections.observableArrayList(DatabaseController.searchBook(
                isbn, title, author, category, year
        ));

        searchTable_TableView.setItems(bookList);
    }

    @FXML
    private void handleBorrowButton() {
        List<Book> books = getSelectedBooks(bookList);
        if (books == null) {
            return;
        }
        if (books.isEmpty()) {
            PopupController.showSuccessAlert("No books are selected");
            return;
        }
        List<Transaction> transactions = new ArrayList<>();
        Person currentUser = DatabaseController.getCurrentUser();
        StringBuilder alert = new StringBuilder();
        if (currentUser == null) {
            PopupController.showSuccessAlert("User is null");
        }
        for (Book book : books) {
            if (book.getQuantity() <= 0) {
                alert.append(book.getTitle()).append('\n');
            }
            if (alert.isEmpty()) {
                transactions.add(new Transaction(book, currentUser));
            }
        }
        if (alert.isEmpty()) {
            DatabaseController.addBorrowTransactions(transactions);
            DatabaseController.updateBookAmountAfterBorrowed(books);
            PopupController.showSuccessAlert("Borrowed " + books.size() + " books successfully");
            cleanUp();
        } else {
            PopupController.showSuccessAlert(alert.toString() + "doesn't not have enough quantity :((");
        }
    }

    private void cleanUp() {
        handleSearchButton();
    }

}
