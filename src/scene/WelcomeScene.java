package scene;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import controller.GameController;

import java.util.*;

import exception.CannotFieldException;
import exception.IllegalDestroyException;
import view.GameScene;
import view.ImageCache;

public class WelcomeScene {

    private Pane backgroundPane = new Pane(); // For animated marbles
    private final Stage primaryStage ; 
    
    public WelcomeScene (Stage primaryStage) {
    	this.primaryStage = primaryStage ; 
    }

    public Scene createScene() throws Exception {

        // Title
        Text text = new Text("Enter your name:");
        text.setFont(Font.font("Arial Rounded MT", 25));
        text.setFill(Color.WHITE);

        // Name input
        TextField inputField = new TextField();
        inputField.setPromptText("Enter your name");

        // Buttons
        Button play = createStyledButton("Play");
        Button about = createStyledButton("About");
        Button settings = createStyledButton("Settings");
        

        play.setOnAction(evt -> {
            String playerName = inputField.getText().trim();
            if (playerName.isEmpty()) {
            	// GameScene.showExceptionPopup
            	// ("You didn't enter a name \n you will be called Player",(StackPane) backgroundPane);
            	playerName = "Player";
            }
            try {
            	GameController gameController = new GameController(playerName, primaryStage);
            	Scene gameScene = gameController.getGameScene().CreateScene();
            	primaryStage.setScene(gameScene);
            	primaryStage.setFullScreen(true);
            	
            } catch (Exception e) {
            	GameScene.showExceptionPopup("The game Failed to load" ,(StackPane) backgroundPane);
            }
        });

        VBox vbox = new VBox(10, text, inputField, play, about, settings);
        vbox.setAlignment(Pos.CENTER);
        vbox.setFillWidth(false);
        vbox.setPadding(Insets.EMPTY);
        vbox.setStyle("-fx-background-color: transparent;");

        // Scene
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();

        StackPane root = new StackPane(); // Stack background and UI
        backgroundPane.setPrefSize(width, height);
        backgroundPane.setStyle("-fx-background-color: black;"); // Set background color

        root.getChildren().addAll(backgroundPane, vbox); // Add background + UI
        Scene scene = new Scene(root, width, height);
        scene.setOnKeyPressed((KeyEvent e1) ->{
    		if(e1.getCode() == KeyCode.ENTER){
    			play.fire();
    		}
    	});
        inputField.prefWidthProperty().bind(scene.widthProperty().multiply(0.15));
        inputField.prefHeightProperty().bind(scene.heightProperty().multiply(0.05));

        for (Button btn : new Button[]{play, about, settings}) {
            btn.prefWidthProperty().bind(scene.widthProperty().multiply(0.15));
            btn.prefHeightProperty().bind(scene.heightProperty().multiply(0.05));
        }

        vbox.prefWidthProperty().bind(scene.widthProperty().multiply(0.75));
        vbox.prefHeightProperty().bind(scene.heightProperty().multiply(0.5));

        // Start marble animation 🎆
        startMarbleAnimation(
            "/resources/marble_images/BLUE_marble.png",
            "/resources/marble_images/RED_marble.png",
            "/resources/marble_images/GREEN_marble.png",
            "/resources/marble_images/YELLOw_marble.png",
            width, height
        );
     

        return scene;
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", 16));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2;");
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: white; -fx-border-width: 2;");
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2;");
        });
        return btn;
    }

    private void startMarbleAnimation(String redPath, String bluePath, String greenPath, String yellowPath, double sceneWidth, double sceneHeight) {
        String[] paths = {redPath, bluePath, greenPath, yellowPath};
        Random random = new Random();

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0), e -> {
                String path = paths[random.nextInt(paths.length)];
                ImageView marble = createMarble(path, sceneHeight);
                backgroundPane.getChildren().add(marble);
                animateMarble(marble, sceneWidth);
            }),
            new KeyFrame(Duration.seconds(0.5)) // new marble every 2 seconds
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private ImageView createMarble(String path, double sceneHeight) {
   
        ImageView marble = new ImageView(ImageCache.getImage(path));
        marble.setPreserveRatio(true);
        marble.setSmooth(true);

        marble.setFitWidth(80);
        marble.setFitHeight(80);
        marble.setPreserveRatio(true);

        double startY = new Random().nextDouble() * (sceneHeight - 40);
        marble.setLayoutX(-50); // Start just off-screen to the left
        marble.setLayoutY(startY);

        return marble;
    }

    private void animateMarble(ImageView marble, double sceneWidth) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(12), marble);
        transition.setFromX(0);
        transition.setToX(sceneWidth + 100);
        transition.setOnFinished(e -> backgroundPane.getChildren().remove(marble)); // Remove when done
        transition.play();
    }
}
