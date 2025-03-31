package engine.board;

import java.security.KeyStore.Entry;
import java.util.ArrayList;

import org.hamcrest.Condition.Step;

import engine.GameManager;
import exception.*;
import model.Colour;
import model.player.*;

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
	
	public void sendToSafe(Marble marble) throws InvalidMarbleException {
		Colour currentColour = gameManager.getActivePlayerColour();
		Colour marbleColour = marble.getColour(); 
		// this is to check the marble colour is my own 
		if (!marbleColour.equals(currentColour)) {
			throw new InvalidMarbleException ("Attempting to save an Opponent Marble");
		}
		
		// finding the indices of safezone and track and validate 
		ArrayList<Cell> safeZoneCells = getSafeZone(marbleColour);
		int positionInSafeZone = getPositionInPath(safeZoneCells, marble);
		int positionOnTrack = getPositionInPath(track, marble); 
		validateSavings(positionInSafeZone, positionOnTrack);
		
		ArrayList<Cell> freeCells = new ArrayList<>(); 
		// place all the available free places here to pick one randomly and place marble there
		for (Cell c : safeZoneCells) {
			if (c.getMarble() == null) freeCells.add(c); 
		}
		if (freeCells.isEmpty()) {
			throw new InvalidMarbleException("No available Space in SafeZone");
		}
		Cell randomCell = freeCells.get((int) (Math.random()*freeCells.size()));
		randomCell.setMarble(marble);
	}
	
	private void validateSavings (int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException {
		// check if it is already in safe zone
		if (positionInSafeZone != -1) {
			throw new InvalidMarbleException ("Marble is already in Safe Zone"); 
		}
		
		// check that it is on the track
		if (positionOnTrack < 0 || positionOnTrack >= 100) {
			throw new InvalidMarbleException ("Marble is not on Track"); 
		}
		
		/* To whomever reading this, make sure that in the parent method calling "sendToSafe", i checked that the marble i am saving is my own marble 
			and not the opponent marble as this check can't be done using the parameters
		 */
	}
	
	public void destroyMarble (Marble marble) throws IllegalDestroyException {
		Colour currentColour = gameManager.getActivePlayerColour();
		Colour marbleColour = marble.getColour();
		// this is to check the marble colour is my own 
		if (marbleColour.equals(currentColour)) {
			throw new IllegalDestroyException ("Attempting to destroy your own marble");
		}
		
		int positionInPath = getPositionInPath(track, marble);
	    validateDestroy(positionInPath);
	    // if valid, destroy by removing it from the board completely  
	    Cell current = track.get(positionInPath); 
	    current.setMarble(null);
	}
	
	private void validateDestroy (int positionInPath) throws IllegalDestroyException {
		// check it is on the track
		if (positionInPath == -1) {
			throw new IllegalDestroyException ("Not on Track");
		}
		
		// check that it is a normale/entry cell and not base or safe
		Cell c = track.get(positionInPath); 
		if (c.getCellType() == CellType.BASE || c.getCellType() == CellType.SAFE) {
			throw new IllegalDestroyException ("Marble is on a protected Cell, Can't Destroy");
		}
	}
	
	public ArrayList<Marble> getActionableMarbles() {
		ArrayList<Marble> actionableMarbles = new ArrayList<>(); 
		Colour current = gameManager.getActivePlayerColour();
		
		// add all marbles on track 
		for (Cell c : track) {
			Marble m = c.getMarble();
			if (m != null) actionableMarbles.add(m);
		}
		
		// my safezone marbles 
		for (SafeZone sz : safeZones) {
			if (sz.getColour() == current) {
				ArrayList<Cell> szc = sz.getCells();
				for (Cell c : szc) {
					Marble m = c.getMarble(); 
					if (m!= null) actionableMarbles.add(m);
				}
			}
		}
		
		return actionableMarbles; 
	}
}
