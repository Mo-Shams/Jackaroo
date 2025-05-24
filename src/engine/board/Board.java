package engine.board;

import java.util.ArrayList;

import model.Colour;
import model.player.Marble;
import engine.GameManager;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.IllegalSwapException;
import exception.InvalidMarbleException;

public class Board implements BoardManager {
	//instance variables
	private final GameManager gameManager;
	private final ArrayList<Cell> track;
    private final ArrayList<SafeZone> safeZones ;
    private int splitDistance ;
    
    private Cell targetCell  ;
    
    //constructor
    public Board(ArrayList<Colour> colourOrder, GameManager gameManager){
        this.gameManager = gameManager ;
        this.track = new ArrayList<>();
        this.safeZones = new ArrayList<>();
        this.splitDistance = 3 ;
        targetCell = null ; 
        setupTrack();
        for (int i = 0; i < 8; i++) assignTrapCell();

        for (int i = 0; i < 4; i++) {
            safeZones.add(new SafeZone(colourOrder.get(i)));
        }
    }
    
    //helper methods for the constructor
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
    
    
    
    
    
    
    
    //simple getter methods for the instance variables
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
    public Cell getTargetCell() {
		return targetCell;
	}

	//helper getter methods of special positions and cells
    //must be used whenever possible in the upcoming methods
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
    private int getEntryPosition(Colour colour){ // you can use the previous method 
        for (int i = 0; i < safeZones.size(); i++) {
            SafeZone safeZone = safeZones.get(i);
            if (safeZone.getColour() == colour)
                return ((i * 25 - 2) + 100) % 100; 
        }
        return -1 ;
    }
    
    
    
    
    
    
    
    //helper methods to validate the number of steps a marble can move
    //helpers of the method moveBy
    private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException {
        int start = getPositionInPath(track, marble);
        if (start != -1){
            if (steps == -4) return validateStepsForFour(marble, steps, start); // 4 or -4 ? 
            else return validateStepsOnTrack(marble, steps, start); 
        }
        ArrayList<Cell> safezone = getSafeZone(marble.getColour()); 
        start = getPositionInPath(safezone,marble);
        if (start != -1){
            return validateStepsOnSafeZone(marble, steps, start) ; 
        }

        throw new IllegalMovementException("The Marble Cannnot be moved");
    }
    private ArrayList<Cell> validateStepsOnTrack(Marble marble, int steps, int start) throws IllegalMovementException {
    
        int target = start + steps ;
        ArrayList<Cell> path = new ArrayList<>();
 
        for (int i = start; i <= target; i++) {
            if (getEntryPosition(marble.getColour()) == i  && 
                (marble.getColour() == gameManager.getActivePlayerColour())) { 
                
                path.add(track.get(i%100));
                path.addAll(validateStepsOnSafeZone(marble, target - i - 1,0));
                break ;
            }
            path.add(track.get(i%100));
        }
        return path;
    }
    private ArrayList<Cell> validateStepsOnSafeZone(Marble marble, int steps, int start) 
        throws IllegalMovementException {
        ArrayList<Cell> safezone = getSafeZone(marble.getColour());
        ArrayList<Cell> path = new ArrayList<>();
        int target = start + steps  ; // 0, 3

        if (target >= 4) throw new IllegalMovementException("Rank is too high");
        for (int i = start; i <= target; i++) {
            path.add(safezone.get(i));
        }
        return path ;
    }
    private ArrayList<Cell> validateStepsForFour(Marble marble, int steps, int start) throws IllegalMovementException {
        ArrayList<Cell> path = new ArrayList<>();
        int target = start + steps  ; // assuming it is -4 not 4 
        for (int i = start; i >= target; i--) {
            path.add(track.get((i + 100) % 100)); // this order is correct right ? 
        }
        return path ;
        // cannot move the four in the safezone 
    }
    
    //helper method to validate the path returned by validateSteps
    //helper of the method moveBy
    private void validatePath(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalMovementException{
        // Safe Zone Entry         	
//        if (start.getCellType() == CellType.NORMAL && end.getCellType() == CellType.SAFE) {
//        	if (track.get(getEntryPosition(movingMarble)).getMarble() != null && !destroy) {
//                throw new IllegalMovementException("Safe Zone Entry");
//            }
//        }
    	
        // other problems  
        int blockers = 0; 
        for (int i = 1; i < fullPath.size(); i++) {
        	
            Marble found = fullPath.get(i).getMarble();
            //System.out.println(track.indexOf(fullPath.get(i)) != -1 || fullPath.get(i).getCellType() == CellType.SAFE);
            if (found != null){ 
            	// Base Cell Blockage
                if (getBasePosition(found.getColour()) == track.indexOf(fullPath.get(i)))
                    throw new IllegalMovementException("found a cell on it's base");
                
                if (found.getColour() == gameManager.getActivePlayerColour()){ // marble same colour as me 
                	// Self Blockage
                	if (!destroy)  
                        throw new IllegalMovementException("can't pypass or destroy your own marbles");
                    // Safe-Zone Immunity
                    if (fullPath.get(i).getCellType() == CellType.SAFE)
                        throw new IllegalMovementException("Can't destroy cells in your safezone");
                } else { // marble colour not same as the marble moving
                    if(fullPath.get(i).getCellType() == CellType.ENTRY && i != fullPath.size() - 1
                    		&& fullPath.get(i+1).getCellType() == CellType.SAFE && !destroy)
                    	throw new IllegalMovementException("Safe Zone Entry Blocked");
                    if (i != fullPath.size() - 1 && !destroy) {
                        // Path Blockage
                        blockers++;
                        if (blockers > 1) {
                            throw new IllegalMovementException("cannot bypass 2 opponentet marbles");
                        }
                    }
                }
            }
        }
    }
    
    
    
    
    //helper method that carries out the actual movement process
    //helper of the method moveBy
    private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalDestroyException{
        Cell start = fullPath.get(0);
        start.setMarble(null);
        if (destroy) {
            for (int i = 1; i < fullPath.size()-1; i++) {
            	Marble currentMarble = fullPath.get(i).getMarble();
                if (currentMarble != null) {
                    destroyMarble(currentMarble);
                }
            }
        }
	    // if no destroy, just place in the final position
	    Cell end = fullPath.get(fullPath.size()-1);
	    if(end.getMarble() != null)
	    	destroyMarble(end.getMarble());
	    end.setMarble(marble);
	    // handle if target
	    if (end.isTrap()){
	        destroyMarble(marble);
	        end.setTrap(false);
	        assignTrapCell();
	    }
	}
    
    //overriden method from the BoardManager interface that handles the movement using the above helper methods
    @Override
	public void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException {
        ArrayList<Cell> fullPath = validateSteps(marble, steps);
        validatePath(marble, fullPath, destroy);
        move(marble, fullPath, destroy);
        targetCell = fullPath.get(fullPath.size()-1);
	}
    
    
    
    
    
    
    
    //methods to validate the swap process and carrying it out
    private void validateSwap(Marble marble_1, Marble marble_2) throws IllegalSwapException{

        int index1 = getPositionInPath(track, marble_1);
        int index2 = getPositionInPath(track, marble_2);

		if(index1 == -1 || index2 == -1) throw new IllegalSwapException("Marbles cannot be swapped.");

        if (gameManager.getActivePlayerColour() == marble_1.getColour() // marble2 is the opp
            && index2 == getBasePosition(marble_2.getColour())) // opp on his base 
                throw new IllegalSwapException("Opponent on his base cell");
           
        if (gameManager.getActivePlayerColour() == marble_2.getColour() // marble1 is the opp
            && index1 == getBasePosition(marble_1.getColour())) // opp on his base 
                throw new IllegalSwapException("Opponent on his base cell");
      
	}
	public void swap (Marble marble_1, Marble marble_2) throws IllegalSwapException{
		validateSwap(marble_1, marble_2);
        int index1 = getPositionInPath(track, marble_1);
        int index2 = getPositionInPath(track, marble_2);

		track.get(index1).setMarble(marble_2); 
		track.get(index2).setMarble(marble_1);
	}
	
	
	
	
	
	
	//methods to validate the destroy process and carrying it out
    private void validateDestroy (int positionInPath) throws IllegalDestroyException {
		//  check it is on the track
		if (positionInPath == -1) {
			throw new IllegalDestroyException ("Not on Track");
		}
        Marble marble = track.get(positionInPath).getMarble();
        if (marble != null && getBasePosition(marble.getColour()) == positionInPath)
            throw new IllegalDestroyException ("Marble is on a protected Cell, Can't Destroy");
	}
    public void destroyMarble (Marble marble) throws IllegalDestroyException { // no need to validate colours 
		int positionInPath = getPositionInPath(track, marble);
	    validateDestroy(positionInPath);
	    // if valid, destroy by removing it from the board completely  
	    Cell current = track.get(positionInPath); 
	    current.setMarble(null);
        gameManager.sendHome(marble);
	}
    
    
    
    
    
    
    
    //methods to validate the fielding process and carrying it out
    //helpers of the method fieldMarble overriden from the GameManager interface in the Game class
    private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException{
        if (occupiedBaseCell.getMarble() != null && 
            occupiedBaseCell.getMarble().getColour() == gameManager.getActivePlayerColour())
            throw new CannotFieldException("There is already a marble in the Base Cell");

	}
	public void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException{
        int index = getBasePosition(marble.getColour());
		Cell baseCell = track.get(index);
		validateFielding(baseCell);
		if(baseCell.getMarble() != null) destroyMarble(baseCell.getMarble()); 
		baseCell.setMarble(marble);
        
        // should i remove it from the players marbles?
		// the remove was alraedy handled in the fieldMarble in the Game class
	}
	
    
	
	
	
	//methods to validate the saving process and carrying it out
	private void validateSaving (int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException {
		// check if it is already in safe zone
		if (positionInSafeZone != -1) {
			throw new InvalidMarbleException ("Marble is already in Safe Zone"); 
		}
		// check that it is on the track
		if (positionOnTrack == -1) {
			throw new InvalidMarbleException ("Marble is not on Track"); 
		}
	}
	public void sendToSafe(Marble marble) throws InvalidMarbleException {
		// finding the indices of safezone and track and validate 
		Colour marbleColour = marble.getColour(); 
		ArrayList<Cell> safeZoneCells = getSafeZone(marbleColour);
		int positionInSafeZone = getPositionInPath(safeZoneCells, marble);
		int positionOnTrack = getPositionInPath(track, marble); 
		validateSaving(positionInSafeZone, positionOnTrack);

        // set the current cell of that marble to null to remove so to send it to one of the freeCells
        Cell curr_cell = track.get(positionOnTrack);
        curr_cell.setMarble(null);
		ArrayList<Cell> freeCells = new ArrayList<>(); 
		// place all the available free places here to pick one randomly and place marble there
		for (Cell c : safeZoneCells) {
			if (c.getMarble() == null) freeCells.add(c); 
		}
		Cell randomCell = freeCells.get((int) (Math.random()*freeCells.size()));
		randomCell.setMarble(marble);
	}
	
	
	
	
	
	
	
	//method to get the marbles that cards can act on
	public ArrayList<Marble> getActionableMarbles() {
		ArrayList<Marble> actionableMarbles = new ArrayList<>(); 
		ArrayList<Cell> safeZone =  getSafeZone(gameManager.getActivePlayerColour());
		
		// add all marbles on track 
		for (Cell cell : track) {
			Marble marble = cell.getMarble();
			if (marble != null) actionableMarbles.add(marble);
		}
		
		// my safezone marbles 
		for (Cell cell : safeZone) {
			if(cell.getMarble() != null)
				actionableMarbles.add(cell.getMarble());
		}
		return actionableMarbles; 
	}
	//hasn't been used in any other class
}
