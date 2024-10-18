package org.SpotyLite.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.SpotyLite.controller.MusicPlayerController;
import org.SpotyLite.model.Song;

public class MusicPlayerGUI extends Application {
    private static MusicPlayerGUI instance;
    private Slider songSlider;
    private Label songPositionLabel;
    private Label totalDurationLabel;
    private MusicPlayerController musicPlayerController;
    private Timeline timeline;

    private Button playButton;
    private Button pauseButton;

    public MusicPlayerGUI() {
        instance = this;
    }

    public static MusicPlayerGUI getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("SpotyLite 2.0");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);

        VBox root = new VBox();
        root.getStyleClass().add("root");

        Region spacer = new Region();
        spacer.getStyleClass().add("spacer");
        root.getChildren().add(spacer);

        songSlider = new Slider(0, 100, 0);
        songSlider.getStyleClass().add("slider");
        songSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (songSlider.isValueChanging()) {
                musicPlayerController.seek(newValue.longValue());
            }
        });

        songPositionLabel = new Label("0:00");
        songPositionLabel.getStyleClass().add("label");

        totalDurationLabel = new Label("0:00");
        totalDurationLabel.getStyleClass().add("label");

        HBox durationBox = new HBox();
        durationBox.getStyleClass().add("duration-box");
        durationBox.getChildren().addAll(songPositionLabel, totalDurationLabel);

        Image playImage = new Image(getClass().getResourceAsStream("/play.png"));
        ImageView playImageView = new ImageView(playImage);
        playImageView.getStyleClass().add("play-image");

        playButton = new Button();
        playButton.getStyleClass().add("play-button");
        playButton.setGraphic(playImageView);
        playButton.setOnAction(event -> {
            if (musicPlayerController != null && !musicPlayerController.isPlaying()) {
                musicPlayerController.play();
                playButton.setVisible(false);
                pauseButton.setVisible(true);
            }
        });

        Image pauseImage = new Image(getClass().getResourceAsStream("/pause.png"));
        ImageView pauseImageView = new ImageView(pauseImage);
        pauseImageView.getStyleClass().add("pause-image");

        pauseButton = new Button();
        pauseButton.getStyleClass().add("pause-button");
        pauseButton.setGraphic(pauseImageView);
        pauseButton.setOnAction(event -> {
            if (musicPlayerController != null && musicPlayerController.isPlaying()) {
                musicPlayerController.pause();
                playButton.setVisible(true);
                pauseButton.setVisible(false);
            }
        });

        pauseButton.setVisible(false);

        StackPane buttonPane = new StackPane(playButton, pauseButton);
        buttonPane.getStyleClass().add("button-pane");

        Region blackBackground = new Region();
        blackBackground.getStyleClass().add("black-background");
        StackPane.setAlignment(blackBackground, Pos.CENTER);
        StackPane.setMargin(blackBackground, new Insets(0, 0, 0, 0));

        StackPane mainPane = new StackPane(blackBackground, root);
        mainPane.setAlignment(Pos.CENTER);

        root.getChildren().addAll(songSlider, durationBox, buttonPane);

        musicPlayerController = new MusicPlayerController();
        Song song = new Song("Test Song", "Test Artist", "src/main/resources/test.mp3");
        musicPlayerController.initialize(song);
        totalDurationLabel.setText(formatTime(musicPlayerController.getSongDuration()));
        songSlider.setMax(musicPlayerController.getSongDuration());

        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(getClass().getResource("/musicPlayer.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void startSliderUpdate() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            long currentPosition = musicPlayerController.getCurrentPosition();
            songSlider.setValue(currentPosition);
            songPositionLabel.setText(formatTime(currentPosition));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
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