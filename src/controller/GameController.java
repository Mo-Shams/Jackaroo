package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.control.Button;
import model.card.Card;
import model.player.Marble;
import model.player.Player;
import view.GameScene;
import view.GameView;
import view.boardView.MarbleView;
import view.playersView.CardView;
import view.playersView.FirePitView;
import view.playersView.HandView;
import engine.Game;
import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;

public class GameController {
	private final Game game ; 
	private final GameScene gameScene ; 
	private final GameView gameView;
	public GameController(String name) throws IOException {
		game = new Game(name);
		gameScene = new GameScene(game);
		gameView = gameScene.getGameView();
	}
	public Game getGame() {
		return game;
	}
	public GameScene getGameScene() {
		return gameScene;
	}
	
	public void playerCanSelectCard(boolean canChoose){
		HandView playerHand = gameView.getHandViews().get(0);
		for(CardView cardView : playerHand.getCardViews()){
			if(canChoose){
				cardView.setOnMouseClicked(e ->{
					game.deselectAll();
					if(!cardView.isSelected()){
						playerHand.clearSelection();
						try {
							game.selectCard(cardView.getCard());
							cardView.setSelected(!cardView.isSelected());
						} catch (InvalidCardException e1) {
							System.err.println(e1.getMessage());
						}
					}
					else{
						cardView.setSelected(false);
					}
				});
			}
			else
				cardView.setOnMouseClicked(null);
		}
	}
	
	public void playerCanSelectMarble(boolean canSelect){
		ArrayList<Marble> actionableMarbles = game.getBoard().getActionableMarbles();
		for(Marble marble : actionableMarbles){
			MarbleView marbleView = MarbleView.MarbleToViewMap.get(marble);
			if(canSelect){
				marbleView.setOnMouseClicked(e ->{
					if(!marbleView.isSelected()){
						try {
							game.selectMarble(marble);
							marbleView.setSelected(!marbleView.isSelected());
						} catch (Exception e1) {
							System.err.println(e1.getMessage());
						}
					}
					else{
						game.deselectMarble(marble);
						marbleView.setSelected(!marbleView.isSelected());
					}
				});
			}
			else
				marbleView.setOnMouseClicked(null);
		}
	}
	
	public void wantToPlay(boolean canPlay){
		Button playButton = gameView.getPlayButton();
		if(canPlay){
			playButton.setOnMouseClicked(evt ->{
				HandView handView = gameView.getHandViews().get(0);
				FirePitView firePitView = gameView.getFirePitView();
				CardView cardView = CardView.cardToViewMap.get(game.getPlayers().get(0).getSelectedCard());
				try {
					game.playPlayerTurn();
					playView(0);
				} 
				catch (GameException e) {
					System.err.println(e.getMessage());
					if(cardView != null){
						cardView.sendToFirePit(firePitView, handView, 0);
						game.endPlayerTurn();
						gameView.updatePlayerProfiles();
						run();
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
	
	
	public void playView(int playerIndex){
		HandView handView = gameView.getHandViews().get(playerIndex);
		CardView cardView = CardView.cardToViewMap.get(game.getPlayers().get(playerIndex).getSelectedCard());
		FirePitView firePitView = gameView.getFirePitView();
		cardView.sendToFirePit(firePitView, handView, playerIndex);
		game.endPlayerTurn();
		gameView.updateBoardView();
		gameView.updatePlayerProfiles();
		run();
	}
	
	public void run(){
		if(game.checkWin() != null) return;
		Player player = game.getPlayers().get(0);
		Player currentPlayer = game.getCurrentPlayer();
		if(game.canPlayTurn()){
			if(currentPlayer == player){
				playerCanSelectCard(true);
				playerCanSelectMarble(true);
				wantToPlay(true);
			}
			else{
				playerCanSelectCard(false);
				playerCanSelectMarble(false);
				wantToPlay(false);
				try {
					game.playPlayerTurn();
				} catch (GameException e) {
					System.err.println(e.getMessage());
				}
				playView(game.getCurrentPlayerIndex());
			}
		}
		else{
			game.endPlayerTurn();
			gameView.updatePlayerProfiles();
			run();
		}
	}
	
	
	
	public void runGame(){
		Scanner sc = new Scanner(System.in);
		Player player = game.getPlayers().get(0);
		while(game.checkWin() == null){
			Player currentPlayer = game.getCurrentPlayer();
			if(game.canPlayTurn()){
				if(currentPlayer == player){
					playerCanSelectCard(true);
					//should be replaced by the mechanism of playing like a button to click
					System.out.print("Do you want to play or Choose a marble first? (type 0 or 1): ");
					while(true){
						//some mechanism of hitting play like a button that can be hitten any time
						//it'll be in the loop for now just for playing on the terminal
						int wantToPlay = sc.nextInt();
						System.out.println();
						if(wantToPlay == 0){
							try {
								game.playPlayerTurn();
							} catch (GameException e) {
								System.out.println(e.getMessage());
							}
							break;
						}
						try {
							// some mechanism of choosing a marble from the actionable marbles
							Marble inputMarble = game.getBoard().getActionableMarbles().get(sc.nextInt());
							// all the marbles that are neither on the track nor the safeZone must be unclickable
							game.selectMarble(inputMarble);
						} catch (InvalidMarbleException | IndexOutOfBoundsException e) {
							System.out.println(e.getMessage());
						}
						System.out.print("Do you want to play now or Choose another marble? (type 0 or 1): ");
					}
				}
				else{
					try {
						currentPlayer.play();;
						System.out.println("CPU " + game.getPlayers().indexOf(currentPlayer) + " played: " + currentPlayer.getSelectedCard().getName());
					} catch (GameException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			game.endPlayerTurn();
		}
	}
	public GameView getGameView() {
		return gameView;
	}
}
