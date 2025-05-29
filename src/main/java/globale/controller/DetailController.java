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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DetailController {

    @FXML private Button buttonAccueil;
    @FXML private VBox cadrePhoto;
    @FXML private Button deletePhoto;
    @FXML private Button uploadButton;

    @FXML private RadioButton radio1;
    @FXML private RadioButton radioIPT;
    @FXML private RadioButton radioDT;

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

    private ArrayList<Image> images = new ArrayList<>(Arrays.asList(null, null, null)); //Cette écriture évite les out of bound exeption
    private Map<String, Image> imagesByCategory = new HashMap<>();
    private String currentCategory = null;
    private Chantier chantier;

    @FXML
    public void initialize(){

        radio1.setUserData("Arrêté");
        radioIPT.setUserData("IPT");
        radioDT.setUserData("DT/DICT");

        this.res1.setText(this.chantier.getDate().toString());
        this.res2.setText(this.chantier.getAdresse());
        this.res3.setText(this.chantier.getConcessionaire().getNom());

        ToggleGroup toggleGroup = new ToggleGroup();
        radio1.setToggleGroup(toggleGroup);
        radioIPT.setToggleGroup(toggleGroup);
        radioDT.setToggleGroup(toggleGroup);

        // Optionnel : définir un RadioButton sélectionné par défaut
        radio1.setSelected(true);

        setLabels();
        setMap();

        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                currentCategory = (String) newToggle.getUserData();
                updateImageView();
            }
        });

        radio1.setSelected(true);
        currentCategory = "Arrêté";
        updateImageView();
    }
    private void updateImageView() {
        // Réinitialiser l'ImageView et les boutons
        img.setImage(null);
        uploadButton.setVisible(true);
        uploadButton.setManaged(true);
        deletePhoto.setVisible(false);
        deletePhoto.setManaged(false);

        // Si une catégorie est sélectionnée, vérifier si elle a une image
        if (currentCategory != null) {
            Image image = imagesByCategory.get(currentCategory);
            if (image != null) {
                img.setImage(image);
                uploadButton.setVisible(false); // Cacher le bouton Upload si une image existe
                uploadButton.setManaged(false);
                deletePhoto.setVisible(true); // Afficher le bouton de suppression
                deletePhoto.setManaged(true);
            }
        }
    }
    public void setChantier(Chantier c) {
        this.chantier = c;
    }

    /**
     * Wraper pour que ça soit plus propre
     */
    public void setLabels(){
        if(this.chantier.getVille() != null){
            this.res4.setText(this.chantier.getVille());
        }

        if(this.chantier.getTelephone() != null){
            this.res5.setText(this.chantier.getTelephone());
        }

        if(this.chantier.getResponsable() != null){
            this.res6.setText(this.chantier.getResponsable());
        }
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
    private void handleUploadImage() throws URISyntaxException {
        if (currentCategory == null) {
            System.out.println("Aucune catégorie sélectionnée.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String imagePath = file.toURI().toString();
           // storeImagePath(imagePath);
            Image image = new Image(imagePath);
            imagesByCategory.put(currentCategory, image);

            updateImageView(); // Mettre à jour l'affichage
        }
    }
    @FXML
    private void handleDeleteImage() {
        if (currentCategory != null) {
            imagesByCategory.remove(currentCategory); // Supprimer l'image de la catégorie
            System.out.println(this.imagesByCategory);
            updateImageView(); // Mettre à jour l'affichage
        }
    }
    public void setMap() {
        this.imagesByCategory.putAll(chantier.getImagesByCategory());
    }






}
