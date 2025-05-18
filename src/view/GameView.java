package view;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.player.Player;
import engine.Game;
import engine.board.Board;
import view.boardView.HomeZoneView;
import view.boardView.TrackView;
import view.playersView.FirePitView;
import view.playersView.HandView;
import view.playersView.PlayerProfile;

public class GameView extends StackPane{
	private final TrackView trackView;
	private final ArrayList<HomeZoneView> homeZones; 
	private final FirePitView firePitView;
	private final ArrayList<HandView> handViews;
	private final ArrayList<PlayerProfile> playerProfiles;
	private final Game game;
	
	public GameView(Game game, double width, double height){
		this.game = game;
		
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
		homeZones = new ArrayList<>();
		int index = 0 ; 
		double paneWidth = width*0.4;
		double paneHeight = height*0.68;
		
		
		trackView.setRotate(-45);
	    trackView.setLayoutX(-90);
	    trackView.setLayoutY(-130);
		
	    boardView.getChildren().add(trackView);
		for (Player player : players){
			HomeZoneView homeZoneView = new HomeZoneView(player.getMarbles());
			homeZones.add(homeZoneView);
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
		
		//setting the firePit
		firePitView = new FirePitView(game.getFirePit(), 1920, 1080);
		
		StackPane.setAlignment(firePitView, Pos.CENTER);
		this.getChildren().add(firePitView);
		this.setMaxSize(width * 0.8, height);
	}
	
	public void fieldMarble(int playerIndex){
		
	}

	public Game getGame() {
		return game;
	}

	public TrackView getTrackView() {
		return trackView;
	}

	public ArrayList<HomeZoneView> getHomeZones() {
		return homeZones;
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
	
}
