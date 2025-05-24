package scene;

import javafx.animation.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.util.Duration;
import view.ImageCache;

import java.util.Random;

public class AnimatedMarbles extends Pane {
    private static final String[] DEFAULT_PATHS = {
        "/resources/marble_images/BLUE_marble.png",
        "/resources/marble_images/RED_marble.png",
        "/resources/marble_images/GREEN_marble.png",
        "/resources/marble_images/YELLOW_marble.png"
    };

    public AnimatedMarbles(double width, double height) {
        setPrefSize(width, height);
        setStyle("-fx-background-color: black;");
        startMarbleAnimation(DEFAULT_PATHS, width, height);
    }
    
    public static Pane fullScreen() {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        return new AnimatedMarbles(bounds.getWidth(), bounds.getHeight());
    }

    public AnimatedMarbles(String[] marbleImagePaths, double width, double height) {
        setPrefSize(width, height);
        setStyle("-fx-background-color: black;");
        startMarbleAnimation(marbleImagePaths, width, height);
    }

    private void startMarbleAnimation(String[] paths, double sceneWidth, double sceneHeight) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, e -> {
                String path = paths[new Random().nextInt(paths.length)];
                ImageView marble = createMarble(path, sceneHeight);
                getChildren().add(marble);
                animateMarble(marble, sceneWidth);
            }),
            new KeyFrame(Duration.seconds(0.5))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private ImageView createMarble(String path, double sceneHeight) {
        ImageView marble = new ImageView(ImageCache.getImage(path));
        marble.setPreserveRatio(true);
        marble.setSmooth(true);
        marble.setFitWidth(80);
        marble.setFitHeight(80);
        marble.setLayoutX(-50);
        marble.setLayoutY(new Random().nextDouble() * (sceneHeight - 40));
        return marble;
    }

    private void animateMarble(ImageView marble, double sceneWidth) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(12), marble);
        t.setFromX(0);
        t.setToX(sceneWidth + 100);
        t.setOnFinished(e -> getChildren().remove(marble));
        t.play();
    }
}