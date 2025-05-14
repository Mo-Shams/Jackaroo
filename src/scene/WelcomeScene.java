package scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import controller.Controller;

public class WelcomeScene{
	String name;
	public Scene createWelcomeScene(Stage stage) throws Exception {
		Controller controller = new Controller(stage);
	    // 1) Build your UI controls:
	    Text text = new Text("Enter your name:");
	    text.setFont(Font.font("Arial Rounded MT", 25));
	    text.setFill(Color.RED);
	    TextField inputField = new TextField();
	    inputField.setPromptText("Enter your name");

	    Button play = makeImageButton("/resources/icon_images/play.png");
	    Button about = makeImageButton("/resources/icon_images/about.png");
	    Button settings = makeImageButton("/resources/icon_images/settings.png");
	    controller.setGameSceneOnButtonClick(play);
	    // 2) Put them in a VBox (only one instance!)
	    VBox vbox = new VBox(10, text, inputField, play, about, settings);
	    vbox.setAlignment(Pos.CENTER);
	    vbox.setFillWidth(false);   // respect children's pref widths
	    vbox.setPadding(Insets.EMPTY);

	    // 3) Create ONE Scene and bind your widths/heights to its properties
	    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); // Visual excludes taskbar

		double width = screenBounds.getWidth();
		double height = screenBounds.getHeight();
	    Scene scene = new Scene(vbox, width, height);

	    // TextField  = 15% width, 5% height of *this* scene

	    
	    inputField.prefWidthProperty().bind(scene.widthProperty().multiply(0.15));
	    inputField.prefHeightProperty().bind(scene.heightProperty().multiply(0.05));

	    // Each button = 15% width, 5% height
	        play.prefWidthProperty().bind(scene.widthProperty().multiply(0.15));
	        play.prefHeightProperty().bind(scene.heightProperty().multiply(0.05));
	        about.prefWidthProperty().bind(scene.widthProperty().multiply(0.15));
	        about.prefHeightProperty().bind(scene.heightProperty().multiply(0.05));
	        settings.prefWidthProperty().bind(scene.widthProperty().multiply(0.15));
	        settings.prefHeightProperty().bind(scene.heightProperty().multiply(0.05));
	    

	    // Optionally, you can size the VBox itself:
	    vbox.prefWidthProperty().bind(scene.widthProperty().multiply(0.75));
	    vbox.prefHeightProperty().bind(scene.heightProperty().multiply(0.5));

	    return scene;
	    
	}

	// Helper: load an image‐only button that stretches its graphic
	private Button makeImageButton(String resourcePath) {
	    Image img = new Image(getClass().getResourceAsStream(resourcePath));
	    ImageView iv = new ImageView(img);
	    iv.setPreserveRatio(false);
	    Button btn = new Button();
	    iv.fitWidthProperty().bind(btn.widthProperty());
	    iv.fitHeightProperty().bind(btn.heightProperty());
	    btn.setGraphic(iv);
	    btn.setPadding(Insets.EMPTY);
	    // bind the ImageView to the button’s size
	    return btn;
	}	

	
}