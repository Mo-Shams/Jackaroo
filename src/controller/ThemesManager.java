package controller;

import java.util.ArrayList;

import model.player.Marble;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
	
	public static int theme = -1 ; 
	
	public static void changeTheme(int id){
		ArrayList<String> playerPaths = new ArrayList<>();
		ArrayList<String> playerNames = new ArrayList<>();
		String themePath ;

		switch (id){
		
		case 0 : // the brown 

			MarbleView.setDEFAULT_MARBLE_SIZE(32);
			
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
			
			
			if (theme != id) SoundManager.playMusic("original.mp3");
			theme = 0 ; 
			return ;
			
		case 1 :
			
			MarbleView.setDEFAULT_MARBLE_SIZE(26);

			themePath = "/resources/themes/sci-fi_dystopia/";
			
			setWithImage("/resources/themes/sci-fi_dystopia/background.png");
			
			setCellsWithColors("c77dff","3c096c");
			changeFirePit("9d4edd","5a189a","a2d2ff");
			changeHomeZone("9d4edd","10002b","e0aaff");
			
			changeSafeZoneCells("5a189a","e0aaff");
			changeHomeZoneCells("5a189a","e0aaff");
			
			changeCards("/resources/themes/sci-fi_dystopia/cardback.png");
			
			playerPaths.add("/resources/themes/sci-fi_dystopia/default.png");
			playerPaths.add("/resources/themes/sci-fi_dystopia/cyborge.png");
			playerPaths.add("/resources/themes/sci-fi_dystopia/mystic.png");
			playerPaths.add("/resources/themes/sci-fi_dystopia/survivor.png");

			
			playerNames.add("cyborge") ; playerNames.add("mystic"); playerNames.add("survivor");
			changePlayerProfiles(playerPaths, playerNames);
			

			changeMarbleImages(themePath);
			if (theme != id) SoundManager.playMusic("science.mp3");
			theme = 1 ; 
			return ;
			
		
			
		case 2 : 
			
			MarbleView.setDEFAULT_MARBLE_SIZE(26);

			themePath = "/resources/themes/anime/";
			
			setWithImage("/resources/themes/anime/background.jpg");
			
			setCellsWithColors("62b6cb","1b4965");
			changeFirePit("cae9ff","1b4965","1b4965");
			changeHomeZone("cae9ff","7f4f24","6c584c");
			changeSafeZoneCells("62b6cb","1b4965");
			changeHomeZoneCells("62b6cb","1b4965");
			
			changeCards("/resources/themes/anime/cardback.png");
			
			playerPaths.add("/resources/themes/anime/default.jpg");
			playerPaths.add("/resources/themes/anime/naruto.jpg");
			playerPaths.add("/resources/themes/anime/luffy.jpg");
			playerPaths.add("/resources/themes/anime/killua.jpg");

			
			playerNames.add("naruto") ; playerNames.add("luffy"); playerNames.add("killua");
			changePlayerProfiles(playerPaths, playerNames);
			

			changeMarbleImages(themePath);
			if (theme != id) SoundManager.playMusic("anime.mp3");
			theme = 2 ; 
			return ;
			
		case 3 : 
			
			MarbleView.setDEFAULT_MARBLE_SIZE(26);

			themePath = "/resources/themes/ancient_civilizations/";
			
			setWithImage("/resources/themes/ancient_civilizations/background.png");
			
			setCellsWithColors("ffb563","e0afa0");
			changeFirePit("ffb563","0a0908","5e503f");
			changeHomeZone("d68c45","7f4f24","6c584c");
			changeSafeZoneCells("c6ac8f","5e503f");
			changeHomeZoneCells("c6ac8f","5e503f");
			
			changeCards("/resources/themes/ancient_civilizations/cardback.png");
			
			playerPaths.add("/resources/themes/ancient_civilizations/default.png");
			playerPaths.add("/resources/themes/ancient_civilizations/pharoah.png");
			playerPaths.add("/resources/themes/ancient_civilizations/roman.png");
			playerPaths.add("/resources/themes/ancient_civilizations/greek.png");

			
			playerNames.add("pharoah") ; playerNames.add("roman"); playerNames.add("greek");
			changePlayerProfiles(playerPaths, playerNames);
			
			
			changeMarbleImages(themePath);
			if (theme != id) SoundManager.playMusic("ancient_civilization.mp3");
			theme = 3 ; 
			return ;

		
			
		case 4 : 

			MarbleView.setDEFAULT_MARBLE_SIZE(32);
			
			themePath = "/resources/themes/girl/";
			setWithColors("ffc2d1");
			setCellsWithColors("ff8fab","fb6f92");
			changeFirePit("ff4d6d","ff758f","ffb3c6");
			changeHomeZone("c9184a","7f4f24","6c584c");
			changeSafeZoneCells("ff4d6d","c9184a");
			changeHomeZoneCells("ff758f","ff4d6d");
			
			changeCards("/resources/themes/girl/pinkback.png");
			
			playerPaths.add("/resources/themes/girl/default.png");
			playerPaths.add("/resources/themes/girl/muscles.png");
			playerPaths.add("/resources/themes/girl/cool.png");
			playerPaths.add("/resources/themes/girl/normal.png");
			
			playerNames.add("muscles") ; playerNames.add("cool"); playerNames.add("normal");
			changePlayerProfiles(playerPaths, playerNames);
			

			changeMarbleImages(themePath);
			if (theme != id) SoundManager.playMusic("girl.mp3");
			theme = 4 ; 
			return ;
			
			
		}
				

	}
	private static void setWithColors(String background){
		gameScene.getRoot().setBackground(new Background(new BackgroundFill(getColor(background), null, null)));
		// gameScene.getRoot().setStyle("-fx-background-color: #" + background + ";"); 
		
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
		    MarbleView marbleView = MarbleView.MarbleToViewMap.get(marble);
		    marbleView.setImage(ImageCache.getImage(themePath + marble.getColour() + "_marble.png"));
		    marbleView.setFitHeight(MarbleView.getDEFAULT_MARBLE_SIZE());
		}
	}
	
	private static Color getColor (String hex){
		if (hex == null) return Color.web("#000000",0.0);
		else return Color.web("#" + hex );
	}
	
	


}
