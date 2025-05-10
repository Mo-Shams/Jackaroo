package controller;



import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import scene.GameScene;

public class Controller {
	private final Stage stage;
	
	public Controller(Stage stage){
		this.stage = stage;
	}
	
	public void setGameSceneOnButtonClick(Button button) throws Exception{
		Scene scene = new GameScene("Muhammad").createGameScene(stage);
		button.setOnAction(event -> {
	        stage.setScene(scene);
	        stage.show();
	    });
	}
}
