package controller;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import scene.EndScreenScene;
import scene.GameScene;
import scene.LoadingScene;
import scene.WelcomeScene;
import view.ImageCache;
import view.boardView.CellView;
import view.boardView.HomeZoneView;
import view.boardView.SafeZoneView;
import view.playersView.CardView;
import view.playersView.HandView;

public class ThemesManager {
	public static GameScene gameScene ;
	public static WelcomeScene welcomeScene ; 
	public static EndScreenScene endScene ;
	public static LoadingScene loadingScene ;
	
	
	public static void changeTheme(int theme){
		switch (theme){
		
		
		case 0 : // the brown 
			setWithColors("faedcd","d4a373","6c584c");
			changeCards("/resources/card_images/brown_back.png");
			changeFirePit("bc6c25","6c584c","a68a64");
			changeHomeZone("99582a","7f4f24","6c584c");
			changeSafeZoneCells("99582a","6c584c");
			changeHomeZoneCells("faedcd","6c584c");
			changePlayerProfiles();
			return ;
		case 1 :
			
		case 2 : 
			
		case 3 : 
			
		case 4 : 
			
			
			
		}
		

	}
	private static void setWithColors(String background, String fillColor, String strokeColor){
		gameScene.getRoot().setStyle("-fx-background-color: #" + background + ";"); 
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
	
	private static void changeOtherScenes () {
		
	}
	
	private static void changePlayerProfiles(){
//		gameScene.getGameView().getPlayerProfiles().get(0).setRotation().play();
//		gameScene.getGameView().getPlayerProfiles().get(0).setPulseEffect().play();
	}
	private static void changeMarbleImages(){
		
	}
	
	private static Color getColor (String hex){
		return Color.web("#" + hex );
	}
	
	


}
