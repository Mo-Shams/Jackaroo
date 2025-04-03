package model.card.wild;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Burner extends Wild {

	public Burner(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super(name, description, boardManager, gameManager);
	}

	public boolean validateMarbleSize (ArrayList<Marble> marbles) {
		return marbles.size() == 1;
	}

	public boolean validateMarbleColours (ArrayList<Marble> marbles) {
		if (marbles.size() == 1) {
			Colour playerColour = gameManager.getActivePlayerColour();
			Colour marbleColour = marbles.get(0).getColour();
			return (marbleColour != playerColour); // marble of diff colour
		}
		return false;
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		// Both checks must be valid to work
		if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
			throw new InvalidMarbleException("Burner Card needs one of opponent marble");
		}
		if (marbles.size() == 1) {
			Marble m = marbles.get(0);
			boardManager.destroyMarble(m);
		} else {
			throw new InvalidMarbleException("Invalid entry");
		}
	}
}
