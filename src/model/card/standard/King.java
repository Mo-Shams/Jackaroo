package model.card.standard;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class King extends Standard{

	public King(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 13, suit, boardManager, gameManager);
    }

    public boolean validateMarbleSize (ArrayList<Marble> marbles) {
        return super.validateMarbleSize(marbles) || marbles.isEmpty();
    }

    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
    	if(marbles.size() == 1)
    		boardManager.moveBy(marbles.get(0), getRank(), true);
    	else
    		gameManager.fieldMarble();
    }
}
