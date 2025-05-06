package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("JavaFX application starting...");
        System.err.println("Initializing primary stage.");
        try {
            System.out.println("Bienvenue ! Loading FXML...");
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/AccueilView.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Gestion des Travaux");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            System.out.println("JavaFX window displayed.");
        } catch (Exception e) {
            System.err.println("Error loading application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting TravauxApp...");
        System.err.println("Application entry point reached.");
        try {
            launch(args);
            System.out.println("JavaFX application launched.");
        } catch (Exception e) {
            System.err.println("Error in main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}