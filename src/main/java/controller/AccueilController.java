package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class AccueilController {
    @FXML private TextField searchAccueil;
    @FXML private ComboBox<String> comboBox;
    @FXML private Label resultLabel;

    @FXML
    public void initialize() {
        comboBox.setItems(FXCollections.observableArrayList(
                "Manuelle", "Automatique"
        ));
        comboBox.setValue("Choix");
    }

    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        String searchText = searchAccueil.getText();
        String selectedCategory = comboBox.getValue();
        if (searchText.isEmpty()) {
            resultLabel.setText("Veuillez entrer un terme de recherche.");
        } else if (selectedCategory == null) {
            resultLabel.setText("Veuillez sélectionner une catégorie.");
        } else {
            resultLabel.setText("Recherche : " + searchText + " dans " + selectedCategory);
        }
    }

    @FXML
    private void handleComboBoxSelection(ActionEvent event) {
        String selectedCategory = comboBox.getValue();
        if (selectedCategory != null) {
            resultLabel.setText("Catégorie sélectionnée : " + selectedCategory);
        }
    }
}