package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

class Seven extends Standard {

	public Seven(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 7, suit, boardManager, gameManager);
	}

}
