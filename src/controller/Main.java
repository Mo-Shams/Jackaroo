package controller;

import view.GameScene;
import engine.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setFullScreen(true); // This will make the scene fill the 			// entire screen
		primaryStage.setResizable(false);// Disable window resizing
		GameController controller = new GameController("Muhammad");
		Scene scene = controller.getGameScene().CreateScene();
		// controller.canSelectCard(true);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Jackaroo");
		primaryStage.show();
		controller.run();
	}
}