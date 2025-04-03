package model.card.standard;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Queen extends Standard{

	public Queen(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 12, suit, boardManager, gameManager);
    }

    public boolean validateMarbleSize (ArrayList<Marble> marbles) {
        return (marbles.size() == 1);
    }

    public boolean validateMarbleColours (ArrayList<Marble> marbles) {
        // Card Discard
        if (marbles.isEmpty()) {
            return true; // always true as no color is always true
            // Move
        } else if (marbles.size() == 1) {
            Colour playerColour = gameManager.getActivePlayerColour();
            Colour marble_colour = marbles.get(0).getColour();
            return marble_colour == playerColour;
        }
        return false;
    }

    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        // check for validity
        if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Queen needs one of my marble");
        }

        if (marbles.isEmpty()) gameManager.discardCard(); // discard random card from gameManager "Still to be implemented"
        else if (marbles.size() == 1) {
            Marble m = marbles.get(0);
            boardManager.moveBy(m, 12, false);
        }
    }

}
