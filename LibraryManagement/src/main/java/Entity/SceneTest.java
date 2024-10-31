package Entity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

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

        stage.setTitle("UET Library Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}