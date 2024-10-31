package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    private Stage stage;

    private FXMLLoader loadFxml(String fxmlFileName) throws IOException {
        return new FXMLLoader(getClass().getResource("/fxml/" + fxmlFileName));
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleHomeTab_Button() throws Exception {
        switchScene("Home.fxml");
    }

    @FXML
    private void handleBookSearch_MenuItem() throws Exception {
        switchScene("Book_Search.fxml");
    }

    @FXML
    private void handleBookReturn_MenuItem() throws Exception {
        switchScene("Book_Return.fxml");
    }

    @FXML
    private void handleAdminOverview_MenuItem() throws Exception {
        switchScene("Admin_Overview.fxml");
    }

    @FXML
    private void handleAdminAnnouncement_MenuItem() throws Exception {
        switchScene("Admin_Announcement.fxml");
    }

    @FXML
    private void handleAdminAlterBook_MenuItem() throws Exception {
        switchScene("Admin_AlterBook.fxml");
    }

    @FXML
    private void handleAdminAddBook_MenuItem() throws Exception {
        switchScene("Admin_AddBook.fxml");
    }

    @FXML
    private void handleAdminRemoveBook_MenuItem() throws Exception {
        switchScene("Admin_RemoveBook.fxml");
    }

    @FXML
    private void handleAccount_Button() throws Exception {
        switchScene("Account.fxml");
    }

    private void switchScene(String fxmlFileName) throws Exception {
        AnchorPane newRoot = null;
        try {
            newRoot = (AnchorPane) loadFxml(fxmlFileName).load();
        } catch (Exception e) {
            System.out.println("Error loading fxml: " + fxmlFileName);
            throw e;
        }

        Scene newScene = new Scene(newRoot);

        HBox topSidebar = SideBarLoader.getTopSidebar();
        VBox leftSidebar = SideBarLoader.getLeftSidebar();
        SideBarLoader.getController().setStage(stage); // can be deleted

        AnchorPane.setTopAnchor(topSidebar, 0.0);
        AnchorPane.setLeftAnchor(topSidebar, 0.0);

        AnchorPane.setTopAnchor(leftSidebar, 45.0);
        AnchorPane.setLeftAnchor(leftSidebar, 0.0);

        newRoot.getChildren().add(topSidebar);
        newRoot.getChildren().add(leftSidebar);

        stage.setScene(newScene);
        stage.show();
    }
}
