package globale.controller;

import globale.model.Chantier;
import globale.model.Concessionaire;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    @FXML private VBox vbox1, vbox2, vbox3, vbox4, vbox5, vbox6;
    @FXML private Label labelAdresse;
    @FXML private Label res1;


    @FXML private Label labelConcessionaire;
    @FXML private Label labelDate;
    @FXML private Label labelResp;
    @FXML private Label labelTel;
    @FXML private Label labelVille;
    @FXML private VBox listeInfo;

    @FXML private Label res2;
    @FXML private Label res3;
    @FXML private Label res4;
    @FXML private Label res5;
    @FXML private Label res6;

    /* ------------------------------ */
    @FXML private HBox topHbox;
    @FXML private ImageView img;
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

        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                currentCategory = (String) newToggle.getUserData();
                updateImageView();
            }
        });

        radio1.setSelected(true);
        currentCategory = "Arrêté";

        configureEditableLabelWraper();

        loadImages();
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

    private void storeImagePath(String imagePath) throws URISyntaxException {
        File sourceFile = new File(new URI(imagePath));

        // Dossier externe : ~/myapp/images
        String userHome = System.getProperty("user.home");
        File targetDir = new File(userHome, "myapp/images");
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        File targetFile = new File(targetDir, sourceFile.getName());


        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            String relativePath = "images/" + sourceFile.getName();
            //System.out.println("Image copiée et stockée : " + relativePath);
            // Stocker relativePath dans une base de données ou une liste
        } catch (Exception e) {
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
            System.out.println();
            Image image = new Image(imagePath);
            storeImagePath(imagePath);
            imagesByCategory.put(currentCategory, image);
            this.chantier.addImage(currentCategory,file.getName());

            updateImageView(); // Mettre à jour l'affichage
        }
    }

    /**
     * Cette fonction permet de charger les images dès l'arrivé dans le détail
     */
    public void loadImages() {
        imagesByCategory.clear(); // Vider la map pour éviter les doublons
        for (Map.Entry<String, String> entry : chantier.getImageFileNames().entrySet()) {
            String category = entry.getKey();
            String url = entry.getValue();
            try {
                Image image = new Image(url);
                imagesByCategory.put(category, image);
            } catch (Exception e) {
                System.err.println("Erreur chargement image pour catégorie " + category + ": " + e.getMessage());
            }
        }
        updateImageView();
    }
    @FXML
    private void handleDeleteImage() {
        if (currentCategory != null) {
            imagesByCategory.remove(currentCategory); // Supprimer l'image de la catégorie
            this.chantier.remove(currentCategory); // On modifie le modèle
            updateImageView(); // Mettre à jour l'affichage
        }
    }

    private void configureEditableLabelWraper(){
        configureEditableLabel(vbox1, res1, "date");
        configureEditableLabel(vbox2, res2, "adresse");
        configureEditableLabel(vbox3, res3, "concessionnaire");
        configureEditableLabel(vbox4, res4, "ville");
        configureEditableLabel(vbox5, res5, "telephone");
        configureEditableLabel(vbox6, res6, "responsable");
    }
    private void configureEditableLabel(VBox vbox, Label label, String attribute) {
        vbox.setOnMouseClicked(event -> {
            TextField textField = new TextField(label.getText());
            textField.setPrefWidth(label.getWidth());
            textField.getStyleClass().add("editable-field");
            int index = vbox.getChildren().indexOf(label);
            vbox.getChildren().set(index, textField);
            textField.requestFocus();
            textField.selectAll();

            // Valider avec Entrée
            textField.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    String newValue = textField.getText().trim();
                    if (!newValue.isEmpty()) {
                        switch (attribute) {
                            case "date" -> chantier.setDate(newValue);
                            case "adresse" -> chantier.setAdresse(newValue);
                            case "concessionnaire" -> chantier.setConcessionnaire(new Concessionaire(newValue));
                            case "ville" -> chantier.setVille(newValue);
                            case "telephone" -> chantier.setTelephone(newValue);
                            case "responsable" -> chantier.setResponsable(newValue);
                        }
                        label.setText(newValue);
                    }
                    vbox.getChildren().set(index, label);
                } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    // Annuler avec Échap
                    vbox.getChildren().set(index, label);
                }
            });

            // Annuler sur perte de focus (clic hors du TextField)
            textField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                if (wasFocused && !isFocused) {
                    // Restaurer le Label sans sauvegarder
                    vbox.getChildren().set(index, label);
                }
            });
        });
    }

    private void validateInput(TextField textField, Label label, String attribute, int index, VBox vbox) {
        String newValue = textField.getText().trim();
        if (!newValue.isEmpty()) {
            switch (attribute) {
                case "date" -> chantier.setDate(newValue);
                case "adresse" -> chantier.setAdresse(newValue);
                case "concessionnaire" -> chantier.setConcessionnaire(new Concessionaire(newValue));
                case "ville" -> chantier.setVille(newValue);
                case "telephone" -> chantier.setTelephone(newValue);
                case "responsable" -> chantier.setResponsable(newValue);
            }
            label.setText(newValue);
        }
        vbox.getChildren().set(index, label);
    }







}
