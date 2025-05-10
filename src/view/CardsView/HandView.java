package view.CardsView;

import java.util.ArrayList;

import model.card.Card;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class HandView {
	private final HBox handView;
	private final ArrayList<Card> hand;
	
	public HandView (ArrayList<Card> hand){
		this.hand = hand;
		handView = new HBox(4);
		handView.setSpacing(20);
		handView.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		for (Card card : hand) {
			CardView cardView = new CardView(card);
			handView.getChildren().add(cardView);
		}
	}

	public HBox getHandView() {
		return handView;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}
	public CardView OtherselectedCard(CardView cardView){
		CardView otherSelected = null;
		for(Node node : handView.getChildren()){
			CardView current = (CardView) node;
			if(current != cardView && current.isSelected()){
				otherSelected = current;
				break;
			}
		}
		return otherSelected;
	}
}
