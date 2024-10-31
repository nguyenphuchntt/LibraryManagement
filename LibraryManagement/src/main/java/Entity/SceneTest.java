package Entity;

import Controllers.SideBarLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneTest extends Application {

    private final static int WIDTH = 1100;
    private final static int HEIGHT = 800;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
        AnchorPane root = rootLoader.load();

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        HBox topSidebar = SideBarLoader.getTopSidebar();
        VBox leftSidebar = SideBarLoader.getLeftSidebar();
        SideBarLoader.getController().setStage(stage);

        AnchorPane.setTopAnchor(topSidebar, 0.0);
        AnchorPane.setLeftAnchor(topSidebar, 0.0);
        AnchorPane.setTopAnchor(leftSidebar, 45.0);
        AnchorPane.setLeftAnchor(leftSidebar, 0.0);
        root.getChildren().add(leftSidebar);
        root.getChildren().add(topSidebar);


        stage.setTitle("UET Library Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}