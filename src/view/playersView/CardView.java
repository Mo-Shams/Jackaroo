package view.playersView;

import java.util.HashMap;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.Colour;
import model.card.Card;
import model.card.standard.Standard;
import view.ImageCache;

public final class CardView extends StackPane {
	public static final Map<Card, CardView> cardToViewMap = new HashMap<>();
	
	
    private static final double SCALE_ANIMATION_DURATION_MS = 150;
    private static final double GLOW_WIDTH = 40;
    private static final double GLOW_HEIGHT = 40;
    private static final double HEIGHT = 140;
    private final Card card;
    private final ImageView imageView;
    private final ImageView backImageView;
    private boolean selected;
    private boolean flipped;
    
    public CardView(Card card, Colour colour) {
        this.card = card;
        this.selected = false;
        this.flipped = false;

        String imagePath = "/resources/card_images/" + generateImageName(card);
        imageView = new ImageView(ImageCache.getImage(imagePath));
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setFitHeight(HEIGHT);
        
        
        String backImagePath = "/resources/card_images/" + colour.name() + "_back.png";
        backImageView = new ImageView(ImageCache.getImage(backImagePath));
        backImageView.setPreserveRatio(true);
        backImageView.setSmooth(true);
        backImageView.setFitHeight(HEIGHT);
        
        
        imageView.setVisible(false);
        backImageView.setVisible(true);
        cardToViewMap.put(card, this);
        getChildren().addAll(imageView, backImageView);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
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

    public void setSelected(boolean select){
    	if(!select){
    		selected = false;
    		setEffect(null);
    		scaleCard(1);
    	}
    	else{
    		selected = true;
    		applyGlow(Color.DODGERBLUE);
    		scaleCard(1.1);
    	}
    }

    public void scaleCard(double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(SCALE_ANIMATION_DURATION_MS), this);
        st.setToX(scale);
        st.setToY(scale);
        st.setInterpolator(Interpolator.EASE_BOTH); // Smoother animation
        st.play();
    }

    private void applyGlow(Color color) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setWidth(GLOW_WIDTH);
        glow.setHeight(GLOW_HEIGHT);
        this.setEffect(glow);
    }
    
    public ParallelTransition sendToFirePit(FirePitView firePit, int playerIndex) {
    	setEffect(null);
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
        double maxRadius = 20.0;

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
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), this);
        tt.setByX(translateX);
        tt.setByY(translateY);
        tt.setInterpolator(Interpolator.EASE_BOTH);
        

        //Create RotateTransition for random rotation between -30 and +30 degrees
        RotateTransition rt = new RotateTransition(Duration.seconds(1), this);
        double randomAngle = (Math.random() * 120) - 60; // from -30 to +30 degrees
        
        rt.setAxis(Rotate.Z_AXIS);
        rt.setByAngle(randomAngle);
        rt.setInterpolator(Interpolator.EASE_BOTH);
        
        ParallelTransition pt = new ParallelTransition(tt, rt);
        // Play both transitions together
        
        pt.setOnFinished(e -> {
        	setMouseTransparent(true); 
        	firePit.addToFirePit(this, playerIndex, randomAngle);
        	
        });
        return pt ; 
      
    }
    
    public SequentialTransition sendToFirePitCpu(FirePitView firePit, int playerIndex){
    	SequentialTransition sq = (SequentialTransition)flip(300, true);
    	sq.setOnFinished(e ->{
    		sendToFirePit(firePit, playerIndex).play();
    	});
    	return sq ;
    	
    }
    
    
    
    public Animation flip(double duration, boolean fullRotation) {
    	RotateTransition rt1 = new RotateTransition(Duration.millis(duration), this);
        rt1.setAxis(Rotate.Y_AXIS); // Y-axis
        rt1.setFromAngle(0);
        rt1.setToAngle(90);
        rt1.setInterpolator(Interpolator.EASE_IN);

        RotateTransition rt2 = new RotateTransition(Duration.millis(duration), this);
        rt2.setAxis(Rotate.Y_AXIS);
        rt2.setFromAngle(-90);
        rt2.setToAngle(0);
        rt2.setInterpolator(Interpolator.EASE_OUT);

        
        flipped = !flipped;
        
        
        if(fullRotation){
        	rt1.setOnFinished(e -> {
                imageView.setVisible(flipped);
                backImageView.setVisible(!flipped);
            });
        	SequentialTransition st = new SequentialTransition(rt1, rt2);
        	return st;
        }
        else{rt1.setOnFinished(e -> {
            imageView.setVisible(flipped);
            backImageView.setVisible(!flipped);
            rt2.play();
        });
        	return rt1;
        }
    }
    
    public void addHoverEffect() {
        setPickOnBounds(false);

        setOnMouseEntered(event -> {
            if (!isSelected()) {
                applyGlow(Color.YELLOW);
                scaleCard(1.1);
            }
        });

        setOnMouseExited(event -> {
            if (!isSelected()) {
                setEffect(null);
                scaleCard(1.0);
            } 
        });
    }
    
    
    //----------------------------------------- Getters ------------------------------------------    
    
    
    public boolean isSelected() {
        return selected;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Card getCard() {
        return card;
    }
}