package Controllers;

import Entity.Account;
import Entity.LibraryManagement;
import Entity.Person;
import database.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.sql.*;

public class AccountController {

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

    public void refreshInfo() {
        cleanUp();

        Person currentUser = DatabaseController.getUserInfo(LibraryManagement.getInstance().getCurrentAccount());

        name_Label.setText("NAME: " + currentUser.getName());

        age_Label.setText("AGE: " + ((currentUser.getYearOfBirth() != null) ? String.valueOf(currentUser.getYearOfBirth()) : "null"));

        department_Label.setText("DEPARTMENT: " + currentUser.getDepartment());

        Account currentAccount = DatabaseController.getAccountInfo(LibraryManagement.getInstance().getCurrentAccount());

        usedTime_Label.setText("JOINED TIME: " + currentAccount.getJoined_date());

//
//        String sqlQuery = "select user.name, user.yearOfBirth, user.department, account.joined_date\n" +
//                "from account\n" +
//                "JOIN\n" +
//                "user ON account.username = user.username\n" +
//                "where user.username = ?";
//        try {
//            Connection connection = DatabaseController.getConnection();
//            Statement useDatabaseStatement = connection.createStatement();
//            useDatabaseStatement.execute("USE library");
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//            preparedStatement.setString(1, LibraryManagement.getInstance().getCurrentAccount());
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                String name = resultSet.getString("name");
//                name_Label.setText("NAME: " + (!name.isEmpty() ? name : "NULL"));
//
//                String yearOfBirthStr = resultSet.getString("yearOfBirth");
//                String ageStr = (yearOfBirthStr != null) ? yearOfBirthStr : "NULL";
//                age_Label.setText("AGE: " + ageStr);
//                if (!ageStr.equals("NULL")) {
//                    int age = Integer.parseInt(ageStr);
//                }
//
//                String department = resultSet.getString("department");
//                department_Label.setText("DEPARTMENT: " + (department != null ? department : "NULL"));
//
//                String joinedDate = resultSet.getString("joined_date");
//                usedTime_Label.setText("JOINED DATE: " + (joinedDate != null ? joinedDate : "NULL"));
//            }
//        } catch (SQLException e) {
//            System.out.println("SQLException -> initialize function of Account controller: " + e.getMessage());
//        }
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
        changePassword(username, newPassword);
    }

    private void changePassword(String username, String password) {
        Connection connection = DatabaseController.getConnection();

        String sqlQuery = "UPDATE account SET password = ? WHERE username = ?";
        try {
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException -> changePassword function of Account controller: " + e.getMessage());
        }
        LibraryManagement.getInstance().setCurrentPassword(password);
        cleanUp();
    }

    private void cleanUp() {
        cfNewPassword_TextField.clear();
        newPassword_TextField.clear();
        currentPassword_TextField.clear();
        changePasswordMessage_Label.setText("");
        setChangePasswordInvisible();
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

}
