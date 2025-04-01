package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

public class King extends Standard{

	public King(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 13, suit, boardManager, gameManager);
    }

    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        // check for validity
        if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("King needs one of my marble")
        }

        Marble m = marbles.get(0);
        // i will get a list of all action marbles and check if my marble is there, if it isn't then this means it's in my home
        ArrayList<Marble> ActionMarbles = boardManager.getActionMarbles();
        if (!ActionMarbles.contain(m)) {
            boardManager.sendToBase(m);
        } else {
            boardManager.moveBy(m, 13, true);
        }


    }
}
