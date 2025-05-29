package globale.controller;

import globale.Observateur;
import globale.model.Chantier;
import globale.model.Concessionaire;
import globale.model.Travaux;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

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
        update();
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
        if (!this.travaux.getChantierMap().isEmpty()) {
            for (Chantier chantier : Travaux.getInstance().getChantierMap().values()) {
                //System.out.println("Voici comment la map est perçus"+Travaux.getInstance().getChantierMap());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemView.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController item = fxmlLoader.getController();
                item.setData(chantier);

                // Ajouter un gestionnaire de clic pour ouvrir DetailView
                anchorPane.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/fxml/DetailView.fxml"));
                        /* Cette syntaxe permet un control plus accru sur la façon dont le controller est initialisé */
                        detailLoader.setControllerFactory(param -> {
                            DetailController detailController = new DetailController();
                            detailController.setChantier(chantier);  // injecter AVANT le .load()
                            return detailController;
                        });
                        Scene newScene = new Scene(detailLoader.load());

                        DetailController detailController = detailLoader.getController();

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

    public void test() throws IOException {
    // Création d'un concessionaire fictif
        Concessionaire fakeConcessionaire = new Concessionaire("NomConcessionaire");

    // Date fictive (format texte attendu par ton constructeur)
        String fakeDate = "28/05/2025";

    // Adresse fictive
        String fakeAdresse = "123 Rue de l'Exemple, Paris";

    // Création d'un chantier fictif
        Chantier fakeChantier = new Chantier(fakeConcessionaire, fakeDate, fakeAdresse);

    // On peut compléter les autres champs si besoin :
        fakeChantier.setTelephone("0123456789");
        fakeChantier.setVille("Paris");
        fakeChantier.setResponsable("Jean Dupont");


    // Ajouter des fichiers/images fictives
        fakeChantier.getArrayListImage().add(new Image("file:/home/lebugne/IdeaProjects/projet_orane/icon.jpeg"));
        fakeChantier.getArrayListImage().add(new Image("file:/home/lebugne/Bureau/insta_photo.jpg"));

        Travaux.getInstance().ajouterChantier(fakeChantier);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemView.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();

        ItemController item = fxmlLoader.getController();
        item.setData(fakeChantier);

        anchorPane.setOnMouseClicked(event -> {
            try {
                FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/fxml/DetailView.fxml"));
                /* Cette syntaxe permet un control plus accru sur la façon dont le controller est initialisé */
                detailLoader.setControllerFactory(param -> {
                    DetailController detailController = new DetailController();
                    detailController.setChantier(fakeChantier);  // injecter AVANT le .load()
                    return detailController;
                });
                Scene newScene = new Scene(detailLoader.load());

                DetailController detailController = detailLoader.getController();

                Stage stage = (Stage) anchorPane.getScene().getWindow();
                stage.setScene(newScene);
                stage.show();
            } catch (IOException e) {
                System.err.println("Erreur lors de l'ouverture de DetailView : " + e.getMessage());
                e.printStackTrace();
            }
        });


        this.grille.add(anchorPane,0,0);
    }


    @Override
    public void reagir() throws IOException {
        update();
    }
}