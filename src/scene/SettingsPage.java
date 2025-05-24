package scene ;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import view.ImageCache;

public class SettingsPage extends Application {

    private BorderPane mainLayout;
    private VBox sideMenu;
    private StackPane contentArea;
    private Settings settings;
    private VBox themeGridContainer;
    private Button saveApplyButton;

    @Override
    public void start(Stage primaryStage) {
        settings = new Settings();
        mainLayout = new BorderPane();
        contentArea = new StackPane();

        setupSideMenu();

        mainLayout.setLeft(sideMenu);
        mainLayout.setCenter(contentArea);

        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setTitle("Settings Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupSideMenu() {
        sideMenu = new VBox(10);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setStyle("-fx-background-color: #2f4f4f;");
        sideMenu.setPrefWidth(200);

        Button themesButton = createSideButton("Themes");
        // Button charactersButton = createSideButton("Characters");
        Button controlButton = createSideButton("Control");

        saveApplyButton = new Button("Apply");
        saveApplyButton.setMaxWidth(Double.MAX_VALUE);
        saveApplyButton.setFont(Font.font(16));
        saveApplyButton.setOnAction(e -> {
            System.out.println("Theme applied: " + settings.selectedTheme);
            System.out.println("Settings saved");
        });

        VBox.setVgrow(saveApplyButton, Priority.ALWAYS);

        themesButton.setOnAction(e -> showThemes(getThemes()));
        controlButton.setOnAction(e -> showControlSettings());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sideMenu.getChildren().addAll(themesButton, controlButton, spacer, saveApplyButton);
    }

    private Button createSideButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setFont(Font.font(16));
        return button;
    }

    private void showThemes(List<Theme> themes) {
        TilePane grid = new TilePane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));
        grid.setPrefColumns(2);
        grid.setAlignment(Pos.TOP_LEFT);

        for (Theme theme : themes) {
            VBox themeCard = createThemeCard(theme);
            if (theme.name.equals(settings.selectedTheme)) {
                themeCard.setStyle(themeCard.getStyle() + "; -fx-border-color: red; -fx-border-width: 3px;");
            }
            grid.getChildren().add(themeCard);
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(10));

        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.TOP_CENTER);

        container.getChildren().addAll(scrollPane);
        contentArea.getChildren().setAll(container);
    }

    private VBox createThemeCard(Theme theme) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #333; -fx-border-radius: 8px;");
        card.setPrefSize(350, 250);

        ImageView imageView = new ImageView(ImageCache.getImage(theme.imagePath));
       
        
        imageView.setFitWidth(200);
        imageView.setFitHeight(120); 
        imageView.setPreserveRatio(false);

        Label nameLabel = new Label(theme.name);
        nameLabel.setFont(Font.font(16));

        Label descLabel = new Label(theme.description);
        descLabel.setWrapText(true);
        descLabel.setFont(Font.font(12));

        card.getChildren().addAll(imageView, nameLabel, descLabel);

        card.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            settings.selectedTheme = theme.name;
            showThemes(getThemes());
        });

        VBox.setVgrow(imageView, Priority.ALWAYS);

        return card;
    }

    private void showControlSettings() {
        VBox controlBox = new VBox(15);
        controlBox.setPadding(new Insets(20));
        controlBox.setAlignment(Pos.TOP_LEFT);

        Font labelFont = Font.font(14);

        CheckBox playAudio = new CheckBox("Play Audio");
        playAudio.setFont(labelFont);
        playAudio.setSelected(settings.playAudio);

        CheckBox playMusic = new CheckBox("Play Music");
        playMusic.setFont(labelFont);
        playMusic.setSelected(settings.playMusic);

        CheckBox playLoading = new CheckBox("Play Loading Scene");
        playLoading.setFont(labelFont);
        playLoading.setSelected(settings.playLoadingScene);

        Label marbleSpeedLabel = new Label("Marble Speed");
        marbleSpeedLabel.setFont(labelFont);
        Slider marbleSpeedSlider = new Slider(0, 10, settings.marbleSpeed);

        Label throwSpeedLabel = new Label("Throw Card Speed");
        throwSpeedLabel.setFont(labelFont);
        Slider throwSpeedSlider = new Slider(0, 10, settings.throwSpeed);

        Label audioLevelLabel = new Label("Audio Level");
        audioLevelLabel.setFont(labelFont);
        Slider audioLevelSlider = new Slider(0, 100, settings.audioLevel);

        controlBox.getChildren().addAll(
            playAudio,
            playMusic,
            playLoading,
            marbleSpeedLabel,
            marbleSpeedSlider,
            throwSpeedLabel,
            throwSpeedSlider,
            audioLevelLabel,
            audioLevelSlider
        );

        saveApplyButton.setOnAction(e -> {
            settings.selectedTheme = settings.selectedTheme; // retained from theme settings
            settings.playAudio = playAudio.isSelected();
            settings.playMusic = playMusic.isSelected();
            settings.playLoadingScene = playLoading.isSelected();
            settings.marbleSpeed = marbleSpeedSlider.getValue();
            settings.throwSpeed = throwSpeedSlider.getValue();
            settings.audioLevel = audioLevelSlider.getValue();
            System.out.println("Theme applied: " + settings.selectedTheme);
            System.out.println("Settings saved");
        });

        contentArea.getChildren().setAll(controlBox);
    }

    private List<Theme> getThemes() {
        List<Theme> themes = new ArrayList<>();
        themes.add(new Theme(1, "Choccy Milk", 
        		"They Say when the Developers Made this theme for the first time, they wanted Choccy Milk ", 
        		"/resources/themes/theme1.png"));
        
        themes.add(new Theme(2, "Dark Mode", 
        		"A sleek dark theme for night use", 
        		"https://via.placeholder.com/150x100.png?text=Dark"));
        
        themes.add(new Theme(3, "Blue Sky", 
        		"A calming blue theme", 
        		"https://via.placeholder.com/150x100.png?text=Blue+Sky"));
        return themes;
    }

    public static void main(String[] args) {
        launch(args);
    }

    static class Theme {
    	int id ; 
        String name;
        String description;
        String imagePath;

        Theme(int id, String name, String description, String imagePath) {
        	this.id = id ; 
            this.name = name;
            this.description = description;
            this.imagePath = imagePath;
        }
    }

    static class Settings {
        String selectedTheme = "Light Mode";
        boolean playAudio = true;
        boolean playMusic = true;
        boolean playLoadingScene = true;
        double marbleSpeed = 5;
        double throwSpeed = 5;
        double audioLevel = 50;
    }
}
