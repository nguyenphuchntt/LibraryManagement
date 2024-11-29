package Entity;

import Controllers.Controller;
import database.DatabaseController;
import Utils.HibernateUtil;
import javafx.application.Application;
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

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Controller.setStage(stage);

        stage.setTitle("UET Library Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch();

        DatabaseController.closeConnection();
        HibernateUtil.shutdown();
    }
}