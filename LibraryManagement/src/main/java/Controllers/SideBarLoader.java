package Controllers;

import Entity.Announcement;
import database.DatabaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class SideBarLoader {
    private static HBox TopSidebar;
    private static VBox LeftSidebar;
    private static Controller Topcontroller;
    private static Controller Leftcontroller;

    public static HBox getTopSidebar() throws IOException {
        if (TopSidebar == null) {
            FXMLLoader loader = new FXMLLoader(SideBarLoader.class.getResource("/fxml/TopSideBar.fxml"));
            TopSidebar = loader.load();
            Topcontroller = loader.getController();
            Topcontroller.loadAnnouncement();
        }
        return TopSidebar;
    }

    public static VBox getLeftSidebar() throws IOException {
        if (LeftSidebar == null) {
            FXMLLoader loader = new FXMLLoader(SideBarLoader.class.getResource("/fxml/LeftSideBar.fxml"));
            LeftSidebar = loader.load();
            Leftcontroller = loader.getController();
        }
        return LeftSidebar;
    }

    public static Controller getTopController() {
        if (Topcontroller == null) {
            try {
                getTopSidebar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Topcontroller;
    }

    public static Controller getLeftController() {
        if (Leftcontroller == null) {
            try {
                getLeftSidebar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Leftcontroller;
    }

    public static void reloadAnnouncement() {
        Topcontroller.loadAnnouncement();
    }
}
