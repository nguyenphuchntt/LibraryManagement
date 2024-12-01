package entities;

import controllers.BookReturnController;
import controllers.BookSearchController;
import controllers.Controller;
import controllers.SideBarLoader;
import database.DatabaseController;
import utils.HibernateUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class SceneTest extends Application {

    private final static int WIDTH = 1100;
    private final static int HEIGHT = 800;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        AnchorPane root = rootLoader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Controller.setStage(stage);
        stage.setTitle("UET Library Management System");
        stage.setScene(scene);
        stage.setResizable(false);

        new Thread(() -> {
            try {
                Statement useDatabaseStatement = DatabaseController.getConnection().createStatement();
                useDatabaseStatement.execute("USE library");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).start();
        DatabaseController.importDataFromCSV();
        new Thread(HibernateUtil::getSessionFactory).start();

        stage.setOnCloseRequest(event -> {
            Scene bookSearchScene = SideBarLoader.getLeftController().getScenes().get("Book_Search.fxml");
            if (bookSearchScene != null) {
                BookSearchController bookSearchController = (BookSearchController) bookSearchScene.getUserData();
                bookSearchController.cleanup();
                Platform.exit();
            }

            Scene bookReturnScene = SideBarLoader.getLeftController().getScenes().get("Book_Return.fxml");
            if (bookReturnScene != null) {
                BookReturnController bookReturnController = (BookReturnController) bookReturnScene.getUserData();
                bookReturnController.cleanup();
                Platform.exit();
            }
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();

        DatabaseController.closeConnection();
        HibernateUtil.shutdown();
    }
}