import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sigmabot.SigmaBot;

/**
 * A GUI for SigmaBot using FXML.
 */
public class Main extends Application {

    private SigmaBot sigmaBot = new SigmaBot();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("SigmaBot");
            stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/images/gigachad.jpg")));
            fxmlLoader.<MainWindow>getController().setSigmaBot(sigmaBot);  // inject the sigmaBot instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
