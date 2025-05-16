package view.CardsView;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
    
    public void sendToFirePit(FirePitView firePit) {
        // Step 1: Get center of FirePit in scene coordinates
        Bounds firePitBounds = firePit.localToScene(firePit.getBoundsInLocal());
        double firePitCenterX = firePitBounds.getMinX() + firePitBounds.getWidth() / 2;
        double firePitCenterY = firePitBounds.getMinY() + firePitBounds.getHeight() / 2;

        // Step 2: Convert FirePit scene coordinates to this card’s parent coordinates
        Point2D firePitInParent = this.getParent().sceneToLocal(firePitCenterX, firePitCenterY);

        // Step 3: Get the current center of the card in its parent's coordinates
        Bounds cardBounds = this.getBoundsInParent();
        double cardCenterX = cardBounds.getMinX() + cardBounds.getWidth() / 2;
        double cardCenterY = cardBounds.getMinY() + cardBounds.getHeight() / 2;

        // Define max radius (in pixels) for random offset around FirePit center
        double maxRadius = 30.0;

        // Generate random angle between 0 and 2*PI
        double angle = Math.random() * 2 * Math.PI;

        // Generate random radius between 0 and maxRadius
        double radius = Math.random() * maxRadius;

        // Calculate offset X and Y using polar coordinates
        double randomOffsetX = Math.cos(angle) * radius;
        double randomOffsetY = Math.sin(angle) * radius;

        // Calculate translation offsets relative to card center
        double translateX = firePitInParent.getX() + randomOffsetX - cardCenterX;
        double translateY = firePitInParent.getY() + randomOffsetY - cardCenterY;

        // Create TranslateTransition to move card to random position around FirePit center
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), this);
        tt.setByX(translateX);
        tt.setByY(translateY);
        tt.setInterpolator(Interpolator.EASE_BOTH);

        // Create RotateTransition for random rotation between -30 and +30 degrees
        RotateTransition rt = new RotateTransition(Duration.seconds(0.5), this);
        double randomAngle = (Math.random() * 60) - 30; // from -30 to +30 degrees
        rt.setByAngle(randomAngle);
        rt.setInterpolator(Interpolator.EASE_BOTH);

        // Play both transitions together
        ParallelTransition pt = new ParallelTransition(tt, rt);
        pt.play();

        pt.setOnFinished(e -> {
            setMouseTransparent(true);
        });
    }


    
    



    @Override
    public String toString() {
        return "CardView{" +
                "card=" + card +
                ", selected=" + selected +
                '}';
    }
}
