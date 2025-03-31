package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
import model.player.Marble;
import model.player.Player;
import engine.board.Board;
import exception.CannotDiscardException;
import exception.CannotFieldException;
import exception.IllegalDestroyException;


public class Game implements GameManager {

	private final Board board;
	private final ArrayList<Player> players;
	private final ArrayList<Card> firePit;
	private int currentPlayerIndex; 
	private int turn;

    public Game(String playerName) throws IOException {
    	ArrayList <Colour> colourOrder = new ArrayList<>(); 
    	colourOrder.add(Colour.RED); colourOrder.add(Colour.GREEN); colourOrder.add(Colour.BLUE); colourOrder.add(Colour.YELLOW);
        Collections.shuffle(colourOrder);
        
        this.board = new Board(colourOrder, this); 
        Deck.loadCardPool(board, this);
        
        this.players = new ArrayList<>(); 
        players.add(new Player(playerName, colourOrder.get(0)));
        
        for (int i = 1; i < 4; i++) {
        	players.add(new CPU("CPU " + i, colourOrder.get(i), board));
        }
        
        for (Player player : players) {
        	ArrayList<Card> player_cards = Deck.drawCards();
        	player.getHand().addAll(player_cards); 
        	
        }
        
        this.currentPlayerIndex = 0;
        this.turn = 0;
        this.firePit = new ArrayList<>();
    }

	public Board getBoard() {
		return board;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Card> getFirePit() {
		return firePit;
	}

	@Override
	public void sendHome(Marble marble) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fieldMarble() throws CannotFieldException,
			IllegalDestroyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discardCard(Colour colour) throws CannotDiscardException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discardCard() throws CannotDiscardException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Colour getActivePlayerColour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Colour getNextPlayerColour() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
