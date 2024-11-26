package Controllers;

import Entity.LibraryManagement;
import Utils.AccountUserUtils;
import database.DatabaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class LoginController {

    @FXML
    private TextField username_TextField;
    @FXML
    private TextField password_TextField; // change to password field
    @FXML
    private Label loginMessage_Label;

    @FXML
    public void initialize() {
    }

    private void cleanUp() {
        username_TextField.clear();
        password_TextField.clear();
        loginMessage_Label.setText("");
    }

    @FXML
    private void loginButtonOnAction(ActionEvent event) {
        String username = username_TextField.getText();
        String password = password_TextField.getText();

        Connection connection = DatabaseController.getConnection();

        boolean loginSuccessful = AccountUserUtils.isExistedAccount(username, password);

        if (!loginSuccessful) {
            loginMessage_Label.setText("Invalid username or password");
        } else {
            switchToHomeScene();
            LibraryManagement.getInstance().setCurrentAccount(username);
            LibraryManagement.getInstance().setCurrentPassword(password);
            cleanUp();
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
