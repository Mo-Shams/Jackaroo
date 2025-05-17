package view.CardsView;

import java.util.ArrayList;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class FirePitView extends StackPane {
	private final Circle circle;
	private final Pane cardLayer;

	public FirePitView(StackPane overlay){
		new ArrayList<>();
		circle = createGlowingCircle(150, Color.DEEPSKYBLUE);
		cardLayer = new Pane();
		getChildren().addAll(circle, cardLayer);  // order matters! circle first, cards on top
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	}
	
	public void addCardToFirePit(CardView cardView, double x, double y, double rotation) {
        cardLayer.getChildren().add(cardView);
        // Set the card position relative to cardLayer (coordinates inside firepit)
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
	
//	public void sendCardToFirePit(CardView card, HandView playerHand) {
//	    // Step 1: Get scene coordinates of card BEFORE removing from hand
//	    Bounds cardSceneBounds = card.localToScene(card.getBoundsInLocal());
//	    double sceneX = cardSceneBounds.getMinX();
//	    double sceneY = cardSceneBounds.getMinY();
//
//	    // Step 2: Remove from hand
//	    playerHand.getChildren().remove(card);
//
//	    // Step 3: Move to overlay for animation
//	    overlayLayer.getChildren().add(card);
//	    Point2D localToOverlay = overlayLayer.sceneToLocal(sceneX, sceneY);
//	    card.relocate(localToOverlay.getX(), localToOverlay.getY());
//
//	    // Step 4: Compute FirePit target + random offset
//	    Bounds pitBounds = this.localToScene(this.getBoundsInLocal());
//	    double pitCenterX = pitBounds.getMinX() + pitBounds.getWidth() / 2;
//	    double pitCenterY = pitBounds.getMinY() + pitBounds.getHeight() / 2;
//
//	    double radius = 40;
//	    double angle = Math.random() * 2 * Math.PI;
//	    double offsetX = radius * Math.cos(angle);
//	    double offsetY = radius * Math.sin(angle);
//
//	    Point2D targetInOverlay = overlayLayer.sceneToLocal(pitCenterX + offsetX, pitCenterY + offsetY);
//
//	    // Step 5: Create transitions
//	    double dx = targetInOverlay.getX() - localToOverlay.getX();
//	    double dy = targetInOverlay.getY() - localToOverlay.getY();
//
//	    TranslateTransition translate = new TranslateTransition(Duration.seconds(0.5), card);
//	    translate.setToX(dx);
//	    translate.setToY(dy);
//
//	    RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), card);
//	    double finalAngle = Math.random() * 360 - 180;
//	    rotate.setByAngle(finalAngle);
//
//	    ParallelTransition transition = new ParallelTransition(translate, rotate);
//	    transition.setInterpolator(Interpolator.EASE_BOTH);
//	    transition.play();
//
//	    // Step 6: On finish, fix final layout
//	    transition.setOnFinished(event -> {
//	        overlayLayer.getChildren().remove(card);
//	        cardLayer.getChildren().add(card);
//
//	        Point2D finalScenePosition = card.localToScene(0, 0);
//	        Point2D finalLocalToCardLayer = cardLayer.sceneToLocal(finalScenePosition);
//
//	        card.setTranslateX(0);
//	        card.setTranslateY(0);
//	        card.setLayoutX(finalLocalToCardLayer.getX());
//	        card.setLayoutY(finalLocalToCardLayer.getY());
//	        card.setRotate(card.getRotate());
//	    });
//	}


	
	public Circle getCircle() {
		return circle;
	}
	
	
}
