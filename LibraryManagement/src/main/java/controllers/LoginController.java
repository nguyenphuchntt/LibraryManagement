package controllers;

import entities.Account;
import entities.LibraryManagement;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import utils.AccountUserUtils;
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
    private PasswordField password_PasswordField;
    @FXML
    private TextField password_TextField;
    @FXML
    private Label loginMessage_Label;
    @FXML
    private CheckBox showPassword_Checkbox;

    @FXML
    public void initialize() {
        password_TextField.textProperty().bindBidirectional(password_PasswordField.textProperty());
        password_TextField.setVisible(false);

        showPassword_Checkbox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            password_TextField.setVisible(isSelected);
            password_PasswordField.setVisible(!isSelected);
        });
    }

    private void cleanUp() {
        username_TextField.clear();
        password_PasswordField.clear();
        loginMessage_Label.setText("");
    }

    @FXML
    private void loginButtonOnAction(ActionEvent event) {
        String username = username_TextField.getText();
        String password = password_PasswordField.getText();

        Connection connection = DatabaseController.getConnection();

        Account account = AccountUserUtils.isExistedAccount(username, password);
        boolean loginSuccessful = account != null;

        if (!loginSuccessful) {
            loginMessage_Label.setText("Invalid username or password");
        } else {
            LibraryManagement.getInstance().setCurrentAccount(username);
            LibraryManagement.getInstance().setCurrentPassword(password);
            switchToHomeScene();
            LibraryManagement.getInstance().setRole((account.getTypeAccount()) ? "admin" : "user");
            SideBarLoader.getLeftController().loadUserInfo();
            SideBarLoader.getLeftController().setAdminPanelVisible(account.getTypeAccount());
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
            password_PasswordField.clear();
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
