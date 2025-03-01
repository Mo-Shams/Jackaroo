package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;

class Five extends Standard {

	public Five(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description, 5, suit, boardManager, gameManager);
		
	}

}
