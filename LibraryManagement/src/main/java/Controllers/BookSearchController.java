package Controllers;

import Entity.Book;
import database.DatabaseController;
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


    private ObservableMap<Integer, Integer> xColumnValues = FXCollections.observableHashMap();

    FilteredList<Book> filteredBooks;

    @FXML
    public void initialize() {
        mapColumnValue();

        ObservableList<Book> bookList = FXCollections.observableArrayList(DatabaseController.getAllBooks());
        searchTable_TableView.setItems(bookList);

        // checkbox column
        amount_Column.setCellFactory(CheckBoxTableCell.forTableColumn(index -> {
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(xColumnValues.getOrDefault(index.intValue(), 0) > 0);

            booleanProperty.addListener((obs, wasSelected, isNowSelected) -> {
                xColumnValues.put(index.intValue(), isNowSelected ? 1 : 0);
            });

            return booleanProperty;
        }));

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

    @FXML
    private void handleSearch() {
        String title = searchBar_TextField.getText().isEmpty() ? null : searchBar_TextField.getText();
        String isbn = isbn_TextField.getText().isEmpty() ? null : isbn_TextField.getText();
        String author = author_TextField.getText().isEmpty() ? null : author_TextField.getText();
        String category = category_TextField.getText().isEmpty() ? null : category_TextField.getText();
        String year = year_TextField.getText().isEmpty() ? null : year_TextField.getText();

        ObservableList<Book> bookList = FXCollections.observableArrayList(DatabaseController.searchBook(
                isbn, title, author, category, year
        ));

        searchTable_TableView.setItems(bookList);
    }

}
