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
        // Crear el botón para cambiar de tema con un ícono
        Button toggleThemeButton = new Button();
        updateThemeIcon(toggleThemeButton); // Establecer el ícono inicial
        toggleThemeButton.setOnAction(e -> {
            toggleTheme();
            updateThemeIcon(toggleThemeButton); // Actualizar el ícono después de cambiar el tema
        });

        // Crear el contenedor de navegación (HBox)
        HBox nav = new HBox(20);
        nav.setAlignment(Pos.CENTER);
        nav.getChildren().addAll(
                createNavLink("Inicio", "home"),
                createNavLink("Playlists", "playlists"),
                createNavLink("Mis Likes", "likes"),
                createNavLink("Cuenta", "user"),
                toggleThemeButton // Agregar el botón al contenedor de navegación
        );

        // Crear el contenedor principal del encabezado (VBox)
        VBox header = new VBox();
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER);

        Label title = new Label(); // Crear el título
        title.getStyleClass().add("title"); // Asegúrate de que "title" esté definido en el CSS

        header.getChildren().addAll(title, nav); // Agregar el título y la navegación al encabezado
        return header; // Retornar el encabezado
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
        isDarkTheme = !isDarkTheme; // Toggle the theme state
        Scene scene = mainContent.getScene(); // Get the current scene
        scene.getStylesheets().clear(); // Clear previous styles
        String theme = isDarkTheme ? "/dark-theme.css" : "/light-theme.css";
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(theme)).toExternalForm()); // Add the new style

        root.getStyleClass().clear(); // Clear style classes
        root.getStyleClass().add(isDarkTheme ? "root" : "root"); // Add the appropriate class
    }


    private Button createNavLink(String text, String sectionId) {
        Button button = new Button(text);
        button.getStyleClass().add("button"); // Add the button class
        button.setOnAction(e -> showSection(sectionId));
        return button;
    }

    private StackPane createMainContent() {
        StackPane mainContent = new StackPane();
        Node homeSection = createHomeSection();
        Node playlistsSection = createPlaylistsSection();
        Node likesSection = createLikesSection();
        Node userSection = createUserSection();

        // Agregar secciones al StackPane
        mainContent.getChildren().addAll(homeSection, playlistsSection, likesSection, userSection);

        // Mostrar solo la sección de inicio al principio
        homeSection.setVisible(true);
        playlistsSection.setVisible(false);
        likesSection.setVisible(false);
        userSection.setVisible(false);

        return mainContent;
    }

    private void showSection(String sectionId) {
        // Variable para almacenar la sección activa
        Node activeSection = null;

        // Determina cuál es la sección activa
        switch (sectionId) {
            case "home":
                activeSection = mainContent.getChildren().get(0); // Home
                break;
            case "playlists":
                activeSection = mainContent.getChildren().get(1); // Playlists
                break;
            case "likes":
                activeSection = mainContent.getChildren().get(2); // Likes
                break;
            case "user":
                activeSection = mainContent.getChildren().get(3); // User
                break;
        }

        // Asegúrate de que la sección activa esté visible
        if (activeSection != null) {
            // Primero, ocultamos todas las secciones
            SequentialTransition transition = new SequentialTransition();

            for (Node section : mainContent.getChildren()) {
                if (section != activeSection) {
                    // Animación de desvanecimiento para ocultar secciones
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(150), section);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> section.setVisible(false)); // Oculta la sección después de la animación
                    transition.getChildren().add(fadeOut); // Agrega a la secuencia
                }
            }

            // Cuando todas las secciones están ocultas, mostramos la sección activa
            Node finalActiveSection = activeSection;
            transition.setOnFinished(e -> {
                finalActiveSection.setVisible(true); // Muestra la sección activa
                finalActiveSection.setOpacity(0); // Asegúrate de que la sección esté oculta antes de animar
                // Animación de desvanecimiento para mostrar la sección activa
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), finalActiveSection);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play(); // Reproduce la animación de desvanecimiento
            });

            transition.play(); // Reproduce la secuencia de animaciones
        }
    }
    private void filterSongs(String query) {
        // Lógica para filtrar canciones según la consulta
        // Puedes implementar la lógica para mostrar solo las canciones que coincidan con la búsqueda
        // Esto puede incluir actualizar la interfaz para mostrar solo las canciones que coinciden
        System.out.println("Filtrar canciones por: " + query);
    }

    private VBox createHomeSection() {
        VBox home = new VBox(20);
        home.setAlignment(Pos.CENTER);
        home.setPadding(new Insets(20));

        // Crear la barra de búsqueda
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar Canciones...");
        searchField.getStyleClass().add("text-field"); // Añadir la clase de estilo
        searchField.setOnKeyReleased(e -> filterSongs(searchField.getText())); // Llama al método de filtrado al escribir

        home.getChildren().add(searchField); // Agregar la barra de búsqueda
        home.getChildren().add(new Label("Bienvenido a SpotyLite"));
        home.getChildren().add(new Label("Tu música, tu estilo."));
        return home;
    }

    private VBox createPlaylistsSection() {
        VBox playlists = new VBox(10);
        playlists.setPadding(new Insets(20));
        playlists.getChildren().add(new Label("Playlists Personalizadas"));

        // Aquí puedes agregar la lógica para mostrar las playlists
        playlists.getChildren().addAll(new Label("Playlist 1"), new Label("Playlist 2"), new Label("Playlist 3"));
        Button createPlaylistButton = new Button("Crear Nueva Playlist");
        playlists.getChildren().add(createPlaylistButton);
        return playlists;
    }

    private VBox createLikesSection() {
        VBox likes = new VBox(10);
        likes.setPadding(new Insets(20));

        // Crear la barra de búsqueda
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar Canciones Favoritas...");
        searchField.getStyleClass().add("text-field"); // Añadir la clase de estilo
        searchField.setOnKeyReleased(e -> filterSongs(searchField.getText())); // Llama al método de filtrado al escribir

        likes.getChildren().add(searchField); // Agregar la barra de búsqueda
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