package scene;

import java.util.ArrayList;

import view.playersView.PlayerProfile;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EndScreenScene {
    
	private ArrayList <PlayerProfile> players; 
	
	public EndScreenScene(ArrayList<PlayerProfile> players) {
        this.players = players;
    }
	
    public Scene createScene() {
        // Root pane with gradient background
        StackPane root = new StackPane();
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#FFD700")), new Stop(1, Color.web("#FFEC8B")) };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        if (players.size() < 4) {
        	throw new IllegalArgumentException("I need atleast 4 PlayerProfiles");
        }
        
        PlayerProfile winner = createWinnerProfile(players.get(0));
        HBox losers = new HBox(50);
        losers.setAlignment(Pos.CENTER);
        losers.setPadding(new Insets(300, 0, 0, 0));
        for (int i = 1; i < players.size(); i++) {
            losers.getChildren().add(createLosingProfile(players.get(i)));
        }

        // this is to group everything together
        VBox layout = new VBox(20, winner, losers);
        layout.setAlignment(Pos.CENTER);
        root.getChildren().add(layout);
        
        
        SequentialTransition seq = new SequentialTransition();
        animationFadeLosers(seq, losers);
        animationFadeWinner(seq, winner);
        
        seq.play();
        
        Scene scene = new Scene(root, 1920, 1080);
        return scene ;
     
    }
    
    private void animationFadeWinner(SequentialTransition seq,
			PlayerProfile winner) {
    	FadeTransition winnerFt = new FadeTransition(Duration.seconds(0.7), winner);
        winnerFt.setFromValue(0.0);
        winnerFt.setToValue(1.0);
        winnerFt.setDelay(Duration.seconds(0.5));
        seq.getChildren().add(winnerFt);
        winner.setOpacity(0);
	}

	private void animationFadeLosers(SequentialTransition seq, HBox losers) {
    	for (Node node : losers.getChildren()) {
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), node);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setDelay(Duration.seconds(0.5)); 
            seq.getChildren().add(ft);
            node.setOpacity(0);
        }	
	}

	private PlayerProfile createWinnerProfile(PlayerProfile original) {
        
    	original.setNextActive(false); // remove these settings
        original.setActive(false);
        
        original.setProfileImage(true); // change the profile image
        
        original.setScaleX(3.0); // Scale 5 times
        original.setScaleY(3.0);
        
        return original;
    }
    
    private PlayerProfile createLosingProfile(PlayerProfile original) {
        
    	original.setNextActive(false); // remove these settings
        original.setActive(false);
        
        original.setProfileImage(false); // change the profile image
        
        original.setScaleX(1.0); // normal scale
        original.setScaleY(1.0);

        return original;
    }

 
}