package controllers;

import entities.Account;
import entities.LibraryManagement;
import entities.User;
import utils.AccountUserUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class AccountController {

    private boolean isChangeInfo = false;

    @FXML
    private Label name_Label;

    @FXML
    private Label age_Label;

    @FXML
    private Label department_Label;

    @FXML
    private Label usedTime_Label;

    @FXML
    private Label password_Label;

    @FXML
    private Label newPassword_Label;

    @FXML
    private Label cfPassword_Label;

    @FXML
    private TextField currentPassword_TextField;

    @FXML
    private TextField newPassword_TextField;

    @FXML
    private TextField cfNewPassword_TextField;

    @FXML
    private Button change_Button;

    @FXML
    private Button changePassword_Button;

    @FXML
    private Pane changePassword_Pane;

    @FXML
    private Label changePasswordMessage_Label;

    @FXML
    private TextField name_TextField;

    @FXML
    private TextField age_TextField;

    @FXML
    private TextField department_TextField;

    @FXML
    private Label changeInfo_Label;

    public void refreshInfo() {
        cleanUp();

        User currentUser = AccountUserUtils.getCurrentUser();

        name_Label.setText("NAME: " + currentUser.getName());

        age_Label.setText("AGE: " + ((currentUser.getYearOfBirth() != null) ? String.valueOf(currentUser.getYearOfBirth()) : "null"));

        department_Label.setText("DEPARTMENT: " + currentUser.getDepartment());

        Account currentAccount = AccountUserUtils.getAccountInfo(LibraryManagement.getInstance().getCurrentAccount());

        usedTime_Label.setText("JOINED TIME: " + currentAccount.getJoined_date());
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        LibraryManagement.getInstance().logout();
        switchToLoginScene();
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        changePassword_Pane.setVisible(true);
    }

    private void setChangePasswordInvisible() {
        changePassword_Pane.setVisible(false);
    }

    @FXML
    private void processChangePassword(ActionEvent event) {
        String currentPassword = currentPassword_TextField.getText();
        String newPassword = newPassword_TextField.getText();
        String cfNewPassword = cfNewPassword_TextField.getText();
        if (!currentPassword.equals(LibraryManagement.getInstance().getCurrentPassword())) {
            changePasswordMessage_Label.setText("Incorrect Password");
            return;
        }
        if (!newPassword.equals(cfNewPassword)) {
            changePasswordMessage_Label.setText("New Password Must Match");
            return;
        }
        if (!Account.isValidPassword(newPassword)) {
            changePasswordMessage_Label.setText("Password Should Have Correct Format");
            return;
        }
        String username = LibraryManagement.getInstance().getCurrentAccount();
        AccountUserUtils.changePassword(username, newPassword);
        cleanUp();
    }

    private void cleanUp() {
        cfNewPassword_TextField.clear();
        newPassword_TextField.clear();
        currentPassword_TextField.clear();
        changePasswordMessage_Label.setText("");
        setChangePasswordInvisible();
        name_TextField.clear();
        age_TextField.clear();
        department_TextField.clear();
        name_TextField.setVisible(false);
        age_TextField.setVisible(false);
        department_TextField.setVisible(false);
    }


    private void switchToLoginScene() {
        try {
            setChangePasswordInvisible();
            Controller.getInstance().goToScene("Login.fxml");
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangeInfoButton() {
        isChangeInfo = isChangeInfo ? false : true;
        if (!isChangeInfo) {
            String name = name_TextField.getText();
            String age = age_TextField.getText();
            int ageInt = 0;
            try {
                if (age != null && !age.trim().isEmpty()) {
                    ageInt = Integer.parseInt(age);
                }
            } catch (NumberFormatException e) {
                changeInfo_Label.setText("Invalid Age");
                return;
            }
            String department = department_TextField.getText();
            AccountUserUtils.updateUserInfo(name, ageInt, department);
            refreshInfo();
        }
        if (isChangeInfo) {
            name_Label.setText("NAME: ");
            age_Label.setText("AGE: ");
            department_Label.setText("DEPARTMENT: ");
        }
        name_TextField.setVisible(isChangeInfo);
        age_TextField.setVisible(isChangeInfo);
        department_TextField.setVisible(isChangeInfo);

    }

}
