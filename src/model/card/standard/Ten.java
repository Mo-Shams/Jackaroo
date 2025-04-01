package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

public class Ten extends Standard {

	public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 10, suit, boardManager, gameManager);

	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		// no marbles
		if (marbles.isEmpty()) {
			if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
				throw new InvalidMarbleException("Ten needs zero marble")
			}
			Colour nextPlayerColour = gameManger.getNextPlayerColour();
			gameManager.discardCard(nextPlayerColour);
		// one marble
		} else if (marbles.size() == 1) {
			if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
				throw new InvalidMarbleException("Ten needs one of my marble")
			}
			Marble m = marbles.get(0);
			boardManager.moveBy(m, 10, false);
		} else {
			throw new InvalidMarbleException("Invalid Entry");
		}
	}

}
