package model.card.standard;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Jack extends Standard{
	
	public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 11, suit, boardManager, gameManager);
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		// check for validity
		if (marbles.size() == 2) {
			if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
				throw new InvalidMarbleException("Jack needs two marbles, yours and opponents");
			}
			Marble m1 = marbles.get(0); Marble m2 = marbles.get(1);
			if (m1.getColour().equals(gameManager.getActivePlayerColour())) boardManager.swap(m1, m2);
			else boardManager.swap(m2, m1);
		} else if (marbles.size() == 1) {
			if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
				throw new InvalidMarbleException("Jack movement requires one of your marble");
			}
			boardManager.moveBy(marbles.get(0), 11, false);
		} else {
			throw new InvalidMarbleException("Invalid entry");
		}
	}
}
