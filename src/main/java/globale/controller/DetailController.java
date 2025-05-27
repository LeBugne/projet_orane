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

import javax.imageio.ImageReader;
import java.io.IOException;
import java.util.ArrayList;

public class DetailController {

    @FXML private Button buttonAccueil;
    @FXML private HBox cadrePhoto;
    @FXML private Label titreImg;

    /* ------------------------------ */
    @FXML private Label labelAdresse;
    @FXML private Label labelConcessionaire;
    @FXML private Label labelDate;
    @FXML private Label labelResp;
    @FXML private Label labelTel;
    @FXML private Label labelVille;
    @FXML private VBox listeInfo;

    @FXML private Label res1;
    @FXML private Label res2;
    @FXML private Label res3;
    @FXML private Label res4;
    @FXML private Label res5;
    @FXML private Label res6;

    /* ------------------------------ */
    @FXML private HBox topHbox;
    @FXML private ImageView img;
    @FXML private Label labelBas;
    private ArrayList<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    private Chantier chantier;

    @FXML
    public void initialize(){

        this.res1.setText(this.chantier.getDate().toString());
        this.res2.setText(this.chantier.getAdresse());
        this.res3.setText(this.chantier.getConcessionaire().getNom());

        if(this.chantier.getVille() != null){
            this.res4.setText(this.chantier.getVille());
        }

        if(this.chantier.getTelephone() != null){
            this.res5.setText(this.chantier.getTelephone());
        }

        if(this.chantier.getResponsable() != null){
            this.res6.setText(this.chantier.getResponsable());
        }

        try {
            for(Image i : this.chantier.getArrayListImage()){
                images.add(i);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des images : " + e.getMessage());
        }

        updateImageDisplay();

    }
    public void setChantier(Chantier c) {
        this.chantier = c;
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
    public void handleNextImage() {
        if (images.isEmpty()) return;
        currentImageIndex = (currentImageIndex + 1) % images.size();
        updateImageDisplay();
    }

    public void updateImageDisplay() {
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

    public void setTitreImg(){
        if(this.currentImageIndex == 1){
            this.titreImg.setText("Arrếté");
        } else if (this.currentImageIndex == 2) {
            this.titreImg.setText("");
        }
    }



}
