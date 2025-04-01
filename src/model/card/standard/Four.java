package model.card.standard;
import engine.GameManager;
import engine.board.BoardManager;

public class Four extends Standard{
	
	public Four(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 4, suit, boardManager, gameManager);
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		// check for validity
		if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
			throw new InvalidMarbleException("Four needs one of my marbles")
		}

		Marble m = marbles.get(0);
		ArrayList<Marble> ActionMarbles = boardManager.getActionMarbles();
		if (!ActionMarbles.contain(m)) {
			throw new InvalidMarbleException ("Attempting to move a marble not on the board")
		} else {
			boardManager.moveBy(m, -4, false);
		}
	}

}
