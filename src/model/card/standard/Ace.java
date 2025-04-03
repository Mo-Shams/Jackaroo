package model.card.standard;

import java.util.ArrayList;

import model.player.Marble;
import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;

public class Ace extends Standard {

    public Ace(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 1, suit, boardManager, gameManager);
    }

    public boolean validateMarbleSize (ArrayList<Marble> marbles) {
        return (marbles.size() == 1);
    }

    public boolean validateMarbleColours (ArrayList<Marble> marbles) {
        if (marbles.size() == 1) {
            Colour playerColour = gameManager.getActivePlayerColour();
            Colour marbleColour = marbles.get(0).getColour();
            return (marbleColour == player_colour); // marble of same colour
        }
        return false;
    }

    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        // check for validity
        if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Ace Card needs one of my marble");
        }

        if (marbles.size() == 1) {
            Marble m = marbles.get(0);
            // i will get a list of all action marbles and check if my marble is there, if it isn't then this means it's in my home
            ArrayList<Marble> ActionMarbles = boardManager.getActionableMarbles();
            if (!ActionMarbles.contains(m)) {
                boardManager.sendToBase(m);
            } else {
                boardManager.moveBy(m, 1, false);
            }
        } else {
            throw new InvalidMarbleException("Invalid entry");
        }
    }
}