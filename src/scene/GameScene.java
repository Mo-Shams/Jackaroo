package scene;

import java.io.IOException;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.player.Marble;
import model.player.Player;
import view.CardsView.CardView;
import view.CardsView.HandView;
import view.boardView.CellView;
import view.boardView.HomeZoneView;
import view.boardView.TrackView;
import view.marbleView.MarbleView;
import controller.GameController;
import engine.Game;
import engine.board.Cell;
import engine.board.SafeZone;

public class GameScene {
	private final Game game;

	public GameScene(String playerName) throws IOException {
		game = new Game(playerName);
	}

	public Scene createGameScene(Stage stage) {
		GameController controller = new GameController(game, stage);
		StackPane root = new StackPane();
		root.setPadding(new Insets(30));

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double width = screenBounds.getWidth();
		double height = screenBounds.getHeight();

		Scene scene = new Scene(root, width, height);

		// Background
		Rectangle background = generateBackground(width, height + 70, Color.SKYBLUE, Color.LIGHTGREEN, Color.YELLOWGREEN);
		root.getChildren().add(0, background);

		// Hands
		renderHands(root, scene, controller, width, height);
		renderHomeZones(root, width, height);

		// Sample marble + cell

		renderTrack(root, width, height);

		return scene;
	}

	public Game getGame() {
		return game;
	}

	// ------------------- Helper Methods -------------------

	private Rectangle generateBackground(double width, double height, Color start, Color inbetween, Color end) {
		Rectangle background = new Rectangle(width, height);
		LinearGradient gradient = new LinearGradient(
			0, 1, 1, 0,
			true,
			javafx.scene.paint.CycleMethod.NO_CYCLE,
			new Stop(0, start),
			new Stop(0.5, inbetween),
			new Stop(1, end)
		);
		background.setFill(gradient);
		return background;
	}

	private void renderHands(StackPane root, Scene scene, GameController controller, double width, double height) {
		int i = 0;
		StackPane container = new StackPane();
		for (Player player : game.getPlayers()) {
			HBox handBox = renderSingleHand(player, scene, controller);
			switch (i) {
				case 0:
					StackPane.setAlignment(handBox, Pos.BOTTOM_CENTER);
					container.getChildren().add(handBox);
					break;
				case 1:
					handBox.setRotate(90);
					VBox handL = new VBox(handBox);
					handL.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
					StackPane.setAlignment(handL, Pos.CENTER_LEFT);
					container.getChildren().add(handL);
					break;
				case 2:
					handBox.setRotate(180);
					StackPane.setAlignment(handBox, Pos.TOP_CENTER);
					container.getChildren().add(handBox);
					break;
				case 3:
					handBox.setRotate(-90);
					VBox handR = new VBox(handBox);
					handR.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
					StackPane.setAlignment(handR, Pos.CENTER_RIGHT);
					container.getChildren().add(handR);
					break;
			}
			i++;
		}
		container.setMaxWidth(width * 0.8);
		container.setMaxHeight(height);
		root.getChildren().add(container);
	}

	private HBox renderSingleHand(Player player, Scene scene, GameController controller) {
		HandView handView = new HandView(player.getHand());
		HBox handBox = handView.getHandView();

		for (Node node : handBox.getChildren()) {
			CardView cardView = (CardView) node;
			cardView.getImageView().fitHeightProperty().bind(scene.heightProperty().multiply(0.12));
			cardView.getImageView().fitWidthProperty().bind(scene.widthProperty().multiply(0.08));
			controller.addCardClickHandler(cardView, handView);
			controller.addCardHoverEffect(cardView);
		}
		return handBox;
	}
	private void renderHomeZones(StackPane root, double width, double height) {
	    // Iterate over each player and render their HomeZoneView
	    for (int i = 0; i < game.getPlayers().size(); i++) {
	        Player player = game.getPlayers().get(i);
	        ArrayList<Marble> marbles = player.getMarbles();
	        
	        // Create the HomeZoneView for each player
	        HomeZoneView homeZoneView = new HomeZoneView(marbles);

	        // Position HomeZone based on player index (4 corners)
	        switch (i) {
	            case 0:
	                StackPane.setAlignment(homeZoneView.getContainer(), Pos.BOTTOM_CENTER);
	                break;
	            case 1:
	                StackPane.setAlignment(homeZoneView.getContainer(), Pos.CENTER_LEFT);
	                break;
	            case 2:
	                StackPane.setAlignment(homeZoneView.getContainer(), Pos.TOP_CENTER);
	                break;
	            case 3:
	                StackPane.setAlignment(homeZoneView.getContainer(), Pos.CENTER_RIGHT);
	                break;
	        }

	        // Set size for HomeZoneView (adjust as needed)
	        homeZoneView.getContainer().setMaxWidth(width * 0.6);
	        homeZoneView.getContainer().setMaxHeight(height * 0.4);
	        //homeZoneView.getContainer().setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	        // Add the HomeZoneView container to the root
	        root.getChildren().add(1, homeZoneView.getContainer());
	    }
	}
	
	private void renderTrack(StackPane root, double width, double height) {
	    ArrayList<Cell> track = game.getBoard().getTrack(); // or however you get it
	    ArrayList<SafeZone> safeZones = game.getBoard().getSafeZones();
	    TrackView trackView = new TrackView(track, safeZones);
	    trackView.setLayoutX((width-1960) / 2);
	    trackView.setLayoutY((height-980) / 2);
	    trackView.setRotate(-45);
	    Pane container = new Pane();
	    container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    container.getChildren().add(trackView);
	    StackPane.setAlignment(container, Pos.CENTER);
	    root.getChildren().add(2,container);
	}

}
