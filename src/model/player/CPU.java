package model.player;

import model.Colour;
import engine.board.BoardManager;

public class CPU extends Player {

	private final BoardManager boardManager;
	
	CPU(String name, Colour colour, BoardManager boardManager){
		super(name,colour);
		this.boardManager = boardManager;
	}
}
