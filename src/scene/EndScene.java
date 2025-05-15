package scene;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * A unified scene to display either a win or loss end screen based on player outcome.
 */
public class EndScene {
    private MediaPlayer mediaPlayer;

    /**
     * Creates an end scene showing either a winning or losing screen.
     *
     * @param stage         the primary stage
     * @param playerWon     true if the player won, false if the CPU won
     * @param playerColor   the player's color
     * @param playerName    the player's display name
     * @param cpuColor      the CPU's color
     * @param cpuName       the CPU's display name
     * @return              the configured Scene
     */
    public Scene createEndScene(Stage stage, boolean playerWon, Color playerColor, String playerName, Color cpuColor, String cpuName) {
        // Determine winner details and resources
        Color winnerColor = playerWon ? playerColor : cpuColor;
        String winnerName = playerWon ? playerName : cpuName;
        String backgroundPath = playerWon
                ? "resources/EndScreenImages/Winning.png"
                : "resources/EndScreenImages/Losing.png";
        String audioPath = playerWon
                ? "resources/EndScreenAudio/Winning.mp3"
                : "resources/EndScreenAudio/Losing.mp3";

        // Get screen bounds
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();

        // Root pane
        StackPane root = new StackPane();

        // Background image
        Image bgImage = new Image(getClass().getResourceAsStream("/" + backgroundPath));
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(width);
        bgView.setFitHeight(height);
        bgView.setPreserveRatio(false);
        root.getChildren().add(bgView);

        // Text display
        String displayText = playerWon
                ? winnerName
                : winnerName + " won";
        Text text = new Text(displayText);
        text.setFont(Font.font("Arial", 48));
        text.setFill(winnerColor);

        VBox textBox = new VBox(text);
        textBox.setAlignment(Pos.CENTER);
        root.getChildren().add(textBox);
        StackPane.setAlignment(textBox, Pos.CENTER);

        // Create scene
        Scene scene = new Scene(root, width, height);

        // Play audio
        try {
            Media media = new Media(getClass().getResource("/" + audioPath).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setVolume(1.0);
        } catch (NullPointerException npe) {
            System.err.println("Audio resource not found: " + audioPath);
        } catch (Exception e) {
            System.err.println("Error playing audio: " + e.getMessage());
        }

        return scene;
    }
}
