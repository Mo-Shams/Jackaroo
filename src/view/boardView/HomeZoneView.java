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
import view.boardView.MarbleView;
import model.player.Marble;

public class HomeZoneView extends StackPane {
    private static final double SIDE_LENGTH = 80;
    private static final double GAP = 5;

    private ArrayList<Marble> marbles;
    private final ArrayList<CellView> cellViews;
    private final Rectangle homeSquare;
    private final GridPane cellGrid;

    public HomeZoneView(ArrayList<Marble> marbles) {
        this.marbles =  marbles ;
        this.cellViews = new ArrayList<>();

        Color color = Color.valueOf(this.marbles.get(0).getColour().toString());


        for (int i = 0; i < marbles.size(); i++) {  // changed from 4 
        	CellView cellView = new CellView(new Cell(CellType.NORMAL), color);
        	cellView.setMarbleView(new MarbleView(marbles.get(i)));
            cellViews.add(cellView);
        }

        homeSquare = createSquare(SIDE_LENGTH, SIDE_LENGTH, color);

        cellGrid = new GridPane();
        cellGrid.setHgap(GAP);
        cellGrid.setVgap(GAP);
        cellGrid.setAlignment(Pos.CENTER);
        
        // Add cells to 2x2 Grid
        cellGrid.add(cellViews.get(0), 0, 0);
        cellGrid.add(cellViews.get(1), 1, 0);
        cellGrid.add(cellViews.get(2), 0, 1);
        cellGrid.add(cellViews.get(3), 1, 1);

        getChildren().addAll(homeSquare, cellGrid);
        this.setMaxSize(100, 100);
        //this.setStyle("-fx-background-color: yellow;");
        
    }

    private Rectangle createSquare(double width, double height, Color color) {
        Rectangle square = new Rectangle(width, height);
        square.setFill(Color.TRANSPARENT);
        square.setStroke(color);
        square.setArcWidth(20);
        square.setArcHeight(20);
        square.setStrokeWidth(3);
        

        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setRadius(5);
        glow.setSpread(0.5);
        square.setEffect(glow);

        return square;
    }

    // Add a marble to the first available empty cell; returns true if added successfully
    public void sendToHome(CellView cellView) {
         for (CellView cell : cellViews){
         	if (cell.getMarbleView() == null) {
         		cellView.moveMarbleTo(cell);
         	}
         }
    }
    
    public void fieldMarble(CellView baseCell){	
        for (CellView cell : cellViews){
        	if (cell.getMarbleView() != null) {
        		cell.moveMarbleTo(baseCell);
        		break ;
        	}
        }	
    }
   
    
    public void updateHomeZoneView() {
    	for(CellView cellView: cellViews) cellView.removeMarbleView();
    	int count = 3;
    	for(Marble marble: marbles){
    		MarbleView marbleView = MarbleView.MarbleToViewMap.get(marble);
    		cellViews.get(count).setMarbleView(marbleView);
    		marbleView.setOnMouseClicked(null);
    		marbleView.setSelected(false);
    		count--;
    		
    	}
	}
    
    // ----------------------- Getters & Setters ----------------------
    
    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    public ArrayList<CellView> getCellViews() {
        return this.cellViews;
    }

	public Rectangle getHomeSquare() {
		return homeSquare;
	}
    
}