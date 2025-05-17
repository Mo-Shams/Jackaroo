package view.boardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import model.Colour;
import model.player.Marble;
import view.marbleView.MarbleView;
import engine.board.Cell;
import engine.board.CellType;
import engine.board.SafeZone;

public class TrackView extends GridPane {
	private static final int P = 4;
	private static final int linearDistance = 1;
	private static final int diagonalDistance = 1;
	private static final int[][] DIRECTIONS = {
        {0, -linearDistance},    // 1 - up
        {-diagonalDistance, -diagonalDistance},  // 2 - diagonal up-left
        {-linearDistance, 0},    // 3 - left
        {0, -linearDistance},    // 4 - up
        {linearDistance, 0},     // 5 - right
        {diagonalDistance, -diagonalDistance},   // 6 - diagonal up-right
        {0, -linearDistance},    // 7 - up
        {linearDistance, 0},     // 8 - right
        {0, linearDistance},     // 9 - down
        {diagonalDistance, diagonalDistance},    // 10 - diagonal down-right
        {linearDistance, 0},     // 11 - right
        {0, linearDistance},     // 12 - down
        {-linearDistance, 0},    // 13 - left
        {-diagonalDistance, diagonalDistance},   // 14 - diagonal down-left
        {0, linearDistance},     // 15 - down
        {-linearDistance, 0}     // 16 - left
    };
	private static final int[] CELLS_IN_DIRECTION = {
        8, 8, 8, 5, 8, 8, 8, 5,
        8, 8, 8, 5, 8, 8, 8, 5
    };
	
	private static final double CELL_SPACING = 0.1;
	
	private final Map<Cell, CellView> cellToViewMap = new HashMap<>();
    private final ArrayList<Cell> track;
   

    public TrackView(ArrayList<Cell> track, ArrayList<SafeZone> safeZones) {
        this.track = track;
        setHgap(CELL_SPACING);
        setVgap(CELL_SPACING);
        ArrayList<SafeZoneView> safeZoneViews = new ArrayList<>();
        for(SafeZone safeZone : safeZones){
        	SafeZoneView safeZoneView = new SafeZoneView(safeZone);
        	safeZoneViews.add(safeZoneView);
        }
        renderTrack(safeZoneViews);
    }

    private void renderTrack(ArrayList<SafeZoneView> safeZoneViews) {
        ArrayList<Cell> cells = track;
        int x = 90, y = 90;
        int cellIndex = 0;
        int j = 0;
        for (int d = 0; d < 16; d++) {
            int len = CELLS_IN_DIRECTION[d];

            for (int i = 0; i < len; i++) {
                if (i == len-1) continue;
                if (cellIndex >= cells.size()) return;

                Cell cell = cells.get(cellIndex++);
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
                cellToViewMap.put(cell, cellView);
                // If there's a marble on this cell, add a MarbleView
                if (cell.getMarble() != null) {
                    Marble marble = cell.getMarble(); // get the model's marble
                    MarbleView marbleView = new MarbleView(marble);
                    cellView.setMarbleView(marbleView);
                }
                if((DIRECTIONS[d][0] == 0 || DIRECTIONS[d][1] == 0))
                	GridPane.setMargin(cellView, new Insets(P, P, P, P));
                
                switch(cellIndex - 1){
                case 7 : GridPane.setMargin(cellView, new Insets(0, P, P, 0)); break;
                case 14: GridPane.setMargin(cellView, new Insets(P, 0, 0, P)); break;
                case 32: GridPane.setMargin(cellView, new Insets(0, 0, P, P)); break;
                case 39: GridPane.setMargin(cellView, new Insets(P, P, 0, 0)); break;
                case 57: GridPane.setMargin(cellView, new Insets(P, 0, 0, P)); break;
                case 64: GridPane.setMargin(cellView, new Insets(0, P, P, 0)); break;
                case 82: GridPane.setMargin(cellView, new Insets(P, P, 0, 0)); break;
                case 89: GridPane.setMargin(cellView, new Insets(0, 0, P, P)); break;
                }

                add(cellView, x, y);
                x += DIRECTIONS[d][0];
                y += DIRECTIONS[d][1];
            }
        }
        cellToViewMap.get(track.get(0)).setMarbleView(new MarbleView(new Marble(Colour.BLUE)));
        cellToViewMap.get(track.get(0)).moveMarbleTo(cellToViewMap.get(track.get(35)));
    }
    
    public Map<Cell, CellView> getCellViewMap() {
        return cellToViewMap;
    }

}

