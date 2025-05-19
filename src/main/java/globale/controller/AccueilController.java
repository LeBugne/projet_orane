package globale.controller;

import globale.Observateur;
import globale.model.Chantier;
import globale.model.Concessionaire;
import globale.model.Travaux;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController implements Observateur {
    @FXML private Label LabelTitre;
    @FXML private TextField searchBar;
    @FXML private Button buttonEdition;

    /* -------------------- */
    @FXML private ScrollPane scrollPane;
    @FXML private GridPane grille;
    private Travaux travaux ;


    public AccueilController(){

    }

    @FXML
    public void initialize() throws IOException {

        this.grille.getStyleClass().add("grid");

        this.travaux = new Travaux();

        String[] noms = {"Veolia", "Kanye West", "EDF", "Bouygues", "Vinci", "Enedis" , "Kanye West", "EDF", "Bouygues", "Vinci", "Enedis"};
        String adresse = "En bas de chez toi";
        String date = "15/05/2025";

        for (String nom : noms) {
            Concessionaire c = new Concessionaire(nom);
            Chantier chantier = new Chantier(c, date, adresse);
            this.travaux.ajouterChantier(chantier);
        }

        System.out.println("Il y a " + this.travaux.arrayList.size() + " chantiers");

        int col = 0;
        int row = 0;

        for (int i = 0; i < this.travaux.arrayList.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemView.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            ItemController item = fxmlLoader.getController();
            item.setData(this.travaux.arrayList.get(i));

            // Passage à la ligne après 3 colonnes
            if (col == 2) {
                col = 0;
                row++;
            }

            this.grille.add(anchorPane, col++, row);
            GridPane.setMargin(anchorPane,new Insets(20));
        }
    }


    private void handleTextField(){
    }

    @FXML
    private void handleButtonEdition(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditionView.fxml"));
            Parent newViewRoot = loader.load();
            Stage stage = (Stage) buttonEdition.getScene().getWindow();
            Scene newScene = new Scene(newViewRoot);
            stage.setScene(newScene);
            stage.show();

        }  catch (Exception e) {
            System.err.println("Erreur lors du changement de vue : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void reagir() {
        System.out.println("Je réagis ! ");
    }
}