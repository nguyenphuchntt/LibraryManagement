package Controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class PopupController {

//    @FXML
//    private TableView<PopupElement> errorTable;
//
//    @FXML
//    private TableColumn<PopupElement, String> bookNameColumn;
//
//    @FXML
//    private TableColumn<PopupElement, String> errorColumn;
//
//    @FXML
//    public void initialize() {
//        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
//        errorColumn.setCellValueFactory(new PropertyValueFactory<>("errorMessage"));
//    }
//
//    @FXML
//    private void handleClose() {
//        Stage stage = (Stage) errorTable.getScene().getWindow();
//        stage.close();
//    }
//
//    public void setErrorData(List<PopupElement> errors) {
//        errorTable.getItems().clear();
//        errorTable.getItems().addAll(errors);
//    }
//
//    private void confirmationPopUp() throws IOException {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Warning!");
//        alert.setHeaderText("Are you sure?");
//        alert.setContentText("If you have checked carefully, click on YES button");
//
//        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
//
//        alert.showAndWait().ifPresent(response -> {
//            if (response == ButtonType.YES) {
//                System.out.println("User chose Yes");
//                //do st
//            } else {
//                System.out.println("User chose No");
//                //do st
//            }
//        });
//    }
//
//    public void showErrorPopup() {
//        Stage stage = new Stage();
//        Scene scene = errorTable.getScene();
//        stage.setScene(scene);
//        stage.setTitle("Error :(");
//        stage.show();
//    }

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
