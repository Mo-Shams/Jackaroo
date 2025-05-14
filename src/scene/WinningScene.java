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
public class WinningScene {
    private MediaPlayer mediaPlayer;
    
    public Scene createWinningScene(Stage stage, Color winnerColor, String winnerColorName, String backgroundPath, String audioResourcePath) {
		
    	Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double width = screenBounds.getWidth();
		double height = screenBounds.getHeight();
		
		StackPane root = new StackPane(); 
			
		Image Background = new Image (getClass().getResourceAsStream(backgroundPath));
		ImageView BackgroundView = new ImageView(Background);
		BackgroundView.setFitWidth(width);
		BackgroundView.setFitHeight(height);
		BackgroundView.setPreserveRatio(false);
        root.getChildren().add(0, BackgroundView);
		
		
		Text winText = new Text("You Win!");
        winText.setFont(Font.font("Arial", 64));
        winText.setFill(winnerColor);

        Text colorText = new Text(winnerColorName + " wins!");
        colorText.setFont(Font.font("Arial", 48));
        colorText.setFill(winnerColor);

        VBox textBox = new VBox(10, winText, colorText);
        textBox.setAlignment(Pos.CENTER);
		
        root.getChildren().add(textBox);
        StackPane.setAlignment(textBox, Pos.CENTER);

		Scene scene = new Scene(root, width, height);
		
        MediaPlayer player = null;
		try {
			Media winSound = new Media(getClass().getResource(audioResourcePath).toExternalForm());
            player = new MediaPlayer(winSound);
            player.setAutoPlay(false); // use explicit play call
            player.setVolume(1.0);
            player.play();
        } catch (NullPointerException npe) {
            System.err.println("Audio resource not found at path: " + audioResourcePath);
        } catch (Exception e) {
            System.err.println("Error loading or playing audio: " + e.getMessage());
        }
        this.mediaPlayer = player;
		mediaPlayer.play();
		
		return scene;
    }
}
