package view.boardView;

import java.util.ArrayList;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Colour;
import model.player.Marble;
import engine.Game;
import engine.board.Cell;

public class TrackView {
	
	private ArrayList<CellView> track ;
	private Group root ;
	public TrackView(ArrayList<Cell> track, Group root){
		this.root = root ;
		this.track = new ArrayList<>();
		setupView();
	}
	
	
	public void setupView(){
		  for (int i = 0; i < 100; i++) {
		    	CellView cellView = new CellView(i,0);
		        track.add(cellView);
		        root.getChildren().add(cellView.getCell());
		   }
		  
	}
	
		
	public void moveMarbleAnimation(int fromIndex, int toIndex, Color colour) {
	    Circle start = track.get(fromIndex).getCell();
	    Circle end = track.get(toIndex).getCell();

	    Circle animatedMarble = new Circle(start.getCenterX(), start.getCenterY(), 14, colour);
	    root.getChildren().add(animatedMarble);

	    TranslateTransition translate = new TranslateTransition(Duration.millis(500), animatedMarble);
	    translate.setByX(end.getCenterX() - start.getCenterX());
	    translate.setByY(end.getCenterY() - start.getCenterY());

	    ScaleTransition scale = new ScaleTransition(Duration.millis(200), animatedMarble);
	    scale.setToX(1.5);
	    scale.setToY(1.5);
	    scale.setAutoReverse(true);
	    scale.setCycleCount(5);

	    ParallelTransition moveAndPulse = new ParallelTransition(translate, scale);

	    moveAndPulse.setOnFinished(e -> root.getChildren().remove(animatedMarble));
	    moveAndPulse.play();
	}
}
