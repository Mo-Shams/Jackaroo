package engine.board;

import java.util.ArrayList;

import engine.GameManager;
import model.Colour;

public class Board implements BoardManager {
	private final GameManager gameManager;
	private final ArrayList<Cell> track;
    private final ArrayList<SafeZone> safeZones ;
    private int splitDistance ;

    public Board(ArrayList<Colour> colourOrder, GameManager gameManager){
        this.gameManager = gameManager ;
        this.track = new ArrayList<>();
        this.safeZones = new ArrayList<>();
        this.splitDistance = 3 ;
        setupTrack();
        assignTrapCell();

        for (int i = 0; i < colourOrder.size(); i++) {
            safeZones.add(new SafeZone(colourOrder.get(i)));
        }
    }
    public void setupTrack(){
        for (int i = 0; i < 100; i++) {
        	if (i == 0 || i == 25 || i == 50 || i == 75) track.add(new Cell(CellType.BASE));
        	else if (i == 23 || i == 48 || i == 73 || i == 98) track.add(new Cell(CellType.ENTRY));
        	else track.add(new Cell(CellType.NORMAL)); 
        }
    }
    private void assignTrapCell(){
        // this method is not developed yet 
    }
    
    @Override
    public int getSplitDistance() {
        return splitDistance;
    }
    
	public void setSplitDistance(int splitDistance) {
		this.splitDistance = splitDistance;
	}
	public GameManager getGameManager() {
		return gameManager;
	}
	public ArrayList<Cell> getTrack() {
		return track;
	}
	public ArrayList<SafeZone> getSafeZones() {
		return safeZones;
	}
}
