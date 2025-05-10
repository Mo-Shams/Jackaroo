package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scene.GameScene;
import scene.WelcomeScene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new WelcomeScene().createWelcomeScene(primaryStage);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Jackaroo");
		primaryStage.show();
	}
	public static void main(String[] args){
		launch();
	}
}

