package Controllers;

import database.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.Optional;

public class AdminAlterBookController {

    @FXML
    private Label alterMessage_Label;

    @FXML
    private TextField search_TextField;

    @FXML
    private TextField title_TextField;

    @FXML
    private TextField author_TextField;

    @FXML
    private TextField description_TextField;

    @FXML
    private TextField amount_TextField;

    @FXML
    private TextField category_TextField;

    @FXML
    private TextField year_TextField;

    @FXML
    private TextField publisher_TextField;

    @FXML
    private Button search_Button;

    @FXML
    private Button change_Button;

    private boolean searched = false;
    private String book_id = null;

    private void cleanUp() {
        alterMessage_Label.setText("");
        search_TextField.clear();
        title_TextField.clear();
        author_TextField.clear();
        description_TextField.clear();
        amount_TextField.clear();
        category_TextField.clear();
        year_TextField.clear();
        publisher_TextField.clear();
        searched = false;
        book_id = null;
    }

    @FXML
    private void handleSearch() {

        if (search_TextField.getText().isEmpty()) {
            alterMessage_Label.setText("Please enter a book ID / ISBN code");
            return;
        }

        Connection connection = DatabaseController.getConnection();

        if (getProperties(connection)) {
            book_id = search_TextField.getText();
            searched = true;
        } else {
            cleanUp();
        }

    }

    private boolean getProperties(Connection connection) {
        String sqlQuerySelect = "SELECT * FROM book " +
                " WHERE book_id = ?";
        try {
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement selectStatement = connection.prepareStatement(sqlQuerySelect);

            selectStatement.setString(1, search_TextField.getText());

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {

                title_TextField.setText(resultSet.getString("book_title"));

                if (resultSet.getString("author") != null) {
                    author_TextField.setText(resultSet.getString("author"));
                }

                if (resultSet.getString("description") != null) {
                    description_TextField.setText(resultSet.getString("description"));
                }

                category_TextField.setText(resultSet.getString("category"));

                if (resultSet.getString("year") != null) {
                    year_TextField.setText(resultSet.getString("year"));
                }

                if (resultSet.getString("publisher") != null) {
                    publisher_TextField.setText(resultSet.getString("publisher"));
                }

                amount_TextField.setText(resultSet.getString("quantity"));

            } else {
                alterMessage_Label.setText("Book does not exist");
                return false;
            }


        } catch (SQLException e) {
            System.out.println("SQLException -> get book properties function of AdminAlterBook controller: " + e.getMessage());
            return false;
        }
        return true;
    }

    @FXML
    private void handleChanges() {
        if (!searched) {
            alterMessage_Label.setText("Please enter a book ID / ISBN code and search");
            return;
        }
        if (!search_TextField.getText().equals(book_id)) {
            alterMessage_Label.setText("Please don't change book ID / ISBN code");
            return;
        }

        if (!PopupController.showConfirmationDialog()) {
            cleanUp();
            return;
        }

        String newTitle = title_TextField.getText().isEmpty() ? null : title_TextField.getText();
        String newAuthor = author_TextField.getText().isEmpty() ? null : author_TextField.getText();
        String newDescription = description_TextField.getText().isEmpty() ? null : description_TextField.getText();
        String newCategory = category_TextField.getText().isEmpty() ? null : category_TextField.getText();
        String newYearText = year_TextField.getText().isEmpty() ? null : year_TextField.getText();
        String newPublisher = publisher_TextField.getText().isEmpty() ? null : publisher_TextField.getText();
        String newQuantity = amount_TextField.getText().isEmpty() ? null : amount_TextField.getText();

        String sqlQueryUpdate = "UPDATE book SET book_title = ?, author = ?, description = ?, category = ?, year = ?, publisher = ?, quantity = ? WHERE book_id = ?";
        try {

            Connection connection = DatabaseController.getConnection();
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");

            PreparedStatement updateStatement = connection.prepareStatement(sqlQueryUpdate);


            if (newTitle != null) {
                updateStatement.setString(1, newTitle);
            } else {
                alterMessage_Label.setText("Title, Category, Quantity must not be null");
                return;
            }

            if (newAuthor != null) {
                updateStatement.setString(2, newAuthor);
            } else {
                updateStatement.setNull(2, java.sql.Types.VARCHAR);
            }

            if (newDescription != null) {
                updateStatement.setString(3, newDescription);
            } else {
                updateStatement.setNull(3, java.sql.Types.VARCHAR);
            }

            if (newCategory != null) {
                updateStatement.setString(4, newCategory);
            } else {
                alterMessage_Label.setText("Title, Category, Quantity must not be null");
                return;
            }

            if (newYearText != null) {
                try {
                    int newYear = Integer.parseInt(newYearText);
                    if (newYear < 0) {
                        alterMessage_Label.setText("Year must be greater than 0");
                        return;
                    }
                    updateStatement.setInt(5, newYear);
                } catch (NullPointerException e) {
                    alterMessage_Label.setText("Incorrect year format");
                    return;
                }
            } else {
                updateStatement.setNull(5, java.sql.Types.INTEGER);
            }

            if (newPublisher != null) {
                updateStatement.setString(6, newPublisher);
            } else {
                updateStatement.setNull(6, java.sql.Types.VARCHAR);
            }

            if (newQuantity != null) {
                try {
                    int quantity = Integer.parseInt(newQuantity);
                    if (quantity < 0) {
                        alterMessage_Label.setText("Quantity must be greater than 0");
                        return;
                    }
                    updateStatement.setInt(7, quantity);
                } catch (NumberFormatException e) {
                    alterMessage_Label.setText("Incorrect quantity format");
                    return;
                }
            } else {
                alterMessage_Label.setText("Title, Category, Quantity must not be null");
                return;
            }

            updateStatement.setString(8, book_id);

            updateStatement.executeUpdate();

            showSuccessAlert();
            cleanUp();

        } catch (SQLException e) {
            System.out.println("SQLException -> change book properties function of AdminAlterBook controller: " + e.getMessage());
            return;
        }
    }

    public void showSuccessAlert() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Message!");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Alter book successfully!");

        successAlert.showAndWait();
    }
}
