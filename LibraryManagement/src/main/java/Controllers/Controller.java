package Controllers;

import Entity.Announcement;
import Entity.LibraryManagement;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import database.DatabaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Controller {

    private static Controller instance = new Controller();

    private static Stage stage;
    private Map<String, Scene> scenes = new HashMap<>();
    private static String currentScene = "Login.fxml";

    public static Controller getInstance() {
        return instance;
    }

    private FXMLLoader loadFxml(String fxmlFileName) throws IOException {
        return new FXMLLoader(getClass().getResource("/fxml/" + fxmlFileName));
    }

    public static void setStage(Stage stage) {
        Controller.stage = stage;
    }

    @FXML
    private Label announcement_Label;

    @FXML
    private Label role_Label;

    @FXML
    private Label username_Label;

    @FXML
    private MenuButton AdminPanelTab_Button;

    @FXML
    private ScrollPane scrollPane;

    public void setScrollPaneBarPolicy() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    @FXML
    private void handleHomeTab_Button() throws Exception {
        if (Objects.equals(currentScene, "Home.fxml")) {
            return;
        }
        switchScene("Home.fxml");
    }

    @FXML
    private void handleBookSearch_MenuItem() throws Exception {
        if (Objects.equals(currentScene, "Book_Search.fxml")) {
            return;
        }
        switchScene("Book_Search.fxml");
    }

    @FXML
    private void handleBookReturn_MenuItem() throws Exception {
        if (Objects.equals(currentScene, "Book_Return.fxml")) {
            return;
        }
        switchScene("Book_Return.fxml");
    }

    @FXML
    private void handleAdminOverview_MenuItem() throws Exception {
        if (Objects.equals(currentScene, "Admin_Overview.fxml")) {
            return;
        }
        switchScene("Admin_Overview.fxml");
    }

    @FXML
    private void handleAdminAnnouncement_MenuItem() throws Exception {
        if (Objects.equals(currentScene, "Admin_Announcement.fxml")) {
            return;
        }
        switchScene("Admin_Announcement.fxml");
    }

    @FXML
    private void handleAdminAlterBook_MenuItem() throws Exception {
        if (Objects.equals(currentScene, "Admin_AlterBook.fxml")) {
            return;
        }
        switchScene("Admin_AlterBook.fxml");
    }

    @FXML
    private void handleAdminAddBook_MenuItem() throws Exception {
        if (Objects.equals(currentScene, "Admin_AddBook.fxml")) {
            return;
        }
        switchScene("Admin_AddBook.fxml");
    }

    @FXML
    private void handleAdminRemoveBook_MenuItem() throws Exception {
        if (Objects.equals(currentScene, "Admin_RemoveBook.fxml")) {
            return;
        }
        switchScene("Admin_RemoveBook.fxml");
    }

    @FXML
    private void handleAccount_Button() throws Exception {
        if (Objects.equals(currentScene, "Account.fxml")) {
            return;
        }
        switchScene("Account.fxml");
        AccountController accountController = (AccountController) scenes.get("Account.fxml").getUserData();
        accountController.refreshInfo();
    }

    private void switchScene(String fxmlFileName) throws Exception {
        System.out.println("switchScene " + fxmlFileName);
        Scene newScene = null;
        if (scenes.get(fxmlFileName) != null) {
            AnchorPane root = (AnchorPane) scenes.get(fxmlFileName).getRoot();
            newScene = scenes.get(fxmlFileName);
            if (!root.getChildren().contains(SideBarLoader.getLeftSidebar())) {
                if (!Objects.equals(fxmlFileName, "Login.fxml") && !Objects.equals(fxmlFileName, "Signup.fxml")) {
                    Controller.setSideBar(root);
                }
            }
        } else {
            AnchorPane newRoot = null;
            FXMLLoader loader;
            try {
                loader = loadFxml(fxmlFileName);
                newRoot = (AnchorPane) loader.load();
            } catch (Exception e) {
                System.out.println("Error loading fxml: " + fxmlFileName);
                throw e;
            }
            newScene = new Scene(newRoot);
            newScene.setUserData(loader.getController());
            if (!Objects.equals(fxmlFileName, "Login.fxml") && !Objects.equals(fxmlFileName, "Signup.fxml")) {
                Controller.setSideBar(newRoot);
            }
            scenes.put(fxmlFileName, newScene);
        }
        stage.setScene(newScene);
        currentScene = fxmlFileName;
        stage.show();
    }

    public void goToScene(String fxmlFileName) throws Exception {
        switchScene(fxmlFileName);
    }

    public static void setSideBar(AnchorPane root) throws IOException {
        VBox leftSidebar = SideBarLoader.getLeftSidebar();
        HBox topSidebar = SideBarLoader.getTopSidebar();

        AnchorPane.setTopAnchor(topSidebar, 0.0);
        AnchorPane.setLeftAnchor(topSidebar, 0.0);

        AnchorPane.setTopAnchor(leftSidebar, 45.0);
        AnchorPane.setLeftAnchor(leftSidebar, 0.0);

        root.getChildren().add(topSidebar);
        root.getChildren().add(leftSidebar);
    }

    public void loadAnnouncement() {
        List<Announcement> announcements = DatabaseController.getAllAnnouncements();
        announcement_Label.setMinHeight(announcements.size() * 16);
        StringBuilder announcementString = new StringBuilder();
        for (Announcement announcement : announcements) {
            announcementString.append(announcement.getContent()).append('\n');
        }
        announcement_Label.setText(announcementString.toString());
    }

    public void loadUserInfo() {
        role_Label.setText(LibraryManagement.getInstance().getRole());
        username_Label.setText(LibraryManagement.getInstance().getCurrentAccount());
    }

    public void setAdminPanelVisible(boolean isVisible) {
        AdminPanelTab_Button.setVisible(isVisible);
    }
}
