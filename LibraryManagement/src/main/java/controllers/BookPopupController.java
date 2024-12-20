package controllers;

import database.DatabaseController;
import entities.Book;
import entities.Comment;
import entities.LibraryManagement;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import utils.BookUtils;
import utils.PopupUtils;

public class BookPopupController {

    @FXML
    private Label id_Label;

    @FXML
    private Label title_Label;

    @FXML
    private Label author_Label;

    @FXML
    private Label publisher_Label;

    @FXML
    private Label year_Label;

    @FXML
    private Label category_Label;

    @FXML
    private Label quantity_Label;

    @FXML
    private Label description_Label;

    @FXML
    private Label postCommentMessage_Label;

    @FXML
    private Label thumbnailMessage_Label;

    @FXML
    private Label comments_Label;

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
    private ImageView thumbnail_ImageView;

    @FXML
    private AnchorPane oldComment_AnchorPane;

    @FXML
    private AnchorPane postComment_AnchorPane;

    private Book book;
    private String star = null;
    private boolean isPostComment;

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
    private void initialize() {
    }

    public void setBook(Book book, boolean isPostComment) {
        this.isPostComment = isPostComment;
        this.book = book;
    }

    public void refreshData() {
        title_Label.setText(book.getTitle());
        id_Label.setText("ID/ISBN: " + book.getId());
        publisher_Label.setText("Publisher: " + (book.getPublisher() == null ? "" : book.getPublisher()));
        year_Label.setText("Year: " + (book.getYear() == 0 ? "" : book.getYear()));
        category_Label.setText("Category: " + (book.getCategory() == null ? "" : book.getCategory()));
        quantity_Label.setText("Quantity: " + book.getQuantity());
        description_Label.setText("Description: " + (book.getDescription() == null ? "" : book.getDescription()));
        author_Label.setText("Author: " + (book.getAuthor() == null ? "" : book.getAuthor()));
        if (book.getThumbnailLink() != null) {
            thumbnailMessage_Label.setText("");
            thumbnail_ImageView.setImage(new Image(book.getThumbnailLink()));
        } else {
            thumbnail_ImageView.setImage(null);
            thumbnailMessage_Label.setText("Not Available!");
        }
        if (!isPostComment) {
            oldComment_AnchorPane.setVisible(true);
            Comment comment = BookUtils.getBestCommentOfBook(book.getId());
            if (comment != null) {
                comments_Label.setText("COMMENT:\n" + comment.toString() + '\n');
            } else {
                comments_Label.setText("");
            }
        } else {
            postComment_AnchorPane.setVisible(true);
        }
    }

    @FXML
    private void handlePostComment() {
        if (comment_TextArea.getText().isEmpty()) {
            postCommentMessage_Label.setText("Please enter a valid comment");
            cleanUpData();
            return;
        }
        if (star == null) {
            postCommentMessage_Label.setText("Please enter a valid rating star");
            cleanUpData();
            return;
        }
        String username = LibraryManagement.getInstance().getCurrentAccount().getUsername();
        Double rate = Double.parseDouble(star);
        String commentContent = comment_TextArea.getText();
        Comment comment = new Comment(book, username, commentContent, rate);
        if (comment.isWrittenComment(username, book.getId())) {
            cleanUpData();
            postCommentMessage_Label.setText("You have been written this book's comment!");
            comment = null;
            return;
        }
        DatabaseController.saveEntity(comment);
        PopupUtils.showAlert("Posted comment successfully");
        cleanUpData();
    }

    private void cleanUpData() {
        comment_TextArea.clear();
        star = null;
    }

}
