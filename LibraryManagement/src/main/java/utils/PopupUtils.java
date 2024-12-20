package utils;
import controllers.BookPopupController;
import entities.Book;
import controllers.ChatController;
import controllers.QuickMessageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class PopupUtils {

    public static void showAlert(String message) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Message!");
        successAlert.setHeaderText(null);
        successAlert.setContentText(message);

        successAlert.showAndWait();
    }

    public static boolean showConfirmationDialog() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Warning!");
        confirmationDialog.setHeaderText("Are you sure?");
        confirmationDialog.setContentText("Do you really want to proceed with this action?");

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        return result.isEmpty() || result.get() != ButtonType.OK;
    }

    public static void openChatWindow(String username, PopupClosedCallback callback) {
        Stage chatStage = new Stage();
        VBox chatBox = null;

        FXMLLoader rootLoader = new FXMLLoader(PopupUtils.class.getResource("/fxml/Message.fxml"));
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

        chatStage.setOnCloseRequest(e -> {
            if (callback != null) {
                callback.onPopupClosed(username);
            }
        });

        chatStage.show();
    }

    public static void openQuickMessage(String username) {
        Stage chatStage = new Stage();
        VBox chatBox = null;

        FXMLLoader rootLoader = new FXMLLoader(PopupUtils.class.getResource("/fxml/QuickMessage.fxml"));
        try {
            chatBox = rootLoader.load();
            QuickMessageController chatController = rootLoader.getController();
            chatController.setUsername(username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene chatScene = new Scene(chatBox, 400, 250);
        chatStage.setScene(chatScene);
        chatStage.setTitle("Quick Message");
        chatStage.setResizable(false);

        chatStage.show();
    }

    public static void openBookPopup(Book book,  boolean isPostComment) {
        Stage bookStage = new Stage();
        Parent root = null;

        FXMLLoader rootLoader = new FXMLLoader(PopupUtils.class.getResource("/fxml/BookPopup.fxml"));
        try {
            root = rootLoader.load();
            BookPopupController bookPopupController = rootLoader.getController();
            bookPopupController.setBook(book, isPostComment);
            bookPopupController.refreshData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene chatScene = new Scene(root);
        bookStage.setScene(chatScene);
        bookStage.setTitle(book.getTitle());
        bookStage.setResizable(false);

        bookStage.show();
    }

    public interface PopupClosedCallback {
        void onPopupClosed(String username);
    }
}
