package view.boardView;

import java.util.HashMap;
import java.util.Map;

import view.ImageCache;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.player.Marble;

public class MarbleView extends ImageView {
	private final Marble marble ; 
	private boolean selected ; 

	private static final double DEFAULT_MARBLE_SIZE = 25;
	private static final double SCALE_ANIMATION_DURATION_MS = 200; 
	
	
	public static final Map<Marble, MarbleView> MarbleToViewMap = new HashMap<>();
	
	public MarbleView (Marble marble){
        super(ImageCache.getImage("/resources/themes/anime/" + marble.getColour().name() + "_marble.png"));
		this.marble = marble ; 
		this.selected = false ; 
        this.setPreserveRatio(true);
        this.setSmooth(true);
        this.setFitHeight(DEFAULT_MARBLE_SIZE);
        this.setClip(new Circle(90));
		MarbleToViewMap.put(marble,this);
	}
	
	public void setSelected(boolean selected){
		this.selected = selected ;
		if (selected){
			applyGlow(Color.DODGERBLUE);
			scaleMarble(1.3);
		}else {
			setEffect(null);
			scaleMarble(1.0);
		}
	}
	public void addHoverEffect(){
		this.setOnMouseEntered(e ->{
			if(!isSelected()){
				applyGlow(Color.GOLD);
				scaleMarble(1.3);
			}
		});
		this.setOnMouseExited(e ->{
			if(!isSelected()){
				setEffect(null);
				scaleMarble(1.0);
			}
		});
	}
	
	public void animateScale(double targetScale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(SCALE_ANIMATION_DURATION_MS), this);
        st.setToX(targetScale);
        st.setToY(targetScale);
        st.setInterpolator(Interpolator.EASE_BOTH);
        st.play();
    }
	
	public void applyGlow(Color color) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setWidth(30);
        glow.setHeight(30);
        this.setEffect(glow);
    }
	
	private void scaleMarble(double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(SCALE_ANIMATION_DURATION_MS), this);
        st.setToX(scale);
        st.setToY(scale);
        st.setInterpolator(Interpolator.EASE_BOTH); // Smoother animation
        st.play();
    }
	
	public void removeHoverEffect(){
		this.setOnMouseEntered(null);
		this.setOnMouseExited(null);
	}

	public boolean isSelected() {
		return selected;
	}

	public Marble getMarble() {
		return marble;
	}

}
