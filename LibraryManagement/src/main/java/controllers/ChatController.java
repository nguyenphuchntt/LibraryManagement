package controllers;

import entities.LibraryManagement;
import entities.Message;
import entities.User;
import utils.AccountUserUtils;
import utils.FormatUtils;
import database.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Timestamp;
import java.util.List;

public class ChatController {

    private String username;

    @FXML
    private VBox chatBox;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendButton;

    @FXML
    public void initialize() {
        sendButton.setOnAction(event -> sendMessage());
    }

    public void setup(String username) {
        this.username = username;
        loadPreviousMessages();
    }

    private void loadPreviousMessages() {
        List<Message> messages = DatabaseController.getMessagesBetweenUsers(username);
        for (Message message : messages) {
            HBox messageBox = new HBox();
            messageBox.setMinWidth(chatBox.getMinWidth());
            Text messageText = new Text(message.getSenderId().getUsername()
                    + "[" + FormatUtils.getDateTimeFormatForMessage().format(message.getTimestamp()) + "]: " + message.getContent());
            messageText.setWrappingWidth(messageBox.getWidth());
            messageBox.getChildren().add(messageText);
            chatBox.getChildren().add(messageBox);
        }
    }

    private void sendMessage() {
        String content = messageInput.getText();
        if (content.isEmpty()) {
            return;
        }

        String currentUsername = LibraryManagement.getInstance().getCurrentAccount();
        User currentAccount = AccountUserUtils.getCurrentUser();
        User receiverAccount = AccountUserUtils.getUserInfo(username);
        Timestamp now = new Timestamp(System.currentTimeMillis());

        Message message = new Message(currentAccount, receiverAccount, content, now);
        DatabaseController.saveEntity(message);

        HBox messageBox = new HBox();
        Text messageText = new Text(currentUsername
                + "[" + FormatUtils.getDateTimeFormatForMessage().format(now) + "]: " + content);

        messageBox.getChildren().add(messageText);

        chatBox.getChildren().add(messageBox);

        messageInput.clear();
    }
}
