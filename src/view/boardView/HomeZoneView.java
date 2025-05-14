package view.boardView;

import java.util.ArrayList;

import engine.board.Cell;
import engine.board.CellType;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.marbleView.MarbleView;
import model.Colour;
import model.player.Marble;

public class HomeZoneView {
	private static final double SIDE_LENGTh = 100;
	private ArrayList<Marble> marbles;
	private ArrayList<MarbleView> marbleViews;
	private final ArrayList<CellView> cellViews;
	private final Rectangle homeZone;
	private final StackPane container;
	private GridPane cellGrid;
	
	
	public HomeZoneView(ArrayList<Marble> marbles){
		Color color = convertToFxColor(marbles.get(0).getColour());
		this.marbles = marbles;
		this.marbleViews = new ArrayList<>();
		this.cellViews = new ArrayList<>();
		
		for(Marble marble : marbles){
			MarbleView marbleView = new MarbleView(marble);
			marbleViews.add(marbleView);
		}
		for(int i = 0; i < 4; i++){
			cellViews.add(new CellView(new Cell(CellType.NORMAL)));
		}
		for (CellView cellView : cellViews) {
		    cellView.setStrokeColor(color);  // Set the stroke color of the cells
		}

		homeZone = createSquare(SIDE_LENGTh, SIDE_LENGTh, color);
		
		cellGrid = new GridPane();
		cellGrid.setHgap(5);
		cellGrid.setVgap(5);
		cellGrid.setAlignment(Pos.CENTER);

		// Add cells to 2x2 Grid
		cellGrid.add(cellViews.get(0), 0, 0); // top-left
		cellGrid.add(cellViews.get(1), 1, 0); // top-right
		cellGrid.add(cellViews.get(2), 0, 1); // bottom-left
		cellGrid.add(cellViews.get(3), 1, 1); // bottom-right
		
		for (int i = 0; i < marbleViews.size() && i < cellViews.size(); i++) {
		    cellViews.get(i).setMarble(marbleViews.get(i));
		}

		container = new StackPane();
		container.getChildren().addAll(homeZone, cellGrid);
	}
	
	private Rectangle createSquare(double width, double height, Color color) {
	    Rectangle square = new Rectangle(width, height);
	    square.setFill(Color.TRANSPARENT);
	    square.setStroke(color);
	    square.setArcWidth(20); // Rounded corners
	    square.setArcHeight(20);
	    square.setStrokeWidth(3);

	    // Glowing effect
	    DropShadow glow = new DropShadow();
	    glow.setColor(color);
	    glow.setRadius(10);
	    glow.setSpread(0.5);
	    square.setEffect(glow);

	    return square;
	}
	public ArrayList<Marble> getMarbles() {
		return marbles;
	}

	public void setMarbles(ArrayList<Marble> marbles) {
		this.marbles = marbles;
	}

	public ArrayList<MarbleView> getMarbleViews() {
		return marbleViews;
	}

	public void setMarbleViews(ArrayList<MarbleView> marbleViews) {
		this.marbleViews = marbleViews;
	}

	public StackPane getContainer() {
		return container;
	}
	public static Color convertToFxColor(Colour colour) {
	    switch (colour) {
	        case RED:
	            return Color.RED;
	        case BLUE:
	            return Color.BLUE;
	        case GREEN:
	            return Color.GREEN;
	        case YELLOW:
	            return Color.YELLOW;
	        // Add more colors if necessary
	        default:
	            return Color.TRANSPARENT; // Fallback if no match
	    }
	}

}
