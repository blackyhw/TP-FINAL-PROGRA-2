package org.spoty.lite.gui;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
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
    private boolean isDarkTheme = true;

    public MusicPlayerGUI() {
        instance = this;
    }

    public static MusicPlayerGUI getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("SpotyLite 2.0");
        stage.setWidth(700);
        stage.setHeight(700);
        stage.setResizable(false);

        root = new BorderPane();
        root.getStyleClass().add("root");

        // Header
        VBox header = createHeader();
        root.setTop(header);

        // Main Content
        mainContent = createMainContent();
        root.setCenter(mainContent);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dark-theme.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeader() {

        Button toggleThemeButton = new Button();
        updateThemeIcon(toggleThemeButton);
        toggleThemeButton.setOnAction(e -> {
            toggleTheme();
            updateThemeIcon(toggleThemeButton);
        });

        HBox nav = new HBox(20);
        nav.setAlignment(Pos.CENTER);
        nav.getChildren().addAll(
                createNavLink("Inicio", "home"),
                createNavLink("Playlists", "playlists"),
                createNavLink("Mis Likes", "likes"),
                createNavLink("Cuenta", "user"),
                toggleThemeButton
        );

        VBox header = new VBox();
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER);

        Label title = new Label();
        title.getStyleClass().add("title");

        header.getChildren().addAll(title, nav);
        return header;
    }

    private void updateThemeIcon(Button button) {
        if (isDarkTheme) {
            button.setText("🌙"); // Icon for dark theme
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Segoe UI Symbol', 'Arial', sans-serif; -fx-font-size: 20px; -fx-border-color: transparent;");
        } else {
            button.setText("☀"); // Icon for light theme
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-family: 'Segoe UI Symbol', 'Arial', sans-serif; -fx-font-size: 20px; -fx-border-color: transparent;");
        }
    }

    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        Scene scene = mainContent.getScene();
        scene.getStylesheets().clear();
        String theme = isDarkTheme ? "/dark-theme.css" : "/light-theme.css";
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(theme)).toExternalForm());

        root.getStyleClass().clear();
        root.getStyleClass().add(isDarkTheme ? "root" : "root");
    }


    private Button createNavLink(String text, String sectionId) {
        Button button = new Button(text);
        button.getStyleClass().add("button");
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
        // Lógica para filtrar canciones según la consulta
        System.out.println("Filtrar canciones por: " + query);
    }

    private VBox createHomeSection() {
        VBox home = new VBox(20);
        home.setAlignment(Pos.CENTER);
        home.setPadding(new Insets(20));

        TextField searchField = new TextField();
        searchField.setPromptText("Buscar Canciones...");
        searchField.getStyleClass().add("text-field");
        searchField.setOnKeyReleased(e -> filterSongs(searchField.getText()));

        home.getChildren().add(searchField);
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

        TextField searchField = new TextField();
        searchField.setPromptText("Buscar Canciones Favoritas...");
        searchField.getStyleClass().add("text-field");
        searchField.setOnKeyReleased(e -> filterSongs(searchField.getText()));

        likes.getChildren().add(searchField);
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

    public static void main(String[] args) {
        launch(args);
    }

    public void stopSliderUpdate() {
    }

    public void startSliderUpdate() {
    }


}