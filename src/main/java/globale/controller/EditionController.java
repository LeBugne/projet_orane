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
import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditionController implements Observateur {
    @FXML private Button buttonBack;

    @FXML private Button crée;

    /* ------------------------- */
    @FXML private Label titreImg;
    @FXML private StackPane stackPane;
    @FXML private Button uploadButton;
    @FXML private HBox cadrePhoto;
    @FXML private ImageView img;
    @FXML private Button next;
    @FXML private Button prev;
    @FXML private Label labelBas;
    private ArrayList<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;

    /* ------------------------- */
    @FXML private TextField adrChantier;
    @FXML private DatePicker calendrier;
    @FXML private TextField concessionnaire;
    @FXML private TextField responsable;
    @FXML private TextField tel;
    @FXML private TextField ville;
    private Travaux travaux;
    private ArrayList<File> arrayListFile = new ArrayList<>();


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
    public void initialize(){
        // Animation pour le survol
        FillTransition fillIn = new FillTransition(Duration.millis(100), this.crée.getShape());
        fillIn.setToValue(Color.web("#07c68a")); // Couleur cible

        // Animation pour la sortie
        FillTransition fillOut = new FillTransition(Duration.millis(200), this.crée.getShape());
        fillOut.setToValue(Color.web("#1899D6")); // Couleur initiale

        // Appliquer les animations
        crée.setOnMouseEntered(event -> fillIn.playFromStart());
        crée.setOnMouseExited(event -> fillOut.playFromStart());

        // Mettre à jour l'affichage initial
        updateImageDisplay();
    }

    private void updateImageDisplay() {
        if (images.isEmpty()) {
            // Aucune image : afficher le bouton d'upload, cacher cadrePhoto
            //img.setImage(null);
            uploadButton.setVisible(true);
            uploadButton.setManaged(true);
        } else {
            // Images présentes : afficher l'image actuelle, montrer cadrePhoto, cacher uploadButton
            this.img.setPickOnBounds(true);
            img.setImage(images.get(currentImageIndex));
            uploadButton.setVisible(false);
            uploadButton.setManaged(false);
            cadrePhoto.setVisible(true);
            cadrePhoto.setManaged(true);
            //On met à jour le label du bas
        }
        this.labelBas.setText((this.currentImageIndex + 1 )+"/3");

    }

    @FXML
    private void handleUploadImage(ActionEvent event) throws URISyntaxException {
        // Créer un FileChooser pour sélectionner une image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        // Filtrer pour n'afficher que les fichiers image
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Afficher la boîte de dialogue et récupérer le fichier sélectionné
        File selectedFile = fileChooser.showOpenDialog(img.getScene().getWindow());
        if (selectedFile != null) {
            // Convertir le fichier en URL pour l'ImageView
            String imagePath = selectedFile.toURI().toString();
            //System.out.println("chemin capturé :" + imagePath);
            Image image = new Image(imagePath);
            img.setImage(image);
            this.images.add(image);

            // Stocker le chemin de l'image (ou une autre référence, voir ci-dessous)
            storeImagePath(imagePath);
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
    public void handleBackButton(){
        switchToAccueil();
    }
    @FXML
    public void handleAjoutezButton(){
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

        for(File f : this.arrayListFile){
            Image i = new Image(f.toURI().toString());
            creation.ajouterImg(i);
        }

        Travaux.getInstance().ajouterChantier(creation);

        // Ajouter la valeur si valide
        textFieldValues.add(adrChantier);

        switchToAccueil();
    }
    public Chantier createChantier(Concessionaire c, String date, String adr){
        return new Chantier(c,date,adr);
    }
    @FXML
    public void handlePremierTextField(){
        String res = this.adrChantier.getText();
    }
    public void switchToAccueil(){
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
    @Override
    public void reagir() {
    }

    public void ajouterFile(File f){ this.arrayListFile.add(f); }

}
