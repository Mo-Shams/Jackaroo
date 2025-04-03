package model.card.wild;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Burner extends Wild {

	public Burner(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super(name, description, boardManager, gameManager);
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		// Both checks must be valid to work
		if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
			throw new InvalidMarbleException("Burner Card needs one opponent marble");
		}

		Marble m = marbles.get(0);
		boardManager.destroyMarble(m);
	}
}
