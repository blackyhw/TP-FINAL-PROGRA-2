package org.spoty.lite.gui;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class MusicPlayerGUI extends Application {
    private static MusicPlayerGUI instance;
    private StackPane mainContent;
    private BorderPane root;
    private Slider songSlider;
    private Label songPositionLabel;
    private Label totalDurationLabel;
    private ToggleButton playPauseButton;
    private Slider volumeSlider;
    private Timeline timeline;
    private ToggleButton likeButton;
    private ToggleButton shuffleButton;

    public MusicPlayerGUI() {
        instance = this;
    }

    public static MusicPlayerGUI getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("SpotyLite 2.0");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);

        root = new BorderPane();
        root.getStyleClass().add("root");

        VBox header = createHeader();
        root.setTop(header);

        mainContent = createMainContent();
        root.setCenter(mainContent);


        HBox playerControls = createPlayerControls();
        root.setBottom(playerControls);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/spotylite-style.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox(20);
        header.setPadding(new Insets(20));
        header.setMinHeight(0);
        header.setAlignment(Pos.CENTER);


        TextField searchField = new TextField();
        searchField.setPromptText("Buscar...");
        searchField.getStyleClass().add("search-field");
        searchField.setOnKeyReleased(e -> filterSongs(searchField.getText()));

        HBox nav = new HBox(20);
        nav.setAlignment(Pos.CENTER);
        nav.getChildren().addAll(
                createNavLink("Inicio", "home"),
                createNavLink("Playlists", "playlists"),
                createNavLink("Likes", "likes"),
                createNavLink("Cuenta", "user"),
                searchField
        );

        header.getChildren().addAll(nav);
        return header;
    }

    private Button createNavLink(String text, String sectionId) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        button.setOnAction(e -> showSection(sectionId));
        return button;
    }

    private StackPane createMainContent() {
        StackPane mainContent = new StackPane();
        Node homeSection = createHomeSection();
        Node playlistsSection = createPlaylistsSection();
        Node likesSection = createLikesSection();
        Node userSection = createUserSection();

        mainContent.getChildren().addAll(homeSection, playlistsSection, likesSection, userSection);

        homeSection.setVisible(true);
        playlistsSection.setVisible(false);
        likesSection.setVisible(false);
        userSection.setVisible(false);

        return mainContent;
    }

    private void showSection(String sectionId) {
        Node activeSection = null;

        switch (sectionId) {
            case "home":
                activeSection = mainContent.getChildren().get(0);
                break;
            case "playlists":
                activeSection = mainContent.getChildren().get(1);
                break;
            case "likes":
                activeSection = mainContent.getChildren().get(2);
                break;
            case "user":
                activeSection = mainContent.getChildren().get(3);
                break;
        }

        if (activeSection != null) {
            SequentialTransition transition = new SequentialTransition();

            for (Node section : mainContent.getChildren()) {
                if (section != activeSection) {
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(150), section);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> section.setVisible(false));
                    transition.getChildren().add(fadeOut);
                }
            }

            Node finalActiveSection = activeSection;
            transition.setOnFinished(e -> {
                finalActiveSection.setVisible(true);
                finalActiveSection.setOpacity(0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), finalActiveSection);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });

            transition.play();
        }
    }

    private void filterSongs(String query) {
        System.out.println("Filtrar canciones por: " + query);
    }

    private VBox createHomeSection() {
        VBox home = new VBox(20);
        home.setAlignment(Pos.CENTER);
        home.setPadding(new Insets(20));


        home.getChildren().add(new Label("Bienvenido a SpotyLite"));
        home.getChildren().add(new Label("Tu música, tu estilo."));
        return home;
    }

    private VBox createPlaylistsSection() {
        VBox playlists = new VBox(10);
        playlists.setPadding(new Insets(20));
        playlists.getChildren().add(new Label("Playlists Personalizadas"));

        playlists.getChildren().addAll(new Label("Playlist 1"), new Label("Playlist 2"), new Label("Playlist 3"));
        Button createPlaylistButton = new Button("Crear Nueva Playlist");
        playlists.getChildren().add(createPlaylistButton);
        return playlists;
    }

    private VBox createLikesSection() {
        VBox likes = new VBox(10);
        likes.setPadding(new Insets(20));


        likes.getChildren().add(new Label("Mis Likes"));
        likes.getChildren().addAll(new Label("Canción Favorita 1"), new Label("Canción Favorita 2"), new Label("Canción Favorita 3"));
        return likes;
    }

    private VBox createUserSection() {
        VBox user = new VBox(10);
        user.setPadding(new Insets(20));
        user.getChildren().add(new Label("Datos de Usuario"));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nombre de Usuario");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Button updateButton = new Button("Actualizar Datos");

        user.getChildren().addAll(usernameField, emailField, updateButton);
        return user;
    }

    private HBox createPlayerControls() {
        HBox playerControls = new HBox(10);
        playerControls.setAlignment(Pos.CENTER);
        playerControls.setPadding(new Insets(10));
        playerControls.getStyleClass().add("player-controls");

        VBox songInfo = new VBox(5);
        songInfo.setAlignment(Pos.CENTER_LEFT);
        Label songTitle = new Label("Título de la canción");
        Label artistName = new Label("Nombre del artista");
        songInfo.getChildren().addAll(songTitle, artistName);

        HBox playbackControls = createPlaybackControls();

        songSlider = new Slider(0, 100, 0);
        songSlider.getStyleClass().add("song-slider");
        songSlider.setPrefWidth(300);
        songSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (songSlider.isValueChanging()) {
                System.out.println("Seeking to: " + newValue.doubleValue());
            }
        });

        songPositionLabel = new Label("0:00");
        totalDurationLabel = new Label("0:00");

        HBox timeLabels = new HBox();
        timeLabels.setAlignment(Pos.CENTER);
        timeLabels.getStyleClass().add("time-labels");
        timeLabels.setSpacing(20);
        HBox.setMargin(songPositionLabel, new Insets(0, 120, 0, 0));
        HBox.setMargin(totalDurationLabel, new Insets(0, 0, 0, 120));

        timeLabels.getChildren().addAll(songPositionLabel, totalDurationLabel);

        volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(100);
        volumeSlider.setOrientation(Orientation.VERTICAL);
        volumeSlider.getStyleClass().add("volume-slider");

        VBox sliderBox = new VBox(5);
        sliderBox.setAlignment(Pos.CENTER);
        sliderBox.getChildren().addAll(songSlider, timeLabels);

        playerControls.getChildren().addAll(songInfo, playbackControls, sliderBox, volumeSlider);
        return playerControls;
    }

    private HBox createPlaybackControls() {
        HBox playbackControls = new HBox(10);
        playbackControls.setAlignment(Pos.CENTER);

        shuffleButton = new ToggleButton("🔀");
        shuffleButton.getStyleClass().add("control-button");

        Button prevButton = new Button("⏮");
        prevButton.getStyleClass().add("control-button");

        playPauseButton = new ToggleButton("▶");
        playPauseButton.getStyleClass().add("control-button");
        playPauseButton.setOnAction(event -> togglePlayPause());

        Button nextButton = new Button("⏭");
        nextButton.getStyleClass().add("control-button");

        likeButton = new ToggleButton("♥");
        likeButton.getStyleClass().add("control-button");

        playbackControls.getChildren().addAll(shuffleButton, prevButton, playPauseButton, nextButton, likeButton);
        return playbackControls;
    }

    private void togglePlayPause() {
        if (playPauseButton.isSelected()) {
            playPauseButton.setText("⏸");
            startSliderUpdate();
        } else {
            playPauseButton.setText("▶");
            stopSliderUpdate();
        }
    }

    public void startSliderUpdate() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            double currentPosition = songSlider.getValue() + 1;
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