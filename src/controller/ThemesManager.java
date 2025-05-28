package controller;

import java.util.ArrayList;

import model.player.Marble;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import scene.EndScreenScene;
import scene.GameScene;
import scene.LoadingScene;
import scene.WelcomeScene;
import view.ImageCache;
import view.boardView.CellView;
import view.boardView.HomeZoneView;
import view.boardView.MarbleView;
import view.boardView.SafeZoneView;
import view.playersView.CardView;
import view.playersView.HandView;

public class ThemesManager {
	public static GameScene gameScene ;
	public static WelcomeScene welcomeScene ; 
	public static EndScreenScene endScene ;
	public static LoadingScene loadingScene ;
	
	public static int theme ; 
	
	public static void changeTheme(int theme){
		ArrayList<String> playerPaths = new ArrayList<>();
		ArrayList<String> playerNames = new ArrayList<>();
		String themePath ;

		switch (theme){
		
		case 0 : // the brown 
			themePath = "/resources/themes/original/";
			setWithColors("faedcd");
			setCellsWithColors("d4a373","6c584c");
			changeFirePit("bc6c25","6c584c","a68a64");
			changeHomeZone("99582a","7f4f24","6c584c");
			changeSafeZoneCells("99582a","6c584c");
			changeHomeZoneCells("faedcd","6c584c");
			
			changeCards("/resources/themes/original/cardback.png");
			
			playerPaths.add("/resources/themes/original/default.png");
			playerPaths.add("/resources/themes/original/muscles.png");
			playerPaths.add("/resources/themes/original/cool.png");
			playerPaths.add("/resources/themes/original/normal.png");
			
			playerNames.add("Muscles") ; playerNames.add("Cool"); playerNames.add("Normal");
			changePlayerProfiles(playerPaths, playerNames);
			
			changeMarbleImages(themePath);
			
			return ;
		case 1 :
			themePath = "/resources/themes/ancient_civilizations/";
			
			setWithImage("/resources/themes/ancient_civilizations/background.png");
			
			changeFirePit("bc6c25","6c584c","a68a64");
			changeHomeZone("99582a","7f4f24","6c584c");
			changeSafeZoneCells("99582a","6c584c");
			changeHomeZoneCells("faedcd","6c584c");
			
			changeCards("/resources/themes/ancient_civilizations/cardback.png");
			
			playerPaths.add("/resources/themes/ancient_civilizations/pharoah.png");
			playerPaths.add("/resources/themes/ancient_civilizations/greek.png");
			playerPaths.add("/resources/themes/ancient_civilizations/roman.png");
			playerPaths.add("/resources/themes/ancient_civilizations/viking.png");
			
			playerNames.add("Yoo") ; playerNames.add("shams"); playerNames.add("walid");
			changePlayerProfiles(playerPaths, playerNames);
			
			changeMarbleImages(themePath);
			
			return ;
			
			
		case 2 : 
			
		case 3 : 
			
		case 4 : 
			
			
			
		}
		

	}
	private static void setWithColors(String background){
		gameScene.getRoot().setBackground(null);
		gameScene.getRoot().setStyle("-fx-background-color: #" + background + ";"); 
		
	}
	private static void setWithImage(String path){
		double WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
		double HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();
		Image bgImage = new Image(path);
		BackgroundImage backgroundImage = new BackgroundImage(
			    bgImage,
			    BackgroundRepeat.NO_REPEAT,
			    BackgroundRepeat.NO_REPEAT,
			    BackgroundPosition.DEFAULT,
			    new BackgroundSize(
			    		WIDTH, HEIGHT, // 100% width & height
			    	    true, true, // interpreted as %s
			    	    false, true // contain=false, cover=true
			    )
			);; 
			gameScene.getRoot().setBackground(new Background(backgroundImage));

	}
	
	private static void setCellsWithColors(String fillColor, String strokeColor){
		CellView.setFILLING_COLOR(getColor(fillColor));
		for (CellView cellView : gameScene.getGameView().getTrackView().getCellViews()){
			cellView.getCircle().setFill(getColor(fillColor));;
			cellView.getCircle().setStroke(getColor(strokeColor));
		}
	}
	private static void changeCards(String imagePath){
	  	for (HandView handView : gameScene.getGameView().getHandViews()){
	  		for (CardView cardView : handView.getCardViews()){
	  			ImageView backImageView = cardView.getBackImageView(); // existing view already added to scene
	  			backImageView.setImage(ImageCache.getImage(imagePath));
	  			backImageView.setPreserveRatio(true);
		        backImageView.setSmooth(true);
		        backImageView.setFitHeight(160);
	  			
	  		}
	  	}
	}
	private static void changeFirePit (String fillColor, String strokeColor, String glowColor) {
		Circle circle = gameScene.getGameView().getFirePitView().getCircle();
		circle.setFill(getColor(fillColor));
		circle.setStroke(getColor(fillColor));
		circle.setStrokeWidth(5);
		DropShadow glow = new DropShadow();
		glow.setColor(Color.web("#a68a64"));
		glow.setRadius(40);
		glow.setSpread(0.5);
		glow.setBlurType(BlurType.THREE_PASS_BOX);
		circle.setEffect(glow);
	}
	
	private static void changeHomeZone (String fillColor, String strokeColor, String glowColor){
		for (HomeZoneView homeZoneView : gameScene.getGameView().getHomeZoneViews() ){
			Rectangle square =  homeZoneView.getHomeSquare();
		    square.setFill(getColor(fillColor));
	        square.setStroke(getColor(strokeColor));
	        square.setArcWidth(20);
	        square.setArcHeight(20);
	        square.setStrokeWidth(3);
	        

	        DropShadow glow = new DropShadow();
	        glow.setColor(getColor(glowColor));
	        glow.setRadius(5);
	        glow.setSpread(0.5);
	        square.setEffect(glow);
		}
	}
	
	private static void changeSafeZoneCells (String fillColor, String strokeColor){
		for (SafeZoneView safeZoneView : gameScene.getGameView().getTrackView().getSafeZoneViews()){
			for (CellView cellView : safeZoneView.getCellViews()){
				cellView.getCircle().setFill(getColor(fillColor));;
				cellView.getCircle().setStroke(getColor(strokeColor));
			}
		}
	}
	private static void changeHomeZoneCells(String fillColor, String strokeColor){
		for (HomeZoneView homeZoneView : gameScene.getGameView().getHomeZoneViews()){
			for (CellView cellView : homeZoneView.getCellViews()){
				cellView.getCircle().setFill(getColor(fillColor));;
				cellView.getCircle().setStroke(getColor(strokeColor));
			}
		}
	}

	private static void changePlayerProfiles(ArrayList<String> playerPaths, ArrayList<String> playerNames){
		Image image = ImageCache.getImage(playerPaths.get(0));
		gameScene.getGameView().getPlayerProfiles().get(0).getCircle().setFill(new ImagePattern(image));
		for (int i = 1 ; i < 4 ; i++){
			image = ImageCache.getImage(playerPaths.get(i));
			gameScene.getGameView().getPlayerProfiles().get(i).getCircle().setFill(new ImagePattern(image));
			gameScene.getGameView().getPlayerProfiles().get(i).getLabel().setText(playerNames.get(i-1));
		}
	}
	
	
	private static void changeOtherScenes (String backgroundColors, String ButtonColors) {
		// loadingScene.get
	}
	
	private static void changeMarbleImages(String themePath){
		int i = 0 ; 
		for (Marble marble : MarbleView.MarbleToViewMap.keySet()) {
		    MarbleView view = MarbleView.MarbleToViewMap.get(marble);
		    view = new MarbleView(marble,themePath + marble.getColour() + "_marble.png");
		}
	}
	
	private static Color getColor (String hex){
		return Color.web("#" + hex );
	}
	
	


}
