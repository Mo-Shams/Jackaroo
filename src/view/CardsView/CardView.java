package view.CardsView;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.card.Card;
import model.card.standard.Standard;

import java.io.InputStream;

public final class CardView extends StackPane {
    private static final double SCALE_ANIMATION_DURATION_MS = 150;
    private static final double GLOW_WIDTH = 40;
    private static final double GLOW_HEIGHT = 40;

    private final Card card;
    private final ImageView imageView;
    private boolean selected;

    public CardView(Card card) {
        this.card = card;
        this.selected = false;

        String imageName = generateImageName(card);
        InputStream imageStream = getClass().getResourceAsStream("/resources/card_images/" + imageName);

        if (imageStream == null) {
            imageStream = getClass().getResourceAsStream("/resources/card_images/default.png");
            if (imageStream == null) {
                throw new IllegalArgumentException("Card image not found and no fallback image available: " + imageName);
            }
        }

        Image image = new Image(imageStream);
        imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        getChildren().add(imageView);
        Tooltip.install(this, new Tooltip(card.toString()));
    }

    private String generateImageName(Card card) {
        StringBuilder imageName = new StringBuilder(card.getName());
        if (card instanceof Standard) {
        	Standard standard = (Standard) card;
            imageName.append("_of_").append(standard.getSuit().toString().toLowerCase());
        }
        imageName.append(".png");
        return imageName.toString();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateSelectionVisuals();
    }

    private void updateSelectionVisuals() {
        if (!selected) {
            this.setEffect(null);
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Card getCard() {
        return card;
    }

    public void scaleCard(double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(SCALE_ANIMATION_DURATION_MS), this);
        st.setToX(scale);
        st.setToY(scale);
        st.setInterpolator(Interpolator.EASE_BOTH); // Smoother animation
        st.play();
    }

    public void applyGlow(Color color) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setWidth(GLOW_WIDTH);
        glow.setHeight(GLOW_HEIGHT);
        this.setEffect(glow);
    }

    @Override
    public String toString() {
        return "CardView{" +
                "card=" + card +
                ", selected=" + selected +
                '}';
    }
}
