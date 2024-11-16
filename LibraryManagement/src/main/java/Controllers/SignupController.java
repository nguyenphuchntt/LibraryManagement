package Controllers;

import Entity.Account;
import Entity.Library;
import Entity.Person;
import database.DatabaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SignupController {

    @FXML
    private TextField username_TextField;
    @FXML
    private TextField password_TextField; // change to password field
    @FXML
    private TextField confirmPassword_TextField; // change to password field
    @FXML
    private Label signupMessage_Label;

    @FXML
    public void initialize() {}

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        String username = username_TextField.getText();
        String password = password_TextField.getText();
        String confirmPassword = confirmPassword_TextField.getText();
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signupMessage_Label.setText("Please fill all the fields");
            return;
        }
        if (!password.equals(confirmPassword)) {
            signupMessage_Label.setText("Passwords do not match");
            return;
        }

        try {
            ResultSet user = null;
            Connection connection = DatabaseController.getConnection();
            String sqlQuery = "SELECT * FROM account WHERE username = ?";
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, username);
            user = preparedStatement.executeQuery();

            if (user.next()) {
                signupMessage_Label.setText("Username already exists");
                return;
            }
        } catch (SQLException e) {
            System.out.println("SQLException -> Create account: " + e.getMessage());
        }

        long timestamp = System.currentTimeMillis() % 1000000;
        int randomNum = new Random().nextInt(1000);

        String userID = String.format("%012d%03d", timestamp, randomNum);

        Person user = new Person.Builder<>()
                .person_ID(userID)
                .role("user")
                .build();

        timestamp = System.currentTimeMillis() % 1000000;
        String accountID = String.format("%012d%03d", timestamp, randomNum);

        Account newAccount = new Account.Builder()
                .account_ID(accountID)
                .user_ID(userID)
                .username(username)
                .password(password)
                .typeAccount("user")
                .build();

        System.out.println(newAccount.getUser_ID());

//        user.setAccount(newAccount);
//        newAccount.setOwner(user);

        DatabaseController.addUser(user);
        DatabaseController.addAccount(newAccount);

        switchToLoginScene();
        username_TextField.clear();
        password_TextField.clear();
        confirmPassword_TextField.clear();
    }

    @FXML
    private void handleHadAccount(ActionEvent event) {
        switchToLoginScene();
    }

    private void switchToLoginScene() {
        try {
            Controller.getInstance().goToScene("Login.fxml");
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
        }
    }


}
