package view.CardsView;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import model.Colour;
import model.card.Card;

public class HandView extends StackPane {
    private final HBox handView;
    private final ArrayList<Card> hand;

    public HandView(ArrayList<Card> hand, Colour colour, boolean showFrontInitially) {
        this.hand = hand;
        handView = new HBox(20);
        for (int i = 0; i < hand.size(); i++) {
        	Card card = hand.get(i);
            CardView cardView = new CardView(card, showFrontInitially, colour);
            handView.getChildren().add(cardView);
        }
        handView.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        getChildren().add(handView);
    }

    public HBox getHandView() {
        return handView;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public CardView OtherselectedCard(CardView cardView) {
        for (Node node : handView.getChildren()) {
            CardView current = (CardView) node;
            if (current != cardView && current.isSelected()) {
                return current;
            }
        }
        return null;
    }

    public void clearSelection() {
        for (Node node : handView.getChildren()) {
            CardView cardView = (CardView) node;
            cardView.setEffect(null);
            cardView.scaleCard(1.0);
            cardView.setSelected(false);
        }
    }

    public void addCard(Card card, Colour colour, boolean showFrontInitially) {
        CardView cardView = new CardView(card, showFrontInitially, colour);
        hand.add(card);
        handView.getChildren().add(cardView);
    }

    public void removeCard(Card card) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).equals(card)) {
                hand.remove(i);
                handView.getChildren().remove(i);
                break;
            }
        }
    }
}
