package view;

import java.util.concurrent.CompletableFuture;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
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
		root = new StackPane();
		// root.setPadding(new Insets(30));
		root.setStyle("-fx-background-color: lightgreen;");
		gameView = new GameView(game, WIDTH, HEIGHT);
		root.getChildren().add(gameView);
	}
	
	public void showExceptionPopup( String message) {
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
	
	public CompletableFuture<Integer> SplitDistanceView() {
        CompletableFuture<Integer> selectedNumberFuture = new CompletableFuture<>();

        // Overlay background
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        overlay.setPrefSize(2000, 1200);

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        // Central message
        Label message = new Label("Select split distance for 7");
        message.setTextFill(Color.WHITE);
        message.setFont(Font.font(24));

        // Button row
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GOLD);
        shadow.setRadius(10);
        
        for (int i = 1; i <= 7; i++) {
            Button btn = new Button(String.valueOf(i));
            btn.setPrefSize(60, 60);
            btn.setFont(Font.font(18));
            btn.setStyle(
                "-fx-background-radius: 12;" +
                "-fx-background-color: linear-gradient(to top, #006400, #00FF00);" +
                "-fx-text-fill: white;"
            );
            btn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            	btn.setEffect(shadow);
            });
            btn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            	btn.setEffect(null);
            });
            
            final int number = i;
            btn.setOnAction(e -> {
                selectedNumberFuture.complete(number);   
                root.getChildren().remove(overlay);
                
                
                //some call back for what happens after the player choice 
                // DoTheAnimation 
            });

            buttonBox.getChildren().add(btn);
        }

        container.getChildren().addAll(message, buttonBox);
        // VBox.setMargin(buttonBox, new Insets(0, 0, 350, 0));
        overlay.getChildren().add(container);

        StackPane.setAlignment(container, Pos.CENTER);

        root.getChildren().add(overlay);

        return selectedNumberFuture;
    }

	
	public Scene CreateScene(){
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		return scene ;
	}
	public Game getGame() {
		return game;
	}
	public GameView getGameView() {
		return gameView;
	}
	
}
