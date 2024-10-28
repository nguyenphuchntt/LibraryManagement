package LibSystem.Entity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneTest extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneTest.class.getResource("/fxml/AdminPanel_RemoveBook.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("UETLIB");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
