package globale.controller;

import globale.Observateur;
import globale.model.Chantier;
import globale.model.Concessionaire;
import globale.model.SujetObserve;
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

    public AccueilController(Travaux model){
        this.travaux = model;
        this.travaux.ajouterObservateur(this);

    }

    @FXML
    public void initialize() throws IOException {

        this.grille.getStyleClass().add("grid");

        /*String[] noms = {"Veolia", "Kanye West", "EDF", "Bouygues", "Vinci", "Enedis" , "Kanye West", "EDF", "Bouygues", "Vinci", "Enedis"};
        String adresse = "En bas de chez toi";
        String date = "15/05/2025";

        for (String nom : noms) {
            Concessionaire c = new Concessionaire(nom);
            Chantier chantier = new Chantier(c, date, adresse);
            this.travaux.ajouterChantier(chantier);
        }*/

       // System.out.println("Il y a " + this.travaux.arrayList.size() + " chantiers");

        int col = 0;
        int row = 0;
        if(this.travaux.arrayList.size() != 0){
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

    }


    private void handleTextField(){
    }

    @FXML
    private void handleButtonEdition() {
        try {
            // Créer le loader pour EditionView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditionView.fxml"));
            // Utiliser l'instance existante de EditionController
            EditionController ec = ControllerManager.getInstance().getEditionController();
            loader.setControllerFactory(param -> ec);
            // Charger la vue
            Parent newViewRoot = loader.load();
            // Obtenir le Stage actuel
            Stage stage = (Stage) buttonEdition.getScene().getWindow();
            // Créer et définir la nouvelle scène
            Scene newScene = new Scene(newViewRoot);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du changement de vue : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Si on ajoute un chantier une vignette dois s'ajouter
     */
    private void update() throws IOException {
        grille.getChildren().clear(); // Vider le GridPane avant de recréer
        int col = 0;
        int row = 0;
        if (!this.travaux.arrayList.isEmpty()) {
            for (int i = 0; i < this.travaux.arrayList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemView.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController item = fxmlLoader.getController();
                Chantier chantier = this.travaux.arrayList.get(i);
                item.setData(chantier);

                // Ajouter un gestionnaire de clic pour ouvrir DetailView
                anchorPane.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/fxml/DetailView.fxml"));
                        detailLoader.setController(ControllerManager.getInstance().getDetailController());
                        Scene newScene = new Scene(detailLoader.load());
                        DetailController detailController = detailLoader.getController();
                        detailController.setData(chantier); // Passer les données
                        Stage stage = (Stage) anchorPane.getScene().getWindow();
                        stage.setScene(newScene);
                        stage.show();
                    } catch (IOException e) {
                        System.err.println("Erreur lors de l'ouverture de DetailView : " + e.getMessage());
                        e.printStackTrace();
                    }
                });

                // Passage à la ligne après 3 colonnes
                if (col == 2) {
                    col = 0;
                    row++;
                }

                grille.add(anchorPane, col++, row);
                GridPane.setMargin(anchorPane, new Insets(20));
            }
        }
    }


    @Override
    public void reagir() {
    }
}