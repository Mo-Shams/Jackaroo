package view.playersView;

import java.util.ArrayList;

import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import model.Colour;
import model.card.Card;

public class HandView extends HBox {
    private ArrayList<Card> hand;
    private ArrayList<CardView> cardViews;
    private final Colour colour;
    private final boolean player;
    
    public HandView (ArrayList<Card> hand, Colour colour, boolean isPlayer) {
    	super(20);
    	this.colour = colour;
    	player = isPlayer;
        //drawCardViews(hand);
        this.setMaxSize(460.0, 140.0);
        this.setAlignment(Pos.CENTER);
        //this.setStyle("-fx-background-color: yellow;");
    }
    
    public void drawCardViews(ArrayList<Card> hand){
    	this.getChildren().clear();
    	this.hand = hand;
        cardViews = new ArrayList<>();
    	for (int i = 0; i < hand.size(); i++) {
        	Card card = hand.get(i);
            CardView cardView = new CardView(card, colour);
            cardViews.add(cardView);
            this.getChildren().add(cardView);
            
        }
    	if(player){
    		SequentialTransition st = new SequentialTransition();
    		for(CardView cardView : cardViews){
    			cardView.addHoverEffect();
    			Tooltip.install(cardView, new Tooltip(cardView.getCard().toString()));
    			st.getChildren().add((RotateTransition)cardView.flip(750, false));
    		}
    		st.play();
    	}
    }

    //deselect all current selected card;
    public void clearSelection() {
        for(CardView cardView : cardViews){
        	cardView.setSelected(false);
        }
    }
    
    public void removeCard(CardView cardView) {
        cardViews.remove(cardView);
        this.getChildren().remove(cardView);
    }
    
    public CardView getSelectedCardView(){
    	for(CardView cardView: cardViews)
    		if(cardView.isSelected())
    			return cardView;
    	
    	return null;
    }
    
    //---------------------------------------- Getters ------------------------------------
    
    public ArrayList<Card> getHand() {
        return hand;
    }
	public ArrayList<CardView> getCardViews() {
		return cardViews;
	}

}