package org.SpotyLite.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LoginGUI extends Application {
    private TextField usertext;
    private PasswordField passwordField;
    private Label loginMessage;

    public static final String FRAME_COLOR = "-fx-background-color: lightgray;";
    public static final String TEXT_COLOR = "-fx-text-fill: black;";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SpotyLite 2.0");
        primaryStage.setWidth(350);
        primaryStage.setHeight(350);
        primaryStage.setResizable(false);

        VBox root = new VBox();
        root.setStyle(FRAME_COLOR);
        root.setPadding(new Insets(20));
        root.setSpacing(5);
        root.setAlignment(Pos.TOP_CENTER);

        // Cargar el logo
        ImageView logo = loadImage("src/main/resources/logo.png");
        if (logo != null) {
            logo.setFitHeight(150); // Establece la altura del logo.
            logo.setPreserveRatio(true); // Mantiene la proporción de la imagen.
            root.getChildren().add(logo);
            VBox.setMargin(logo, new Insets(-15, 0, -30, 0));
        }

        // Crear el panel de inicio de sesión
        GridPane loginPanel = new GridPane();
        loginPanel.setHgap(10);
        loginPanel.setVgap(10);
        loginPanel.setPadding(new Insets(20));
        loginPanel.setAlignment(Pos.CENTER);

        // Etiqueta y campo de usuario
        Label userLabel = new Label("Usuario:");
        userLabel.setStyle(TEXT_COLOR);
        loginPanel.add(userLabel, 0, 0);

        usertext = new TextField();
        loginPanel.add(usertext, 1, 0);
        // Etiqueta y campo de contraseña
        Label passwordLabel = new Label("Contraseña:");
        passwordLabel.setStyle(TEXT_COLOR);
        loginPanel.add(passwordLabel, 0, 1);

        passwordField = new PasswordField();
        loginPanel.add(passwordField, 1, 1);

        // Mensaje de inicio de sesión
        loginMessage = new Label();
        loginMessage.setStyle("-fx-text-fill: green;");
        loginPanel.add(loginMessage, 1, 2);

        root.getChildren().add(loginPanel);

        // Botones de inicio de sesión y registro
        HBox loginBtns = new HBox(20);
        loginBtns.setPadding(new Insets(-25, 0, 0, 0));
        loginBtns.setStyle(FRAME_COLOR);
        loginBtns.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Iniciar Sesion");
        loginBtn.setStyle(TEXT_COLOR);
        loginBtn.setOnAction(e -> {
            try {
                handleLogin();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        loginBtns.getChildren().add(loginBtn);

        Button registerBtn = new Button("Registrarse");
        registerBtn.setStyle(TEXT_COLOR);
        registerBtn.setOnAction(e -> handleRegister());
        loginBtns.getChildren().add(registerBtn);

        root.getChildren().add(loginBtns);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView loadImage(String imagePath) {
        try {
            FileInputStream input = new FileInputStream(imagePath);
            Image image = new Image(input);
            return new ImageView(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleLogin() throws Exception {
        String username = usertext.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            loginMessage.setText("Por favor, complete todos los campos.");
            loginMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        if (username.equals("admin") && password.equals("admin")) {
            loginMessage.setText("Login successful");
            MusicPlayerGUI musicPlayerGUI = new MusicPlayerGUI();
            musicPlayerGUI.start(new Stage());
            ((Stage) usertext.getScene().getWindow()).close();
        } else {
            loginMessage.setText("Login failed");
            loginMessage.setStyle("-fx-text-fill: red;");
        }
    }

    private void handleRegister() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Register");
        alert.setHeaderText(null);
    }

}