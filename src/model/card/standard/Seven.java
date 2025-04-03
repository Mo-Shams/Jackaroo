package model.card.standard;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Seven extends Standard {

	public Seven(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 7, suit, boardManager, gameManager);
	}

	public boolean validateMarbleSize (ArrayList<Marble> marbles) {
		return super.validateMarbleColours(marbles) || marbles.size() == 2;
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		if(marbles.size() == 1)
			super.act(marbles);
		
		else if (marbles.size() == 2) {
			Marble m1 = marbles.get(0); Marble m2 = marbles.get(1);
			int splitDistance = boardManager.getSplitDistance();
			boardManager.moveBy(m1, splitDistance, false);
			boardManager.moveBy(m2, 7-splitDistance, false);
		}
	}

}
