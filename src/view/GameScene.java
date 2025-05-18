package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
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
		root.setPadding(new Insets(30));
		root.setStyle("-fx-background-color: lightgreen;");
		gameView = new GameView(game, WIDTH, HEIGHT);
		root.getChildren().add(gameView);
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
