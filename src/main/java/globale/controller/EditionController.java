package globale.controller;

import globale.Observateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditionController implements Observateur {
    @FXML private Button buttonBack;
    @FXML private TextField premierField;

    public EditionController(){

    }

    @FXML
    public void initialize(){
        System.out.println("Salut (Edition)");
    }

    @FXML
    public void handleBackButton(){
        System.out.println("test");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilView.fxml"));
            Parent newViewRoot = loader.load();
            Stage stage = (Stage) buttonBack.getScene().getWindow();
            Scene newScene = new Scene(newViewRoot);
            stage.setScene(newScene);
            stage.show();

        }  catch (Exception e) {
            System.err.println("Erreur lors du changement de vue : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handlePremierTextField(){
        String res = this.premierField.getText();
        System.out.println("J'ai trouvé : " + res);
        this.premierField.clear();


    }


    @Override
    public void reagir() {
        System.out.println("Je réagis");
    }
}
