package model.card.standard;
import java.util.ArrayList;

import model.card.Card;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Standard extends Card {
	private final int rank;
	private final Suit suit;

	public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, boardManager, gameManager);
		this.rank = rank;
		this.suit = suit;
	}

	public int getRank() {
		return rank;
	}

	public Suit getSuit() {
		return suit;
	}

	@Override
	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		boardManager.moveBy(marbles.get(0), rank, false);
	}

	@Override
	public String toString() {
		return super.toString() + " \nRank : " + rank + ", Suit : " + suit;
	}

}
