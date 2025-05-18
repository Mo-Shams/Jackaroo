package view.boardView;

import java.util.HashMap;
import java.util.Map;

import view.ImageCache;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.player.Marble;

public class MarbleView extends ImageView {
	private final Marble marble ; 
	private boolean selected ; 

	private static final double DEFAULT_MARBLE_SIZE = 30 ;
	private static final double SCALE_ANIMATION_DURATION_MS = 200; 
	
	
	public static final Map<Marble, MarbleView> MarbleToViewMap = new HashMap<>();
	
	public MarbleView (Marble marble){
        super(ImageCache.getImage("/resources/marble_images/" + marble.getColour().name() + "_marble.png"));
		this.marble = marble ; 
		this.selected = false ; 
        this.setPreserveRatio(true);
        this.setSmooth(true);
        this.setFitHeight(DEFAULT_MARBLE_SIZE);
        
		MarbleToViewMap.put(marble,this);
	}
	
	public void setSelected(boolean selected){
		this.selected = selected ;
		if (selected){
			applyGlow(Color.valueOf(marble.getColour().toString()));
			scaleMarble(1.1);
		}else {
			setEffect(null);
			scaleMarble(1.0);
		}
	}
	
	private void applyGlow(Color color) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setWidth(10);
        glow.setHeight(10);
        this.setEffect(glow);
    }
	
	private void scaleMarble(double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(SCALE_ANIMATION_DURATION_MS), this);
        st.setToX(scale);
        st.setToY(scale);
        st.setInterpolator(Interpolator.EASE_BOTH); // Smoother animation
        st.play();
    }

	public boolean isSelected() {
		return selected;
	}

	public Marble getMarble() {
		return marble;
	}

}
