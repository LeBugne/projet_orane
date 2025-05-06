package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML avec le chemin correct

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/AccueilView.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Gestion des Travaux");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("Application JavaFX démarrée");
        launch(args);
    }
}