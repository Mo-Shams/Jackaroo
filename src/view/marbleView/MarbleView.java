package view.marbleView;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.player.Marble;

public class MarbleView extends StackPane{
	private final Marble marble;
	private final ImageView imageView;
	private boolean selected;
	
	public MarbleView(Marble marble){
		selected = false;
		this.marble = marble;
		String imageName = marble.getColour().toString() + "_marble.png";
		Image image = new Image(getClass().getResourceAsStream("/resources/marble_images/" + imageName));
		imageView = new ImageView(image);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);
		imageView.setFitHeight(50);
		getChildren().add(imageView);
	}

	public Marble getMarble() {
		return marble;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
