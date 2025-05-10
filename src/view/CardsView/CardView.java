package view.CardsView;

import javafx.animation.ScaleTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.card.Card;
import model.card.standard.Standard;

public class CardView extends StackPane{
	private final Card card;
	private final ImageView imageView;
	private boolean selected;
	public CardView(Card card){
		selected = false;
		this.card = card;
		String imageName = card.getName();
		if(card instanceof Standard){
			Standard standard = (Standard)card;
			imageName += "_of_" + standard.getSuit().toString().toLowerCase();
		}
		imageName += ".png";
		
		
		//System.out.println(imageName);
		Image image = new Image(getClass().getResourceAsStream("/resources/card_images/" + imageName));
		imageView = new ImageView(image);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);
		imageView.setFitHeight(120);
		
		getChildren().add(imageView);
		
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public Card getCard() {
		return card;
	}
	public void scaleCard(double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), this);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }
	
	public void applyGlow(Color color) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setWidth(40);
        glow.setHeight(40);
        this.setEffect(glow);
    }
}
