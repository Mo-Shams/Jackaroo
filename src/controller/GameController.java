package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Colour;
import model.card.Card;
import model.card.standard.Ace;
import model.card.standard.Four;
import model.card.standard.Jack;
import model.card.standard.King;
import model.card.standard.Queen;
import model.card.standard.Seven;
import model.card.standard.Standard;
import model.card.standard.Ten;
import model.card.wild.Burner;
import model.card.wild.Saver;
import model.player.Marble;
import model.player.Player;
import scene.EndScreenScene;
import view.GameScene;
import view.GameView;
import view.boardView.CellView;
import view.boardView.MarbleView;
import view.playersView.CardView;
import view.playersView.FirePitView;
import view.playersView.HandView;
import view.playersView.PlayerProfile;
import engine.Game;
import exception.CannotFieldException;
import exception.GameException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;
import exception.SplitOutOfRangeException;


public class GameController{
	private final Game game ; 
	private final GameScene gameScene ; 
	private final GameView gameView;
	
	private final Stage primaryStage ; 
	
	public GameController(String name, Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage ;
		game = new Game(name);
		gameScene = new GameScene(game);
		gameView = gameScene.getGameView();
		run();
		addShortCuts();
		addEventHandlers();
	}
	public Game getGame() {
		return game;
	}
	public GameScene getGameScene() {
		return gameScene;
	}
	
	// -------------------------------------- Event handlers ----------------------------------
	

	public void addShortCuts(){
		((StackPane)gameView).setOnKeyPressed((KeyEvent e1) ->{
			try{
				if(e1.getCode() == KeyCode.S){
					game.fieldMarble(0);
				}
				else if(e1.getCode() == KeyCode.A){
					game.fieldMarble(1);
				}
				else if(e1.getCode() == KeyCode.W){
					game.fieldMarble(2);
				}
				else if(e1.getCode() == KeyCode.D){
					game.fieldMarble(3);
				}
				gameView.updateBoardView();
				addEventHandlers();
			}
			catch(IllegalDestroyException | CannotFieldException e){
				GameScene.showExceptionPopup(e.getMessage(), gameScene.getRoot());
			}
		});
	}
	
	
	public void addEventHandlers(){
		HandView playerHand = gameView.getHandViews().get(0);
		for(CardView cardView : playerHand.getCardViews()){
			cardView.setOnMouseClicked(e ->{
				game.deselectCard();
				if(!cardView.isSelected()){
					playerHand.clearSelection();
					try {
						game.selectCard(cardView.getCard());
						cardView.setSelected(true);
					} catch (InvalidCardException e1) {
						GameScene.showExceptionPopup(e1.getMessage(), gameScene.getRoot());
					}
				}
				else cardView.setSelected(false);
				
			});
		}
		
		ArrayList<Marble> actionableMarbles = game.getBoard().getActionableMarbles();
		for(Marble marble : actionableMarbles){
			MarbleView marbleView = MarbleView.MarbleToViewMap.get(marble);
			marbleView.addHoverEffect();
			marbleView.setOnMouseClicked(e ->{
				if(!marbleView.isSelected()){
					try {
						game.selectMarble(marble);
						marbleView.setSelected(true);
					} catch (Exception e1) {
						GameScene.showExceptionPopup(e1.getMessage(), gameScene.getRoot());
					}
				}
				else{
					game.deselectMarble(marble);
					marbleView.setSelected(false);
				}
			});
		}
		
	}
	
	public void clearPlayerSelections(){
		Player player = game.getPlayers().get(0);
		CardView cardView = CardView.cardToViewMap.get(player.getSelectedCard());
		cardView.setSelected(false);
		cardView.scaleCard(1.15);
		for(Marble marble : player.getSelectedMarbles()){
			MarbleView.MarbleToViewMap.get(marble).setSelected(false);
		}
	}
	public void drawAllHands(){
		ArrayList<Player> players = game.getPlayers();
		int i = 0;
		for(HandView handView : gameView.getHandViews()){
			handView.drawCardViews(players.get(i).getHand());
			i++;
		}
	}
	
	
	public void canPlayTurn(boolean canPlay){
		Button playButton = gameView.getPlayButton();
		if(canPlay){
			playButton.setOnMouseClicked(evt ->{
				HandView handView = gameView.getHandViews().get(0);
				FirePitView firePitView = gameView.getFirePitView();
				Card selectedCard = game.getPlayers().get(0).getSelectedCard(); 
				CardView cardView = CardView.cardToViewMap.get(game.getPlayers().get(0).getSelectedCard());
				if (selectedCard instanceof Seven && game.getPlayers().get(0).getSelectedMarbles().size() == 2) {
					SplitDistanceView();
					return ;
				}

				try {
					game.playPlayerTurn();
					doTheAnimation(0);
				} 
				catch (GameException e) {
					GameScene.showExceptionPopup(e.getMessage(), gameScene.getRoot());
					if(cardView != null){
						cardView.dimCard();
						cardView.sendToFirePit(firePitView, 0).play();
						clearPlayerSelections();
						game.endPlayerTurn();
						gameView.updatePlayerProfiles();
					}
					game.deselectAll();
					handView.clearSelection();				
					run();
				}
			});
		}
		else
			playButton.setOnMouseClicked(null);
		
	}
	
	public void continueAfter7 () {
		HandView handView = gameView.getHandViews().get(0);
		FirePitView firePitView = gameView.getFirePitView();
		CardView cardView = CardView.cardToViewMap.get(game.getPlayers().get(0).getSelectedCard());
		try {
			game.playPlayerTurn();
			doTheAnimation(0);
		} 
		catch (GameException e) {
			GameScene.showExceptionPopup(e.getMessage(), gameScene.getRoot());
			if(cardView != null){
				cardView.dimCard();
				cardView.sendToFirePit(firePitView, 0).play();
				game.endPlayerTurn();
				gameView.updatePlayerProfiles();
			}
			game.deselectAll();
			handView.clearSelection();				
			run();
		}
	}
	
	public void SplitDistanceView() {
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
        
        for (int i = 1; i <= 6; i++) {
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
                gameScene.getRoot().getChildren().remove(overlay);
                
                
               try {
				game.editSplitDistance(number);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				GameScene.showExceptionPopup(e1.getMessage(), gameScene.getRoot());
			}
                
               continueAfter7();
            });

            buttonBox.getChildren().add(btn);
        }

        container.getChildren().addAll(message, buttonBox);
        // VBox.setMargin(buttonBox, new Insets(0, 0, 350, 0));
        overlay.getChildren().add(container);

        StackPane.setAlignment(container, Pos.CENTER);

        gameScene.getRoot().getChildren().add(overlay);

    }
	
	// ------------------------- the main Game Animation WorkFlow --------------------------
	
	
//	public void doTheAnimation(int playerIndex){
//		Card selectedCard = game.getPlayers().get(playerIndex).getSelectedCard(); 
//		CardView cardView = CardView.cardToViewMap.get(selectedCard);
//		FirePitView firePitView = gameView.getFirePitView();
//		SequentialTransition st = new SequentialTransition();
//		if (playerIndex == 0) st.getChildren().add(cardView.sendToFirePit(firePitView, playerIndex));
//		else st.getChildren().add(cardView.sendToFirePitCpu(firePitView, playerIndex));
//				PauseTransition pt = new PauseTransition(Duration.seconds(1.5)); // 1.5 second pause
//		
//	
//		
//		pt.setOnFinished(e ->{
//			game.endPlayerTurn();
//			gameView.updatePlayerProfiles();
//			run();
//		});
//		st.setOnFinished(e -> {
//			if(playerIndex == 0)
//				clearPlayerSelections();
//				gameView.updateBoardView();
//			// gameView.checkDiscard();
//			pt.play();
//		});
//		st.play();
//	}
	
	public void doTheAnimation(int playerIndex){
		Card selectedCard = game.getPlayers().get(playerIndex).getSelectedCard(); 
		ArrayList<Marble> selectedMarbles = game.getPlayers().get(playerIndex).getSelectedMarbles();
		CardView cardView = CardView.cardToViewMap.get(selectedCard);
		FirePitView firePitView = gameView.getFirePitView();
		
		SequentialTransition st = new SequentialTransition();
		
		if (playerIndex == 0) st.getChildren().add(cardView.sendToFirePit(firePitView, playerIndex));
		else st.getChildren().add(cardView.sendToFirePitCpu(firePitView, playerIndex));
		PauseTransition pt = new PauseTransition(Duration.seconds(1.5)); // 1.5 second pause
		SequentialTransition st2 = new SequentialTransition();
		PauseTransition pt2 = new PauseTransition(Duration.seconds(1.5)); // 1.5 second pause
		
	
	
		CardView cardViewDiscardedContainer = null;
		for(int playerIndex2=0; playerIndex2<4; playerIndex2++){
			HandView handView = gameView.getHandViews().get(playerIndex2);
			if(handView.getCardViews().size() == handView.getHand().size()) continue;
			for(CardView cardViewDiscarded: handView.getCardViews()){
				if(!handView.getHand().contains(cardViewDiscarded.getCard())) {
					cardViewDiscardedContainer = cardViewDiscarded;
					if (playerIndex2 == 0) st2.getChildren().add(cardViewDiscarded.sendToFirePit(firePitView ,playerIndex2));
					else st2.getChildren().add(cardViewDiscarded.sendToFirePitCpu(firePitView,playerIndex2));
					break;
				}
			}
		}
		CardView cardViewDiscardedContainer2 = cardViewDiscardedContainer;
		if(st2.getChildren().size() == 0){
			pt.setOnFinished(e ->{
				game.endPlayerTurn();
				gameView.updatePlayerProfiles();
				run();
			
			});
			st.setOnFinished(e -> {
				// ----
				if (getAction(selectedCard,selectedMarbles) > 0){
					MarbleView marbleView = MarbleView.MarbleToViewMap.get(selectedMarbles.get(0));
					CellView start = (CellView) marbleView.getParent();
					int i = gameView.getTrackView().getCellViews().indexOf(start);
					int steps = getAction(selectedCard,selectedMarbles);
					CellView target = gameView.getTrackView().getCellViews().get(i + steps);
					if(playerIndex == 0) clearPlayerSelections();
					start.moveMarbleTo(target);
					PauseTransition timer = new PauseTransition(Duration.millis(CellView.getMarbleSpeed()*steps+200));
					timer.setOnFinished(ev -> {
						gameView.updateBoardView();
						if (target.isWasTrap()) {
							gameScene.showSeeingTrappedEffect();
							target.setWasTrap(false);
						}
						
						pt.play();
					});
					timer.play();
				}else {
					gameView.updateBoardView();
					if(playerIndex == 0) clearPlayerSelections();
					pt.play();
				}
				
				
			});
			st.play();
		}else{
			pt.setDuration(Duration.seconds(0.5));
			pt2.setOnFinished(e ->{
				game.endPlayerTurn();
				gameView.updatePlayerProfiles();
				run();
			});
			st2.setOnFinished(e -> {
				if(playerIndex == 0) clearPlayerSelections();
				gameView.updateBoardView();
				pt2.play();
			});
			st.setOnFinished(e -> {
				pt.play();
			});
			pt.setOnFinished(e -> {
				cardViewDiscardedContainer2.dimCard();
				st2.play();
			});
			st.play();
		}
	}
	
	// moveAction -> 1, backwordAction -> 2, discardAction -> 3, 
	// fieldAction -> 4, saveAction -> 5, BurnAction -> 6, swapAction -> 7
	public int getAction (Card card, ArrayList<Marble> marbles){ 
		
		switch (marbles.size()){
			case 0 : 
				if (card instanceof King || card instanceof Ace) return -1 ; // field action
				if (card instanceof Queen || card instanceof Ten ) return -2 ; // discard action 
				return -100 ;
			case 2 : 
				if (card instanceof Jack) return -3 ; // swap action ;
				if (card instanceof Seven) return -4 ; // dual move action 
				return -100 ;
			default : 
				if (card instanceof Four) return -5 ; // backword Move action 
				if (card instanceof Saver) return -6 ; // Saver Action 
				if (card instanceof Burner) return -7 ; // burn Action 
				
				Standard moveCard = (Standard)card ;
				return moveCard.getRank() ; // nomarl move action 
		}
	}


	
	// ------------------------ The Main Logic WorkFlow ------------------------------------- 
	
	public void run(){
		if(game.checkWin() != null) end();
		
		Player realPlayer = game.getPlayers().get(0);
		Player currentPlayer = game.getCurrentPlayer();
		if(game.canPlayTurn()){
			if(currentPlayer == realPlayer){
				if(game.getTurn() == 0){
					gameView.getFirePitView().updateFirePitView();
					drawAllHands();
				}
				addEventHandlers();
				canPlayTurn(true);
			}
			else{
				canPlayTurn(false);
				try {
					game.playPlayerTurn();
					doTheAnimation(game.getCurrentPlayerIndex());
				} catch (GameException e) {
					GameScene.showExceptionPopup(e.getMessage(), gameScene.getRoot());
				}
			}
		}
		else{
			
			game.endPlayerTurn();
			gameView.updatePlayerProfiles();
			run();
		}
	}
	
	public void end () {
		Colour colour = game.checkWin();
		ArrayList<PlayerProfile> players = gameView.getPlayerProfiles();
		ArrayList<PlayerProfile> winners = new  ArrayList<PlayerProfile>();
				
		for (PlayerProfile player : players)
			if (player.getColour() == colour) winners.add(player);
		for (PlayerProfile player : players)
			if (player.getColour() != colour) winners.add(player);

		
		EndScreenScene endScene = new EndScreenScene(winners);
		Scene scene = endScene.createScene();
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setFullScreen(true);
	}
	
	
	
	public GameView getGameView() {
		return gameView;
	}
}
