package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scene.WelcomeScene;

public class Main extends Application {
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setFullScreen(true); // This will make the scene fill the 			// entire screen
		primaryStage.setResizable(false);// Disable window resizing
		Scene scene = new WelcomeScene().createWelcomeScene(primaryStage);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Jackaroo");
		primaryStage.show();
	}
}
