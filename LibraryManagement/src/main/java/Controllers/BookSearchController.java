package Controllers;

import Entity.Book;
import Entity.Person;
import Entity.Transaction;
import database.DatabaseController;
import Utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookSearchController {

    @FXML
    private Label thumbnailMessage_Label;

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
    private Text title_Text;

    @FXML
    private Text author_Text;

    @FXML
    private Text category_Text;

    @FXML
    private Text description_Text;

    @FXML
    private Text publishedYear_Text;

    @FXML
    private ImageView thumbnail_ImageView;

    private ObservableList<Book> bookList;

    private List<Object[]> recommendedBookList;

    private int priorityOrder = 0;

    @FXML
    public void initialize() {
        mapColumnValue();

        recommendedBookList = DatabaseController.getBookForRecommend();

        showRecommendedBooks();

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
        List<String> booksID = new ArrayList<>();
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
                booksID.add(book.getIsbn());
            }
        }
        if (alert.isEmpty()) {
            DatabaseController.addBorrowTransactions(transactions);
            DatabaseController.updateBookAmountAfterBorrowed(booksID, false);
            PopupController.showSuccessAlert("Borrowed " + books.size() + " books successfully");
            cleanUp();
        } else {
            PopupController.showSuccessAlert(alert.toString() + "doesn't not have enough quantity :((");
        }
    }

    private void showRecommendedBooks() {
        Object[] book = recommendedBookList.get(priorityOrder);
        title_Text.setText("Title:" + book[0].toString() + '\n');
        author_Text.setText("Author: " + book[1].toString() + '\n');
        category_Text.setText("Category: " + book[2].toString() + '\n');
        publishedYear_Text.setText("Published Year: " + book[3].toString() + '\n');
        description_Text.setText("Description: " + (book[4].toString().length() > 500 ? book[4].toString().substring(0, 500) + "..." : book[4].toString()) + '\n');

        ratingStar_Label.setText("Rating: " + book[5].toString());
        if (book[7] != null) {
            thumbnail_ImageView.setImage(new Image(book[7].toString()));
            thumbnailMessage_Label.setText("");
        } else {
            thumbnail_ImageView.setImage(null);
            thumbnailMessage_Label.setText("No thumbnail available!");
        }
        priorityOrder = (priorityOrder + 1) % recommendedBookList.size();
    }

    @FXML
    private void handleBorrowRecommendBook() {
        String book_id = recommendedBookList.get((priorityOrder + 9) % recommendedBookList.size())[6].toString();
        List<String> books = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Book book = session.get(Book.class, book_id);
        session.close();
        books.add(book.getIsbn());

        Person currentUser = DatabaseController.getCurrentUser();
        transactions.add(new Transaction(book, currentUser));

        DatabaseController.addBorrowTransactions(transactions);
        DatabaseController.updateBookAmountAfterBorrowed(books, false);
        PopupController.showSuccessAlert("Borrowed " + book.getTitle() + " successfully");
    }

    @FXML
    private void handleRefreshButton() {
        showRecommendedBooks();
    }

    private void cleanUp() {
        handleSearchButton();
    }

}
