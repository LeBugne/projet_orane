package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PropositionController {
    @FXML private Button backButton;

    public PropositionController(){

    }

    @FXML
    public void initialize(){
        System.out.println("Salut");

    }

    @FXML
    public void handleBackButton(){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilView.fxml"));
                Parent newViewRoot = loader.load();
                Stage stage = (Stage) backButton.getScene().getWindow();
                Scene newScene = new Scene(newViewRoot);
                stage.setScene(newScene);
                stage.show();

            }  catch (Exception e) {
                System.err.println("Erreur lors du changement de vue : " + e.getMessage());
                e.printStackTrace();
            }
    }


}
