package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChatController {

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

    private void sendMessage() {
        String message = messageInput.getText();
        if (message.isEmpty()) {
            return;
        }

        HBox messageBox = new HBox();
        Text messageText = new Text(message);

        messageBox.getChildren().add(messageText);

        chatBox.getChildren().add(messageBox);

        messageInput.clear();
    }
}
