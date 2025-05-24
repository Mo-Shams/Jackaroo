package controller;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;

public class SoundManager {
    public static boolean Music = true;
    public static boolean Effects = true;
    public static boolean Jokes = true;

    // For music and long media
    private static final HashMap<String, Media> mediaCache = new HashMap<>();
    
    // For short sound effects
    private static final HashMap<String, AudioClip> audioClipCache = new HashMap<>();

    // ✅ New method using AudioClip for effects (no thread issues)
    public static void playEffect(String filename) {
        if (!Effects) return;

        try {
            AudioClip clip = audioClipCache.computeIfAbsent(filename, fn ->
                new AudioClip(new File("src/resources/sounds/" + fn).toURI().toString())
            );
            clip.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🎵 Still here if you ever need longer audio (like background music)
    public static MediaPlayer getLoopingMusic(String filename) {
        if (!Music) return null;

        Media media = new Media(new File("src/assets/sounds/" + filename).toURI().toString());
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        return player;
    }
}
