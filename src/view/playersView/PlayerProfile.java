package view.playersView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import controller.ThemesManager;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import model.Colour;
import view.ImageCache;


	

public class PlayerProfile extends StackPane {
	
	private ScaleTransition pulseEffect ;

    private static final double WIDTH = 450;
    private static final double HEIGHT = 200;
    private final Label name;
    private final Colour colour;
    private boolean active, nextActive;
    private final Circle circle;
    private Label chatBubble;
    
    private static final String[] THEME_FOLDERS = {
    	"original",
        "ancient_civilizations",              // theme 0
        "anime",  // theme 1
        "sci-fi_dystopia", // theme 3
        "original"
    };

    public PlayerProfile(Label name, Colour colour, boolean active, boolean nextActive, int playerIndex) {
		this.pulseEffect = this.setPulseEffect();
    	
        this.name = name;
        this.name.setFont(Font.font("Arial", 32));
        this.name.setAlignment(Pos.CENTER);
        this.colour = colour;
        Color color = Color.valueOf(colour.toString());
        this.name.setTextFill(color);

        circle = new Circle(HEIGHT * 0.4);
        circle.setStroke(color);
        circle.setStrokeWidth(5);
        String imagePath = "/resources/themes/original/" + name.getText() + ".png";
        Image image = ImageCache.getImage(imagePath);
        circle.setFill(new ImagePattern(image));

        setActive(active);
        setNextActive(nextActive);

        VBox profileContainer = new VBox(5);
        profileContainer.setAlignment(Pos.CENTER);
        //profileContainer.setStyle("-fx-background-color: lightgray;");
        profileContainer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        profileContainer.getChildren().addAll(circle, this.name);
        chatBubble = new Label();
        chatBubble.setStyle("-fx-background-color: #f5deb3; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-size: 20;");
        chatBubble.setVisible(false);
        // Positioning chat bubble based on player corner
        switch (playerIndex) {
            case 0:
                StackPane.setAlignment(profileContainer, Pos.CENTER_LEFT);
                StackPane.setAlignment(chatBubble, Pos.TOP_RIGHT);
                break;

            case 1:
                StackPane.setAlignment(profileContainer, Pos.CENTER_RIGHT);
                StackPane.setAlignment(chatBubble, Pos.TOP_LEFT);
                break;

            case 2:
                StackPane.setAlignment(profileContainer, Pos.CENTER_RIGHT);
                StackPane.setAlignment(chatBubble, Pos.TOP_LEFT);
                break;

            case 3:
                StackPane.setAlignment(profileContainer, Pos.CENTER_LEFT);
                StackPane.setAlignment(chatBubble, Pos.TOP_RIGHT);
                break;

        }

        this.getChildren().addAll(profileContainer, chatBubble);
        this.setMaxSize(WIDTH, HEIGHT);
        //this.setStyle("-fx-background-color: lightgray;");
    }
    
    public PlayerProfile(String name, Colour colour, boolean active, boolean nextActive) {
        this.name = new Label(name);
        this.name.setFont(Font.font("Arial", 25));
        this.name.setAlignment(Pos.CENTER);
        this.colour = colour;
        Color color = Color.valueOf(colour.toString());
        this.name.setTextFill(color);

        circle = new Circle(HEIGHT * 0.4);
        circle.setStroke(color);
        circle.setStrokeWidth(5);
        String developerName;
        switch(name.charAt(name.length()-1)){
        case 'a': developerName = "yoo"; break;
        case 's': developerName = "shams"; break;
        case 'd': developerName = "walid"; break;
        default : developerName = "el2ot"; break;
        }
        String imagePath = "/resources/developers/" + developerName + ".png";
        Image image = ImageCache.getImage(imagePath);
        circle.setFill(new ImagePattern(image));

        setActive(active);
        setNextActive(nextActive);
        
        VBox profileContainer = new VBox(5);
        profileContainer.setAlignment(Pos.CENTER);
        //profileContainer.setStyle("-fx-background-color: lightgray;");
        profileContainer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        profileContainer.getChildren().addAll(circle, this.name);

        StackPane.setAlignment(circle, Pos.TOP_CENTER);
        StackPane.setAlignment(this.name, Pos.BOTTOM_CENTER);

        this.getChildren().add(profileContainer);
        this.setPrefSize(USE_PREF_SIZE, HEIGHT);
        //this.setStyle("-fx-background-color: lightgray;");
    }
    

	public void setActive(boolean active){
		this.active = active;
		if(active){
			startPulse();
			circle.setEffect(null);
		}
		else
			stopPulse();
	}
	
	public void setNextActive(boolean nextActive){
		this.nextActive = nextActive;
		if(nextActive)
			applyGlow(Color.LIGHTSKYBLUE);
		else circle.setEffect(null);
	}
	

    private void applyGlow(Color color) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setSpread(0.5);
        glow.setRadius(120);
        circle.setEffect(glow);
    }

	public void setProfileImage(boolean isWinner) {
		int themeId = ThemesManager.theme;
		String themeFolder = "original";
	    if (themeId >= 0 && themeId < THEME_FOLDERS.length) {
	        themeFolder = THEME_FOLDERS[themeId];
	    }
		String basePath = isWinner ? "Winning" : "Losing";
		String prefix;
		Set<String> validNames;
	    switch (themeFolder) {
	        case "original": {
	            prefix = "/resources/themes/original/" + basePath + "/";
	            validNames = new HashSet<>(Arrays.asList("cool", "muscles", "normal", "smart"));
	            
	            break;
	        }
	        case "anime": {
	            prefix = "/resources/themes/anime/" + basePath + "/";
	            validNames = new HashSet<>(Arrays.asList("luffy", "naruto", "killua", "sun_jin-woo"));
	            break;
	        }
	        case "sci-fi_dystopia": {
	            prefix = "/resources/themes/sci-fi_dystopia/" + basePath + "/";
	            validNames = new HashSet<>(Arrays.asList("cyborge", "enforcer", "mystic", "survivor"));
	            break;
	        }
	        case "ancient_civilizations": {
	            prefix = "/resources/themes/ancient_civilizations/" + basePath + "/";
	            validNames = new HashSet<>(Arrays.asList("greek", "pharoah", "roman", "viking"));
	            break;
	        }
	        default: {
	            // fallback to original
	            prefix = "/resources/themes/original/" + basePath + "/";
	            validNames = new HashSet<>(Arrays.asList("cool", "muscles", "normal", "smart"));
	            break;
	        }
	    }

	    String playerName = name.getText();
	    String finalName = validNames.contains(playerName.toLowerCase())
	        ? playerName
	        : "default";

	    // 5) full path and load
	    String path = prefix + finalName + ".png";
	    Image image = ImageCache.getImage(path);
	    circle.setFill(new ImagePattern(image));
	}
	
	
	
	public void startPulse() {
	    stopPulse(); // just to be clean
	    pulseEffect = setPulseEffect();
	    pulseEffect.play();
	}
	
	public void stopPulse() {
	    if (pulseEffect != null) {
	        pulseEffect.stop(); // stop the animation
			ScaleTransition stop = new ScaleTransition(Duration.seconds(0.25), circle);
			stop.setToX(1.0);
			stop.setToY(1.0);
	    }
	}

	
	public ScaleTransition setPulseEffect(){
		ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.5), circle);
		pulse.setFromX(1.0);
		pulse.setFromY(1.0);
		pulse.setToX(1.2);
		pulse.setToY(1.2);
		pulse.setCycleCount(Animation.INDEFINITE);
		pulse.setAutoReverse(true); // scale back down
		pulse.setInterpolator(Interpolator.EASE_BOTH); // smooth breathing effect
		return pulse ;
	}

    public void showChatMessage(String message) {
        chatBubble.setText("");
        chatBubble.setVisible(true);

        String[] words = message.split(" ");
        Timeline timeline = new Timeline();

        for (int i = 0; i < words.length; i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(150 * i), e -> {
                String currentText = chatBubble.getText();
                if (currentText.isEmpty()) {
                    chatBubble.setText(words[index]);
                } else {
                    chatBubble.setText(currentText + " " + words[index]);
                }
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setOnFinished(e -> {
            // Wait for 2 seconds then hide the chat bubble
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished(event -> hideChatBubble());
            delay.play();
        });

        timeline.play();
    }

    public void hideChatBubble() {
        chatBubble.setVisible(false);
        chatBubble.setText("");
    }

    //--------------------------------------- getters --------------------------------------------------
    
    public Colour getColour() {
        return colour;
    }
	public String getName() {
		return name.getText();
	}

	public ScaleTransition getPulseEffect() {
		return pulseEffect;
	}
	
	
	public boolean isActive() {
		return active;
	}

	public boolean isNextActive() {
		return nextActive;
	}

	public Circle getCircle() {
		return circle;
	}
	public Label getLabel(){
		return name ; 
	}
	
	public void updateTheme (int themeId, int playerIndex){
		switch (playerIndex){
		
		}
	}
	
	
	
}
