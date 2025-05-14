package model.card;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

abstract public class Card {
	//instance variables
	private final String name;
	private final String description;
	protected BoardManager boardManager;
	protected GameManager gameManager;
	
	//constructor
	public Card(String name, String description, BoardManager boardManager, GameManager gameManager) {
		this.name = name;
		this.description = description;
		this.boardManager = boardManager;
		this.gameManager = gameManager;
	}
	
	
	
	
	
	
	
	//simple getter methods
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	
	
	
	
	
	
	
	public boolean validateMarbleSize(ArrayList<Marble> marbles) {
		return marbles.size() == 1;
	}

	public boolean validateMarbleColours(ArrayList<Marble> marbles) {
		boolean flag = true;
		for(Marble marble : marbles)
			flag &= (marble.getColour() == gameManager.getActivePlayerColour());
		return flag;
	}


	public abstract void act (ArrayList<Marble> marbles) throws ActionException,
			InvalidMarbleException;
	
	public String toString(){
		return description;
	}
}
