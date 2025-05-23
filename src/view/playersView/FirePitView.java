package view.playersView;

import java.util.ArrayList;

import engine.Game;
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
	private static int UPDATE_COUNTER = 0;
	private static CardView AFTER_REFILLING = null;
	private final ArrayList<Card> firePit;
	private final Circle circle;
	private final ArrayList<CardView> cardViews;

	public FirePitView(ArrayList<Card> firePit, double width, double height){
		//super();
		this.firePit = Game.FIRE_PIT;
		cardViews = new ArrayList<>();
		circle = createGlowingCircle(150, Color.DEEPSKYBLUE);
		circle.setLayoutX((width-1620)/2);
		circle.setLayoutY((height-720)/2);
		getChildren().add(circle);  // order matters! circle first, cards on top
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
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
	    cardViews.add(cardView);
	    cardView.scaleCard(1.3);
	    // Step 4: Set layout to match final visual position
	    cardView.setLayoutX(firePitCoords.getX());
	    cardView.setLayoutY(firePitCoords.getY());
	    
	    // Step 5: Reset any animation translation offsets (important!)
	    cardView.setTranslateX(0);
	    cardView.setTranslateY(0);
	    
	 // Step 6: Keep rotation from animation
	    switch(playerIndex){
        case 1: cardView.setRotate(randomAngle + 90); break;
        case 2: cardView.setRotate(randomAngle + 180); break;
        case 3: cardView.setRotate(randomAngle - 90); break;
        }
	    
	    
	    cardView.scaleCard(1.15);
	    // This is preserved automatically unless you used RotateTransition on a different node.
	}
	
	public void updateFirePitView(){
		UPDATE_COUNTER++;
		if(firePit.isEmpty())return;
		this.getChildren().remove(AFTER_REFILLING);
		for(int i = 0; i < firePit.size() - 1; i++){
			Card card = firePit.get(i);
			CardView cardView = CardView.cardToViewMap.get(card);
			this.getChildren().remove(cardView);
		}
		AFTER_REFILLING = null;
		if(UPDATE_COUNTER == 7){
			AFTER_REFILLING = CardView.cardToViewMap.get(firePit.get(firePit.size()-1));
			firePit.clear();
			UPDATE_COUNTER = 1;
		}
	}
	
	public Circle getCircle() {
		return circle;
	}

	public ArrayList<Card> getFirePit() {
		return firePit;
	}

	public ArrayList<CardView> getCardViews() {
		return cardViews;
	}
	
	
}