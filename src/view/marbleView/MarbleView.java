package view.marbleView;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.player.Marble;

import java.io.InputStream;

public final class MarbleView extends StackPane {
    private static final double DEFAULT_MARBLE_HEIGHT = 40;

    private final Marble marble;
    private final ImageView imageView;
    private boolean selected;

    public MarbleView(Marble marble) {
        this.marble = marble;
        this.selected = false;

        String imageName = marble.getColour().toString() + "_marble.png";
        InputStream imageStream = getClass().getResourceAsStream("/resources/marble_images/" + imageName);

        if (imageStream == null) {
            throw new IllegalArgumentException("Image for marble not found: " + imageName);
        }

        Image image = new Image(imageStream);
        imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setFitHeight(DEFAULT_MARBLE_HEIGHT);

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
        updateSelectionStyle();
    }

    public void setMarbleSize(double height) {
        imageView.setFitHeight(height);
    }

    private void updateSelectionStyle() {
        if (selected) {
            setStyle("-fx-border-color: gold; -fx-border-width: 2;");
        } else {
            setStyle(null);
        }
    }

    @Override
    public String toString() {
        return "MarbleView{" +
                "colour=" + marble.getColour() +
                ", selected=" + selected +
                '}';
    }
}
