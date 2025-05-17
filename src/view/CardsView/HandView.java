package view.CardsView;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import model.Colour;
import model.card.Card;

public class HandView extends StackPane {
    private final HBox handBox;
    private final ArrayList<Card> hand;

    public HandView(ArrayList<Card> hand, Colour colour, boolean isPlayer) {
        this.hand = hand;
        handBox = new HBox(20);
        for (int i = 0; i < hand.size(); i++) {
        	Card card = hand.get(i);
            CardView cardView = new CardView(card, isPlayer, colour);
            handBox.getChildren().add(cardView);
        }
        handBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        getChildren().add(handBox);
    }

    public HBox getHandBox() {
        return handBox;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    //deselect all current selected card;
    public void clearSelection() {
        for (Node node : handBox.getChildren()) {
            CardView cardView = (CardView) node;
            cardView.updateSelectionAnimation(false);
        }
    }
    
    
    //adding and removing methods
    public void addCard(Card card, Colour colour, boolean isPlayer) {
        CardView cardView = new CardView(card, isPlayer, colour);
        handBox.getChildren().add(cardView);
    }

    public void removeCard(CardView cardView) {
        for(Node node : handBox.getChildren()){
        	CardView current = (CardView) node;
        	if(current == cardView){
        		handBox.getChildren().remove(current);
        	}
        }
    }
}
