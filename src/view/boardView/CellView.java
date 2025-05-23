package view.boardView;

import java.util.HashMap;
import java.util.Map;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import engine.board.Cell;
import engine.board.CellType;

public class CellView extends StackPane {
	private static final double DEFAULT_RADIUS = 12;
	private static final Color DEFAULT_COLOR = Color.DARKSLATEBLUE;
	private static final Color FILLING_COLOR = Color.LIGHTGRAY;
	private static final int MARBLE_SPEED = 100 ;

	
    private final Cell cell;
    private final Circle circle;
    private MarbleView marbleView;
    
    private CellView next ; 
    private CellView prev ; 
    private CellView enter ;
    
    private boolean wasTrap ;
    
    
	public static final Map<Cell, CellView> cellToViewMap = new HashMap<>();

    
    public CellView(Cell cell){
    	this(cell, DEFAULT_COLOR);
    }

    public static int getMarbleSpeed() {
		return MARBLE_SPEED;
	}

	public CellView(Cell cell, Color strokeColor) {
        this.cell = cell;
        circle = createCircle(strokeColor);
        this.getChildren().add(circle);
        StackPane.setAlignment(circle, Pos.CENTER);

        if (cell.getCellType() == CellType.SAFE) 
            applyGlow(strokeColor);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        cellToViewMap.put(cell,this);
    }

    private Circle createCircle(Color strokeColor) {
        Circle circle = new Circle(DEFAULT_RADIUS);
        circle.setFill(FILLING_COLOR);
        // if (cell.isTrap()) circle.setFill(Color.BLACK);// debuging 
        circle.setStroke(strokeColor);
        circle.setStrokeWidth(2);
        return circle;
    }
    
    public void moveMarbleTo(CellView target) {
		if (this == target) {
			if (target.getCell().getMarble() == null) wasTrap = true ;
			target.setMarbleView(marbleView);
			return ;
		}
		if (this.getCell().getCellType() == CellType.ENTRY && target.getCell().getCellType() == CellType.SAFE){
			return ;
		}
		PauseTransition pause = new PauseTransition(Duration.millis(MARBLE_SPEED));
		pause.setOnFinished(e -> {
			//System.out.println(marbleView);
			next.setMarbleView(this.marbleView);
			next.moveMarbleTo(target);
		});
		pause.play();
    }
    
    public void moveBackword(int i) {
 		if (i == 0) {
 			if (this.getCell().getMarble() == null) wasTrap = true ;
 			this.setMarbleView(marbleView);
 			return ;
 		}
 		PauseTransition pause = new PauseTransition(Duration.millis(MARBLE_SPEED));
 		pause.setOnFinished(e -> {
 			prev.setMarbleView(this.marbleView);
 			prev.moveBackword(i-1);
 		});
 		pause.play();
     }
    
    // Add or update the marble in the cell
    public void setMarbleView(MarbleView marbleView) {
        this.marbleView = marbleView;     // this might be wrong 
        if (!getChildren().contains(marbleView) && marbleView != null) {
            getChildren().add(marbleView);
            StackPane.setAlignment(marbleView, Pos.CENTER);
        }
    }

    // Clear the marble (e.g. when moving it elsewhere)
    public void removeMarbleView() {
        if (marbleView != null) {
            getChildren().remove(marbleView);
            marbleView = null;
        }
    }
    
    private void applyGlow(Color color){
    	if(color == null) return;
    	DropShadow glow = new DropShadow();
    	glow.setColor(color);
    	glow.setWidth(25);
    	glow.setHeight(25);
    	circle.setEffect(glow);
    }
    
    
   
    
    // ----------------------- Getters & Setters -----------------------------------------
    
    public Cell getCell() {
        return cell;
    }

    public Circle getCircle() {
        return circle;
    }

    public MarbleView getMarbleView() {
        return marbleView;
    }

	public CellView getNext() {
		return next;
	}

	public void setNext(CellView next) {
		this.next = next;
	}

	public CellView getPrev() {
		return prev;
	}

	public boolean isWasTrap() {
		return wasTrap;
	}

	public void setWasTrap(boolean wasTrap) {
		this.wasTrap = wasTrap;
	}

	public void setPrev(CellView prev) {
		this.prev = prev;
	}

}
