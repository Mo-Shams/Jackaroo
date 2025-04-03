package model.card.standard;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import exception.SplitOutOfRangeException;

public class Seven extends Standard {

	public Seven(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 7, suit, boardManager, gameManager);
	}

	public boolean validateMarbleSize (ArrayList<Marble> marbles) {
		return (marbles.size() == 1 || marbles.size() == 2);
	}

	public boolean validateMarbleColours (ArrayList<Marble> marbles) {
		Colour playerColour = gameManager.getActivePlayerColour();
		if (marbles.size() == 2) {
			Marble m1 = marbles.get(0);
			Marble m2 = marbles.get(1);
			return (m1.getColour()== playerColour) && (m2.getColour() == playerColour);
			// no split
		} else if (marbles.size() == 1) {
			Colour marble_colour = marbles.get(0).getColour();
			return marble_colour == playerColour;
		}
		return false;
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
			throw new InvalidMarbleException("Seven needs one of my marble");
		}
		// one marble
		if (marbles.size() == 1) {
			Marble m = marbles.get(0);
			boardManager.moveBy(m, 7, false);
		} else if (marbles.size() == 2) {
			Marble m1 = marbles.get(0); Marble m2 = marbles.get(1);
			int splitDistance = boardManager.getSplitDistance();
			boardManager.moveBy(m1, splitDistance, false);
			boardManager.moveBy(m2, 7-splitDistance, false);
		}  else {
			throw new InvalidMarbleException("Invalid entry");
		}
	}

}
