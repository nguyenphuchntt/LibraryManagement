package Controllers;

import Entity.PopupElement;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PopupController {

    @FXML
    private TableView<PopupElement> errorTable;

    @FXML
    private TableColumn<PopupElement, String> bookNameColumn;

    @FXML
    private TableColumn<PopupElement, String> errorColumn;

    @FXML
    public void initialize() {
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        errorColumn.setCellValueFactory(new PropertyValueFactory<>("errorMessage"));
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) errorTable.getScene().getWindow();
        stage.close();
    }

    public void setErrorData(List<PopupElement> errors) {
        errorTable.getItems().clear();
        errorTable.getItems().addAll(errors);
    }

    private void confirmationPopUp() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning!");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("If you have checked carefully, click on YES button");

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                System.out.println("User chose Yes");
                //do st
            } else {
                System.out.println("User chose No");
                //do st
            }
        });
    }

    public void showErrorPopup() {
        Stage stage = new Stage();
        Scene scene = errorTable.getScene();
        stage.setScene(scene);
        stage.setTitle("Error :(");
        stage.show();
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
