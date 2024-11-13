package Controllers;

import Entity.Account;
import Entity.Library;
import Entity.LibraryManagement;
import database.DatabaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.List;

public class LoginController {

    @FXML
    private TextField username_TextField;
    @FXML
    private TextField password_TextField; // change to password field
    @FXML
    private Label loginMessage_Label;

//    private List<Account> accounts;

    @FXML
    public void initialize() {
//        accounts = Library.getInstance().getManagerAccounts(); // chi lay manager account
//
//        accounts.add(new Account.Builder()
//                .username("admin001")
//                .password("Admin001")
//                .build()
//        );           // account for testing
    }

    @FXML
    private void loginButtonOnAction(ActionEvent event) {
        boolean loginSuccessful = false;
        String username = username_TextField.getText();
        String password = password_TextField.getText();

//        for (Account account : accounts) {
//            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
//                loginMessage_Label.setText("Welcome " + account.getUsername());
//                switchToHomeScene();
//                username_TextField.clear();
//                password_TextField.clear();
//                loginMessage_Label.setText("");
//                loginSuccessful = true;
//            }
//        }
//        if (!loginSuccessful) {
//            loginMessage_Label.setText("Invalid username or password");
//        }
        Connection connection = DatabaseController.getConnection();

        String sqlQuery = "SELECT * FROM account WHERE username = ? AND password = ?";
        try {
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                switchToHomeScene();
                LibraryManagement.getInstance().setCurrentAccount(username);
                username_TextField.clear();
                password_TextField.clear();
                loginMessage_Label.setText("");
                loginSuccessful = true;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("Login: " + username + " failed by exception: " + e);
        }
        if (!loginSuccessful) {
            loginMessage_Label.setText("Invalid username or password");
        }
    }

    @FXML
    private void handleDontHaveAccount(ActionEvent event) {
        switchToSignupScene();
    }

    private void switchToSignupScene() {
        try {
            username_TextField.clear();
            password_TextField.clear();
            loginMessage_Label.setText("");
            Controller.getInstance().goToScene("Signup.fxml");
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
        }
    }

    private void switchToHomeScene() {
        try {
            Controller.getInstance().goToScene("Home.fxml");
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
        }
    }

}
