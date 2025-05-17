package view.boardView;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import engine.board.Cell;
import engine.board.SafeZone;

public class SafeZoneView{
	private final SafeZone safeZone;
	private final ArrayList<CellView> cellViews;
	
	public SafeZoneView(SafeZone safeZone){
		this.safeZone = safeZone;
		cellViews = new ArrayList<>();
		for(Cell cell : safeZone.getCells()){
			CellView cellView = new CellView(cell, Color.valueOf(safeZone.getColour().toString()));
			cellViews.add(cellView);
		}
	}
	public SafeZone getSafeZone() {
		return safeZone;
	}

	public ArrayList<CellView> getCellViews() {
		return cellViews;
	}
	public Color getColor(){
		return Color.valueOf(safeZone.getColour().toString());
	}
	
}
