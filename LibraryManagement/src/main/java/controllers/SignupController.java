package controllers;

import utils.AccountUserUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.PasswordHasher;

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

        if(AccountUserUtils.isExistedUsername(username)) {
            signupMessage_Label.setText("Account already exists");
            return;
        }
        try {
            String hashedPassword = PasswordHasher.hashPassword(password);
            AccountUserUtils.addAccount(username, hashedPassword);
            AccountUserUtils.addUser(username);
        } catch (IllegalArgumentException e) {
            signupMessage_Label.setText("Invalid username or password");
            return;
        }
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
