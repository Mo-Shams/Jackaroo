package scene;

import java.util.ArrayList;
import java.util.List;

import view.playersView.PlayerProfile;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EndScreenScene {
    private final List<PlayerProfile> players;

    public EndScreenScene(List<PlayerProfile> players) {
        if (players.size() < 4) {
            throw new IllegalArgumentException("Need at least 4 PlayerProfiles");
        }
        this.players = new ArrayList<>(players);
    }

    public Scene createScene() {
        // Root pane with gradient background
        Stop[] stops = {
            new Stop(0, Color.web("#FFD700")),
            new Stop(1, Color.web("#FFEC8B"))
        };
        LinearGradient gradient = new LinearGradient(
            0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops
        );
        StackPane root = new StackPane();
        root.setPadding(new Insets(40));
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // Winner and losers
        PlayerProfile winner = configureProfile(players.get(0), true);
        HBox losers = new HBox(100);
        losers.setMaxSize(Region.USE_PREF_SIZE, 200);
        //losers.setAlignment(Pos.CENTER);
        // no internal padding here—layout spacing handles vertical gap
        for (int i = 1; i < players.size(); i++) {
            losers.getChildren().add(configureProfile(players.get(i), false));
        }

        // Group them in a VBox, center the group
        VBox layout = new VBox(40, winner, losers);
        layout.setSpacing(300);
        layout.setAlignment(Pos.CENTER);
        layout.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        // Optionally add margin on losers to tweak vertical spacing:
        // VBox.setMargin(losers, new Insets(20, 0, 0, 0));
        
        StackPane.setAlignment(layout, Pos.BOTTOM_CENTER);
        root.getChildren().add(layout);
        
        // Fade animations
        SequentialTransition seq = new SequentialTransition();
        fadeInNodes(seq, losers.getChildren(), 0.5, 0.5);
        fadeInNode(seq, winner, 0.7, 0.5);
        seq.play();
        return new Scene(root, 1920, 1080);
    }

    private PlayerProfile configureProfile(PlayerProfile original, boolean isWinner) {
        original.setActive(false);
        original.setNextActive(false);
        original.setProfileImage(isWinner);
        original.setScaleX(isWinner ? 3.0 : 1.0);
        original.setScaleY(isWinner ? 3.0 : 1.0);
        return original;
    }

    private void fadeInNodes(SequentialTransition seq, Iterable<javafx.scene.Node> nodes, double duration, double delay) {
        for (javafx.scene.Node node : nodes) {
            node.setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.seconds(duration), node);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.setDelay(Duration.seconds(delay));
            seq.getChildren().add(ft);
        }
    }

    private void fadeInNode(SequentialTransition seq, javafx.scene.Node node, double duration, double delay) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.seconds(duration), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDelay(Duration.seconds(delay));
        seq.getChildren().add(ft);
    }
}
