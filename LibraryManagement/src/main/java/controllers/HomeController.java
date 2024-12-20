package controllers;

import entities.LibraryManagement;
import entities.User;
import javafx.application.Platform;
import utils.AccountUserUtils;
import utils.PopupUtils;
import database.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import utils.TransactionUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeController implements PopupUtils.PopupClosedCallback {

    private List<String> usersChatWith = new ArrayList<>();

    @FXML
    private ListView<HBox> messages_ListView;

    @FXML
    private TextField receiver_TextField;

    @FXML
    private Label notifications_Label;

    @FXML
    private Label totalBorrowedBooks_Label;

    @FXML
    private Label totalReturnedBooks_Label;

    @FXML
    private Label totalIssues_Label;

    private int totalOverDueBooks;
    private int totalBorrowedBooks;
    private int totalReturnedBooks;

    @FXML
    public void initialize() {
        refreshUsersIntoListView();
    }

    public void refreshUsersIntoListView() {
        receiver_TextField.clear();
        loadUsersIntoListView();
        new Thread(() -> {
            refreshTotalTransaction();
            Platform.runLater(this::showTotalTransaction);
        }).start();
    }

    private HBox createUserHBox(User user) {
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
            PopupUtils.openChatWindow(user.getUsername(), this);
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
        if (receiver.equals(LibraryManagement.getInstance().getCurrentAccount().getUsername())) {
            notifications_Label.setText("You can not chat with yourself");
            return;
        }
        usersChatWith.add(receiver);
        PopupUtils.openChatWindow(receiver, this);
    }

    private void loadUsersIntoListView() {
        List<User> users = DatabaseController.getUsersWhoSentMessagesToCurrentUser();
        ObservableList<HBox> userHBoxes = FXCollections.observableArrayList();
        for (User user : users) {
            userHBoxes.add(createUserHBox(user));
        }
        messages_ListView.setItems(userHBoxes);
    }

    @Override
    public void onPopupClosed(String username) {
        this.usersChatWith.remove(username);
    }

    private void refreshTotalTransaction() {
        String username = LibraryManagement.getInstance().getCurrentAccount().getUsername();
        totalOverDueBooks = TransactionUtils.getTotalOverDueTransactions(username);
        totalBorrowedBooks = TransactionUtils.getTotalBorrowedBook(username);
        totalReturnedBooks = TransactionUtils.getTotalReturnedBook(username);
    }

    private void showTotalTransaction() {
        totalBorrowedBooks_Label.setText(String.valueOf(totalBorrowedBooks));
        totalReturnedBooks_Label.setText(String.valueOf(totalReturnedBooks));
        totalIssues_Label.setText(String.valueOf(totalOverDueBooks));
    }
}
