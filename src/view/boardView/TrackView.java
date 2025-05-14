package view.boardView;

import engine.board.Cell;
import engine.board.SafeZone;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

import view.marbleView.MarbleView;
import model.player.Marble;

public class TrackView extends GridPane {
	private static final int[][] DIRECTIONS = {
        {0, -3},    // 1 - up
        {-1, -1},   // 2 - diagonal up-left
        {-3, 0},   // 3 - left
        {0, -3},    // 4 - up
        {3, 0},   // 5 - right
        {1, -1},   // 6 - diagonal up-right
        {0, -3},  // 7 - up
        {3, 0},   // 8 - right
        {0, 3},   // 9 - down
        {1, 1},    // 10 - diagonal down-right
        {3, 0},   // 11 - right
        {0, 3},   // 12 - down
        {-3, 0},    // 13 - left
        {-1, 1},    // 14 - diagonal down-left
        {0, 3},    // 15 - down
        {-3, 0}     // 16 - left
    };
	private static final int[] CELLS_IN_DIRECTION = {
        7, 8, 8, 5, 8, 8, 8, 5,
        8, 8, 8, 5, 8, 8, 8, 5
    };
	
	private static final int CELL_HSPACING = 1;
	private static final int CELL_VSPACING = 1;
	
	
	private final HashMap<Cell, CellView> cellViewMap;
    private final ArrayList<Cell> track;
   

    public TrackView(ArrayList<Cell> track, ArrayList<SafeZone> safeZones) {
        this.track = track;
        setHgap(CELL_HSPACING);
        setVgap(CELL_VSPACING);
        cellViewMap  = new HashMap<>();
        ArrayList<Color> colors = new ArrayList<>();
        for(SafeZone safeZone : safeZones){
        	colors.add(HomeZoneView.convertToFxColor(safeZone.getColour()));
        }
        renderTrack(colors);
    }

    private void renderTrack(ArrayList<Color> colors) {
        ArrayList<Cell> cells = track;
        int x = 80, y = 80;
        int cellIndex = 0;
        int j = 0;
        for (int d = 0; d < 16; d++) {
            int len = CELLS_IN_DIRECTION[d];

            for (int i = 0; i < len; i++) {
                if (i == len-1 && d > 0) continue;
                if (cellIndex >= cells.size()) return;

                Cell cell = cells.get(cellIndex++);
                CellView cellView = new CellView(cell);
                if(j < colors.size() && i == 0 && (d%4 == 0)){
                	cellView.setStrokeColor(colors.get(j));
                	j++;
                }
                cellViewMap.put(cell, cellView);
                // If there's a marble on this cell, add a MarbleView
                if (cell.getMarble() != null) {
                    Marble marble = cell.getMarble(); // get the model's marble
                    MarbleView marbleView = new MarbleView(marble);
                    cellView.setMarble(marbleView);
                }

                add(cellView, x, y);
                x += DIRECTIONS[d][0];
                y += DIRECTIONS[d][1];
            }
        }
    }
}




























//package view.boardView;
//
//import java.util.ArrayList;
//
//import javafx.animation.ParallelTransition;
//import javafx.animation.ScaleTransition;
//import javafx.animation.TranslateTransition;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import model.Colour;
//import model.player.Marble;
//import engine.Game;
//import engine.board.Cell;
//
//public class TrackView {
//	
//	private ArrayList<CellView> track ;
//	private Group root ;
//	public TrackView(ArrayList<Cell> track, Group root){
//		this.root = root ;
//		this.track = new ArrayList<>();
//		setupView();
//	}
//	
//	
//	public void setupView(){
//		  for (int i = 0; i < 100; i++) {
//		    	CellView cellView = new CellView(i,0);
//		        track.add(cellView);
//		        root.getChildren().add(cellView.getCell());
//		   }
//		  
//	}
//	
//		
//	public void moveMarbleAnimation(int fromIndex, int toIndex, Color colour) {
//	    Circle start = track.get(fromIndex).getCell();
//	    Circle end = track.get(toIndex).getCell();
//
//	    Circle animatedMarble = new Circle(start.getCenterX(), start.getCenterY(), 14, colour);
//	    root.getChildren().add(animatedMarble);
//
//	    TranslateTransition translate = new TranslateTransition(Duration.millis(500), animatedMarble);
//	    translate.setByX(end.getCenterX() - start.getCenterX());
//	    translate.setByY(end.getCenterY() - start.getCenterY());
//
//	    ScaleTransition scale = new ScaleTransition(Duration.millis(200), animatedMarble);
//	    scale.setToX(1.5);
//	    scale.setToY(1.5);
//	    scale.setAutoReverse(true);
//	    scale.setCycleCount(5);
//
//	    ParallelTransition moveAndPulse = new ParallelTransition(translate, scale);
//
//	    moveAndPulse.setOnFinished(e -> root.getChildren().remove(animatedMarble));
//	    moveAndPulse.play();
//	}
//}
