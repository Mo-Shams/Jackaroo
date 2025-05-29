// scene/AboutScene.java (updated with moving background marble animation)
package scene;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import scene.AnimatedMarbles;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.playersView.PlayerProfile;
import model.Colour;
import scene.AnimatedMarbles;
import view.ImageCache;

import java.awt.Desktop;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import controller.SoundManager;

/**
 * AboutScene shows animated marble background, "About" text, and clickable player profiles.
 */
public class AboutScene {
    private static final String[] LINES = {
    	  "Jackaroo: A New Game Spin",
    	  "",
    	  "DESCRIPTION",
    	  "Confused about what the card does? Hover over it to find out!",
    	  "Wish to field a marble without having to wait for a fielding card? Simply use the WASD keys to field for yourself and all other players!",
    	  "Once you get all your marbles in the Safe Zone, you win the game!",
    	  "",
    	  "CHANGES",
    	  "Introduced two new additions to the game: Burner and Saver to enrich the gameplay for the user",
    	  "Trap cells to keep the player on edge while playing and many more for you to discover ;)",
    	  "",
    	  "LICENSE",
    	  "© 2025 GUC - MET Department. All rights reserved.",
    	  "",
    	  "The game was crafted with passion by our dedicated team",
    	  "We hope you enjoy every moment of gameplay!",
    	  "Click on our profiles below to visit our GitHub pages"

    };
    private final Stage stage;
    private final VBox textContainer = new VBox(5);
    private final Pane dimOverlay = new Pane();
	
	private final List<String> names = Arrays.asList(
	    "Youssef\nTarek", "Mohammad\nWalid", "Youssef\nMostafa", "Mohammad\nShams"
	);
    
	private final List<String> urls = Arrays.asList(
        "https://github.com/Bob-youssef",
        "https://github.com/MoWaleid",
        "https://github.com/Yootooo",
        "https://github.com/Mo-Shams"
    );
    
    private final List<Colour> colours = Arrays.asList(
        Colour.BLUE, Colour.GREEN, Colour.RED, Colour.YELLOW
    );

    public AboutScene(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
    	Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = screenBounds.getWidth();
        double height = screenBounds.getHeight();

        // Add the animated Background
        Pane backgroundPane = AnimatedMarbles.fullScreen();
        setupDimOverlay(width, height);

        // Ading the ScrollPane
        ScrollPane scroll = createScrollPane(height);
        playTypingEffect();
        
        // Profile cards
        HBox profilesBox = createProfilesBox();
        Label back = createBackButton();
        
        // Setup the content
        VBox content = new VBox(20, scroll, profilesBox);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40, 20, 20, 20));
        
        StackPane root = new StackPane(backgroundPane, dimOverlay, content, back);
        // add the back button in the top left for now
        StackPane.setAlignment(back, Pos.TOP_LEFT);

        SoundManager.playMusic("ending.m4a");
        
        return new Scene(root, width, height);
    }
    
    private void setupDimOverlay(double width, double height) {
        dimOverlay.setPrefSize(width, height);
        dimOverlay.setStyle("-fx-background-color: black;");
        dimOverlay.setOpacity(0.6);
    } 
    
    private ScrollPane createScrollPane(double totalHeight) {
        ScrollPane scroll = new ScrollPane(textContainer);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(totalHeight * 0.65);
        scroll.prefWidthProperty().bind(stage.widthProperty().multiply(0.7));
        textContainer.setPadding(new Insets(20));
        textContainer.setAlignment(Pos.CENTER);
        return scroll;
    }
    
    private void playTypingEffect() {
    	textContainer.getChildren().clear();
        SequentialTransition allLines = new SequentialTransition();
        for (String line : LINES) {
            // For each line, create an initially empty Label
            Label lbl = new Label("");
            lbl.setFont(Font.font(18));
            lbl.setTextFill(Color.WHITE);
            textContainer.getChildren().add(lbl);

            // Build a Timeline that adds one more character every 50ms
            Timeline charByChar = new Timeline();
            for (int i = 0; i < line.length(); i++) {
                final int idx = i;
                charByChar.getKeyFrames().add(
                    new KeyFrame(
                        Duration.millis(35 * (i + 1)),
                        e -> lbl.setText(line.substring(0, idx + 1))
                    )
                );
            }

            // Optional: add a short pause after each full line
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

            // Add the typing timeline and pause into the master sequence
            allLines.getChildren().addAll(charByChar, pause);
        }
        
        allLines.setOnFinished(e -> {
            addingFinalLine(textContainer);
        });

        allLines.play();
    }
    
    private HBox createProfilesBox() {
        HBox box = new HBox(80);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        for (int i = 0; i < names.size(); i++) {
            PlayerProfile profile = new PlayerProfile(names.get(i), colours.get(i), false, false);
            String url = urls.get(i);
            profile.setOnMouseClicked(e -> openUrl(url));
            box.getChildren().add(profile);
        }
        return box;
    }
    
    private Label createBackButton() {
        Label back = new Label("« Back");
        back.setFont(Font.font(16));
        back.setStyle("-fx-text-fill: white; -fx-cursor: hand;");
        back.setTranslateX(10);
        back.setTranslateY(10);
        back.setOnMouseClicked(e -> {
            try {
                WelcomeScene welcome = new WelcomeScene(stage);
                stage.setScene(welcome.createScene());
                stage.setFullScreen(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return back;
    }
    
    private void addingFinalLine(VBox textContainer) {
    	// Add the pdf link, after all the lines
        Label pdfPrefix = new Label("Still Confused about the rules? You can check out this pdf for the game rules ");
        pdfPrefix.setFont(Font.font(24));
        pdfPrefix.setTextFill(Color.WHITE);
        Hyperlink pdfLink = new Hyperlink("online");
        pdfLink.setFont(Font.font(24));
        pdfLink.setOnAction(e2 -> openUrl("https://www.jackarooworld.com/wp-content/uploads/2025/03/Jackaroo-Manual.pdf"));
        HBox pdfBox = new HBox(pdfPrefix, pdfLink);
        pdfBox.setAlignment(Pos.CENTER);
        textContainer.getChildren().add(pdfBox);
    }

    private void openUrl(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
