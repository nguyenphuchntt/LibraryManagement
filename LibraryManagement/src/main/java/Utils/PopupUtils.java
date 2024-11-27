package Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
    
}
