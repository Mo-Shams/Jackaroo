package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import model.player.Marble;
import model.player.Player;
import view.GameScene;
import view.GameView;
import view.boardView.MarbleView;
import view.playersView.CardView;
import view.playersView.FirePitView;
import view.playersView.HandView;
import engine.Game;
import exception.CannotFieldException;
import exception.GameException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;

public class GameController {
	private final Game game ; 
	private final GameScene gameScene ; 
	private final GameView gameView;
	public GameController(String name) throws IOException {
		game = new Game(name);
		gameScene = new GameScene(game);
		gameView = gameScene.getGameView();
		addShortCuts();
	}
	public Game getGame() {
		return game;
	}
	public GameScene getGameScene() {
		return gameScene;
	}
	
	// -------------------------------------- Event handlers ----------------------------------
	

//	public void canSelectCard(boolean canSelect){
//		for(CardView cardView : playerHand.getCardViews()){
//			if(canSelect){
//				cardView.setOnMouseClicked(e ->{
//					game.deselectAll();
//					if(!cardView.isSelected()){
//						playerHand.clearSelection();
//						try {
//							game.selectCard(cardView.getCard());
//							cardView.setSelected(true);
//						} catch (InvalidCardException e1) {
//							gameScene.showExceptionPopup(e1.getMessage());
//						}
//					}
//					else{
//						cardView.setSelected(false);
//					}
//				});
//			}
//			else
//				cardView.setOnMouseClicked(null);
//		}
//	}
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
				gameScene.showExceptionPopup(e.getMessage());
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
					//gameScene.SplitDistanceView();
					try {
						game.selectCard(cardView.getCard());
						cardView.setSelected(true);
					} catch (InvalidCardException e1) {
						gameScene.showExceptionPopup(e1.getMessage());
					}
				}
				else cardView.setSelected(false);
				
			});
		}
		
		ArrayList<Marble> actionableMarbles = game.getBoard().getActionableMarbles();
		for(Marble marble : actionableMarbles){
			MarbleView marbleView = MarbleView.MarbleToViewMap.get(marble);
			marbleView.setOnMouseClicked(e ->{
				if(!marbleView.isSelected()){
					try {
						game.selectMarble(marble);
						marbleView.setSelected(true);
					} catch (Exception e1) {
						gameScene.showExceptionPopup(e1.getMessage());
					}
				}
				else{
					game.deselectMarble(marble);
					marbleView.setSelected(false);
				}
			});
		}
		
	}
	
	
//	public void canSelectMarble(boolean canSelect){
//		ArrayList<Marble> actionableMarbles = game.getBoard().getActionableMarbles();
//		for(Marble marble : actionableMarbles){
//			MarbleView marbleView = MarbleView.MarbleToViewMap.get(marble);
//			if(canSelect){
//				marbleView.setOnMouseClicked(e ->{
//					
//					if(!marbleView.isSelected()){
//						try {
//							game.selectMarble(marble);
//							marbleView.setSelected(true);
//						} catch (Exception e1) {
//							gameScene.showExceptionPopup(e1.getMessage());
//						}
//					}
//					else{
//						game.deselectMarble(marble);
//						marbleView.setSelected(false);
//					}
//				});
//			}
//			else
//				marbleView.setOnMouseClicked(null);
//		}
//	}
	
		
	
	
	public void canPlayTurn(boolean canPlay){
		Button playButton = gameView.getPlayButton();
		if(canPlay){
			playButton.setOnMouseClicked(evt ->{
				HandView handView = gameView.getHandViews().get(0);
				FirePitView firePitView = gameView.getFirePitView();
				CardView cardView = CardView.cardToViewMap.get(game.getPlayers().get(0).getSelectedCard());

				try {
					game.playPlayerTurn();
					doTheAnimation(0);
				} 
				catch (GameException e) {
					gameScene.showExceptionPopup(e.getMessage());
					System.out.println(cardView);
					if(cardView != null){
						cardView.sendToFirePit(firePitView, 0).play();
						game.endPlayerTurn();
						gameView.updatePlayerProfiles();
						// run();
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
	
	// ------------------------- the main Game Animation WorkFlow --------------------------
	
	
	public void doTheAnimation(int playerIndex){
		HandView handView = gameView.getHandViews().get(playerIndex);
		CardView cardView = CardView.cardToViewMap.get(game.getPlayers().get(playerIndex).getSelectedCard());
		FirePitView firePitView = gameView.getFirePitView();
		SequentialTransition st = new SequentialTransition();
		if (playerIndex == 0) st.getChildren().add(cardView.sendToFirePit(firePitView, playerIndex));
		else st.getChildren().add(cardView.sendToFirePitCpu(firePitView, playerIndex));
		
		PauseTransition pt = new PauseTransition(Duration.seconds(1.5)); // 0.5 second pause
		game.endPlayerTurn();
		gameView.updatePlayerProfiles();
		pt.setOnFinished(e ->{
			run();
		});
		st.setOnFinished(e -> {
			gameView.updateBoardView();
			pt.play();
		});
		st.play();
	}
	
	
	// ------------------------ The Main Logic WorkFlow ------------------------------------- 
	
	public void run(){
		addEventHandlers();
		if(game.checkWin() != null) return;
		Player player = game.getPlayers().get(0);
		Player currentPlayer = game.getCurrentPlayer();
		if(game.canPlayTurn()){
			if(currentPlayer == player){
//				canSelectCard(true);
//				canSelectMarble(true);
				canPlayTurn(true);
			}
			else{
//				canSelectCard(false);
//				canSelectMarble(false);
				canPlayTurn(false);
				try {
					game.playPlayerTurn();
				} catch (GameException e) {
					gameScene.showExceptionPopup(e.getMessage());
				}
				doTheAnimation(game.getCurrentPlayerIndex());
			}
		}
		else{
			// doTheAnimation(game.getCurrentPlayerIndex());
			game.endPlayerTurn();
			gameView.updatePlayerProfiles();
			run();
		}
	}
	
	
	
	public GameView getGameView() {
		return gameView;
	}
}
