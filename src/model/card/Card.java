package model.card;
import java.util.ArrayList;

import model.Colour;
import model.card.wild.Wild;
import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

abstract public class Card {
	private final String name;
	private final String description;
	protected BoardManager boardManager;
	protected GameManager gameManager;
	
	public Card(String name, String description, BoardManager boardManager, GameManager gameManager) {
		this.name = name;
		this.description = description;
		this.boardManager = boardManager;
		this.gameManager = gameManager;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	public boolean validateMarbleSize(ArrayList<Marble> marbles) {
		if (this instanceof model.card.standard.Jack || this instanceof model.card.standard.Seven) {
			return (marbles.size() == 1 || marbles.size() == 2);
		} else if (this instanceof model.card.standard.King || this instanceof model.card.standard.Ten) {
			return (marbles.size() == 0 || marbles.size() == 1);
		} else return marbles.size() == 1;
	}
/*
	public boolean validateMarbleColours(ArrayList<Marble> marbles) {
		// for wild cards
		if (this instanceof Wild) {
			Colour player_colour = gameManager.getActivePlayerColour();
			Colour marbleColour = marbles.get(0).getColour();
			if (marbles.size() != 1) return false;
			if (this instanceof model.card.wild.Burner) return (!marbleColour.equals(player_colour)); // marble of diff colour
			else if (this instanceof model.card.wild.Saver) return (marbleColour.equals(player_colour)); // marble of same colour
		}
		// for standard ards
		else if (this instanceof model.card.standard.Standard) {
			// Jack
			Colour playerColour = gameManager.getActivePlayerColour();
			if (this instanceof model.card.standard.Jack) {
				// swap
				if (marbles.size() == 2) {
					Marble m1 = marbles.get(0); Marble m2 = marbles.get(1);
					// i considered both situations, first is mine second is not and first is not mine and second is
					return (m1.getColour().equals(playerColour) &&
							!m2.getColour().equals(playerColour)) || (!m1.getColour().equals(playerColour) &&
							m2.getColour().equals(playerColour));
				// no swap
				} else if (marbles.size() == 1) {
					// same colour as mine
					Colour marble_colour = marbles.get(0).getColour();
					return marble_colour.equals(playerColour);
				}
			}
			// Queen
			else if (this instanceof model.card.standard.Queen) {
				// Card Discard
				if (marbles.isEmpty()) {
					return true; // always true as no color is always true
				// Move
				} else if (marbles.size() == 1) {
					Colour marble_colour = marbles.get(0).getColour();
					return marble_colour.equals(playerColour);
				}
			}
			// Five
			else if (this instanceof model.card.standard.Five) {
				return true; // always true as any colour is fine
			}
			// Seven
			else if (this instanceof model.card.standard.Seven) {
				// split
				if (marbles.size() == 2) {
					Marble m1 = marbles.get(0);
					Marble m2 = marbles.get(1);
					return m1.getColour().equals(playerColour) && m2.getColour().equals(playerColour);
				// no split
				} else if (marbles.size() == 1) {
					Colour marble_colour = marbles.get(0).getColour();
					return marble_colour.equals(playerColour);
				}
			}
			// Any other Card must have same color as me
			else {
				if (marbles.size() != 1) {
					return false;
				} // must have one marble
				Colour marble_colour = marbles.get(0).getColour();
				return marble_colour.equals(playerColour);
			}
		}
		return false;
	}

*/
	public abstract void act (ArrayList<Marble> marbles) throws ActionException,
			InvalidMarbleException;

}
