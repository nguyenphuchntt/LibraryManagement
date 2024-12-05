package controllers;

import DTO.TransactionDTO;
import entities.Book;
import entities.Comment;
import entities.Person;
import entities.Transaction;
import utils.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.hibernate.Session;

import javax.persistence.Index;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BookSearchController {

    private static final int ROWS_PER_PAGE = 12;

    private int currentPage = 0;
    int totalRows = BookUtils.getTotalRowBook();
    private int pageCount = (int) Math.ceil((double) totalRows / ROWS_PER_PAGE);

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
    private Text comment_Text;

    @FXML
    private ImageView thumbnail_ImageView;

    @FXML
    private ImageView searchThumbnail_ImageView;

    private ObservableList<Book> bookList;

    private List<Object[]> recommendedBookList;

    private int priorityOrder = 0;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Future<?> runningTask;

    @FXML
    public void initialize() {
        mapColumnValue();

        getRecommendBooks();

        bookList = FXCollections.observableArrayList(BookUtils.getAllBooks());
        showTable();

        amount_Column.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        amount_Column.setCellFactory(CheckBoxTableCell.forTableColumn(amount_Column));

        addTextFieldListener(searchBar_TextField);
        addTextFieldListener(year_TextField);
        addTextFieldListener(category_TextField);
        addTextFieldListener(author_TextField);
        addTextFieldListener(isbn_TextField);

        searchTable_TableView.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                Book selectedItem = searchTable_TableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    handleDoubleClick(selectedItem);
                }
            }
        });

        searchTable_TableView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Book>() {
                    @Override
                    public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
                        if (newValue != null) {
                            if (newValue.getThumbnailLink() != null) {
                                searchThumbnail_ImageView.setImage(new Image(newValue.getThumbnailLink()));
                            } else {
                                searchThumbnail_ImageView.setImage(null);
                            }
                        }
                    }
                }
        );
    }

    private void addTextFieldListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (runningTask != null && !runningTask.isDone()) {
                runningTask.cancel(true);
            }

            Task<ObservableList<Book>> task = new Task<>() {
                @Override
                protected ObservableList<Book> call() throws Exception {
                    try {
                        currentPage = 0;
                        searchBooks();
                        if (isCancelled()) return null;
                        return bookList;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    bookList = getValue();
                    Platform.runLater(() -> {
                        if (bookList != null) {
                            showTable();
                        }
                    });
                }

                @Override
                protected void cancelled() {
                    super.cancelled();
                }
            };

            runningTask = executorService.submit(task);
        });
    }

    public void refresh() {
        getRecommendBooks();
        searchBooks();
        showTable();
    }

    private void getRecommendBooks() {
        recommendedBookList = BookUtils.getBookListForRecommend();
        showRecommendedBooks();
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

    private void searchBooks() {
        String title = searchBar_TextField.getText().isEmpty() ? null : searchBar_TextField.getText();
        String isbn = isbn_TextField.getText().isEmpty() ? null : isbn_TextField.getText();
        String author = author_TextField.getText().isEmpty() ? null : author_TextField.getText();
        String category = category_TextField.getText().isEmpty() ? null : category_TextField.getText();
        String year = year_TextField.getText().isEmpty() ? null : year_TextField.getText();

        bookList = FXCollections.observableArrayList(BookUtils.searchBook(
                isbn, title, author, category, year, currentPage * ROWS_PER_PAGE, ROWS_PER_PAGE
        ));
    }

    private void showTable() {
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
            PopupUtils.showAlert("No books are selected");
            return;
        }
        List<Transaction> transactions = new ArrayList<>();
        Person currentUser = AccountUserUtils.getCurrentUser();
        StringBuilder alert = new StringBuilder();
        if (currentUser == null) {
            PopupUtils.showAlert("User is null");
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
            TransactionUtils.addBorrowTransactions(transactions);
            BookUtils.updateBookAmountAfterBorrowed(booksID, false);
            PopupUtils.showAlert("Borrowed " + books.size() + " books successfully");
            refresh();
        } else {
            PopupUtils.showAlert(alert.toString() + "doesn't not have enough quantity :((");
        }
    }

    private void showRecommendedBooks() {
        Object[] book = null;
        try {
             book = recommendedBookList.get(priorityOrder);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        title_Text.setText("Title:" + book[0].toString() + '\n');
        author_Text.setText("Author: " + book[1].toString() + '\n');
        category_Text.setText("Category: " + book[2].toString() + '\n');
        publishedYear_Text.setText("Published Year: " + book[3].toString() + '\n');
        description_Text.setText("Description: " + FormatUtils.getShortDescription(book[4].toString(), 500) + '\n');

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

        Person currentUser = AccountUserUtils.getCurrentUser();
        transactions.add(new Transaction(book, currentUser));

        TransactionUtils.addBorrowTransactions(transactions);
        BookUtils.updateBookAmountAfterBorrowed(books, false);
        PopupUtils.showAlert("Borrowed " + book.getTitle() + " successfully");
    }

    @FXML
    private void handlePreviousPageButton() {
        if (currentPage == 0) {
            return;
        }
        currentPage--;
        System.out.println("do previous page");
        searchBooks();
        showTable();
    }

    @FXML
    private void handleNextPageButton() {
        if (currentPage == pageCount - 1) {
            return;
        }
        currentPage++;
        System.out.println("do next page");
        searchBooks();
        showTable();
    }

    private void handleDoubleClick(Book book) {
        PopupUtils.openBookPopup(book, false);
    }

    @FXML
    private void handleRefreshButton() {
        showRecommendedBooks();
    }

    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

}
