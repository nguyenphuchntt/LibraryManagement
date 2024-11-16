package Controllers;

import database.DatabaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class AdminRemoveBookController {

    @FXML
    private TextField bookID_TextField;

    @FXML
    private TextField amount_TextField;

    @FXML
    private Button remove_Button;

    @FXML
    private Label removeMessage_Label;

    @FXML
    private Label description_Label;

    @FXML
    private Button check_Button;

    @FXML
    private void handleRemoveBook(ActionEvent event) {
        String bookID = bookID_TextField.getText();
        int amount = 0;
        try {
            amount = Integer.parseInt(amount_TextField.getText());
            if (amount < 0) {
                removeMessage_Label.setText("Invalid Amount");
                return;
            }
        } catch (NumberFormatException e) {
            removeMessage_Label.setText("Please enter a valid number");
            return;
        }

        if (PopupController.showConfirmationDialog()) {
            cleanUp();
            return;
        }

        Connection connection = DatabaseController.getConnection();

        String sqlQuerySelect = "SELECT quantity FROM book" +
                " WHERE book_id = ?";
        String sqlQueryUpdate = "UPDATE book SET quantity = ? WHERE book_id = ?";
        try {
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement selectStatement = connection.prepareStatement(sqlQuerySelect);
            selectStatement.setString(1, bookID);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");
                if (quantity >= amount) {
                    PreparedStatement updateStatement = connection.prepareStatement(sqlQueryUpdate);
                    quantity -= amount;
                    updateStatement.setInt(1, quantity);
                    updateStatement.setString(2, bookID);
                    updateStatement.executeUpdate();
                    showSuccessAlert();
                    cleanUp();
                } else {
                    removeMessage_Label.setText("This number is larger than book amount");
                }
            } else {
                removeMessage_Label.setText("Book does not exist");
            }

        } catch (SQLException e) {
            System.out.println("SQLException -> removeBook function of AdminRemoveBook controller: " + e.getMessage());
            return;
        }
    }

    @FXML
    private void handleCheck(ActionEvent event) {
        removeMessage_Label.setText("");
        String bookID = bookID_TextField.getText();

        Connection connection = DatabaseController.getConnection();

        String sqlQuerySelect = "SELECT book_title, description, quantity FROM book" +
                " WHERE book_id = ?";

        try {
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement selectStatement = connection.prepareStatement(sqlQuerySelect);
            selectStatement.setString(1, bookID);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String description = null;
                String title = resultSet.getString("book_title");
                if (resultSet.getString("description") != null) {
                    description = resultSet.getString("description");
                }
                int quantity = resultSet.getInt("quantity");
                description_Label.setText("Title: " + title + "\nDescription: " + (description == null ? "NULL" : description)
                                            + "\nQuantity: " + quantity);
            } else {
                removeMessage_Label.setText("Book does not exist");
            }

        } catch (SQLException e) {
            System.out.println("SQLException -> check info function of AdminRemoveBook controller: " + e.getMessage());
        }
    }

    public void showSuccessAlert() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Message!");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Remove book successfully!");

        successAlert.showAndWait();
    }

    private void cleanUp() {
        bookID_TextField.clear();
        amount_TextField.clear();
        removeMessage_Label.setText("");
        description_Label.setText("");
    }
}
