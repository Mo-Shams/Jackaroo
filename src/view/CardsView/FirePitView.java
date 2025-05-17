package view.CardsView;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class FirePitView extends StackPane {
	private final Circle circle;
	private final ArrayList<CardView> firePit;
	private final StackPane overlayLayer;
	public Pane cardLayer;

	public FirePitView(StackPane overlayLayer){
		this.overlayLayer = overlayLayer;
		firePit = new ArrayList<>();
		circle = createGlowingCircle(150, Color.DEEPSKYBLUE);
		cardLayer = new Pane();
		getChildren().addAll(circle, cardLayer);  // order matters! circle first, cards on top
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
	
<<<<<<< Updated upstream
	public void addToFirePit(CardView cardView) {
	    // Step 1: Get the card's current position in scene coordinates
	    Bounds sceneBounds = cardView.localToScene(cardView.getBoundsInLocal());
	    double sceneX = sceneBounds.getMinX() + sceneBounds.getWidth()/5;
	    double sceneY = sceneBounds.getMinY() + sceneBounds.getHeight()/5;

	    // Step 2: Convert scene position to cardLayer's local coordinates
	    Point2D firePitCoords = cardLayer.sceneToLocal(sceneX, sceneY);

	    // Step 3: Remove from current parent and add to FirePit
	    ((Pane) cardView.getParent()).getChildren().remove(cardView);
	    cardLayer.getChildren().add(cardView);

	    // Step 4: Set layout to match final visual position
	    cardView.setLayoutX(firePitCoords.getX());
	    cardView.setLayoutY(firePitCoords.getY());

	    // Step 5: Reset any animation translation offsets (important!)
	    cardView.setTranslateX(0);
	    cardView.setTranslateY(0);
	    
	    
	    cardView.setEffect(null);
	    cardView.scaleCard(1.2);

=======

	
	public void addToFirePit(CardView cardView) {
	    // Step 1: Get the card's current position in scene coordinates
	    Bounds sceneBounds = cardView.localToScene(cardView.getBoundsInLocal());
	    double sceneX = sceneBounds.getMinX() + sceneBounds.getWidth()/5;
	    double sceneY = sceneBounds.getMinY() + sceneBounds.getHeight()/5;

	    // Step 2: Convert scene position to cardLayer's local coordinates
	    Point2D firePitCoords = cardLayer.sceneToLocal(sceneX, sceneY);

	    // Step 3: Remove from current parent and add to FirePit
	    ((Pane) cardView.getParent()).getChildren().remove(cardView);
	    cardLayer.getChildren().add(cardView);

	    // Step 4: Set layout to match final visual position
	    cardView.setLayoutX(firePitCoords.getX());
	    cardView.setLayoutY(firePitCoords.getY());

	    // Step 5: Reset any animation translation offsets (important!)
	    cardView.setTranslateX(0);
	    cardView.setTranslateY(0);
	    
	    
	    cardView.setEffect(null);
	    cardView.scaleCard(1.2);

>>>>>>> Stashed changes
	    // Step 6: Keep rotation from animation
	    // This is preserved automatically unless you used RotateTransition on a different node.
	}


	
	public Circle getCircle() {
		return circle;
	}
	
	
}
