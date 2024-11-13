package Controllers;

import Entity.LibraryManagement;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;

public class AccountController {

    @FXML
    private Label username_Label;

    @FXML
    private Label age_Label;

    @FXML
    private Label department_Label;

    @FXML
    private Label usedTime_Label;

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
