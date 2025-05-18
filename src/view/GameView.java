package view;

import java.util.ArrayList;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.player.Marble;
import model.player.Player;
import engine.Game;
import engine.board.Board;
import engine.board.Cell;
import engine.board.SafeZone;
import view.boardView.CellView;
import view.boardView.HomeZoneView;
import view.boardView.MarbleView;
import view.boardView.SafeZoneView;
import view.boardView.TrackView;
import view.playersView.FirePitView;
import view.playersView.HandView;
import view.playersView.PlayerProfile;

public class GameView extends StackPane{
	private final TrackView trackView;
	private final ArrayList<HomeZoneView> homeZoneViews; 
	private final FirePitView firePitView;
	private final ArrayList<HandView> handViews;
	private final ArrayList<PlayerProfile> playerProfiles;
	private final Button playButton;
	private final Game game;
	
	public GameView(Game game, double width, double height){
		this.game = game;
		playButton = createGameButton("Play");
		playButton.setPrefSize(width * 0.1, height * 0.08);
		Pane ButtonContainer = new Pane();
		playButton.setLayoutX(0);
		playButton.setLayoutY(0);
		this.setPadding(new Insets(30));


		ButtonContainer.setMaxSize(width * 0.25, height * 0.08);
		ButtonContainer.getChildren().add(playButton);
		
		//setting the players profiles
		ArrayList<Player> players = game.getPlayers();
		Board board = game.getBoard();
		playerProfiles = new ArrayList<>();
		int i = 0;
		for(Player player : players){
			PlayerProfile playerProfile = new PlayerProfile(player.getName(), player.getColour(), player == game.getCurrentPlayer(), player == game.getNextPlayer());
			switch(i){
			case 0: StackPane.setAlignment(playerProfile, Pos.BOTTOM_RIGHT);break;
			case 1: StackPane.setAlignment(playerProfile, Pos.BOTTOM_LEFT);break;
			case 2: StackPane.setAlignment(playerProfile, Pos.TOP_LEFT);break;
			case 3: StackPane.setAlignment(playerProfile, Pos.TOP_RIGHT);break;
			}
			playerProfiles.add(playerProfile);
			this.getChildren().add(playerProfile);
			i++;
		}
		
		
		//setting the boardView that includes the trackView and the HomeZoneView
		Pane boardView = new Pane();
		trackView = new TrackView (board.getTrack(), board.getSafeZones());
		homeZoneViews = new ArrayList<>();
		int index = 0 ; 
		double paneWidth = width*0.4;
		double paneHeight = height*0.65;
		
		
		trackView.setRotate(-45);
	    trackView.setLayoutX(-90);
	    trackView.setLayoutY(-130);
		
	    boardView.getChildren().add(trackView);
		for (Player player : players){
			HomeZoneView homeZoneView = new HomeZoneView(player.getMarbles());
			homeZoneViews.add(homeZoneView);
			switch (index){
				case 0 : homeZoneView.setLayoutX(paneWidth/2 - 40);homeZoneView.setLayoutY(paneHeight - 80); break;
				case 1 : homeZoneView.setLayoutX(0);homeZoneView.setLayoutY(paneHeight/2 - 40); break;
				case 2 : homeZoneView.setLayoutX(paneWidth/2 - 40);homeZoneView.setLayoutY(0);break;
				case 3 : homeZoneView.setLayoutX(paneWidth - 80);homeZoneView.setLayoutY(paneHeight/2 - 40);break;
			}
			index++ ;
			boardView.getChildren().add(homeZoneView);
			
		}
		boardView.setMaxSize(paneWidth, paneHeight);
		this.getChildren().add(boardView);
		
		
		
		//setting the handViews
		handViews = new ArrayList<>();
		i = 0;
		for(Player player : players){
			HandView handView = new HandView(player.getHand() , player.getColour(), player == game.getCurrentPlayer());
			switch(i){
			case 0: 
				StackPane.setAlignment(handView, Pos.BOTTOM_CENTER);break;
			case 1: 
				handView.setRotate(90);
				StackPane.setAlignment(handView, Pos.CENTER_LEFT);break;
			case 2: 
				handView.setRotate(180);
				StackPane.setAlignment(handView, Pos.TOP_CENTER);break;
			case 3: 
				handView.setRotate(-90);
				StackPane.setAlignment(handView, Pos.CENTER_RIGHT);break;
			}
			handViews.add(handView);
			this.getChildren().add(handView);
			i++;
		}
		
		this.getChildren().add(ButtonContainer);
		StackPane.setAlignment(ButtonContainer, Pos.BOTTOM_RIGHT);
		//setting the firePit
		firePitView = new FirePitView(game.getFirePit(), 1920, 1080);
		
		StackPane.setAlignment(firePitView, Pos.CENTER);
		this.getChildren().add(firePitView);
		this.setMaxSize(width * 0.8, height);
	}
	
	public void updateBoardView(){
		for(CellView cellView : trackView.getCellViews()){
			Marble marble = cellView.getCell().getMarble();
			if(marble != null)	cellView.setMarbleView(MarbleView.MarbleToViewMap.get(marble));
		}
		for(SafeZoneView safeZoneView : trackView.getSafeZoneViews()){
			for(CellView cellView : safeZoneView.getCellViews()){
				Marble marble = cellView.getCell().getMarble();
				if(marble != null) cellView.setMarbleView(MarbleView.MarbleToViewMap.get(marble));
			}
		}
		for(HomeZoneView homeZoneView: homeZoneViews){
			homeZoneView.updateHomeZoneView();
		}
	}
	
	public void updatePlayerProfiles(){
		for(PlayerProfile playerProfile : playerProfiles){
			if(playerProfile.getColour() == game.getActivePlayerColour()){
				playerProfile.setActive(true);
				continue;
			}
			else if(playerProfile.getColour() == game.getNextPlayerColour()){
				playerProfile.setNextActive(true);
				continue;
			}
			else{
				playerProfile.setActive(false);
				playerProfile.setNextActive(false);
			}
			
		}
	}
	
	private Button createGameButton(String text) {
        Button button = new Button(text);

        button.setStyle(
                "-fx-background-color: linear-gradient(#d6b97b, #a1763f);" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: gold;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 15;" +
                "-fx-text-fill: #3b2f2f;" +
                "-fx-font-size: 22px;" +
                "-fx-font-family: 'Verdana';" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 20 10 20;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GOLD);
        shadow.setRadius(10);

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            button.setEffect(shadow);
            button.setScaleX(1.05);
            button.setScaleY(1.05);
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            button.setEffect(null);
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });

        button.setOnMousePressed(e -> {
            button.setScaleX(0.95);
            button.setScaleY(0.95);
        });

        button.setOnMouseReleased(e -> {
            button.setScaleX(1.05);
            button.setScaleY(1.05);
        });

        return button;
    }

	public Game getGame() {
		return game;
	}

	public TrackView getTrackView() {
		return trackView;
	}
	
	

	

	public FirePitView getFirePitView() {
		return firePitView;
	}

	public ArrayList<HandView> getHandViews() {
		return handViews;
	}

	public ArrayList<PlayerProfile> getPlayerProfiles() {
		return playerProfiles;
	}

	public Button getPlayButton() {
		return playButton;
	}

	public ArrayList<HomeZoneView> getHomeZoneViews() {
		return homeZoneViews;
	}
	
}
