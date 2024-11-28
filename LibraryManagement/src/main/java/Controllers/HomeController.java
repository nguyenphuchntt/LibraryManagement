package Controllers;

import Entity.LibraryManagement;
import Entity.Message;
import Entity.Person;
import Utils.AccountUserUtils;
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
import javafx.scene.control.TextField;
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
    private TextField receiver_TextField;

    @FXML
    private Label notifications_Label;

    @FXML
    public void initialize() {
        loadUsersIntoListView();
    }

    private HBox createUserHBox(Person user) {
        HBox hbox = new HBox();
        hbox.setSpacing(10);

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

    @FXML
    private void handleNewMessagesButton() {
        String receiver = receiver_TextField.getText().trim();
        if (receiver.isEmpty()) {
            return;
        }
        if (!AccountUserUtils.isExistedUsername(receiver)) {
            notifications_Label.setText("Username does not exist");
            return;
        }
        notifications_Label.setText("");
        if (usersChatWith.contains(receiver)) {
            notifications_Label.setText("You are already in a chat");
            return;
        }
        if (receiver.equals(LibraryManagement.getInstance().getCurrentAccount())) {
            notifications_Label.setText("You can not chat with yourself");
            return;
        }
        usersChatWith.add(receiver);
        openChatWindow(receiver);
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
            ChatController chatController = rootLoader.getController();
            chatController.setup(username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        chatBox.setSpacing(10);

        Scene chatScene = new Scene(chatBox, 700, 500);
        chatStage.setScene(chatScene);
        chatStage.setTitle("Chat with " + username);
        chatStage.setResizable(false);

        chatStage.setOnCloseRequest((WindowEvent event) -> {
            usersChatWith.remove(username);
        });

        chatStage.show();
    }
}
