package view.playersView;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import model.Colour;
import view.ImageCache;

public class PlayerProfile extends StackPane{
	private ScaleTransition pulseEffect ;
	
	private static final double SIZE = 200;
	private final Label name;
	private final Colour colour;
	private boolean active, nextActive;
	private final Circle circle;
	
	public PlayerProfile(String name, Colour colour, boolean active, boolean nextActive){
		this.pulseEffect = this.setPulseEffect();
		this.name = new Label(name);
		this.name.setFont(Font.font("Arial", 25));
		this.colour = colour;
		Color color = Color.valueOf(colour.toString());
		this.name.setTextFill(color);
		circle = new Circle(SIZE * 0.4);
		// circle.setStroke(color);
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
		// pulseEffect.play();
	}
	
	public void setActive(boolean active){
		this.active = active;
		if(active){
			startPulse();
			circle.setEffect(null);
		}
		else
			stopPulse();
	}
	
	public void setNextActive(boolean nextActive){
		this.nextActive = nextActive;
		if(nextActive)
			applyGlow(Color.VIOLET);
		else circle.setEffect(null);
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
	
	
	public RotateTransition setRotation () {
		setActive(false);
		setNextActive(false);
		circle.getStrokeDashArray().addAll(15.0, 30.0); // dash, gap
		RotateTransition rotate = new RotateTransition(Duration.seconds(2), circle);
		rotate.setByAngle(360);
		rotate.setCycleCount(Animation.INDEFINITE);
		rotate.setInterpolator(Interpolator.LINEAR); // Smooth constant speed
		return rotate;
	}
	
	public void startPulse() {
	    stopPulse(); // just to be clean
	    pulseEffect = setPulseEffect();
	    pulseEffect.play();
	}
	
	public void stopPulse() {
	    if (pulseEffect != null) {
	        pulseEffect.stop(); // stop the animation
			ScaleTransition stop = new ScaleTransition(Duration.seconds(0.25), circle);
			stop.setToX(1.0);
			stop.setToY(1.0);
	    }
	}

	
	public ScaleTransition setPulseEffect(){
		ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.5), circle);
		pulse.setFromX(1.0);
		pulse.setFromY(1.0);
		pulse.setToX(1.2);
		pulse.setToY(1.2);
		pulse.setCycleCount(Animation.INDEFINITE);
		pulse.setAutoReverse(true); // scale back down
		pulse.setInterpolator(Interpolator.EASE_BOTH); // smooth breathing effect
		return pulse ;
	}

	public Colour getColour() {
		return colour;
	}

	public ScaleTransition getPulseEffect() {
		return pulseEffect;
	}
	
	
	
}