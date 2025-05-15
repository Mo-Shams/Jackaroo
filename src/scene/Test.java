// src/main/java/scene/TestWinningApp.java
package scene;

import engine.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Launch this class to verify WinningScene works.
 */
public class Test extends Application {
    @Override
    public void start(Stage primaryStage) {
        // full classpath‑relative paths (leading slash)

        EndScene es = new EndScene();
        boolean playerWon = true; 
        Color playerColor = Color.RED;
        String playerName = "Ahmed";
        Color cpuColor = Color.BLUE;
        String cpuName = "CPU";
        
        Scene scene = es.createEndScene(
                primaryStage,
                playerWon,
                playerColor,
                playerName,
                cpuColor,
                cpuName
            );

        primaryStage.setScene(scene);
        primaryStage.setTitle(playerWon ? "Test Winning Scene" : "Test Losing Scene");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
