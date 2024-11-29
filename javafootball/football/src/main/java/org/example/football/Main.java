package org.example.football;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/football/MainView.fxml"));
        Scene scene = new Scene(loader.load());

        // Add the stylesheet
        scene.getStylesheets().add(getClass().getResource("/org/example/football/styles.css").toExternalForm());

        // Set the application icon
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/football/icon.png")));

        primaryStage.setTitle("Football Matches Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}