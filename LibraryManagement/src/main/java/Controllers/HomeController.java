package Controllers;

import Entity.LibraryManagement;
import Entity.Message;
import Entity.Person;
import Utils.FormatUtils;
import database.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeController {

    private List<String> usersChatWith = new ArrayList<>();

    @FXML
    private ListView<HBox> messages_ListView;

    @FXML
    public void initialize() {
        loadUsersIntoListView();
    }

    private HBox createUserHBox(Person user) {
        HBox hbox = new HBox();
        hbox.setSpacing(50);

        Label roleLabel = new Label(user.getAccount().getTypeAccount() ? "admin" : "user");
        Label usernameLabel = new Label(user.getUsername());

        hbox.getChildren().addAll(roleLabel, usernameLabel);

        hbox.setOnMouseClicked(event -> {
            if (usersChatWith.contains(user.getUsername())) {
                return;
            }
            usersChatWith.add(user.getUsername());
            openChatWindow(user.getUsername());
        });

        return hbox;
    }

    private void loadUsersIntoListView() {
        List<Person> users = DatabaseController.getUsersWhoSentMessagesToCurrentUser();
        ObservableList<HBox> userHBoxes = FXCollections.observableArrayList();
        for (Person user : users) {
            userHBoxes.add(createUserHBox(user));
        }
        messages_ListView.setItems(userHBoxes);
    }

    private void openChatWindow(String username) {
        Stage chatStage = new Stage();
        VBox chatBox = null;

        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/fxml/Message.fxml"));
        try {
            chatBox = rootLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        chatBox.setSpacing(10);
        List<Message> messages = DatabaseController.getMessagesBetweenUsers(username);

        for (Message message : messages) {
            Text messageText = null;
            messageText = new Text(message.getSenderId().getUsername()
                    + "[" + FormatUtils.getDateTimeFormatForMessage().format(message.getTimestamp()) + "]: " + message.getContent());
            chatBox.getChildren().add(messageText);
        }

        Scene chatScene = new Scene(chatBox, 400, 600);
        chatStage.setScene(chatScene);
        chatStage.setTitle("Chat with " + username);
        chatStage.setResizable(false);

        chatStage.setOnCloseRequest((WindowEvent event) -> {
            usersChatWith.remove(username);
        });

        chatStage.show();
    }
}
