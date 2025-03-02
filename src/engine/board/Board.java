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
        // this method is not developed yet 
    }
    private void assignTrapCell(){
        // this method is not developed yet 
    }
	public int getSplitDistances() {
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
