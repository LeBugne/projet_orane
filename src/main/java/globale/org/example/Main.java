package globale.org.example;

import globale.controller.ControllerManager;
import globale.model.Travaux;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.TransferQueue;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Travaux.getInstance().loadFromFile(); // Si il y'a une sauvegarde on charge déjà le modèle ( càd la HashMap )
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/AccueilView.fxml"));
            loader.setControllerFactory(param -> ControllerManager.getInstance().getAccueilController());
            Parent root = loader.load();
            primaryStage.setTitle("Gestion des Travaux");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error loading application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            launch(args);
            System.out.println("JavaFX application launched.");
        } catch (Exception e) {
            System.out.println("Error in main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}