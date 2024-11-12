package Controllers;

import Entity.Account;
import Entity.Library;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class LoginController {

    @FXML
    private TextField username_TextField;
    @FXML
    private TextField password_TextField; // change to password field
    @FXML
    private Label loginMessage_Label;

    private List<Account> accounts;

    @FXML
    public void initialize() {
        accounts = Library.getInstance().getManagerAccounts(); // chi lay manager account

        accounts.add(new Account.Builder()
                .username("admin001")
                .password("Admin001")
                .build()
        );           // account for testing
    }

    @FXML
    private void loginButtonOnAction(ActionEvent event) {
        boolean loginSuccessful = false;
        String username = username_TextField.getText();
        String password = password_TextField.getText();

        for (Account account : accounts) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                loginMessage_Label.setText("Welcome " + account.getUsername());
                switchToHomeScene();
                username_TextField.clear();
                password_TextField.clear();
                loginMessage_Label.setText("");
                loginSuccessful = true;
            }
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
