package controller;

import javafx.scene.paint.Color;
import model.card.Card;
import view.CardsView.CardView;
import view.CardsView.HandView;
import engine.Game;
import exception.InvalidCardException;

public class GameController {
	private final Game game;
	public GameController(Game game){
		this.game = game;
	}
	public void addCardClickHandler(CardView cardView, HandView handView) {
		cardView.setOnMouseClicked(event -> {
			game.deselectAll();
			Card card = cardView.getCard();
			if(!cardView.isSelected()){
				try {
					game.selectCard(card);
					CardView otherSelected = handView.OtherselectedCard(cardView);
					if(otherSelected != null){
						otherSelected.setEffect(null);
						otherSelected.scaleCard(1.0);
						otherSelected.setSelected(false);
					}
					cardView.applyGlow(Color.DODGERBLUE);
					cardView.scaleCard(1.1);
	                cardView.setSelected(true);
				} catch (InvalidCardException e) {
					System.out.println(e.getMessage());
				}
			}
			else{
				cardView.setEffect(null);
				cardView.scaleCard(1.0);
				cardView.setSelected(false);
			}
		});
	}

	public void addCardHoverEffect(CardView cardView) {
		cardView.setPickOnBounds(false);
		cardView.setOnMouseEntered(event -> {
			if(!cardView.isSelected()){
				cardView.applyGlow(Color.YELLOW);
				cardView.scaleCard(1.1);
			}
		});
		cardView.setOnMouseExited(event -> {
			if(!cardView.isSelected()){
				cardView.setEffect(null);
				cardView.scaleCard(1.0);
			}
			else{
				cardView.applyGlow(Color.DODGERBLUE);
                cardView.scaleCard(1.1);
			}
		});
	}
}
