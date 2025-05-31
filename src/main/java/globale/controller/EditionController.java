package globale.controller;

import globale.Observateur;
import globale.model.Chantier;
import globale.model.Concessionaire;
import globale.model.Travaux;
import javafx.animation.FillTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class EditionController implements Observateur {
    @FXML private Button buttonBack;
    @FXML private Button crée;

    /* ------------------------- */
    @FXML private StackPane stackPane;
    @FXML private Button uploadButton;
    @FXML private HBox cadrePhoto;
    @FXML private Button deletePhoto;
    @FXML private ImageView img;
    @FXML private HBox choixImage;
    @FXML private RadioButton radio1;
    @FXML private RadioButton radioDT;
    @FXML private RadioButton radioIPT;

    /* --------------------------- */
    @FXML private Map<String, Image> imagesByCategory = new HashMap<>();
    ToggleGroup toggleGroup = new ToggleGroup(); // ça permet de séléctionnez un unique bouton à la fois

    private final List<RadioButton> radioButtons = Arrays.asList(radio1, radioIPT, radioDT);
    private String currentCategory = null;

    /* ------------------------- */
    @FXML private TextField adrChantier;
    @FXML private DatePicker calendrier;
    @FXML private TextField concessionnaire;
    @FXML private TextField responsable;
    @FXML private TextField tel;
    @FXML private TextField ville;
    private Travaux travaux;
    private ArrayList<File> arrayListFile = new ArrayList<>();

    private static final String IMAGE_DIR = System.getProperty("user.home") + "/myapp/images/";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private static final Pattern TEL_PATTERN = Pattern.compile(
            "^(?:0|\\+33\\s?)[1-9](?:[\\s.-]?[0-9]{2}){4}$"
    );

    public EditionController(Travaux model){
        this.travaux = model;
        this.travaux.ajouterObservateur(this);
    }

    @FXML
    private void initialize() {
        // Associer les radio buttons à des catégories
        radio1.setUserData("Arrêté");
        radioIPT.setUserData("IPT");
        radioDT.setUserData("DT/DICT");

        radio1.setToggleGroup(toggleGroup);
        radioIPT.setToggleGroup(toggleGroup);
        radioDT.setToggleGroup(toggleGroup);

        // Écouter les changements de sélection des radio buttons
        // C'est cette portion qui gère le changement d'image
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                currentCategory = (String) newToggle.getUserData();
                updateImageView();
            }
        });

        // Sélectionner un radio button par défaut (optionnel)
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
            storeImagePath(imagePath);
            Image image = new Image(imagePath);
            imagesByCategory.put(currentCategory, image); // Associer l'image à la catégorie
            updateImageView(); // Mettre à jour l'affichage
        }
    }


    /**
     * Cette fonction est utile pour ne pas dépendre du chemin absolu d'une image
     * @param imagePath
     */
    private void storeImagePath(String imagePath) throws URISyntaxException {
        File sourceFile = new File(new URI(imagePath));

        // Dossier externe : ~/myapp/images
        String userHome = System.getProperty("user.home");
        File targetDir = new File(userHome, "myapp/images");
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        File targetFile = new File(targetDir, sourceFile.getName());
        this.arrayListFile.add(targetFile);

        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            String relativePath = "images/" + sourceFile.getName();
            //System.out.println("Image copiée et stockée : " + relativePath);
            // Stocker relativePath dans une base de données ou une liste
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void handleBackButton(){ switchToAccueil(); }

    /**
     * Lorsque l'on appuiera sur le bouton Ajoutez voici tout ce qui se passera.
     */
    @FXML
    public void handleAjoutezButton() throws IOException {
        ArrayList<String> textFieldValues = new ArrayList<>();
        String adrChantier = this.adrChantier.getText().trim();
        String conce = this.concessionnaire.getText().trim();
        String tel = this.tel.getText().trim();
        String responsable = this.responsable.getText().trim();
        String ville = this.ville.getText().trim();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Vérifier si le contenu du searchBar correspond à une adresse e-mail
        if (adrChantier.isEmpty() || conce.isEmpty()) {
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur");
            alert.setContentText("Un champs obligatoire est vide");
            alert.showAndWait();
            return;
        }

        // Récupérer la date du DatePicker et la convertir en String
        LocalDate selectedDate = this.calendrier.getValue();
        if (selectedDate == null) {
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur");
            alert.setContentText("Il faut une date");
            alert.showAndWait();
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateString = selectedDate.format(formatter);

        Concessionaire c = new Concessionaire(conce);

        Chantier creation = createChantier(c,dateString,adrChantier);
        creation.setVille(ville);
        creation.setResponsable(responsable);
        creation.setTelephone(tel);

        /* Ici on verse les images chargé, dans l'objet du modèle (Chantier) qui permet de garder en mémoire les images */
        creation.getImagesByCategory().putAll(this.imagesByCategory);
        for (Map.Entry<String, Image> entry : imagesByCategory.entrySet()) {
            String key = entry.getKey();
            String url = entry.getValue().getUrl();
            url = "file:" + IMAGE_DIR + url.substring(url.lastIndexOf('/') + 1);;
            creation.getImageFileNames().put(key, url);
        }

        Travaux.getInstance().ajouterChantier(creation);
        Travaux.getInstance().ajouterChantier(creation.getId(),creation);

        // Ajouter la valeur si valide
        textFieldValues.add(adrChantier);
        clearImages();

        switchToAccueil();
    }
    public Chantier createChantier(Concessionaire c, String date, String adr){
        return new Chantier(c,date,adr);
    }
    @FXML public void handlePremierTextField(){
        String res = this.adrChantier.getText();
    }
    public void switchToAccueil(){
        clearImages();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilView.fxml"));

            // Crée ou récupère une instance du contrôleur
            loader.setControllerFactory(param -> ControllerManager.getInstance().getAccueilController());

            Parent newViewRoot = loader.load();

            Stage stage = (Stage) buttonBack.getScene().getWindow();
            Scene newScene = new Scene(newViewRoot);
            stage.setScene(newScene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Erreur lors du changement de vue : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDeleteImage() {
        if (currentCategory != null) {
            imagesByCategory.remove(currentCategory); // Supprimer l'image de la catégorie
            updateImageView(); // Mettre à jour l'affichage
        }
    }
    public void clearImages(){
        this.imagesByCategory.clear();
    }
    @Override
    public void reagir() {
    }


}
