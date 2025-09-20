import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sigmabot.SigmaBot;
import javafx.application.Platform;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private SigmaBot sigmaBot;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/crying_wojack.png"));
    private Image sigmaBotImage = new Image(this.getClass().getResourceAsStream("/images/gigachad.jpg")); 

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the SigmaBot instance */
    public void setSigmaBot(SigmaBot sb) {
        sigmaBot = sb;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing SigmsBot's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();  
        if (sigmaBot.isBye(input)) {
            Platform.exit();
        } 

        String response = sigmaBot.nextTaskfromString(input).getPrintMsg();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getSigmaBotDialog(response, sigmaBotImage)
        );
        userInput.clear();
    }
}