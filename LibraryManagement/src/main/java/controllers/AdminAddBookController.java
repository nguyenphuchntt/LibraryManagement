package controllers;

import entities.Book;
import utils.APIUtils;
import utils.BookUtils;
import utils.PopupUtils;
import database.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdminAddBookController {
    Book book;
    boolean selected = false;

    @FXML
    private TextField bookTitle_TextField;

    @FXML
    private TextField bookId_TextField;

    @FXML
    private TextField author_TextField;

    @FXML
    private TextField year_TextField;

    @FXML
    private TextField category_TextField;

    @FXML
    private TextField amount_TextField;

    @FXML
    private TextArea description_TextArea;

    @FXML
    private Button add_Button;

    @FXML
    private Button search_Button;

    @FXML
    private Label addBookMessage_Label;

    @FXML
    private ImageView thumbnail_ImageView;

    @FXML
    private Label internetError_Label;

    @FXML
    private void initialize() {
        internetError_Label.setVisible(false);
    }

    @FXML
    private void handleSearch() {
        internetError_Label.setVisible(false);
        addBookMessage_Label.setText("");
        String isbn = bookId_TextField.getText();
        if (isbn.trim().isEmpty() || isbn.matches(".*[^0-9].*")) {
            addBookMessage_Label.setText("Invalid Book ID");
            return;
        }
        try {
            book = APIUtils.searchBooks(isbn);
        } catch (Exception e) {
            System.out.println("Error in Add book controller: " + e.getMessage());
            internetError_Label.setVisible(true);
            throw new RuntimeException(e);
        }
        if (book == null) {
            addBookMessage_Label.setText("Book not found");
            return;
        }
        fillBookProperties(book);
        selected = true;
    }

    @FXML
    private void handleAdd() {
        String isbn = bookId_TextField.getText();
        String amount = amount_TextField.getText();
        try {
            int amountInt = Integer.parseInt(amount);
            if (amountInt < 0) {
                throw new NumberFormatException();
            }
            book.setAmount(amountInt);
        } catch (NumberFormatException e) {
            addBookMessage_Label.setText("Invalid Amount");
            return;
        }

        if (PopupUtils.showConfirmationDialog()) {
            return;
        }

        if (BookUtils.hadBook(isbn)) {
            addBookMessage_Label.setText("Book already exists. You can switch to \"Change book\" tab to edit this book!");
            return;
        }
        if (selected) {
            if (!isbn.equalsIgnoreCase(book.getId())) {
                addBookMessage_Label.setText("You should not change the book ID");
                return;
            }
            new Thread(() -> {
                DatabaseController.saveEntity(book);
            }).start();
            PopupUtils.showAlert("Book added successfully");
            cleanUp();
        }
    }

    private void fillBookProperties(Book book) {
        bookTitle_TextField.setText(book.getTitle());
        author_TextField.setText(book.getAuthor());
        year_TextField.setText(String.valueOf(book.getYear()));
        category_TextField.setText(book.getCategory());
        description_TextArea.setText(book.getDescription());
        Image thumbnail = new Image(book.getThumbnailLink());
        thumbnail_ImageView.setImage(thumbnail);
    }

    public void cleanUp() {
        bookTitle_TextField.clear();
        bookId_TextField.clear();
        author_TextField.clear();
        year_TextField.clear();
        description_TextArea.clear();
        category_TextField.clear();
        amount_TextField.clear();
        thumbnail_ImageView.setImage(null);
        book = null;
        selected = false;
        internetError_Label.setVisible(false);
    }
}
