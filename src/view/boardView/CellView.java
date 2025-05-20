package view.boardView;

import java.util.HashMap;
import java.util.Map;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import engine.board.Cell;
import engine.board.CellType;

public class CellView extends StackPane {
	private static final double DEFAULT_RADIUS = 12;
	private static final Color DEFAULT_COLOR = Color.DARKSLATEBLUE;
	private static final Color FILLING_COLOR = Color.LIGHTGRAY;

	
    private final Cell cell;
    private final Circle circle;
    private MarbleView marbleView;
    
    
	public static final Map<Cell, CellView> cellToViewMap = new HashMap<>();

    
    public CellView(Cell cell){
    	this(cell, DEFAULT_COLOR);
    }

    public CellView(Cell cell, Color strokeColor) {
        this.cell = cell;
        circle = createCircle(strokeColor);
        this.getChildren().add(circle);
        StackPane.setAlignment(circle, Pos.CENTER);

        if (cell.getCellType() == CellType.SAFE) 
            applyGlow(strokeColor);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        cellToViewMap.put(cell,this);
    }

    private Circle createCircle(Color strokeColor) {
        Circle circle = new Circle(DEFAULT_RADIUS);
        circle.setFill(FILLING_COLOR);
        circle.setStroke(strokeColor);
        circle.setStrokeWidth(2);
        return circle;
    }
    
    public PauseTransition moveMarbleTo(CellView target) {
    	
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(e -> {
            if (marbleView != null) {
                target.setMarbleView(marbleView);
                this.removeMarbleView();
                System.out.println("hello");
            }
        });
        return pause;
    }
    
//    public ParallelTransition animateMoving(CellView target) {
//    	
//    	MarbleView marbleview = this.getMarbleView();
//    	
//    	Point2D p1 = this.getCircle().localToScene(this.getCircle().getCenterX(), this.getCircle().getCenterY());
//    	Point2D p2 = target.getCircle().localToScene(target.getCircle().getCenterX(), target.getCircle().getCenterY());
//    	
//    	Parent parent = marbleView.getParent();
//    	
//    	Point2D start = parent.sceneToLocal(p1);
//    	Point2D end = parent.sceneToLocal(p2);
//    	
//    	double dx = end.getX() - start.getX();
//    	double dy = end.getY() - start.getY();
//    	
//
//    	
//        // Translate Transition (movement)
//        TranslateTransition translate = new TranslateTransition(Duration.millis(800), marbleview);
//        translate.setByX(dx);
//        translate.setByY(dy);
//        translate.setInterpolator(Interpolator.EASE_BOTH);
//
//        // Scale Transition (scaling effect)
//        ScaleTransition scale = new ScaleTransition(Duration.millis(400), marbleview);
//        scale.setFromX(1.0);
//        scale.setFromY(1.0);
//        scale.setToX(1.3); // Slightly larger
//        scale.setToY(1.3);
//        scale.setAutoReverse(true);
//        scale.setCycleCount(2); // Go up then back down
//
//        // Combine both animations
//        ParallelTransition parallel = new ParallelTransition(translate, scale);
//        parallel.setOnFinished(e -> {
//            // Reset position after translation so layout stays correct
//        	marbleview.setTranslateX(0);
//        	marbleview.setTranslateY(0);
//        	
//        	this.moveMarbleTo(target);
//        
//        });
//
//        return parallel ;
//
//   }
    
    // Add or update the marble in the cell
    public void setMarbleView(MarbleView marbleView) {
        this.marbleView = marbleView;                   // this might be wrong 
        if (!getChildren().contains(marbleView)) {
            getChildren().add(marbleView);
            StackPane.setAlignment(marbleView, Pos.CENTER);
        }
    }

    // Clear the marble (e.g. when moving it elsewhere)
    public void removeMarbleView() {
        if (marbleView != null) {
            getChildren().remove(marbleView);
            marbleView = null;
        }
    }
    
    private void applyGlow(Color color){
    	if(color == null) return;
    	DropShadow glow = new DropShadow();
    	glow.setColor(color);
    	glow.setWidth(25);
    	glow.setHeight(25);
    	circle.setEffect(glow);
    }
    
    
   
    
    // ----------------------- Getters & Setters -----------------------------------------
    
    public Cell getCell() {
        return cell;
    }

    public Circle getCircle() {
        return circle;
    }

    public MarbleView getMarbleView() {
        return marbleView;
    }

}
