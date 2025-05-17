package controller;



import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scene.GameScene;

public class Controller {
	private final Stage stage;
	
	public Controller(Stage stage){
		this.stage = stage;
	}
	
	public void setGameSceneOnButtonClick(Button button, TextField nameField) throws Exception{
		// Controller.java
		button.setOnAction(evt -> {
	        String name = nameField.getText().trim();
	        if (name.isEmpty()) name = "Player";
	        try {
	            Scene scene = new GameScene(name).createGameScene(stage);
	            stage.setScene(scene);
	            stage.setFullScreen(true);
	            stage.setResizable(false);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    });
	}
}
