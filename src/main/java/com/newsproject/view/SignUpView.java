
package com.newsproject.view;

import com.newsproject.controller.LoginController;
import com.newsproject.controller.SignUpController;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SignUpView extends Application {

    private Stage stage;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField visiblePasswordField;
    private CheckBox showPasswordCheck;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        openAuthWindow();
    }

    public void openAuthWindow() {
        buildUI();
        stage.show();
    }

    // 🔒 Only logout button will call this
    public void openMainAppWindow() {
        stage.close();
        Stage newsStage = new Stage();
        new NewsView(this, newsStage).show();
    }

    private void buildUI() {

        /* ---------- INPUT FIELDS ---------- */

        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setMaxWidth(260);

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(260);

        visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Enter your password");
        visiblePasswordField.setMaxWidth(260);
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);

        String fieldStyle =
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: #cccccc;" +
                "-fx-padding: 8;" +
                "-fx-font-size: 13;";

        emailField.setStyle(fieldStyle);
        passwordField.setStyle(fieldStyle);
        visiblePasswordField.setStyle(fieldStyle);

        /* ---------- SHOW PASSWORD ---------- */

        showPasswordCheck = new CheckBox("Show Password");

        showPasswordCheck.setOnAction(e -> {
            if (showPasswordCheck.isSelected()) {
                visiblePasswordField.setText(passwordField.getText());
                visiblePasswordField.setVisible(true);
                visiblePasswordField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
            } else {
                passwordField.setText(visiblePasswordField.getText());
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                visiblePasswordField.setVisible(false);
                visiblePasswordField.setManaged(false);
            }
        });

        /* ---------- BUTTONS ---------- */

        Button loginBtn = new Button("Login");
        Button signupBtn = new Button("Sign Up");

        String btnStyle =
                "-fx-background-color: #1e88e5;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 8 34 8 34;";

        loginBtn.setStyle(btnStyle);
        signupBtn.setStyle(btnStyle);

        /* ---------- FORGOT PASSWORD ---------- */

        Hyperlink forgotPassword = new Hyperlink("Forgot Password?");
        forgotPassword.setOnAction(e ->
                showAlert("Password reset link will be sent to your email (Feature demo).")
        );

        /* ---------- CONTROLLERS ---------- */

        SignUpController signUpController = new SignUpController();

        signupBtn.setOnAction(e -> {
            String password = showPasswordCheck.isSelected()
                    ? visiblePasswordField.getText()
                    : passwordField.getText();

            SignUpController.Result res =
                    signUpController.signUp(emailField.getText(), password);

            if (res.success) {
                openMainAppWindow();
            } else {
                showAlert(res.message);
            }
        });

        loginBtn.setOnAction(e -> {
            String password = showPasswordCheck.isSelected()
                    ? visiblePasswordField.getText()
                    : passwordField.getText();

            LoginController.Result res =
                    new LoginController().login(emailField.getText(), password);

            if (res.success) {
                openMainAppWindow();
            } else {
                showAlert(res.message);
            }
        });

        /* ---------- CARD (HEIGHT REDUCED) ---------- */

        Label title = new Label("News on NewsApp");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill:#1976d2;");

        VBox card = new VBox(10,
                title,
                emailField,
                passwordField,
                visiblePasswordField,
                showPasswordCheck,
                forgotPassword,
                loginBtn,
                signupBtn
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(22));
        card.setMaxWidth(320);
        card.setMaxHeight(200);   // HEIGHT REDUCED

        card.setStyle(
                "-fx-background-color: rgba(255,255,255,0.93);" +
                "-fx-background-radius: 22;" +
                "-fx-effect: dropshadow(gaussian, #00000055, 16, 0.25, 0, 6);"
        );

        /* ---------- BACKGROUND ---------- */

        Image bgImage = new Image(
                getClass().getResource("/bg_image.png").toExternalForm()
        );

        BackgroundImage bg = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );

        StackPane root = new StackPane(card);
        root.setBackground(new Background(bg));
        root.setAlignment(Pos.CENTER);

        /* ---------- SCENE ---------- */

        Scene scene = new Scene(root, 545, 800);
        stage.setScene(scene);
        stage.setTitle("News App");
        stage.setResizable(false);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
