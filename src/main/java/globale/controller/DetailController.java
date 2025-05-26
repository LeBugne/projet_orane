package globale.controller;

import globale.model.Chantier;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class DetailController {

    @FXML private Button buttonAccueil;
    @FXML private HBox cadrePhoto;
    @FXML private Label labelAdresse;
    @FXML private Label labelConcessionaire;
    @FXML private Label labelDate;
    @FXML private Label labelResp;
    @FXML private Label labelTel;
    @FXML private Label labelVille;
    @FXML private VBox listeInfo;
    @FXML private HBox topHbox;
    @FXML private ImageView img;
    @FXML private Label labelBas;
    private ArrayList<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;


    @FXML
    public void initialize(){
        try {
            images.add(new Image(getClass().getResourceAsStream("/images/photo.png")));
            images.add(new Image(getClass().getResourceAsStream("/images/star.jpg")));
            images.add(new Image(getClass().getResourceAsStream("/images/photo.png")));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des images : " + e.getMessage());
        }

        // Mettre à jour l'affichage initial
        updateImageDisplay();
    }

    public void setData(Chantier chantier) {
        labelConcessionaire.setText("Nom: " + chantier.getConcessionaire().getNom());
        labelAdresse.setText("Adresse: " + chantier.getAdresse());
    }


    public void handleAccueilButton(){
        switchToAccueil();
    }

    public void switchToAccueil(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilView.fxml"));

            // Crée ou récupère une instance du contrôleur
            loader.setControllerFactory(param -> ControllerManager.getInstance().getAccueilController());

            Parent newViewRoot = loader.load();

            Stage stage = (Stage) buttonAccueil.getScene().getWindow();
            Scene newScene = new Scene(newViewRoot);
            stage.setScene(newScene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Erreur lors du changement de vue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handlePreviousImage() {
        if (images.isEmpty()) return;
        currentImageIndex = (currentImageIndex - 1 + images.size()) % images.size();
        updateImageDisplay();
    }
    @FXML
    private void handleNextImage() {
        if (images.isEmpty()) return;
        currentImageIndex = (currentImageIndex + 1) % images.size();
        updateImageDisplay();
    }

    private void updateImageDisplay() {
        if (images.isEmpty()) {
            // Aucune image : afficher le bouton d'upload, cacher cadrePhoto
            img.setImage(null);
            cadrePhoto.setVisible(false);
            cadrePhoto.setManaged(false);
        } else {
            // Images présentes : afficher l'image actuelle, montrer cadrePhoto, cacher uploadButton
            img.setImage(images.get(currentImageIndex));
            cadrePhoto.setVisible(true);
            cadrePhoto.setManaged(true);
            //On met à jour le label du bas
            this.labelBas.setText((this.currentImageIndex + 1)+"/"+this.images.size());
        }
    }
}
