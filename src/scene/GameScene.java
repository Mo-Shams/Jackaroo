package scene;

import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.player.Player;
import view.CardsView.CardView;
import view.CardsView.HandView;
import view.marbleView.MarbleView;
import controller.GameController;
import engine.Game;

public class GameScene {
	private final Game game;
	public GameScene(String playerName) throws IOException{
		game = new Game(playerName);
	}
	public Scene createGameScene(Stage stage){
		GameController controller = new GameController(game, stage);
		StackPane root = new StackPane();
		root.setPadding(new Insets(20));
		root.setStyle("-fx-background-color: lightgreen;");
		Scene scene = new Scene(root, 1400, 900);
		HBox handBox = null;
		int i = 0;
		for(Player player: game.getPlayers()){
			HandView handView = new HandView(player.getHand());
			handBox = handView.getHandView();
			for(Node node: handBox.getChildren()){
				CardView cardView = (CardView) node;
				cardView.getImageView().fitHeightProperty().bind(scene.heightProperty().multiply(0.2));
				cardView.getImageView().fitWidthProperty().bind(scene.widthProperty().multiply(0.12));
				controller.addCardClickHandler(cardView, handView);
				controller.addCardHoverEffect(cardView);
			}
			switch(i){
			case 0:StackPane.setAlignment(handBox, Pos.BOTTOM_CENTER); break;
			case 1:StackPane.setAlignment(handBox, Pos.CENTER_LEFT);handBox.setRotate(90);break;
			case 2:StackPane.setAlignment(handBox, Pos.TOP_CENTER);handBox.setRotate(180);break;
			case 3:StackPane.setAlignment(handBox, Pos.CENTER_RIGHT);handBox.setRotate(-90);break;
			}
			root.getChildren().add(handBox);
			i++;
		}
		MarbleView marbleView = new MarbleView(game.getPlayers().get(0).getMarbles().get(0));
		StackPane.setAlignment(marbleView, Pos.CENTER);
		root.getChildren().add(marbleView);
		marbleView.toBack();
		return scene;
	}
	public Game getGame() {
		return game;
	}
}
