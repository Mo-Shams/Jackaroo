package model.card.standard;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalSwapException;
import exception.InvalidMarbleException;

public class Jack extends Standard{
	
	public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 11, suit, boardManager, gameManager);
	}

	public boolean validateMarbleSize (ArrayList<Marble> marbles) {
		return (marbles.size() == 1 || marbles.size() == 2);
	}

	public boolean validateMarbleColours (ArrayList<Marble> marbles) {

		Colour playerColour = gameManager.getActivePlayerColour();
		if (marbles.size() == 2) {
			Marble m1 = marbles.get(0); Marble m2 = marbles.get(1);
			// i considered both situations, first is mine second is not and first is not mine and second is
			boolean Mine_first = ((m1.getColour() == playerColour) && !(m2.getColour() == playerColour));
			boolean Mine_second = ((m1.getColour() != playerColour) && (m2.getColour() == playerColour));
			return (Mine_first || Mine_second);
			// no swap
		} else if (marbles.size() == 1) {
			// same colour as mine
			Colour marble_colour = marbles.get(0).getColour();
			return marble_colour == playerColour;
		}
		return false;
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		// check for validity
		if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
			throw new InvalidMarbleException("Jack needs two marbles, yours and opponents");
		}

		Colour playerColour = gameManager.getActivePlayerColour();
		if (marbles.size() == 2) {
			Marble m1 = marbles.get(0); Marble m2 = marbles.get(1);
			
				if (m1.getColour() == playerColour) {
					boardManager.swap(m1, m2);
				} else {
					boardManager.swap(m2, m1);
				}
			
		} else if (marbles.size() == 1) {
			boardManager.moveBy(marbles.get(0), 11, false);
		} else {
			throw new InvalidMarbleException("Invalid entry");
		}
	}
}
