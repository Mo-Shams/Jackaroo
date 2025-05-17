package scene;

import java.io.IOException;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Colour;
import model.player.Marble;
import model.player.Player;
import view.CardsView.CardView;
import view.CardsView.FirePitView;
import view.CardsView.HandView;
import view.boardView.HomeZoneView;
import view.boardView.TrackView;
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

		Rectangle background = generateBackground(width, height +70, Color.SKYBLUE, Color.LIGHTGREEN, Color.YELLOWGREEN);

		root.getChildren().add(0, background);
		
		// Player Profile Pane
        addPlayerProfiles(root, game.getPlayers(), width, height);
		
		// Hands
		FirePitView firePitView = new FirePitView(root);
		renderHomeZones(root, controller, width, height);
		renderHands(root, scene, controller, width, height, firePitView);
		
		renderTrack(root, controller,  width, height);
		
		
		// Sample marble + cell

		
		
		firePitView.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		StackPane.setAlignment(firePitView, Pos.CENTER);
		root.getChildren().add(5, firePitView);

		renderButtons(root, controller, firePitView);
		
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
	
	private void renderButtons(StackPane root, GameController controller, FirePitView firePitView) {
	    // 1) create the buttons
	    Button playBtn     = new Button("Play");
	    Button deselectBtn = new Button("Deselect");
	    playBtn.setPrefWidth(100);
	    deselectBtn.setPrefWidth(100);

	    // 2) wire them up in the controller
	    controller.addControlButtons(playBtn, deselectBtn);

	    // 3) layout in an HBox and add to the root
	    HBox controls = new HBox(10, playBtn, deselectBtn);
	    controls.setPadding(new Insets(10));
	    controls.setAlignment(Pos.CENTER);
	    controls.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    StackPane.setAlignment(controls, Pos.BOTTOM_RIGHT);

	    root.getChildren().add(controls);
	}

	private void renderHands(StackPane root, Scene scene, GameController controller, double width, double height, FirePitView firePitView) {
		int i = 0;
		StackPane container = new StackPane();
		for (Player player : game.getPlayers()) {
			HBox handBox = renderSingleHand(player, scene, controller, firePitView,width, height);
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
		root.getChildren().add(2, container);
		container.toFront();
	}

	private HBox renderSingleHand(Player player, Scene scene, GameController controller, FirePitView firePitView, double width, double height) {
		boolean isPlayer = player.getColour() == game.getPlayers().get(0).getColour();
		HandView handView = new HandView(player.getHand(), player.getColour(), isPlayer);
		HBox handBox = handView.getHandBox();

		for (Node node : handBox.getChildren()) {
			CardView cardView = (CardView) node;
			controller.addCardClickHandler(cardView, handView, firePitView);
		}
		return handBox;
	}
	
	private void renderHomeZones(StackPane root,GameController controller, double width, double height) {
	    // Iterate over each player and render their HomeZoneView
	    for (int i = 0; i < game.getPlayers().size(); i++) {
	        Player player = game.getPlayers().get(i);
	        ArrayList<Marble> marbles = player.getMarbles();
	        
	        // Create the HomeZoneView for each player
	        HomeZoneView homeZoneView = new HomeZoneView(marbles);
	        controller.addHomeZoneMarbleHandler(homeZoneView);
	        controller.addHoverEffect(homeZoneView);
	        // Position HomeZone based on player index (4 corners)
	        switch (i) {
	            case 0:
	                StackPane.setAlignment(homeZoneView, Pos.BOTTOM_CENTER);
	                break;
	            case 1:
	                StackPane.setAlignment(homeZoneView, Pos.CENTER_LEFT);
	                break;
	            case 2:
	                StackPane.setAlignment(homeZoneView, Pos.TOP_CENTER);
	                break;
	            case 3:
	                StackPane.setAlignment(homeZoneView, Pos.CENTER_RIGHT);
	                break;
	        }

	        // Set size for HomeZoneView (adjust as needed)
	        homeZoneView.setMaxWidth(width * 0.6);
	        homeZoneView.setMaxHeight(height * 0.4);
	        //homeZoneView.getContainer().setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	        //Add the HomeZoneView container to the root
	        root.getChildren().add(homeZoneView);
	    }
	}
	
	private void renderTrack(StackPane root,GameController controller, double width, double height) {
	    ArrayList<Cell> track = game.getBoard().getTrack();
	    ArrayList<SafeZone> safeZones = game.getBoard().getSafeZones();
	    TrackView trackView = new TrackView(track, safeZones);
	    controller.setTrackView(trackView);
	    trackView.setLayoutX((width-1925) / 2);
	    trackView.setLayoutY((height-1025) / 2);
	    trackView.setRotate(-45);
	    Pane container = new Pane();
	    container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    container.getChildren().add(trackView);
	    StackPane.setAlignment(container, Pos.CENTER);
	    root.getChildren().add(4, container);
	}
	
	
	private void addPlayerProfiles (StackPane root, ArrayList<Player> players, double width, double height) {
		StackPane container = new StackPane();
	    for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            VBox box = createProfileBox(p.getName(), p.getColour());
            box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            switch (i) {
                case 0:
                    StackPane.setAlignment(box, Pos.BOTTOM_RIGHT);
                    break;
                case 1:
                    StackPane.setAlignment(box, Pos.BOTTOM_LEFT);
                    break;
                case 2:
                    StackPane.setAlignment(box, Pos.TOP_LEFT);
                    break;
                case 3:
                    StackPane.setAlignment(box, Pos.TOP_RIGHT);
                    break;
            }
            container.getChildren().add(box);
        }
	    container.setMaxSize(width * 0.6, height);
	    root.getChildren().add(1, container);
	}
		
	private VBox createProfileBox(String name, Colour colour) {
        Circle avatar = new Circle(60, Color.LIGHTGRAY);
        avatar.setStroke(Color.GRAY);
        avatar.setStrokeWidth(2);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", 25));
        nameLabel.setAlignment(Pos.CENTER);
        Color color; 
        switch (colour) {
	        case RED:   color = Color.RED;   break;
	        case GREEN: color = Color.GREEN; break;
	        case BLUE:  color = Color.BLUE;  break;
	        case YELLOW: color = Color.YELLOW; break; 
	        default:    color = Color.BLACK; break;
	        
	    }
        nameLabel.setTextFill(color);

        VBox box = new VBox(10);
        box.getChildren().add(avatar);
        box.getChildren().add(nameLabel);
        box.setAlignment(Pos.CENTER);
        return box;
    }

}
