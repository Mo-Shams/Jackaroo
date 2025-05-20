package controller;

import scene.EndScreenScene;
import scene.WelcomeScene;
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
		
		WelcomeScene welcomeScene = new WelcomeScene(primaryStage);
		Scene scene = welcomeScene.createScene();
		primaryStage.setScene(scene);
		primaryStage.setTitle("Jackaroo");
		primaryStage.show();
	}
}