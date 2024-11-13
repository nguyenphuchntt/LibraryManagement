package Entity;

import Controllers.Controller;
import Controllers.SideBarLoader;
import database.DatabaseController;
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

        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        AnchorPane root = rootLoader.load();

        Scene scene = new Scene(root, WIDTH, HEIGHT);

//        Controller.setSideBar(root);
        Controller.setStage(stage);

        stage.setTitle("UET Library Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        DatabaseController.getConnection();

        launch();

        DatabaseController.closeConnection();
    }
}