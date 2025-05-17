package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.List;


import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.*;
import engine.board.Board;
import engine.board.SafeZone;
import exception.CannotDiscardException;
import exception.CannotFieldException;
import exception.GameException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import exception.SplitOutOfRangeException;


public class Game implements GameManager {
	//instance variables
	private final Board board;
	private final ArrayList<Player> players;
	private final ArrayList<Card> firePit;
	private int currentPlayerIndex; 
	private int turn;
	
	//constructor
    public Game(String playerName) throws IOException {
    	ArrayList <Colour> colourOrder = new ArrayList<>(); 
    	colourOrder.add(Colour.RED); colourOrder.add(Colour.GREEN); colourOrder.add(Colour.BLUE); colourOrder.add(Colour.YELLOW);
        Collections.shuffle(colourOrder);
        
        this.board = new Board(colourOrder, this); 
        Deck.loadCardPool(board, this);
        
        this.players = new ArrayList<>(); 
        players.add(new Player(playerName, colourOrder.get(0)));
        
        List<String> cpuNamePool = Arrays.asList(
                "Adham", "Seif", "Ziad", "Alice",
                "Bob", "Tourist", "Mostafa Saad", "ElHafoozliq",
                "Youssef", "Mohammed", "Jianqly", "Osama"
            );
            // 5) Shuffle and pick the first three:
            Collections.shuffle(cpuNamePool);
            List<String> chosenCpuNames = cpuNamePool.subList(0, 3);

            // 6) Create your three CPU players with those names and distinct colours:
            for (int i = 0; i < 3; i++) {
                String cpuName   = chosenCpuNames.get(i);
                Colour cpuColour = colourOrder.get(i + 1);
                players.add(new CPU(cpuName, cpuColour, board));
            }
        
        for (Player player : players) {
        	ArrayList<Card> player_cards = Deck.drawCards();
        	player.getHand().addAll(player_cards); 
        	
        }
        
        this.currentPlayerIndex = 0;
        this.turn = 0;
        this.firePit = new ArrayList<>();
    }
    
    
    
    
    
    
    
    //simple getter methods for the instance variables
	public Board getBoard() {
		return board;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public ArrayList<Card> getFirePit() {
		return firePit;
	}
	
	
	
	
	
	
	//methods that allow the current player to take an action and executes it if valid
	public void selectCard(Card card) throws InvalidCardException{
		players.get(currentPlayerIndex).selectCard(card);
	}
	public void selectMarble(Marble marble) throws InvalidMarbleException{
		players.get(currentPlayerIndex).selectMarble(marble);
	}
	public void deselectAll(){
		players.get(currentPlayerIndex).deselectAll();
	}
	public void playPlayerTurn() throws GameException{
		players.get(currentPlayerIndex).play();
	}
	
	
	
	
	
	
	//method that edits how 7 is divided between 2 marbles
	public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException{
		if(splitDistance < 1 || splitDistance > 6)
			throw new SplitOutOfRangeException("You cannot split the 7 to " + splitDistance + " and " + (7 - splitDistance));
		board.setSplitDistance(splitDistance);
	}
	
	
	
	
	
	
	
	//method that checks whether the player turn is skipped (i.e. discarded) or not
	public boolean canPlayTurn(){
		return players.get(currentPlayerIndex).getHand().size() == (4-turn);
	}
	
	
	
	
	
	
	
	//method that ends the current player turn and sets the game ready for the next player
	public void endPlayerTurn(){
		Player currentPlayer = players.get(currentPlayerIndex);
		currentPlayer.getHand().remove(currentPlayer.getSelectedCard()); // adding the null to the cardsPool
//		if(currentPlayer.getHand().remove(currentPlayer.getSelectedCard()))
		firePit.add(currentPlayer.getSelectedCard());
		deselectAll();
		currentPlayerIndex++;
		if(currentPlayerIndex == 4){
			turn++;
			currentPlayerIndex = 0;
		}
		if(turn == 4){
			turn = 0;
			for(Player player : players){
				if (Deck.getPoolSize() < 4){ //why not 16? (because you can refill while drawing the cards)
					Deck.refillPool(firePit);
					firePit.clear();
				}
				player.setHand(Deck.drawCards());
			}
		}
	}
	
	
	
	
	
	
	
	//method that checks if the game ended and specifies the winner
	public Colour checkWin(){
		for(int i = 0; i < 4; i++){
			SafeZone currentSafeZone = board.getSafeZones().get(i);
			if(currentSafeZone.isFull())
				return currentSafeZone.getColour();
		}
		return null;
	}

	//methods that defines getting a marble in and out of the home zone
	@Override
	public void sendHome(Marble marble) {
		for(Player player : players)
			if(player.getColour() == marble.getColour())
				player.regainMarble(marble);
	}
	@Override
	public void fieldMarble() throws CannotFieldException, IllegalDestroyException{
		Marble marble = players.get(currentPlayerIndex).getOneMarble();
		if(marble == null)
			throw new CannotFieldException("You don't have any marbles in your home zone");
		
		board.sendToBase(marble);
		players.get(currentPlayerIndex).getMarbles().remove(marble);
	}

	//methods that defines cards effects on other player's cards
	@Override
	public void discardCard(Colour colour) throws CannotDiscardException {
		Player colourPlayer = null;
		for(Player player : players){
			if(player.getColour() == colour)
				colourPlayer = player;
		}
		if(colourPlayer.getHand().isEmpty())
			throw new CannotDiscardException("The player has no cards to be discarded");
		int randomIndex = (int)(Math.random() * colourPlayer.getHand().size());
		firePit.add(colourPlayer.getHand().remove(randomIndex));
	}
	@Override
	public void discardCard() throws CannotDiscardException {
		int randomIndex; //other than the current player index
		do{
			randomIndex = (int)(Math.random() * 4);
		}while(randomIndex == currentPlayerIndex);
		
		Player randomPlayer = players.get(randomIndex);
		discardCard(randomPlayer.getColour());
	}

	//getter methods for the current and next turn
	@Override
	public Colour getActivePlayerColour() {
		return players.get(currentPlayerIndex).getColour();
	}
	@Override
	public Colour getNextPlayerColour() {
		return players.get((currentPlayerIndex + 1) % 4).getColour();
	}
	

	public void runGame(){
		Scanner sc = new Scanner(System.in);
		while(checkWin() == null){
			if(canPlayTurn()){
				if(currentPlayerIndex == 0){ //the player is the one to play
					ArrayList<Card> playerHand = players.get(0).getHand();
					System.out.print("You have: ");
					for(Card card : playerHand){
						System.out.print(card.getName() + " ");
					}
					System.out.println();
					while(true){
						try {
							System.out.print("Choose a card by typing its index: ");
							// some mechanism of the input like setOnMouseClicked
							Card inputCard = players.get(0).getHand().get(sc.nextInt());
							System.out.println();
							// all the cards outside the player hand must be unclickable
							selectCard(inputCard);
							break;
							//animations for selecting a card
						} catch (InvalidCardException | IndexOutOfBoundsException e) {
							System.out.println(e.getMessage());
						}
					}
					System.out.print("Do you want to play or Choose a marble first? (type 0 or 1): ");
					while(true){
						//some mechanism of hitting play like a button that can be hitten any time
						//it'll be in the loop for now just for playing on the terminal
						int wantToPlay = sc.nextInt();
						System.out.println();
						if(wantToPlay == 0){
							try {
								playPlayerTurn();
								
							} catch (GameException e) {
								System.out.println(e.getMessage());
							}
							break;
						}
						try {
							// some mechanism of choosing a marble from the actionable marbles
							Marble inputMarble = board.getActionableMarbles().get(sc.nextInt());
							// all the marbles that are neither on the track nor the safeZone must be unclickable
							selectMarble(inputMarble);
						} catch (InvalidMarbleException | IndexOutOfBoundsException e) {
							System.out.println(e.getMessage());
						}
						System.out.print("Do you want to play now or Choose another marble? (type 0 or 1): ");
					}
				}
				else{
					try {
						Player cpu = players.get(currentPlayerIndex);
						cpu.play();
						System.out.println("CPU " + currentPlayerIndex + " played: " + cpu.getSelectedCard().getName());
					} catch (GameException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			endPlayerTurn();
		}
	}
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Your Name: ");
		String playerName = sc.nextLine();
		try {
			Game game = new Game(playerName);
			System.out.println("Game started!");
			System.out.println("Welcome " + playerName);
			game.runGame();
		} catch (IOException e) {
			System.out.println("game loading failed");
		}
	}
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
}
