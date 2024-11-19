package Controllers;

import Entity.Book;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class BookReturnController {

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
    private TableView<Book> searchTable_TableView;

    @FXML
    private TableColumn<Book, Boolean> amount_Column;

    @FXML
    private TableColumn<Book, String> bookTitle_Column;

    @FXML
    private TableColumn<Book, String> bookID_Column;

    @FXML
    private TableColumn<Book, String> category_Column;

    @FXML
    private TableColumn<Book, String> borrowDate_Column;

    @FXML
    private TableColumn<Book, String> status_Column;

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
    private TextArea comment_TextField;
}
