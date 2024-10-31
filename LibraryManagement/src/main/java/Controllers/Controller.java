package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private static Stage stage;
    private Map<String, Scene> scenes = new HashMap<>();

    private FXMLLoader loadFxml(String fxmlFileName) throws IOException {
        return new FXMLLoader(getClass().getResource("/fxml/" + fxmlFileName));
    }


    public static void setStage(Stage stage) {
        Controller.stage = stage;
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
        Scene newScene;
        if (scenes.get(fxmlFileName) != null) {
            AnchorPane root = (AnchorPane) scenes.get(fxmlFileName).getRoot();
            newScene = scenes.get(fxmlFileName);
            if (!root.getChildren().contains(SideBarLoader.getLeftSidebar())) {
                Controller.setSideBar(root);
            }
        } else {
            AnchorPane newRoot = null;
            try {
                newRoot = (AnchorPane) loadFxml(fxmlFileName).load();
            } catch (Exception e) {
                System.out.println("Error loading fxml: " + fxmlFileName);
                throw e;
            }
            newScene = new Scene(newRoot);
            Controller.setSideBar(newRoot);
            scenes.put(fxmlFileName, newScene);
        }

        stage.setScene(newScene);
        stage.show();
    }

    public static void setSideBar(AnchorPane root) throws IOException {
        HBox topSidebar = SideBarLoader.getTopSidebar();
        VBox leftSidebar = SideBarLoader.getLeftSidebar();

        AnchorPane.setTopAnchor(topSidebar, 0.0);
        AnchorPane.setLeftAnchor(topSidebar, 0.0);

        AnchorPane.setTopAnchor(leftSidebar, 45.0);
        AnchorPane.setLeftAnchor(leftSidebar, 0.0);

        root.getChildren().add(topSidebar);
        root.getChildren().add(leftSidebar);
    }
}
