package view.CardsView;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.Colour;
import model.card.Card;
import model.card.standard.Standard;
import view.ImageCache;

public final class CardView extends StackPane {
    private static final double SCALE_ANIMATION_DURATION_MS = 150;
    private static final double FLIPPING_ANIMATION_DURATION_MS = 200;
    private static final double GLOW_WIDTH = 40;
    private static final double GLOW_HEIGHT = 40;

    private final Card card;
    private final ImageView imageView;
    private boolean selected;
    private final ImageView backImageView;
    private boolean flipped;

    
    public CardView(Card card, boolean showFrontInitially, Colour colour) {
        this.card = card;
        this.selected = false;
        this.flipped = !showFrontInitially;

        String imagePath = "/resources/card_images/" + generateImageName(card);
        imageView = new ImageView(ImageCache.getImage(imagePath));
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setFitHeight(140);
        
        
        String backImagePath = "/resources/card_images/" + colour.name() + "_back.png";
        backImageView = new ImageView(ImageCache.getImage(backImagePath));
        backImageView.setPreserveRatio(true);
        backImageView.setSmooth(true);
        backImageView.setFitHeight(140);
        if (showFrontInitially) {
            imageView.setVisible(true);
            backImageView.setVisible(false);
        } else {
            imageView.setVisible(false);
            backImageView.setVisible(true);
        }

        getChildren().addAll(imageView, backImageView);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
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
    
    public void sendToFirePit(FirePitView firePit, HandView handView) {
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
    	   //  handView.removeCard(this.getCard()); 
    	    firePit.addToFirePit(this);
        
        });
    }
    
    public void flip() {
        if (flipped) {
            flipToFront();
        } else {
            flipToBack();
        }
    }

    private void flipToBack() {
        RotateTransition rt1 = new RotateTransition(Duration.millis(FLIPPING_ANIMATION_DURATION_MS), this);
        rt1.setAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0)); // Y-axis
        rt1.setFromAngle(0);
        rt1.setToAngle(90);
        rt1.setInterpolator(Interpolator.EASE_IN);

        RotateTransition rt2 = new RotateTransition(Duration.millis(FLIPPING_ANIMATION_DURATION_MS), this);
        rt2.setAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0));
        rt2.setFromAngle(-90);
        rt2.setToAngle(0);
        rt2.setInterpolator(Interpolator.EASE_OUT);

        rt1.setOnFinished(e -> {
            imageView.setVisible(false);
            backImageView.setVisible(true);
            rt2.play();
        });

        rt1.play();
        flipped = true;
    }

    private void flipToFront() {
        RotateTransition rt1 = new RotateTransition(Duration.millis(FLIPPING_ANIMATION_DURATION_MS), this);
        rt1.setAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0));
        rt1.setFromAngle(0);
        rt1.setToAngle(90);
        rt1.setInterpolator(Interpolator.EASE_IN);

        RotateTransition rt2 = new RotateTransition(Duration.millis(FLIPPING_ANIMATION_DURATION_MS), this);
        rt2.setAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0));
        rt2.setFromAngle(-90);
        rt2.setToAngle(0);
        rt2.setInterpolator(Interpolator.EASE_OUT);

        rt1.setOnFinished(e -> {
            backImageView.setVisible(false);
            imageView.setVisible(true);
            rt2.play();
        });

        rt1.play();
        flipped = false;
    }



    
    



    @Override
    public String toString() {
        return "CardView{" +
                "card=" + card +
                ", selected=" + selected +
                '}';
    }
}
