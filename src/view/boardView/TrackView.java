package view.boardView;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import model.player.Marble;
import engine.board.Cell;
import engine.board.CellType;
import engine.board.SafeZone;

public class TrackView extends GridPane {
	private static final int P = 4;
	private static final int DISTANCE = 1;
	private static final double CELL_SPACING = 1;
	private static final int[][] DIRECTIONS = {
        {0, -DISTANCE},    // 1 - up
        {-DISTANCE, -DISTANCE},  // 2 - diagonal up-left
        {-DISTANCE, 0},    // 3 - left
        {0, -DISTANCE},    // 4 - up
        {DISTANCE, 0},     // 5 - right
        {DISTANCE, -DISTANCE},   // 6 - diagonal up-right
        {0, -DISTANCE},    // 7 - up
        {DISTANCE, 0},     // 8 - right
        {0, DISTANCE},     // 9 - down
        {DISTANCE, DISTANCE},    // 10 - diagonal down-right
        {DISTANCE, 0},     // 11 - right
        {0, DISTANCE},     // 12 - down
        {-DISTANCE, 0},    // 13 - left
        {-DISTANCE, DISTANCE},   // 14 - diagonal down-left
        {0, DISTANCE},     // 15 - down
        {-DISTANCE, 0}     // 16 - left
    };
	private static final int[] CELLS_IN_DIRECTION = {
        8, 8, 8, 5, 8, 8, 8, 5,
        8, 8, 8, 5, 8, 8, 8, 5
    };
	
	
    private final ArrayList<Cell> track;
    private final ArrayList<SafeZoneView> safeZoneViews ;
    private final ArrayList<CellView> cellViews ;


    public TrackView(ArrayList<Cell> track, ArrayList<SafeZone> safeZones) {
        this.track = track;
        setHgap(CELL_SPACING);
        setVgap(CELL_SPACING);
        cellViews = new ArrayList<>();
        safeZoneViews = new ArrayList<>();
        for(SafeZone safeZone : safeZones){
        	SafeZoneView safeZoneView = new SafeZoneView(safeZone);
        	safeZoneViews.add(safeZoneView);
        }
        renderTrack(safeZoneViews);
        //this.setStyle("-fx-background-color: yellow");
    }

    private void renderTrack(ArrayList<SafeZoneView> safeZoneViews) {
        int x = 50, y = 50;
        int cellIndex = 0;
        int j = 0;
        double lineaarSize = 30;
        double diagonalSize = 15;
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(lineaarSize);   // preferred width
        col1.setMinWidth(lineaarSize);    // minimum width
        col1.setMaxWidth(lineaarSize);    // maximum width (no resizing)

        RowConstraints row1 = new RowConstraints();
        row1.setPrefHeight(lineaarSize);
        row1.setMinHeight(lineaarSize);
        row1.setMaxHeight(lineaarSize);
        
        for (int d = 0; d < 16; d++) {
            int len = CELLS_IN_DIRECTION[d];

            for (int i = 0; i < len; i++) {
                if (i == len-1) continue;
                if (cellIndex >= track.size()) return;

                Cell cell = track.get(cellIndex++);
                CellView cellView;
                if(j < safeZoneViews.size() && cell.getCellType() == CellType.BASE){
                	cellView = new CellView(cell, safeZoneViews.get(j++).getColor());
                }
                else if(cell.getCellType() == CellType.ENTRY){
                	int index;
                	int dx;
                	int dy;
                	switch(cellIndex - 1){
                	case 98:index = 0; dx = 0; dy = -1 ; break;
                	case 23:index = 1; dx = 1; dy = 0 ; break;
                	case 48:index = 2; dx = 0; dy = 1 ; break;
                	default:index = 3; dx = -1; dy = 0 ; break;
                	}
                	SafeZoneView safeZoneView = safeZoneViews.get(index);
                	cellView = new CellView(cell, safeZoneView.getColor());
                	ArrayList<CellView> cellViews = safeZoneView.getCellViews();
                	for(int k = 1; k <= cellViews.size(); k++)
                		add(cellViews.get(k-1), (k * dx) + x, (k * dy) + y);
                }
                else{
                	cellView = new CellView(cell);
                }
                cellViews.add(cellView);
                // If there's a marble on this cell, add a MarbleView
                if (cell.getMarble() != null) {
                    Marble marble = cell.getMarble(); // get the model's marble
                    MarbleView marbleView = new MarbleView(marble);
                    cellView.setMarbleView(marbleView);
                }
                
//                	GridPane.setMargin(cellView, new Insets(P, P, P, P));
//                
//                switch(cellIndex - 1){
//                case 7 : GridPane.setMargin(cellView, new Insets(0, P, P, 0)); break;
//                case 14: GridPane.setMargin(cellView, new Insets(P, 0, 0, P)); break;
//                case 32: GridPane.setMargin(cellView, new Insets(0, 0, P, P)); break;
//                case 39: GridPane.setMargin(cellView, new Insets(P, P, 0, 0)); break;
//                case 57: GridPane.setMargin(cellView, new Insets(P, 0, 0, P)); break;
//                case 64: GridPane.setMargin(cellView, new Insets(0, P, P, 0)); break;
//                case 82: GridPane.setMargin(cellView, new Insets(P, P, 0, 0)); break;
//                case 89: GridPane.setMargin(cellView, new Insets(0, 0, P, P)); break;
//                }
                this.getColumnConstraints().add(col1);
            	this.getRowConstraints().add(row1);
                add(cellView, x, y);
                x += DIRECTIONS[d][0];
                y += DIRECTIONS[d][1];
            }
        }
   
    }
  

	public ArrayList<Cell> getTrack() {
		return track;
	}

	public ArrayList<SafeZoneView> getSafeZoneViews() {
		return safeZoneViews;
	}

	public ArrayList<CellView> getCellViews() {
		return cellViews;
	}

}