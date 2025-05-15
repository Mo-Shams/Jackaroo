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

// A Scene to Display in Case a Player Wins
public class LosingScene {
    private MediaPlayer mediaPlayer;
    
    public Scene createLosingScene(Stage stage, Color winnerColor, String winnerColorName, String backgroundPath, String audioResourcePath) {
        // Get screen bounds
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();
        
        // Root stack pane
        StackPane root = new StackPane(); 
        
        // Load and display background image
        Image background = new Image(getClass().getResourceAsStream("/" + backgroundPath));  // Make sure the path starts with '/'
        ImageView backgroundView = new ImageView(background);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(false);
        root.getChildren().add(0, backgroundView);  // Set the background at the bottom

        // Text elements
/*        Text winText = new Text("You Win!");
        winText.setFont(Font.font("Arial", 64));
        winText.setFill(winnerColor);
*/
        Text colorText = new Text(winnerColorName);
        colorText.setFont(Font.font("Arial", 48));
        colorText.setFill(winnerColor);

        VBox textBox = new VBox(10, colorText);
        textBox.setAlignment(Pos.CENTER);
        
        root.getChildren().add(textBox);
        StackPane.setAlignment(textBox, Pos.CENTER);  // Align text to the center

        // Scene setup
        Scene scene = new Scene(root, width, height);
        
        // Audio
        try {
            Media winSound = new Media(getClass().getResource("/" + audioResourcePath).toExternalForm());  // Make sure the path starts with '/'
            mediaPlayer = new MediaPlayer(winSound);
            mediaPlayer.setAutoPlay(false);  // Explicit play call
            mediaPlayer.setVolume(1.0);
            mediaPlayer.play();
        } catch (NullPointerException npe) {
            System.err.println("Audio resource not found at path: " + audioResourcePath);
            System.err.println("Resource URL: " + getClass().getResource(audioResourcePath));  // Debugging line
        } catch (Exception e) {
            System.err.println("Error loading or playing audio: " + e.getMessage());
        }
        
        return scene;
    }
}
