package model.card.standard;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Ten extends Standard {

	public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 10, suit, boardManager, gameManager);

	}

	public boolean validateMarbleSize (ArrayList<Marble> marbles) {
		// case of dicard card or movement
		return (marbles.size() == 1 || marbles.size() == 0);
	}

	public boolean validateMarbleColours (ArrayList<Marble> marbles) {
		if (marbles.size() == 1) {
			Colour playerColour = gameManager.getActivePlayerColour();
			Colour marbleColour = marbles.get(0).getColour();
			return (marbleColour == player_colour); // marble of same colour
		} else if (marbles.size() == 0) return true;
		return false;
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
			throw new InvalidMarbleException("Ten needs zero marble");
		}
		if (marbles.size() == 1) {
			Marble m = marbles.get(0);
			boardManager.moveBy(m, 10, false);
		} else if (marbles.size() == 0) {
			int nextPlayerIndex = gameManager.getNextPlayerColour();
			gameManager.discardCard(nextPlayerIndex);
		} else {
			throw new InvalidMarbleException("Invalid Entry");
		}
	}

}
