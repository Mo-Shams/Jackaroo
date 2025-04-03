package model.card.wild;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Saver extends Wild {

	public Saver(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super(name, description, boardManager, gameManager);
	}
	public boolean validateMarbleSize (ArrayList<Marble> marbles) {
		return marbles.size() == 1;
	}

	public boolean validateMarbleColours (ArrayList<Marble> marbles) {
		if (marbles.size() == 1) {
			Colour playerColour = gameManager.getActivePlayerColour();
			Colour marbleColour = marbles.get(0).getColour();
			return (marbleColour == player_colour); // marble of same colour
		}
		return false;
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		// Both checks must be valid to work
		if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
			throw new InvalidMarbleException("Saver Card needs one of my marble");
		}
		if (marbles.size() == 1) {
			Marble m = marbles.get(0);
			boardManager.sendToSafe(m);
		} else {
			throw new InvalidMarbleException("Invalid entry");
		}
	}

}
