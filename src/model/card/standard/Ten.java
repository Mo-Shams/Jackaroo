package model.card.standard;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Ten extends Standard {

	public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 10, suit, boardManager, gameManager);

	}

	public boolean validateMarbleSize (ArrayList<Marble> marbles) {
		return super.validateMarbleSize(marbles) || marbles.isEmpty();
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		if(marbles.size() == 1)
			super.act(marbles);
		else
			gameManager.discardCard(gameManager.getNextPlayerColour());
	}

}
