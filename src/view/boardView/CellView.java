package view.boardView;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import view.marbleView.MarbleView;
import engine.board.Cell;
import engine.board.CellType;

public class CellView extends StackPane {
	private static final double DEFAULT_RADIUS = 12;
	private static final Color DEFAULT_COLOR = Color.DARKSLATEBLUE;
    private final Cell cell;
    private final Circle circle;
    private MarbleView marbleView;
    
    
    public CellView(Cell cell){
    	this(cell, DEFAULT_COLOR);
    }

    public CellView(Cell cell, Color strokeColor) {
        this.cell = cell;
        circle = createCircle(strokeColor);
        getChildren().add(circle);

        if (cell.getCellType() == CellType.SAFE) {
            applyGlow(strokeColor);
        }
    }

    private Circle createCircle(Color strokeColor) {
        Circle circle = new Circle(DEFAULT_RADIUS);
        circle.setFill(Color.LIGHTGRAY);
        circle.setStroke(strokeColor);
        circle.setStrokeWidth(2);
        return circle;
    }
    
    public Cell getCell() {
        return cell;
    }

    public Circle getCircle() {
        return circle;
    }

    public MarbleView getMarbleView() {
        return marbleView;
    }

    // Add or update the marble in the cell
    public void setMarbleView(MarbleView marbleView) {
        this.marbleView = marbleView;
        if (!getChildren().contains(marbleView)) {
            getChildren().add(marbleView);
        }
    }

    // Clear the marble (e.g. when moving it elsewhere)
    public void removeMarbleView() {
        if (marbleView != null) {
            getChildren().remove(marbleView);
            marbleView = null;
        }
    }
    
    public void applyGlow(Color color){
    	if(color == null) return;
    	DropShadow glow = new DropShadow();
    	glow.setColor(color);
    	glow.setWidth(25);
    	glow.setHeight(25);
    	circle.setEffect(glow);
    }

    public void moveMarbleTo(CellView target) {
        // Retrieve the marble from this (source) cell.
        MarbleView marble = this.getMarbleView();
        if (marble == null) {
            return; // No marble to move.
        }
        
        // Get the target cell's bounds in scene coordinates and compute its center.
        Bounds targetBoundsScene = target.localToScene(target.getBoundsInLocal());
        double targetCenterSceneX = targetBoundsScene.getMinX() + targetBoundsScene.getWidth() / 2;
        double targetCenterSceneY = targetBoundsScene.getMinY() + targetBoundsScene.getHeight() / 2;
        
        // Get the marble's current bounds (center) in scene coordinates.
        Bounds marbleBoundsScene = marble.localToScene(marble.getBoundsInLocal());
        double marbleCenterSceneX = marbleBoundsScene.getMinX() + marbleBoundsScene.getWidth() / 2;
        double marbleCenterSceneY = marbleBoundsScene.getMinY() + marbleBoundsScene.getHeight() / 2;
        
        // Convert both the target cell's center and the marble's center into the coordinate system
        // of the marble's immediate parent.
        Point2D targetInParent = marble.getParent().sceneToLocal(targetCenterSceneX, targetCenterSceneY);
        Point2D marbleCenterInParent = marble.getParent().sceneToLocal(marbleCenterSceneX, marbleCenterSceneY);
        
        // Calculate the translation offsets required.
        double deltaX = targetInParent.getX() - marbleCenterInParent.getX();
        double deltaY = targetInParent.getY() - marbleCenterInParent.getY();
        
        // Create and configure the TranslateTransition.
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), marble);
        transition.setByX(deltaX);
        transition.setByY(deltaY);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        
        // When the transition finishes, update the marble's layout to reflect its new "permanent" position,
        // reset any temporary translation values, remove it from this cell, and add it to the target cell.
        transition.setOnFinished(event -> {
            marble.setLayoutX(marble.getLayoutX() + deltaX);
            marble.setLayoutY(marble.getLayoutY() + deltaY);
            marble.setTranslateX(0);
            marble.setTranslateY(0);
            
            // Remove marble from the current cell (this) and add it to the target CellView.
            this.removeMarbleView();
            target.setMarbleView(marble);
        });
        
        // Start the animation.
        transition.play();
    }


}
