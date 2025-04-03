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

	public boolean validateMarbleColours (ArrayList<Marble> marbles) {
		if(marbles.isEmpty())
			return false;
		return marbles.get(0).getColour() != gameManager.getActivePlayerColour();
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		boardManager.destroyMarble(marbles.get(0));
	}
}
