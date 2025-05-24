package controller;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;

public class SoundManager {
	public static boolean Music = true ;
	public static boolean Effects = true ; 
	public static boolean Jokes = true ;
	
    private static final HashMap<String, Media> cache = new HashMap<>();

    public static void playSound(String filename) {
        try {
            Media media = cache.computeIfAbsent(filename, fn ->
                new Media(new File("src/resources/sounds/" + fn).toURI().toString())
            );
            MediaPlayer player = new MediaPlayer(media);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MediaPlayer getLoopingMusic(String filename) {
        Media media = new Media(new File("src/assets/sounds/" + filename).toURI().toString());
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        return player;
    }
}
