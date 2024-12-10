package controllers;

import entities.Book;
import entities.LibraryManagement;
import entities.Manager;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.BookUtils;
import utils.FormatUtils;
import utils.PopupUtils;
import database.DatabaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class AdminRemoveBookController {

    @FXML
    private TextField bookID_TextField;

    @FXML
    private Button remove_Button;

    @FXML
    private Label removeMessage_Label;

    @FXML
    private Label description_Label;

    @FXML
    private Button check_Button;

    @FXML
    private ImageView thumbnail_ImageView;

    @FXML
    private Label thumbnail_Label;


    @FXML
    private void handleRemoveBook(ActionEvent event) {
        String bookID = bookID_TextField.getText();

        if (PopupUtils.showConfirmationDialog()) {
            cleanUp();
            return;
        }
        if (BookUtils.isBeingBorrowed(bookID)) {
            removeMessage_Label.setText("You are being borrowed so you can't remove this book -(");
            return;
        }
        new Thread(() -> {
            ((Manager) (LibraryManagement.getInstance().getCurrentUser())).removeBooks(bookID);
            Platform.runLater(this::cleanUp);
        }).start();
    }

    @FXML
    private void handleCheck(ActionEvent event) {
        String isbn = bookID_TextField.getText();
        Book book = BookUtils.getBookByISBN(isbn);
        if (book == null) {
            removeMessage_Label.setText("Book not found");
            return;
        }
        StringBuilder bookDescription = new StringBuilder();
        bookDescription.append("- Book isbn: ").append(book.getId()).append("\n");
        bookDescription.append("- Book title: ").append(book.getTitle()).append("\n");
        bookDescription.append("- Author: ").append((book.getAuthor() == null) ? "null" : book.getAuthor()).append("\n");
        bookDescription.append("- Category: ").append((book.getCategory() == null) ? "null" : book.getCategory()).append("\n");
        bookDescription.append("- Quantity: ").append(book.getQuantity()).append("\n");
        bookDescription.append("- Description: ").append((book.getDescription() == null) ? "null" : book.getDescription()).append("\n");
        description_Label.setText(bookDescription.toString());
        if (book.getThumbnailLink() != null) {
            thumbnail_ImageView.setImage(new Image(book.getThumbnailLink()));
            thumbnail_Label.setText("Not available!");
        } else {
            thumbnail_ImageView.setImage(null);
            thumbnail_Label.setText("");
        }
    }

    public void cleanUp() {
        bookID_TextField.clear();
        thumbnail_Label.setText("");
        removeMessage_Label.setText("");
        description_Label.setText("");
    }
}
