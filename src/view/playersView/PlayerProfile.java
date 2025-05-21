package view.playersView;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import model.Colour;
import view.ImageCache;

public class PlayerProfile extends StackPane{
	private static final double SIZE = 200;
	private final Label name;
	private final Colour colour;
	private boolean active, nextActive;
	private final Circle circle;
	
	public PlayerProfile(String name, Colour colour, boolean active, boolean nextActive){
		this.name = new Label(name);
		this.name.setFont(Font.font("Arial", 25));
		this.colour = colour;
		Color color = Color.valueOf(colour.toString());
		this.name.setTextFill(color);
		circle = new Circle(SIZE * 0.4);
		circle.setStroke(color);
		circle.setStrokeWidth(5);
		String imagePath = "/resources/player_images/" + name + ".png";
		Image image = ImageCache.getImage(imagePath);
		circle.setFill(new ImagePattern(image));
		setActive(active);
		setNextActive(nextActive);
		StackPane.setAlignment(circle, Pos.TOP_CENTER);
		StackPane.setAlignment(this.name, Pos.BOTTOM_CENTER);
		this.getChildren().addAll(circle, this.name);
		this.setMaxSize(SIZE, SIZE);
	}
	
	public void setActive(boolean active){
		this.active = active;
		if(active)
			//circle.setStroke(Color.GREEN);
			 applyGlow(Color.DEEPSKYBLUE);
		
		else
			circle.setEffect(null);
	}
	
	public void setNextActive(boolean nextActive){
		this.nextActive = nextActive;
		if(nextActive)
			//circle.setStroke(Color.VIOLET);
			applyGlow(Color.VIOLET);
		else if (!active) circle.setEffect(null);
	}
	
	private void applyGlow(Color color) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setSpread(0.5);
        glow.setRadius(120);
        //glow.setWidth(60);
        //glow.setHeight(40);
        circle.setEffect(glow);
    }
	
	public void setProfileImage(boolean isWinner) {
		String basePath = isWinner ? "winners" : "losers";
		String path ;
		if (name.getText().equals("CPU 1") || name.getText().equals("CPU 2") || name.getText().equals("CPU 3")) 
			path = "/resources/EndScreenProfiles/" + basePath + "/" + name.getText() + ".png";
		else path = "/resources/EndScreenProfiles/" + basePath + "/default.png";
	    Image image = ImageCache.getImage(path);
	    if (image != null) {
	    	circle.setFill(new ImagePattern(image));
	    }
	}
	

	public Colour getColour() {
		return colour;
	}
	
}