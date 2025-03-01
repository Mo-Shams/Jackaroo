package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

class Ten extends Standard {

	public Ten(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 10, suit, boardManager, gameManager);

	}

}
