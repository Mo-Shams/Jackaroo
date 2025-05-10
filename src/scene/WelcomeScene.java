package scene;

import java.util.List;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class WelcomeScene extends Application {

	String name;
	@Override
	
	public void start(Stage primaryStage) {
	    // 1) Build your UI controls:
	    Text text = new Text("Enter your name:");
	    text.setFont(Font.font("Arial Rounded MT", 25));
	    text.setFill(Color.RED);
	    TextField inputField = new TextField();
	    inputField.setPromptText("Enter your name");

	    Button play = makeImageButton("/resources/icon_images/play.png");
	    Button about = makeImageButton("/resources/icon_images/about.png");
	    Button settings = makeImageButton("/resources/icon_images/settings.png");
	    // 2) Put them in a VBox (only one instance!)
	    VBox vbox = new VBox(10, text, inputField, play, about, settings);
	    vbox.setAlignment(Pos.CENTER);
	    vbox.setFillWidth(false);   // respect children's pref widths
	    vbox.setPadding(Insets.EMPTY);

	    // 3) Create ONE Scene and bind your widths/heights to its properties
	    Scene scene = new Scene(vbox, 1400, 900);

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

	    // 4) Show it
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Welcome screen");
	    primaryStage.show();
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