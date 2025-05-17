package view.boardView;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
}
