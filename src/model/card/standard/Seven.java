package model.card.standard;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import exception.SplitOutOfRangeException;

public class Seven extends Standard {

	public Seven(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 7, suit, boardManager, gameManager);
	}

	public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
		// one marble
		if (marbles.size() == 1) {
			if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
				throw new InvalidMarbleException("Seven needs one of my marble");
			}
			Marble m = marbles.get(0);
			boardManager.moveBy(m, 7, false);
		} else if (marbles.size() == 2) {
			Marble m1 = marbles.get(0); Marble m2 = marbles.get(1);
			int splitDistance;
			try {
				/*
				I have no idea yet how to deal with this so i can get the player split distance so i am leaving it empty for now
				 */
			} catch (SplitOutOfRangeException e) {
				throw new InvalidMarbleException ("Invalid Split Distance, Should be 1-6 and not " + e.getMessage());
			}
			boardManager.moveBy(m1, splitDistance, false);
			boardManager.moveBy(m2, 7-splitDistance, false);
		} else {
			throw new InvalidMarbleException("Invalid entry");
		}
	}

}
