package controllers;

import entities.LibraryManagement;
import entities.Message;
import entities.Person;
import utils.AccountUserUtils;
import database.DatabaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.Timestamp;

public class QuickMessageController {

    @FXML
    private TextArea message_TextArea;

    String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private void sendMessage(ActionEvent event) {
        String content = message_TextArea.getText();
        if (content.trim().isEmpty()) {
            return;
        }

        String currentUsername = LibraryManagement.getInstance().getCurrentAccount();
        Person currentAccount = AccountUserUtils.getCurrentUser();
        Person receiverAccount = AccountUserUtils.getUserInfo(username);
        Timestamp now = new Timestamp(System.currentTimeMillis());

        Message message = new Message(currentAccount, receiverAccount, content, now);
        DatabaseController.saveEntity(message);


        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
