 package scene;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import controller.ThemesManager;
import view.GameView;
import model.player.Marble;
import model.player.Player;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;
import engine.Game;
import exception.CannotFieldException;
import exception.IllegalDestroyException;

public class GameScene {
	private final StackPane root;
	private final Game game ;
	private final GameView gameView;
	private static final double WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
	private static final double HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();
	// private final PlayerView playerView ;
	public GameScene (Game game){
		super();
		this.game = game ;	
		ThemesManager.gameScene = this ; 
		root = new StackPane();
		// root.setPadding(new Insets(30));
		
		
		root.setStyle("-fx-background-color: #faedcd;");
		gameView = new GameView(game, WIDTH, HEIGHT);
		root.getChildren().add(gameView);
	}
	public Scene CreateScene(){
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		return scene ;
	}
	
	public static void showExceptionPopup(String message, StackPane root) {
	    // Dimmed background
	    Rectangle dim = new Rectangle(root.getWidth(), root.getHeight(), Color.rgb(0, 0, 0, 0.7));
	    dim.widthProperty().bind(root.widthProperty());
	    dim.heightProperty().bind(root.heightProperty());
	    dim.setMouseTransparent(false); // Block interactions behind it

	    // Popup content
	    VBox popup = new VBox(15);
	    popup.setAlignment(Pos.CENTER);
	    popup.setPadding(new Insets(25));
	    popup.setStyle("-fx-background-color: white;" +
	                   "-fx-background-radius: 12;" +
	                   "-fx-border-radius: 12;" +
	                   "-fx-border-color: #cccccc;" +
	                   "-fx-border-width: 1;");
	    popup.setMaxWidth(400);
	    popup.setMaxHeight(200);

	    popup.setEffect(new DropShadow(10, Color.gray(0.3)));

	    Label label = new Label(message);
	    label.setWrapText(true);
	    label.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");

	    Button btnContinue = new Button("Continue");
	    btnContinue.setStyle("-fx-background-color: #4CAF50;" +
	                         "-fx-text-fill: white;" +
	                         "-fx-font-weight: bold;" +
	                         "-fx-background-radius: 8;" +
	                         "-fx-padding: 8 16;");

	    // Container to center the popup
	    StackPane popupWrapper = new StackPane(popup);
	    popupWrapper.setPrefSize(root.getWidth(), root.getHeight());
	    popupWrapper.setAlignment(Pos.CENTER);
	    popupWrapper.setMouseTransparent(false);

	    // On click, remove overlay
	    btnContinue.setOnAction(e -> root.getChildren().removeAll(dim, popupWrapper));

	    popup.getChildren().addAll(label, btnContinue);

	    // Add to root
	    root.getChildren().addAll(dim, popupWrapper);

	    // Fade-in effect
	    FadeTransition fade = new FadeTransition(Duration.millis(300), popupWrapper);
	    fade.setFromValue(0);
	    fade.setToValue(1);
	    fade.play();
	}
	
	public PauseTransition showSeeingTrappedEffect() {
        // Dim background with a semi-transparent black rectangle
        Rectangle dim = new Rectangle();
        dim.widthProperty().bind(root.widthProperty());
        dim.heightProperty().bind(root.heightProperty());
        dim.setFill(Color.rgb(0, 0, 0, 0.6)); // 60% transparent black
        
        Rectangle messageBox = new Rectangle(WIDTH * 0.3 , HEIGHT * 0.2);
        messageBox.setStroke(Color.RED);
        messageBox.setStrokeWidth(5);
        messageBox.setArcWidth(50);
        messageBox.setArcHeight(50);
        messageBox.setFill(Color.TRANSPARENT);
        // Create the glowing red message
        Label message = new Label("⚠  Trapped  ⚠");
        message.setStyle("-fx-font-size: 50px; -fx-text-fill: red; -fx-font-weight: bold;");
        
        // Glow effect
        DropShadow glow1 = new DropShadow();
        glow1.setColor(Color.YELLOW);
        glow1.setRadius(25);
        glow1.setSpread(0.5);
        messageBox.setEffect(glow1);
        DropShadow glow2 = new DropShadow();
        glow2.setColor(Color.RED);
        glow2.setRadius(25);
        glow2.setSpread(0.1);
        message.setEffect(glow2);

        // Group dim and message in a temporary overlay
        StackPane overlay = new StackPane(dim, messageBox, message);
        overlay.setAlignment(Pos.CENTER);

        // Add overlay on top of root
        root.getChildren().add(overlay);

        // Wait for 1 second then remove overlay
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e ->{
        	root.getChildren().remove(overlay);
        });
        return pause;
    }
	
	

	//------------------------------------- Getters -------------------------------------------------
	public Game getGame() {
		return game;
	}
	public GameView getGameView() {
		return gameView;
	}
	
	public StackPane getRoot() {
		return root ; 
	}
	
}
