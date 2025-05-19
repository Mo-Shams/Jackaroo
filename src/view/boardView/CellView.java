package view.boardView;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import view.boardView.MarbleView;
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
    
    public void moveMarbleTo(CellView target) {
        if (marbleView != null) target.setMarbleView(marbleView);
   	 	this.removeMarbleView();
   }
    
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
    
    // helper method gets the coordinates of this cell for the scene 
    // method animates moving to other cell 
    
    
    
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
