package scene;

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
    private static final Duration STEP_DELAY = Duration.millis(400);
    private static final double BASE_SCALE = 3;
    private static final double HIGHLIGHT_SCALE = 5;
    private static final List<String> TIPS = Arrays.asList(
        "Tip: If you want your character human, change your name to 'Walid' next time.",
        "Tip: A 4 Card at the start gives you a great advantage.",
        "Tip: Plan your moves ahead of time.",
        "Tip: Use the 'S' key to field for yourself! But make sure the bots don't see you ;).",
        "Tip: Field Marbles in the Base Cell to make your opponent Stuck"
    );

    private final Label tipLabel;

    public LoadingScene() {
        // Background
        setStyle("-fx-background-color: linear-gradient(to bottom, #2ecc71, #3498db);");

        // Create and size marbles at 3×
        MarbleView[] marbles = new MarbleView[]{
            new MarbleView(new Marble(Colour.RED),"/resources/themes/original/RED_marble.png"),
            new MarbleView(new Marble(Colour.GREEN), "/resources/themes/original/GREEN_marble.png"),
            new MarbleView(new Marble(Colour.BLUE), "/resources/themes/original/BLUE_marble.png"),
            new MarbleView(new Marble(Colour.YELLOW), "/resources/themes/original/YELLOW_marble.png")
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
        StackPane.setMargin(tipLabel, new Insets(0, 0, 30, 0));

        getChildren().addAll(marbleBox, tipLabel);

        playSequentialAnimation(marbles);
    }

    public Scene createScene() {
        return new Scene(this, 1920, 1080);
    }

    private void playSequentialAnimation(MarbleView[] marbles) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < marbles.length; i++) {
            MarbleView mv = marbles[i];
            // KeyFrame to grow to 5× with glow
            timeline.getKeyFrames().add(new KeyFrame(
                STEP_DELAY.multiply(i),
                e -> {
                    mv.applyGlow(Color.DODGERBLUE);
                    mv.animateScale(HIGHLIGHT_SCALE);
                }
            ));
            // KeyFrame to revert to 3× and remove glow
            timeline.getKeyFrames().add(new KeyFrame(
                STEP_DELAY.multiply(i).add(Duration.millis(200)),
                e -> {
                    mv.setEffect(null);
                    mv.animateScale(BASE_SCALE);
                }
            ));
        }
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private String getRandomTip() {
        return TIPS.get(new Random().nextInt(TIPS.size()));
    }
}