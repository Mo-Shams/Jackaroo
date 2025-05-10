package view.CardView;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.card.Card;
import model.card.Deck;
import model.card.standard.Standard;
import engine.GameManager;
import engine.board.BoardManager;

public class CardView extends StackPane{
	private final Card card;
	private final ImageView imageView;
	
	public CardView(Card card){
		this.card = card;
		String imageName = card.getName();
		if(card instanceof Standard){
			Standard standard = (Standard)card;
			imageName += "_of_" + standard.getSuit().toString().toLowerCase();
		}
		imageName += ".png";
		System.out.println(imageName);
		Image image = new Image(getClass().getResourceAsStream("resources/" + imageName));
		imageView = new ImageView(image);
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(80);
		imageView.setFitHeight(120);
		getChildren().add(imageView);
	}

	public Card getCard() {
		return card;
	}
}
