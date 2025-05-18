//package controller;
//
//import java.util.Scanner;
//
//import model.player.Marble;
//import model.player.Player;
//import exception.GameException;
//import exception.InvalidMarbleException;
//
//public class OldMethods {
//	public void runGame(){
//		Scanner sc = new Scanner(System.in);
//		Player player = game.getPlayers().get(0);
//		while(game.checkWin() == null){
//			Player currentPlayer = game.getCurrentPlayer();
//			if(game.canPlayTurn()){
//				if(currentPlayer == player){
//					playerCanSelectCard(true);
//					//should be replaced by the mechanism of playing like a button to click
//					System.out.print("Do you want to play or Choose a marble first? (type 0 or 1): ");
//					while(true){
//						//some mechanism of hitting play like a button that can be hitten any time
//						//it'll be in the loop for now just for playing on the terminal
//						int wantToPlay = sc.nextInt();
//						System.out.println();
//						if(wantToPlay == 0){
//							try {
//								game.playPlayerTurn();
//							} catch (GameException e) {
//								System.out.println(e.getMessage());
//							}
//							break;
//						}
//						try {
//							// some mechanism of choosing a marble from the actionable marbles
//							Marble inputMarble = game.getBoard().getActionableMarbles().get(sc.nextInt());
//							// all the marbles that are neither on the track nor the safeZone must be unclickable
//							game.selectMarble(inputMarble);
//						} catch (InvalidMarbleException | IndexOutOfBoundsException e) {
//							System.out.println(e.getMessage());
//						}
//						System.out.print("Do you want to play now or Choose another marble? (type 0 or 1): ");
//					}
//				}
//				else{
//					try {
//						currentPlayer.play();;
//						System.out.println("CPU " + game.getPlayers().indexOf(currentPlayer) + " played: " + currentPlayer.getSelectedCard().getName());
//					} catch (GameException e) {
//						System.out.println(e.getMessage());
//					}
//				}
//			}
//			game.endPlayerTurn();
//		}
//	}
//}
