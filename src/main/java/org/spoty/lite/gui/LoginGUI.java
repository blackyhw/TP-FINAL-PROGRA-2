package org.spoty.lite.gui;

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
    private TextField userTextField;
    private PasswordField passwordField;
    private Label loginMessage;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SpotyLite 2.0");
        primaryStage.setWidth(300);
        primaryStage.setHeight(400);
        primaryStage.setResizable(false);

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        // Logo
        ImageView logo = loadImage("src/main/resources/logo.png");
        if (logo != null) {
            logo.setFitHeight(100);
            logo.setPreserveRatio(true);
            logo.getStyleClass().add("logo");
            root.getChildren().add(logo);
        }

        // Login fields
        userTextField = new TextField();
        userTextField.setPromptText("Usuario");
        passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");

        // Login button
        Button loginBtn = new Button("INICIAR SESIÓN");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> handleLogin());

        // Register link
        Hyperlink registerLink = new Hyperlink("¿No tienes cuenta? Regístrate");
        registerLink.setOnAction(e -> handleRegister());

        // Login message
        loginMessage = new Label();
        loginMessage.setId("loginMessage");

        root.getChildren().addAll(userTextField, passwordField, loginBtn, registerLink, loginMessage);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/spotylite-style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView loadImage(String imagePath) {
        try {
            return new ImageView(new Image(new FileInputStream(imagePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleLogin() {
        String username = userTextField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Por favor, complete todos los campos.", false);
            return;
        }

        if (username.equals("admin") && password.equals("admin")) {
            showMessage("Inicio de sesión exitoso", true);
            MusicPlayerGUI musicPlayerGUI = new MusicPlayerGUI();
            try {
                musicPlayerGUI.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((Stage) userTextField.getScene().getWindow()).close();
            // Aquí iría el código para abrir la ventana principal de la aplicación
        } else {
            showMessage("Usuario o contraseña incorrectos", false);
        }
    }

    private void handleRegister() {

        System.out.println("Abrir ventana de registro");
    }

    private void showMessage(String message, boolean isSuccess) {
        loginMessage.setText(message);
        loginMessage.getStyleClass().removeAll("success", "error");
        loginMessage.getStyleClass().add(isSuccess ? "success" : "error");
    }

    public static void main(String[] args) {
        launch(args);
    }
}