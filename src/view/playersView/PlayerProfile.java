package view.playersView;

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

        String imagePath = "/resources/player_images/" + name + ".png";
        Image image = ImageCache.getImage(imagePath);
        circle.setFill(new ImagePattern(image));

        setActive(active);
        setNextActive(nextActive);


        StackPane.setAlignment(circle, Pos.TOP_CENTER);
        StackPane.setAlignment(this.name, Pos.BOTTOM_CENTER);

        this.getChildren().addAll(circle, this.name);
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
		String basePath = isWinner ? "winners" : "losers";
		String path ;
		if (name.getText().equals("CPU 1") || name.getText().equals("CPU 2") || name.getText().equals("CPU 3")) 
			path = "/resources/EndScreenProfiles/" + basePath + "/" + name.getText() + ".png";
		else {
			if (name.getText().equals("Walid")) {
				path = "/resources/EndScreenProfiles/" + basePath + "/Walid.png";
			} else {
				path = "/resources/EndScreenProfiles/" + basePath + "/default.png";
			}
		}
	    Image image = ImageCache.getImage(path);
	    if (image != null) {
	    	circle.setFill(new ImagePattern(image));
	    }
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
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
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
