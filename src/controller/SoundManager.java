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
    
    public static double MusicVolume = 0.5;

    private static final HashMap<String, Media> mediaCache = new HashMap<>();
    private static final HashMap<String, AudioClip> audioClipCache = new HashMap<>();

    private static MediaPlayer currentMusicPlayer = null;

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

    public static MediaPlayer getLoopingMusic(String filename) {
        if (!Music) return null;

        Media media = mediaCache.computeIfAbsent(filename, fn ->
            new Media(new File("src/resources/sounds/" + fn).toURI().toString())
        );
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        
        player.setVolume(MusicVolume);
        
        return player;
    }

    // ✅ New method: Play music and stop the previous one
    public static void playMusic(String filename) {
        if (!Music) return;

        try {
            if (currentMusicPlayer != null) {
                currentMusicPlayer.stop(); 
            }

            currentMusicPlayer = getLoopingMusic(filename);
            if (currentMusicPlayer != null) {
                currentMusicPlayer.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ New method: Stop any currently playing music
    public static void stopMusic() {
        if (currentMusicPlayer != null) {
            currentMusicPlayer.stop();
            currentMusicPlayer = null;
        }
    }
}
