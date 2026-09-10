package odysseus.ui;

// Reused from https://se-education.org/guides/tutorials/javaFx.html with minor modifications.

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import odysseus.Odysseus;

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

    private Odysseus odysseus;
    private static final double EXIT_DELAY_SECONDS = 1.5;

    private Image userImage = new Image(this.getClass()
            .getResourceAsStream("/images/userImg.png"));
    private Image odysseusImage = new Image(this.getClass()
            .getResourceAsStream("/images/odysseusImg.jpg"));

    /**
     * Binds the scroll pane so it auto-scrolls to show the newest message.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Odysseus instance that command handling is delegated to.
     *
     * @param o the chatbot logic instance
     */
    public void setOdysseus(Odysseus odysseus) {
        this.odysseus = odysseus;
    }

    private void handleExit() {
        PauseTransition delay = new PauseTransition(Duration.seconds(EXIT_DELAY_SECONDS));
        delay.setOnFinished(event -> Platform.exit());
        delay.play();
    }

    /**
     * Handles one round of user input.
     *
     * <p>Echoes the user's message and Odysseus's reply as two dialog boxes,
     * appends them to the dialog container, and clears the input field. If the
     * user has ended the conversation, the window closes after a short pause.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = odysseus.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getOdysseusDialog(response, odysseusImage)
        );
        userInput.clear();

        if (odysseus.isExit()) {
            handleExit();
        }
    }
}
