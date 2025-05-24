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
        Pane backgroundPane = AnimatedMarbles.fullScreen();
    	
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
            String st = inputField.getText().trim();
            final String playerName; 
            if (st.isEmpty()) {
            	// GameScene.showExceptionPopup
            	// ("You didn't enter a name \n you will be called Player",(StackPane) backgroundPane);
            	playerName = "Player";
            } else {
            	playerName = st;
            }
            LoadingScene loaderPane = new LoadingScene();
            Scene loadingScene = loaderPane.createScene();
            primaryStage.setScene(loadingScene);
            primaryStage.setFullScreen(true);
            
            //Pause Transition between loading Scene and Game Scene
            PauseTransition wait = new PauseTransition(Duration.seconds(5.5));
            wait.setOnFinished(ev -> {
                GameController gameController;
				try {
					gameController = new GameController(playerName, primaryStage);
					Scene gameScene = gameController.getGameScene().CreateScene();
                    primaryStage.setScene(gameScene);
                    primaryStage.setFullScreen(true);
				}catch (Exception e) {
               
                // If game loading fails, pop up on the loading pane
                GameScene.showExceptionPopup("Failed to load the game", loaderPane);
				}
            });
            wait.play();
        });
        
        about.setOnAction(e -> {
        	AboutScene aboutscene = new AboutScene(primaryStage);
        	primaryStage.setScene(aboutscene.createScene());
        	primaryStage.setFullScreen(true);
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

        StackPane root = new StackPane(backgroundPane, vbox); // Stack background and UI
        backgroundPane.setPrefSize(width, height);
        backgroundPane.setStyle("-fx-background-color: black;"); // Set background color

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

}
