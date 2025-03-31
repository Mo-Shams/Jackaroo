package engine.board;

import java.security.KeyStore.Entry;
import java.util.ArrayList;

import org.hamcrest.Condition.Step;

import engine.GameManager;
import exception.IllegalMovementException;
import model.Colour;
import model.player.Marble;

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
        for (int i = 0; i < 8; i++) assignTrapCell();

        for (int i = 0; i < 4; i++) {
            safeZones.add(new SafeZone(colourOrder.get(i)));
        }
    }
    private void setupTrack(){
        for (int i = 0; i < 100; i++) {
        	if (i == 0 || i == 25 || i == 50 || i == 75) 
        		track.add(new Cell(CellType.BASE));
        	else if (i == 23 || i == 48 || i == 73 || i == 98) 
        		track.add(new Cell(CellType.ENTRY));
        	else track.add(new Cell(CellType.NORMAL)); 
        }
    }
    
    private void assignTrapCell(){
    	 int i = (int) (Math.random() * 100); 
    	 Cell cell = track.get(i);
    	 if (cell.getCellType() == CellType.NORMAL && !cell.isTrap())
    		 cell.setTrap(true);
    	 else assignTrapCell();
    }

    private ArrayList<Cell> getSafeZone(Colour colour){
        for (int i = 0; i < safeZones.size(); i++) {
            SafeZone safezone = safeZones.get(i);
            if (safezone.getColour() == colour) 
                return safezone.getCells();
        }
        return null ;
    }

    private int getPositionInPath(ArrayList<Cell> path, Marble marble){ 
        // get the track or the safezone.getCells()
        for (int i = 0; i < path.size(); i++) {
            Cell cell = path.get(i);
            if (cell.getMarble() == marble) return i ;
        }
        return -1 ;
    }

    private int getBasePosition(Colour colour){
        for (int i = 0; i < safeZones.size(); i++) {
            SafeZone safeZone = safeZones.get(i);
            if (safeZone.getColour() == colour)
                return i * 25 ;
        }
        return -1 ;
    }
    private int getEntryPosition(Colour colour){
        for (int i = 0; i < safeZones.size(); i++) {
            SafeZone safeZone = safeZones.get(i);
            if (safeZone.getColour() == colour)
                return ((i * 25 - 2) + 100) % 100; // el cp 3amel 4o8l bardo 
        }
        return -1 ;
    }
    private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException {
        try {
        for (int i = 0; i < track.size(); i++) {
            if (track.get(i).getMarble() == marble) 
                if (steps == 5) return validateStepsForFive(marble, steps);
                else return validateStepsOnTrack(marble, steps); // call a heper 
        }
        ArrayList<Cell> safezone = getSafeZone(marble.getColour()); 
        for (int i = 0; i < safezone.size(); i++) {
            if (safezone.get(i).getMarble() == marble) 
                return validateStepsOnSafeZone(marble, steps,getPositionInPath(safezone,marble)) ; // call a helper 
        }
       
        throw new IllegalMovementException("The Marble Cannnot be moved");

        } catch (IllegalMovementException e) {
            System.out.println(e.getMessage());
            return null ; // is this true ? 
        }
    }

    private ArrayList<Cell> validateStepsOnTrack(Marble marble, int steps) throws IllegalMovementException {
        int start = getPositionInPath(track,marble); // i will assume the 4 card is -4 steps 
        int target = start + steps ;
        ArrayList<Cell> path = new ArrayList<>();
        ArrayList<Cell> tempPath = new ArrayList<>();

        boolean passedOne = false ;
        boolean safezonePath = false ;

        for (int i = start; i <= target; i++) {
            if (track.get(i).getMarble() != null){ // cannot access null.colour
                if (track.get(i).getMarble().getColour() == marble.getColour()) 
                    throw new IllegalMovementException("The Rank is too high");
                else {
                    if (passedOne) throw new IllegalMovementException("The Rank is too high");
                    else passedOne = true ; 
                }
            }
            if (track.get(i).getCellType() == CellType.ENTRY) {
                tempPath.addAll(path);
                tempPath.add(track.get(i%100));

                try {
                    tempPath.addAll(validateStepsOnSafeZone(marble, target - i,0));
                    safezonePath = true ;
                } catch (IllegalMovementException e){
                    safezonePath = false ;
                }
            }
            path.add(track.get(i%100));
        }
        if (safezonePath){
            path.addAll(tempPath);
            return path ;
        }
        else 
            return path ;
    }

    private ArrayList<Cell> validateStepsOnSafeZone(Marble marble, int steps, int start) throws IllegalMovementException {
        ArrayList<Cell> safezone = getSafeZone(marble.getColour());
        ArrayList<Cell> path = new ArrayList<>();
        if (start + steps >= 4) throw new IllegalMovementException("Rank is too high");
        for (int i = start; i < safezone.size(); i++) {
            if (safezone.get(i).getMarble() != null)
                throw new IllegalMovementException("Rank is too high");
            else 
                path.add(safezone.get(i));
        }
        return path ;
    }

    private ArrayList<Cell> validateStepsForFive(Marble marble, int steps) throws IllegalMovementException {
        return null ;
    }
    private ArrayList<Cell> validateStepsForFour(Marble marble, int steps) throws IllegalMovementException {
        return null ;
    }
    @Override
    public int getSplitDistance() {
        return splitDistance;
    }
    
	public void setSplitDistance(int splitDistance) {
		this.splitDistance = splitDistance;
	}

	public ArrayList<Cell> getTrack() {
		return track;
	}
	public ArrayList<SafeZone> getSafeZones() {
		return safeZones;
	}
}
