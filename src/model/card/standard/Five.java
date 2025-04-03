package model.card.standard;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Five extends Standard {

	public Five(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 5, suit, boardManager, gameManager);
		
	}

	public boolean validateMarbleSize (ArrayList<Marble> marbles) {
		return (marbles.size() == 1);
	}

	public boolean validateMarbleColours (ArrayList<Marble> marbles) {
		return true;
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		// check for validity
		if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
			throw new InvalidMarbleException("Five needs one marble of any player");
		}

		if (marbles.size() == 1) {
			Marble m = marbles.get(0);
			boardManager.moveBy(m, 5, false);
		} else {
			throw new InvalidMarbleException("Invalid entry");
		}
	}

}
