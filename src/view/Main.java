package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scene.GameScene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		GameScene gameScene = new GameScene("Muhammad");
		Scene scene = GameScene.createGameScene(primaryStage);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Jackaroo");
		primaryStage.show();
	}
	public static void main(String[] args){
		launch();
	}
}

