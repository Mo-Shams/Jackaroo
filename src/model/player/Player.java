package model.player;

import java.util.ArrayList;

import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import model.Colour;
import model.card.Card;

public class Player {
	//instance variables
	private final String name;
	private final Colour colour;
	private ArrayList<Card> hand;
	private final ArrayList<Marble> marbles;
	private Card selectedCard;
	private final ArrayList<Marble> selectedMarbles;

	//constructor
	public  Player(String name, Colour colour){
		this.name = name;
		this.colour = colour;
		this.hand = new ArrayList<Card>();
		this.selectedMarbles = new ArrayList<Marble>();
		this.marbles = new ArrayList<Marble>();
		for(int i=0; i<4; i++) this.marbles.add(new Marble(colour));
		this.selectedCard = null;
	}


	//simple getter and setter methods for the instance variables
	public ArrayList<Card> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public String getName() {
		return name;
	}

	public Colour getColour() {
		return colour;
	}

	public ArrayList<Marble> getMarbles() {
		return marbles;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}

	
	
	
	
	
	//method to add the specified marble to the player's home zone
	public void regainMarble(Marble marble){
		marbles.add(marble);
	}
	
	
	
	
	
	
	//getter method for the marbles in the home zone if any
	public Marble getOneMarble(){
		if(marbles.isEmpty())
			return null;
		return marbles.get(0);
	}
	
	
	
	
	
	
	
	//methods that validates and selects the given card and marble
	public void selectCard(Card card) throws InvalidCardException {
		for(Card currentCard : hand){
			if(currentCard.equals(card)){
				selectedCard = currentCard;
				return;
			}
		}
		throw new InvalidCardException("You don't have this card");
	}
	public void selectMarble(Marble marble) throws InvalidMarbleException{
		if(selectedMarbles.size() >= 2)
			throw new InvalidMarbleException("You can't choose more than 2 marbles");
		selectedMarbles.add(marble);
	}
	
	
	
	
	
	
	
	//method that resets the player choices, both card and marble
	public void deselectAll(){
		selectedCard = null;
		selectedMarbles.clear();
	}
	
	
	
	
	
	
	
	//method that executes the selected card action on the selected marbles if valid
	public void play() throws GameException{
		if(selectedCard == null) throw new InvalidCardException("You didn't choose a card yet");
		/*if(!selectedCard.validateMarbleColours(selectedMarbles) || !selectedCard.validateMarbleSize(selectedMarbles))
			throw new InvalidMarbleException("You cannot use the " + selectedCard.getName() + " card on the selected marble/s");*/
		//will all the cards give the same exception?
		selectedCard.act(selectedMarbles);
	}
	
}
