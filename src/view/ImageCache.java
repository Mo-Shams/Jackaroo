package view;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class ImageCache {
    private static final Map<String, Image> imageMap = new HashMap<>();

    public static Image getImage(String path) {
        return imageMap.computeIfAbsent(path, p -> {
            InputStream stream = ImageCache.class.getResourceAsStream(p);
            if (stream == null) {
            	stream = ImageCache.class.getResourceAsStream("/resources/themes/original/default.png");
            	if(stream == null){
            		System.out.println("image not found");
            		throw new IllegalArgumentException("Image not found: " + p);
            	}
            }
            return new Image(stream);
        });
    }
}