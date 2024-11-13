package Controllers;

import Entity.LibraryManagement;
import database.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;

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
    private void initialize() {
        refreshInfo();
    }

    public void refreshInfo() {
        String sqlQuery = "select name, user.yearOfBirth, user.department, account.joined_date\n" +
                "from account\n" +
                "JOIN\n" +
                "user ON account.user_id = user.user_id\n" +
                "where username = ?";
        try {
            Connection connection = DatabaseController.getConnection();
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, LibraryManagement.getInstance().getCurrentAccount());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                name_Label.setText("NAME: " + (!name.isEmpty() ? name : "NULL"));

                String yearOfBirthStr = resultSet.getString("yearOfBirth");
                String ageStr = (yearOfBirthStr != null) ? yearOfBirthStr : "NULL";
                age_Label.setText("AGE: " + ageStr);
                if (!ageStr.equals("NULL")) {
                    int age = Integer.parseInt(ageStr);
                }

                String department = resultSet.getString("department");
                department_Label.setText("DEPARTMENT: " + (department != null ? department : "NULL"));

                String joinedDate = resultSet.getString("joined_date");
                usedTime_Label.setText("JOINED DATE: " + (joinedDate != null ? joinedDate : "NULL"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException -> initialize function of Account controller: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        LibraryManagement.getInstance().logout();
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
