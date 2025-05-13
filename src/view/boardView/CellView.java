package view.boardView;

import scene.GameScene;
import model.Colour;
import model.player.Marble;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CellView {
	
		private Circle cell ;
	
		public CellView (int index, int type){
			this.cell = setupTrackCell(index);
		}
		
		public Circle setupTrackCell(int index) {
	    double radius = 10;
	    double gap = 2;
	    double diameter = radius * 2;
	    int numPerSide = 26;

	    double squareSize = numPerSide * diameter + numPerSide * gap;
	    double startX = (1400 - squareSize) / 2;
	    double startY = (900 - squareSize) / 2;

	    double x = 0;
	    double y = 0;

	    if (index < 25) {
	        x = startX;
	        y = startY + squareSize - diameter - index * (diameter + gap);
	    } else if (index < 50) {
	        x = startX + (index - 25) * (diameter + gap);
	        y = startY;
	    } else if (index < 75) {
	        x = startX + squareSize - diameter;
	        y = startY + (index - 50) * (diameter + gap);
	    } else {
	        x = startX + squareSize - diameter - (index - 75) * (diameter + gap);
	        y = startY + squareSize - diameter;
	    }
	    
	    Circle circle = new Circle(x, y, radius, getCellColour(index));
	    return circle;
	}
	
	public Color getCellColour (int i){
		Marble marble = null ;
		// GameScene.getGame().getBoard().getTrack().get(i).getMarble(); 
		if (marble != null){
			if (marble.getColour() == Colour.RED) return Color.RED ;
			if (marble.getColour() == Colour.GREEN) return Color.GREEN ;
			if (marble.getColour() == Colour.YELLOW) return Color.YELLOW ;
			if (marble.getColour() == Colour.BLUE) return Color.BLUE ;
		}
		return Color.BLACK ;	
	}
	
	public Circle getCell(){
		return this.cell ;
	}

}
