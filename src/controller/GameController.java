package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import model.card.Card;
import model.player.Marble;
import model.player.Player;
import view.GameScene;
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
	public GameController(String name) throws IOException {
		game = new Game(name);
		gameScene = new GameScene(game);
	}
	public Game getGame() {
		return game;
	}
	public GameScene getGameScene() {
		return gameScene;
	}
	
	public void playerCanSelectCard(boolean canChoose){
		HandView playerHand = gameScene.getGameView().getHandViews().get(0);
		for(CardView cardView : playerHand.getCardViews()){
			if(canChoose){
				cardView.setOnMouseClicked(e ->{
					game.deselectAll();
					if(!cardView.isSelected()){
						playerHand.clearSelection();
						try {
							game.selectCard(cardView.getCard());
						} catch (InvalidCardException e1) {
							System.err.println(e1.getMessage());
						}
					}
					cardView.setSelected(!cardView.isSelected());
				});
			}
			else
				cardView.setOnMouseClicked(null);
		}
	}
	
	public void playCard(int playerIndex){
		HandView handView = gameScene.getGameView().getHandViews().get(playerIndex);
		CardView cardView = CardView.cardToViewMap.get(game.getPlayers().get(playerIndex).getSelectedCard());
		FirePitView firePitView = gameScene.getGameView().getFirePitView();
		cardView.sendToFirePit(firePitView, handView, playerIndex);
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
								playCard(0);
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
}
