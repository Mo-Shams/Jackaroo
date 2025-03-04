package engine;

import engine.board.*;
import model.player.*;
import model.card.*;
import model.Colour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


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
	
}
