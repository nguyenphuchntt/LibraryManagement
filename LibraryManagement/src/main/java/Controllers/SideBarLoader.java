package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SideBarLoader {
    private static HBox TopSidebar;
    private static VBox LeftSidebar;
    private static Controller controller;



    public static HBox getTopSidebar() throws IOException {
        if (TopSidebar == null) {
            FXMLLoader loader = new FXMLLoader(SideBarLoader.class.getResource("/fxml/TopSideBar.fxml"));
            TopSidebar = loader.load();
            controller = loader.getController();
        }
        return TopSidebar;
    }

    public static VBox getLeftSidebar() throws IOException {
        if (LeftSidebar == null) {
            FXMLLoader loader = new FXMLLoader(SideBarLoader.class.getResource("/fxml/LeftSideBar.fxml"));
            LeftSidebar = loader.load();
            controller = loader.getController();
        }
        return LeftSidebar;
    }

    public static Controller getController() {
        return controller;
    }
}