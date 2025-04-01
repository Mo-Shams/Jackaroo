package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;

public class Saver extends Wild {

	public Saver(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super(name, description, boardManager, gameManager);
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		// Both checks must be valid to work
		if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
			throw new InvalidMarbleException("Saver Card needs one of my marble")
		}

		Marble m = marbles.get(0);
		boardManager.sendToSafe(m);
	}

}
