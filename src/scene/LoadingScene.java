package scene;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.scene.effect.Glow;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import model.Colour;
import model.player.Marble;
import view.boardView.MarbleView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LoadingScene extends StackPane {
    private static final double SPACING = 100;
    private static final Duration STEP_DELAY = Duration.millis(200);
    private static final double BASE_SCALE = 3;
    private static final double HIGHLIGHT_SCALE = 5;
    private static final List<String> TIPS = Arrays.asList(
        "If You Got Boared, Change The Theme By Pressing T (Turn The Magic On)."
   
    );

    private final Label tipLabel;

    public LoadingScene() {
        // Background
        setStyle("-fx-background-color: black;");

        // Create and size marbles at 3×
        MarbleView[] marbles = new MarbleView[]{
            new MarbleView(new Marble(Colour.RED),"/resources/themes/anime/RED_marble.png"),
            new MarbleView(new Marble(Colour.GREEN), "/resources/themes/anime/GREEN_marble.png"),
            new MarbleView(new Marble(Colour.BLUE), "/resources/themes/anime/BLUE_marble.png"),
            new MarbleView(new Marble(Colour.YELLOW), "/resources/themes/anime/YELLOW_marble.png")
        };
        for (MarbleView mv : marbles) {
            mv.setScaleX(BASE_SCALE);
            mv.setScaleY(BASE_SCALE);
        }

        HBox marbleBox = new HBox(SPACING, marbles);
        marbleBox.setAlignment(Pos.CENTER);
        marbleBox.prefWidthProperty().bind(this.widthProperty());
        
        // Tip label (base font 14, scaled 3×)
        tipLabel = new Label(getRandomTip());
        tipLabel.setFont(Font.font(14));
        tipLabel.setScaleX(3);
        tipLabel.setScaleY(3);
        tipLabel.setTextFill(Color.WHITE);
        StackPane.setAlignment(tipLabel, Pos.BOTTOM_CENTER);
        StackPane.setMargin(tipLabel, new Insets(0, 0, 400, 0));

        getChildren().addAll(marbleBox, tipLabel);

        playSequentialAnimation(marbles);
    }

    public Scene createScene() {
        return new Scene(this, 1920, 1080);
    }

    private void playSequentialAnimation(MarbleView[] marbles) {
        SequentialTransition sequence = new SequentialTransition();

        for (int i = 0; i < marbles.length; i++) {
            MarbleView mv = marbles[i];

            // Scale Up + Glow
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), mv);
            scaleUp.setToX(HIGHLIGHT_SCALE);
            scaleUp.setToY(HIGHLIGHT_SCALE);

            // Glow Effect
            scaleUp.setOnFinished(e -> mv.applyGlow(Color.DODGERBLUE));

            // Scale Down
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), mv);
            scaleDown.setToX(BASE_SCALE);
            scaleDown.setToY(BASE_SCALE);

            // Remove glow after scaling down
            scaleDown.setOnFinished(e -> mv.setEffect(null));

            // Overlap trick: pause before next starts
            PauseTransition pause = new PauseTransition(Duration.millis(10));

            // Parallel = scaleUp then scaleDown with slight pause in between
            SequentialTransition marbleAnim = new SequentialTransition(
                scaleUp,
                pause,
                scaleDown
            );

            sequence.getChildren().add(marbleAnim);
        }

        // Loop the entire sequence
        sequence.setCycleCount(Animation.INDEFINITE);
        sequence.play();
    }

    private String getRandomTip() {
        return TIPS.get(new Random().nextInt(TIPS.size()));
    }
}