package view.playersView;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import model.Colour;
import view.ImageCache;

public class PlayerProfile extends StackPane{
	private static final double SIZE = 200;
	private final Label name;
	private Color color;
	private boolean active, nextActive;
	private final Circle circle;
	
	public PlayerProfile(String name, Colour colour, boolean active, boolean nextActive){
		this.name = new Label(name);
		this.name.setFont(Font.font("Arial", 25));
		this.color = Color.valueOf(colour.toString());
		this.name.setTextFill(color);
		circle = new Circle(SIZE * 0.4);
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
		this.setMaxSize(SIZE, SIZE);
	}
	
	public void setActive(boolean active){
		this.active = active;
		if(active)
			applyGlow(Color.DEEPSKYBLUE);
		else
			this.setEffect(null);
	}
	
	public void setNextActive(boolean nextActive){
		this.nextActive = nextActive;
		if(nextActive)
			applyGlow(Color.VIOLET);
		else
			this.setEffect(null);
	}
	
	private void applyGlow(Color color) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setSpread(0.4);
        //glow.setRadius(50);
        glow.setWidth(60);
        glow.setHeight(40);
        circle.setEffect(glow);
    }
	
}

//private void addPlayerProfiles (StackPane root, ArrayList<Player> players, double SIZE, double height) {
//	StackPane container = new StackPane();
//    for (int i = 0; i < players.size(); i++) {
//        Player p = players.get(i);
//        VBox box = createProfileBox(p.getName(), p.getColour());
//        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        switch (i) {
//            case 0:
//                StackPane.setAlignment(box, Pos.BOTTOM_RIGHT);
//                break;
//            case 1:
//                StackPane.setAlignment(box, Pos.BOTTOM_LEFT);
//                break;
//            case 2:
//                StackPane.setAlignment(box, Pos.TOP_LEFT);
//                break;
//            case 3:
//                StackPane.setAlignment(box, Pos.TOP_RIGHT);
//                break;
//        }
//        container.getChildren().add(box);
//    }
//    container.setMaxSize(SIZE * 0.6, height);
//    root.getChildren().add(1, container);
//}
//	
//private VBox createProfileBox(String name, Colour colour) {
//    Circle avatar = new Circle(60, Color.LIGHTGRAY);
//    avatar.setStroke(Color.GRAY);
//    avatar.setStrokeSIZE(2);
//
//    Label nameLabel = new Label(name);
//    nameLabel.setFont(Font.font("Arial", 25));
//    nameLabel.setAlignment(Pos.CENTER);
//    Color color; 
//    switch (colour) {
//        case RED:   color = Color.RED;   break;
//        case GREEN: color = Color.GREEN; break;
//        case BLUE:  color = Color.BLUE;  break;
//        case YELLOW: color = Color.YELLOW; break; 
//        default:    color = Color.BLACK; break;
//        
//    }
//    nameLabel.setTextFill(color);
//    VBox box = new VBox(10);
//    box.getChildren().add(avatar);
//    box.getChildren().add(nameLabel);
//    box.setAlignment(Pos.CENTER);
//    return box;
//    }