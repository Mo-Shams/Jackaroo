package view.playersView;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import model.Colour;
import view.ImageCache;

public class PlayerProfile extends StackPane {
    private static final double WIDTH = 450;
    private static final double HEIGHT = 200;
    private final Label name;
    private final Colour colour;
    private boolean active, nextActive;
    private final Circle circle;
    private final Label chatBubble;

    public PlayerProfile(String name, Colour colour, boolean active, boolean nextActive, int playerIndex) {
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


        chatBubble = new Label();
        chatBubble.setStyle("-fx-background-color: #f5deb3; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-size: 20;");
        chatBubble.setVisible(false);
        // Positioning chat bubble based on player corner
        switch (playerIndex) {
	        case 0:
	        	StackPane.setAlignment(circle, Pos.TOP_LEFT);
	        	StackPane.setAlignment(this.name, Pos.BOTTOM_LEFT);
	        	this.name.setTranslateX(45);
	            StackPane.setAlignment(chatBubble, Pos.TOP_RIGHT);
	            break;
	            
            case 1:
            	StackPane.setAlignment(circle, Pos.TOP_RIGHT);
            	StackPane.setAlignment(this.name, Pos.BOTTOM_RIGHT);
            	this.name.setTranslateX(-45);
                StackPane.setAlignment(chatBubble, Pos.TOP_LEFT);
                break;
                
            case 2:
            	StackPane.setAlignment(circle, Pos.TOP_RIGHT);
            	StackPane.setAlignment(this.name, Pos.BOTTOM_RIGHT);
            	this.name.setTranslateX(-45);
                StackPane.setAlignment(chatBubble, Pos.TOP_LEFT);
                break;
                
            case 3:
            	StackPane.setAlignment(circle, Pos.TOP_LEFT);
            	StackPane.setAlignment(this.name, Pos.BOTTOM_LEFT);
            	this.name.setTranslateX(45);
                StackPane.setAlignment(chatBubble, Pos.TOP_RIGHT);
                break;
            
        }

        this.getChildren().addAll(circle, this.name, chatBubble);
        this.setMaxSize(WIDTH, HEIGHT);
//      this.setStyle("-fx-background-color: lightgray;");
    }
    
    public PlayerProfile(String name, Colour colour, boolean active, boolean nextActive) {
        this(name, colour, active, nextActive, 0);
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active)
            applyGlow(Color.DEEPSKYBLUE);
        else
            circle.setEffect(null);
    }

    public void setNextActive(boolean nextActive) {
        this.nextActive = nextActive;
        if (nextActive)
            applyGlow(Color.VIOLET);
        else if (!active)
            circle.setEffect(null);
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

    public Colour getColour() {
        return colour;
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
}
