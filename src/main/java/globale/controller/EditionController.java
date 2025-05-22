package globale.controller;

import globale.Observateur;
import globale.model.Chantier;
import globale.model.Concessionaire;
import globale.model.Travaux;
import javafx.animation.FillTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditionController implements Observateur {
    @FXML private TextField adrChantier;
    @FXML private Button buttonBack;
    @FXML private Button crée;
    @FXML private TextField concessionnaire;
    @FXML private TextField responsable;
    @FXML private TextField tel;
    @FXML private TextField ville;
    @FXML private DatePicker calendrier;
    private Travaux travaux;


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
    }

    public void handleBackButton(){
        switchToAccueil();
    }

    @FXML
    public void handleAjoutezButton(){
        ArrayList<String> textFieldValues = new ArrayList<>();
        String adrChantier = this.adrChantier.getText().trim();
        String conce = this.concessionnaire.getText().trim();
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
        this.adrChantier.clear();


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
}
