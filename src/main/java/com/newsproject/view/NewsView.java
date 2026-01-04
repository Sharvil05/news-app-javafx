package com.newsproject.view;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * NewsView is responsible for displaying news articles and categories in a
 * JavaFX application.
 * It provides functionality to show news categories, general news articles,
 * and sources for specific categories.
 * This class handles user interactions, fetches news data from an API,
 * and updates the UI accordingly.
 */
public class NewsView {

    private SignUpView signUpView;
    private static int page = 0;
    private Stage c2w_primaryStage;
    private VBox c2w_newsContainer;
    private com.newsproject.controller.ApiController apiController = new com.newsproject.controller.ApiController();

    public NewsView(SignUpView signUpView, Stage c2w_primaryStage) {
        this.c2w_primaryStage = c2w_primaryStage;
        this.signUpView = signUpView;
    }

    public void show() {
        showCategorySelection();
    }

    private void showCategorySelection() {

        GridPane c2w_grid = new GridPane();
        c2w_grid.setAlignment(Pos.CENTER);
        c2w_grid.setHgap(30);
        c2w_grid.setVgap(30);
        c2w_grid.setPadding(new Insets(40));
        c2w_grid.setStyle("-fx-background-color: #FFFAF5;");

        String[] categories = { "general", "business", "entertainment", "health", "science", "sports" };

        String[] gradients = {
                "-fx-background-color: linear-gradient(to right,rgb(86, 199,227), #6dd5ed);",
                "-fx-background-color: linear-gradient(to right, #cc2b5e, #753a88);",
                "-fx-background-color: linear-gradient(to right, #ee9ca7,rgb(248, 138, 150));",
                "-fx-background-color: linear-gradient(to right, #56ab2f, #a8e063);",
                "-fx-background-color: linear-gradient(to right, #614385, #516395);",
                "-fx-background-color: linear-gradient(to right, #e65c00, #f9d423);"
        };

        int c2w_index = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {

                if (c2w_index < categories.length) {

                    String c2w_category = categories[c2w_index];
                    Button c2w_btn = new Button(capitalize(c2w_category));
                    c2w_btn.setPrefWidth(190);
                    c2w_btn.setPrefHeight(200);
                    c2w_btn.setStyle(
                            gradients[c2w_index] +
                                    " -fx-text-fill: white; -fx-font-size: 22; -fx-font-weight: bold; -fx-background-radius: 18;"
                    );

                    c2w_btn.setOnAction(e -> handleCategorySelection(c2w_category));

                    ScaleTransition c2w_stEnlarge = new ScaleTransition(Duration.millis(180), c2w_btn);
                    c2w_stEnlarge.setToX(1.08);
                    c2w_stEnlarge.setToY(1.08);

                    ScaleTransition c2w_stNormal = new ScaleTransition(Duration.millis(180), c2w_btn);
                    c2w_stNormal.setToX(1.0);
                    c2w_stNormal.setToY(1.0);

                    c2w_btn.setOnMouseEntered(e -> c2w_stEnlarge.playFromStart());
                    c2w_btn.setOnMouseExited(e -> c2w_stNormal.playFromStart());

                    c2w_grid.add(c2w_btn, col, row);
                    c2w_index++;
                }
            }
        }

        Button c2w_logoutButton = new Button("Logout");
        c2w_logoutButton.setPrefWidth(120);
        c2w_logoutButton.setPrefHeight(50);
        c2w_logoutButton.setStyle(
                "-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 24;"
        );

        c2w_logoutButton.setOnMouseEntered(e ->
                c2w_logoutButton.setStyle(
                        "-fx-background-color: #b71c1c; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 24;"
                )
        );

        c2w_logoutButton.setOnMouseExited(e ->
                c2w_logoutButton.setStyle(
                        "-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 24;"
                )
        );

        // c2w_logoutButton.setOnAction(e -> signUpView.openAuthWindow());

        c2w_logoutButton.setOnAction(e -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");

            ButtonType yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noBtn = new ButtonType("No", ButtonBar.ButtonData.NO);

            alert.getButtonTypes().setAll(yesBtn, noBtn);

            alert.showAndWait().ifPresent(response -> {
                if (response == yesBtn) {
                    signUpView.openAuthWindow(); // logout
                }
            });
        });


        c2w_grid.add(c2w_logoutButton, 0, 3, 2, 1);

        Scene c2w_scene = new Scene(c2w_grid, 545, 800);
        c2w_primaryStage.setScene(c2w_scene);
        c2w_primaryStage.setTitle("News App");
        c2w_primaryStage.setResizable(false);
        c2w_primaryStage.show();
    }

    private void handleCategorySelection(String c2w_category) {
        if (c2w_category.equals("general")) {
            showGeneralNews();
        } else {
            showCategorySources(c2w_category);
        }
    }

    private void showGeneralNews() {

        c2w_primaryStage.setTitle("News App");

        c2w_newsContainer = new VBox(10);
        c2w_newsContainer.setPadding(new Insets(10));

        ScrollPane c2w_scrollPane = new ScrollPane(c2w_newsContainer);
        c2w_scrollPane.setFitToWidth(true);
        c2w_scrollPane.setStyle("-fx-background: #FFFAF5;");

        Button c2w_loadNewsButton = new Button("Load News");
        c2w_loadNewsButton.setStyle(
                "-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 24; -fx-padding: 8 32 8 32;"
        );

        ProgressIndicator c2w_loader = new ProgressIndicator();
        c2w_loader.setVisible(false);

        Button c2w_backButton = new Button("Back");
        c2w_backButton.setStyle(
                "-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 24; -fx-padding: 8 32 8 32;"
        );

        c2w_backButton.setOnAction(e -> showCategorySelection());

        c2w_loadNewsButton.setOnAction(e -> {
            c2w_loader.setVisible(true);
            loadNewsAsync(c2w_loader);
        });

        HBox c2w_bottomBox = new HBox(16, c2w_loadNewsButton, c2w_loader, c2w_backButton);
        c2w_bottomBox.setPadding(new Insets(20, 0, 40, 0));
        c2w_bottomBox.setAlignment(Pos.CENTER);

        BorderPane c2w_root = new BorderPane();
        c2w_root.setCenter(c2w_scrollPane);
        c2w_root.setBottom(c2w_bottomBox);

        Scene c2w_scene = new Scene(c2w_root, 545, 800);
        c2w_primaryStage.setScene(c2w_scene);
        c2w_primaryStage.setResizable(false);
        c2w_primaryStage.show();

        c2w_loader.setVisible(true);
        loadNewsAsync(c2w_loader);
    }

    private void showCategorySources(String c2w_category) {

        VBox c2w_sourcesContainer = new VBox(10);
        c2w_sourcesContainer.setPadding(new Insets(10));
        c2w_sourcesContainer.setAlignment(Pos.TOP_CENTER);

        ProgressIndicator c2w_loader = new ProgressIndicator();
        c2w_loader.setVisible(true);

        Button c2w_backButton = new Button("Back");
        c2w_backButton.setStyle(
                "-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 24; -fx-padding: 8 32 8 32;"
        );
        c2w_backButton.setOnAction(e -> showCategorySelection());

        BorderPane c2w_root = new BorderPane();
        ScrollPane c2w_scrollPane = new ScrollPane(c2w_sourcesContainer);
        c2w_scrollPane.setFitToWidth(true);
        c2w_scrollPane.setStyle("-fx-background: #f5f5f5;");

        c2w_root.setCenter(c2w_scrollPane);

        HBox c2w_bottomBox = new HBox(16, c2w_loader, c2w_backButton);
        c2w_bottomBox.setPadding(new Insets(20, 0, 40, 0));
        c2w_bottomBox.setAlignment(Pos.CENTER);

        c2w_root.setBottom(c2w_bottomBox);

        Scene scene = new Scene(c2w_root, 545, 800);
        c2w_primaryStage.setScene(scene);
        c2w_primaryStage.show();

        Task<Void> c2w_task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                JSONArray c2w_sources = apiController.getNews(c2w_category);

                Platform.runLater(() -> {
                    c2w_loader.setVisible(false);
                    c2w_sourcesContainer.getChildren().clear();

                    if (c2w_sources.length() == 0) {
                        c2w_sourcesContainer.getChildren().add(
                                new Label("No sources found for this category.")
                        );
                    }

                    for (int i = 0; i < c2w_sources.length(); i++) {

                        JSONObject source = c2w_sources.getJSONObject(i);
                        String name = source.getString("name");
                        String description = source.optString("description", "");
                        String urlSource = source.getString("url");

                        HBox c2w_card = createSourceCard(name, description, urlSource);
                        c2w_sourcesContainer.getChildren().add(c2w_card);
                    }
                });
                return null;
            }
        };

        new Thread(c2w_task).start();
    }

    private void loadNewsAsync(ProgressIndicator c2w_loader) {

        Task<Void> c2w_task = new Task<Void>() {
            @Override
            protected Void call() {
                loadNews();
                return null;
            }
        };

        c2w_task.setOnSucceeded(ev -> c2w_loader.setVisible(false));
        c2w_task.setOnFailed(ev -> c2w_loader.setVisible(false));

        new Thread(c2w_task).start();
    }

    private void loadNews() {

        try {
            page++;

            String c2w_articlesUrl =
                    "https://newsapi.org/v2/everything?q=tech&page=" + page +
                            "&apiKey=3d609fde7de445beb00b54dbb26c80e6&pageSize=10";

            URL c2w_url = new URL(c2w_articlesUrl);
            HttpURLConnection conn = (HttpURLConnection) c2w_url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader c2w_in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = c2w_in.readLine()) != null) {
                content.append(inputLine);
            }

            c2w_in.close();
            conn.disconnect();

            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray articles = jsonResponse.getJSONArray("articles");

            Platform.runLater(() -> {
                for (int i = 0; i < articles.length(); i++) {

                    JSONObject article = articles.getJSONObject(i);
                    String title = article.getString("title");
                    String description = article.optString("description", "");
                    String url = article.getString("url");

                    VBox card = createNewsCard(title, description, url);
                    c2w_newsContainer.getChildren().add(card);
                }
            });

        } catch (Exception e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "Failed to load news").show()
            );
        }
    }

    private VBox createNewsCard(String title, String description, String url) {

        VBox card = new VBox(8);
        card.setPadding(new Insets(16));
        card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 16; -fx-border-radius: 16;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);

        Hyperlink link = new Hyperlink("Read more");
        link.setOnAction(e -> {
            Stage webStage = new Stage();
            WebView webView = new WebView();
            webView.getEngine().load(url);
            webStage.setScene(new Scene(webView, 900, 600));
            webStage.show();
        });

        card.getChildren().addAll(titleLabel, descLabel, link);
        return card;
    }

    private HBox createSourceCard(String name, String description, String url) {

        VBox box = new VBox(6);
        box.setPadding(new Insets(12));

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);

        Hyperlink link = new Hyperlink(url);
        link.setOnAction(e -> {
            Stage webStage = new Stage();
            WebView webView = new WebView();
            webView.getEngine().load(url);
            webStage.setScene(new Scene(webView, 900, 600));
            webStage.setResizable(false);
            webStage.show();
        });

        box.getChildren().addAll(nameLabel, descLabel, link);

        HBox card = new HBox(box);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        card.setPadding(new Insets(10));

        return card;
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}