package Controllers;

import database.DatabaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    private void handleRemoveBook(ActionEvent event) {
        String bookID = bookID_TextField.getText();
        int amount = 0;
        try {
            amount = Integer.parseInt(amount_TextField.getText());
        } catch (NumberFormatException e) {
            removeMessage_Label.setText("Please enter a valid number");
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
                if (quantity > amount) {
                    PreparedStatement updateStatement = connection.prepareStatement(sqlQueryUpdate);
                    quantity -= amount;
                    updateStatement.setInt(1, quantity);
                    updateStatement.setString(2, bookID);
                    updateStatement.executeUpdate();
                    removeMessage_Label.setText("Book has been removed");
                } else {
                    removeMessage_Label.setText("This number is larger than book amount");
                }
            } else {
                removeMessage_Label.setText("Book does not exist");
                return;
            }

        } catch (SQLException e) {
            System.out.println("SQLException -> removeBook function of AdminRemoveBook controller: " + e.getMessage());
            return;
        }
    }



    private void cleanUp() {
        bookID_TextField.clear();
        amount_TextField.clear();
        removeMessage_Label.setText("");
    }
}
