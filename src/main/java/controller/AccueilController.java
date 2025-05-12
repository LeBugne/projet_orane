package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController {
    @FXML private TextField searchAccueil;
    @FXML private ComboBox<String> comboBox;
    @FXML private Label LabelTitre;
    @FXML private Button searchButton;

    public AccueilController(){

    }

    @FXML
    public void initialize() {
        try {
            String imagePath = "/images/loupe.png"; // Chemin relatif à src/main/resources
            // Vérifie si la ressource existe
            if (getClass().getResource(imagePath) == null) {
                System.err.println("Image not found at: " + imagePath);
                throw new RuntimeException("Image not found: " + imagePath);
            }
            // Charge l'image directement avec l'URL
            Image img = new Image(getClass().getResource(imagePath).toExternalForm());
            ImageView imageView = new ImageView(img);
            imageView.setFitHeight(32);
            imageView.setFitWidth(32);
            searchButton.setGraphic(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement de l'image", e);
        }
    }

    private void handleTextField(){
    }

    @FXML
    private void handleSearchButtonAction() {
        String searchText = searchAccueil.getText();
        System.out.println("Texte extraie : " + searchText);
        searchButton.setOnAction(event -> {
            System.out.println("test ");
        });
    }

    @FXML
    private void handleComboBoxSelection(ActionEvent event) {
        String selectedCategory = comboBox.getValue();
        if (selectedCategory != null) {
            LabelTitre.setText("Catégorie sélectionnée : " + selectedCategory);
        }
    }
}