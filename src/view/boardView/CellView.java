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
    private final Cell cell;
    private final Circle circle;
    private MarbleView marbleView;

    public CellView(Cell cell) {
        this.cell = cell;

        // Create the visual circular base
        circle = new Circle(DEFAULT_RADIUS);
        circle.setFill(Color.LIGHTGRAY);
        circle.setStroke(Color.DARKRED);
        circle.setStrokeWidth(2);
        getChildren().add(circle);
    }
    public CellView(Cell cell, Color color) {
        this.cell = cell;

        // Create the visual circular base
        circle = new Circle(DEFAULT_RADIUS);
        circle.setFill(Color.LIGHTGRAY);
        circle.setStroke(color);
        circle.setStrokeWidth(2);
        if(cell.getCellType() == CellType.SAFE)
        	applyGlow(color);
        getChildren().add(circle);
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
    public void setMarble(MarbleView marbleView) {
        this.marbleView = marbleView;
        if (!getChildren().contains(marbleView)) {
            getChildren().add(marbleView);
        }
    }

    // Clear the marble (e.g. when moving it elsewhere)
    public void removeMarble() {
        if (marbleView != null) {
            getChildren().remove(marbleView);
            marbleView = null;
        }
    }
    
    public void setStrokeColor(Color color) {
    	circle.setStroke(color);
    }

    public void resetStrokeColor() {
        circle.setStroke(Color.BROWN); // or your default color
    }
    public void applyGlow(Color color){
    	DropShadow glow = new DropShadow();
    	glow.setColor(color);
    	glow.setWidth(25);
    	glow.setHeight(25);
    	this.setEffect(glow);
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
            this.removeMarble();
            target.setMarble(marble);
        });
        
        // Start the animation.
        transition.play();
    }

	
//	public Circle setupTrackCell(int index) {
//		double radius = 10;
//		double gap = 2;
//		double diameter = radius * 2;
//		int numPerSide = 26;
//
//	    double squareSize = numPerSide * diameter + numPerSide * gap;
//	    double startX = (1400 - squareSize) / 2;
//	    double startY = (900 - squareSize) / 2;
//
//	    double x = 0;
//	    double y = 0;
//
//	    if (index < 25) {
//	        x = startX;
//	        y = startY + squareSize - diameter - index * (diameter + gap);
//	    } else if (index < 50) {
//	        x = startX + (index - 25) * (diameter + gap);
//	        y = startY;
//	    } else if (index < 75) {
//	        x = startX + squareSize - diameter;
//	        y = startY + (index - 50) * (diameter + gap);
//	    } else {
//	        x = startX + squareSize - diameter - (index - 75) * (diameter + gap);
//	        y = startY + squareSize - diameter;
//	    }
//	    
//	    Circle circle = new Circle(x, y, radius, getCellColour(index));
//	    return circle;
//	}
//	
//	public Color getCellColour (int i){
//		Marble marble = null ;
//		// GameScene.getGame().getBoard().getTrack().get(i).getMarble(); 
//		if (marble != null){
//			if (marble.getColour() == Colour.RED) return Color.RED ;
//			if (marble.getColour() == Colour.GREEN) return Color.GREEN ;
//			if (marble.getColour() == Colour.YELLOW) return Color.YELLOW ;
//			if (marble.getColour() == Colour.BLUE) return Color.BLUE ;
//		}
//		return Color.BLACK ;	
//	}
//	
//	public Circle getCell(){
//		return this.cell ;
//	}

}
