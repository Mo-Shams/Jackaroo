package view.playersView;

import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.card.Card;

public class FirePitView extends Pane {
	private final ArrayList<Card> firePit;
	private final Circle circle;

	public FirePitView(ArrayList<Card> firePit, double width, double height){
		super();
		this.firePit = firePit;
		circle = createGlowingCircle(150, Color.DEEPSKYBLUE);
		circle.setLayoutX((width-1620)/2);
		circle.setLayoutY((height-720)/2);
		getChildren().add(circle);  // order matters! circle first, cards on top
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	}
	
	public void addCardToFirePit(CardView cardView, double x, double y, double rotation) {
        this.getChildren().add(cardView);
        // Set the card position relative to this (coordinates inside firepit)
        cardView.setLayoutX(x);
        cardView.setLayoutY(y);
        cardView.setRotate(rotation);
    }
	
	private Circle createGlowingCircle(double redius, Color color){
		Circle circle = new Circle(redius);
		circle.setStroke(color);
		circle.setFill(Color.TRANSPARENT);
		circle.setStrokeWidth(5);
		DropShadow glow = new DropShadow();
		glow.setColor(color);
		glow.setRadius(40);
		glow.setSpread(0.5);
		glow.setBlurType(BlurType.THREE_PASS_BOX);
		circle.setEffect(glow);
		return circle;
	}
	
	public void addToFirePit(CardView cardView, int playerIndex, double randomAngle) {
	    // Step 1: Get the card's current position in scene coordinates
	    Bounds sceneBounds = cardView.localToScene(cardView.getBoundsInLocal());
	    double sceneX = sceneBounds.getMinX() + sceneBounds.getWidth()/9;
	    double sceneY = sceneBounds.getMinY() + sceneBounds.getHeight()/9;

	    // Step 2: Convert scene position to this's local coordinates
	    Point2D firePitCoords = this.sceneToLocal(sceneX, sceneY);
	    

	    // Step 3: Remove from current parent and add to FirePit
	    ((Pane) cardView.getParent()).getChildren().remove(cardView);
	    this.getChildren().add(cardView);
	    cardView.scaleCard(1.2);
	    // Step 4: Set layout to match final visual position
	    cardView.setLayoutX(firePitCoords.getX());
	    cardView.setLayoutY(firePitCoords.getY());
	    
	    // Step 5: Reset any animation translation offsets (important!)
	    cardView.setTranslateX(0);
	    cardView.setTranslateY(0);
	    switch(playerIndex){
        case 1: cardView.setRotate(randomAngle + 90); break;
        case 2: cardView.setRotate(randomAngle + 180); break;
        case 3: cardView.setRotate(randomAngle - 90); break;
        }
	    cardView.scaleCard(1.1);
	    // Step 6: Keep rotation from animation
	    // This is preserved automatically unless you used RotateTransition on a different node.
	}


	
	public Circle getCircle() {
		return circle;
	}

	public ArrayList<Card> getFirePit() {
		return firePit;
	}
	
	
}