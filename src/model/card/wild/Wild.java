package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;

abstract public class Wild extends Card {

	public Wild(String name, String description, BoardManager boardManager, GameManager gameManager) {
		super(name, description, boardManager, gameManager);
	}

}
