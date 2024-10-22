package org.spoty.lite.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.spoty.lite.controller.MusicPlayerController;
import org.spoty.lite.model.Song;

import java.util.Objects;

public class MusicPlayerGUI extends Application {
    private static MusicPlayerGUI instance;
    private Slider songSlider;
    private Label songPositionLabel;
    private Label totalDurationLabel;
    private MusicPlayerController musicPlayerController;
    private Timeline timeline;
    private Button playPauseButton;
    private ImageView playImageView;
    private ImageView pauseImageView;
    private ListView<String> playlistView;
    private ImageView albumCoverView;
    private Slider volumeSlider;
    private ToggleButton repeatShuffleButton;

    public MusicPlayerGUI() {
        instance = this;
    }

    public static MusicPlayerGUI getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("SpotyLite 2.0");
        stage.setWidth(400);
        stage.setHeight(600);
        stage.setResizable(false);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Top section: Album cover and song info
        VBox topSection = createTopSection();
        root.setTop(topSection);

        // Center section: Playlist
        VBox centerSection = createCenterSection();
        root.setCenter(centerSection);

        // Bottom section: Player controls
        VBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);

        musicPlayerController = new MusicPlayerController();
        Song song = new Song("Test Song", "Test Artist", "src/main/resources/test.mp3");
        musicPlayerController.initialize(song);
        totalDurationLabel.setText(formatTime(musicPlayerController.getSongDuration()));
        songSlider.setMax(musicPlayerController.getSongDuration());

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/spotylite-style.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private VBox createTopSection() {
        VBox topSection = new VBox(10);
        topSection.setAlignment(Pos.CENTER);
        topSection.setPadding(new Insets(20));
/*
        albumCoverView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/default-album-art.png"))));
        albumCoverView.setFitHeight(200);
        albumCoverView.setFitWidth(200);
        albumCoverView.getStyleClass().add("album-cover");
*/
        Label songTitle = new Label("Test Song");
        songTitle.getStyleClass().add("song-title");
        Label artistName = new Label("Test Artist");
        artistName.getStyleClass().add("artist-name");

        topSection.getChildren().addAll( songTitle, artistName);
        return topSection;
    }

    private VBox createCenterSection() {
        VBox centerSection = new VBox(10);
        centerSection.setPadding(new Insets(0, 20, 20, 20));

        Label playlistLabel = new Label("Playlist");
        playlistLabel.getStyleClass().add("playlist-label");

        playlistView = new ListView<>();
        playlistView.getStyleClass().add("playlist-view");
        ObservableList<String> items = FXCollections.observableArrayList(
                "Song 1", "Song 2", "Song 3", "Song 4", "Song 5"
        );
        playlistView.setItems(items);

        centerSection.getChildren().addAll(playlistLabel, playlistView);
        return centerSection;
    }

    private VBox createBottomSection() {
        VBox bottomSection = new VBox(10);
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setPadding(new Insets(20));

        songSlider = new Slider(0, 100, 0);
        songSlider.getStyleClass().add("song-slider");
        songSlider.setPrefWidth(350);
        songSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (songSlider.isValueChanging()) {
                musicPlayerController.seek(newValue.longValue());
            }
        });

        songPositionLabel = new Label("0:00");
        totalDurationLabel = new Label("0:00");

        HBox durationBox = new HBox(songPositionLabel, new Region(), totalDurationLabel);
        durationBox.getStyleClass().add("duration-box");
        HBox.setHgrow(durationBox.getChildren().get(1), Priority.ALWAYS);

        HBox controlBox = createControlBox();

        volumeSlider = new Slider(0, 100, 50);
        volumeSlider.getStyleClass().add("volume-slider");
        volumeSlider.setPrefWidth(100);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (musicPlayerController != null) {
                musicPlayerController.setVolume(newValue.doubleValue() / 100);
            }
        });

        HBox volumeBox = new HBox(10, new Label("Volume:"), volumeSlider);
        volumeBox.setAlignment(Pos.CENTER);

        bottomSection.getChildren().addAll(songSlider, durationBox, controlBox, volumeBox);
        return bottomSection;
    }

    private HBox createControlBox() {
        playImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/play.png"))));
        pauseImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pause.png"))));
        playImageView.setFitHeight(30);
        playImageView.setFitWidth(30);
        pauseImageView.setFitHeight(30);
        pauseImageView.setFitWidth(30);

        playPauseButton = new Button();
        playPauseButton.setGraphic(playImageView);
        playPauseButton.getStyleClass().add("play-pause-button");
        playPauseButton.setOnAction(event -> togglePlayPause());

        Button prevButton = new Button("⏮");
        prevButton.getStyleClass().add("control-button");
        prevButton.setOnAction(event -> musicPlayerController.previousTrack());

        Button nextButton = new Button("⏭");
        nextButton.getStyleClass().add("control-button");
        nextButton.setOnAction(event -> musicPlayerController.nextTrack());

        repeatShuffleButton = new ToggleButton("🔁");
        repeatShuffleButton.getStyleClass().add("control-button");
        repeatShuffleButton.setOnAction(event -> toggleRepeatShuffle());

        HBox controlBox = new HBox(20, prevButton, playPauseButton, nextButton, repeatShuffleButton);
        controlBox.setAlignment(Pos.CENTER);
        return controlBox;
    }

    private void togglePlayPause() {
        if (musicPlayerController != null) {
            if (musicPlayerController.isPlaying()) {
                musicPlayerController.pause();
                playPauseButton.setGraphic(playImageView);
            } else {
                musicPlayerController.play();
                playPauseButton.setGraphic(pauseImageView);
            }
        }
    }

    private void toggleRepeatShuffle() {
        if (repeatShuffleButton.isSelected()) {
            repeatShuffleButton.setText("🔀");
            // Implementar lógica para modo aleatorio
        } else {
            repeatShuffleButton.setText("🔁");
            // Implementar lógica para modo repetir
        }
    }

    public void startSliderUpdate() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            long currentPosition = musicPlayerController.getCurrentPosition();
            songSlider.setValue(currentPosition);
            songPositionLabel.setText(formatTime(currentPosition));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void stopSliderUpdate() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private String formatTime(double seconds) {
        int minutes = (int) seconds / 60;
        int secs = (int) seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }

    public static void main(String[] args) {
        launch(args);
    }
}