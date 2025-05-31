package globale.controller;

import globale.Observateur;
import globale.model.Chantier;
import globale.model.Concessionaire;
import globale.model.Travaux;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
        setupSearchBar();
        update();
    }

    private void setupSearchBar() {
        // Créer un ContextMenu pour les suggestions
        ContextMenu suggestionsMenu = new ContextMenu();

        // Écouter les changements dans searchBar
        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            suggestionsMenu.getItems().clear();



            // Filtrer les adresses correspondant au texte saisi
            List<String> matchingAdresses = Travaux.getInstance().getChantierMap().values().stream().map(Chantier::getAdresse)
                    .filter(adresse -> adresse.toLowerCase().contains(newValue.toLowerCase()))
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            // Ajouter les suggestions au ContextMenu
            for (String adresse : matchingAdresses) {
                MenuItem item = new MenuItem(adresse);
                item.setOnAction(event -> {
                    searchBar.setText(adresse);
                    filterChantiers(adresse);
                    suggestionsMenu.hide();
                });
                suggestionsMenu.getItems().add(item);
            }

            // Afficher les suggestions si elles existent
            if (!matchingAdresses.isEmpty() && searchBar.getScene() != null && searchBar.getScene().getWindow() != null) {
                // Je veux que le menu est la même largeur que la barre de recherche
                suggestionsMenu.setMinWidth(searchBar.getWidth());

                // Utiliser localToScreen pour des coordonnées précises
                Point2D screenPos = searchBar.localToScreen(0, searchBar.getHeight());
                if (screenPos != null) {
                    suggestionsMenu.show(
                            searchBar,
                            screenPos.getX(),
                            screenPos.getY()
                    );
                } else {
                    System.out.println("Erreur : localToScreen a retourné null");
                }
            } else {
                System.out.println("Aucune suggestion ou Scene/Window null");
                suggestionsMenu.hide();
            }

            // Filtrer les chantiers en temps réel
            filterChantiers(newValue);
        });

        // Cacher le menu des suggestions lorsque searchBar perd le focus
        searchBar.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                suggestionsMenu.hide();
            }
        });
    }

    private void filterChantiers(String searchText) {
        // Filtrer les chantiers dont l’adresse contient le texte saisi
        List<Chantier> filteredChantiers = Travaux.getInstance().getChantierMap().values().stream()
                .filter(chantier -> chantier.getAdresse().toLowerCase().contains(searchText.toLowerCase()))
                .toList();

        // Mettre à jour le GridPane
        afficherSuggestion(filteredChantiers);
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

    private void afficherSuggestion(List<Chantier> chantiers) {
        // Vider le GridPane
        grille.getChildren().clear();

        // Configuration de la grille
        int numColumns = 2;
        int numRows = (int) Math.ceil((double) chantiers.size() / numColumns);

        // Ajouter chaque chantier au GridPane
        for (int i = 0; i < chantiers.size(); i++) {
            Chantier chantier = chantiers.get(i);
            try {
                // Charger ItemView.fxml
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemView.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                // Configurer le contrôleur
                ItemController itemController = fxmlLoader.getController();
                itemController.setData(chantier);

                // Gestionnaire de clic pour ouvrir DetailView
                anchorPane.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/fxml/DetailView.fxml"));
                        detailLoader.setControllerFactory(param -> {
                            DetailController detailController = new DetailController();
                            detailController.setChantier(chantier);
                            return detailController;
                        });
                        Scene newScene = new Scene(detailLoader.load());
                        Stage stage = (Stage) anchorPane.getScene().getWindow();
                        stage.setScene(newScene);
                        stage.show();
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Erreur de navigation");
                        alert.setContentText("Impossible d’ouvrir DetailView : " + e.getMessage());
                        alert.showAndWait();
                    }
                });

                // Ajouter au GridPane
                int row = i / numColumns;
                int col = i % numColumns;
                grille.add(anchorPane, col, row);
                GridPane.setMargin(anchorPane, new Insets(20));

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de chargement");
                alert.setContentText("Impossible de charger ItemView : " + e.getMessage());
                alert.showAndWait();
            }
        }

        // Ajuster les espacements
        grille.setHgap(10);
        grille.setVgap(10);

    }
    public void test() throws IOException {
        // Définir des données fictives de base
        String baseConcessionnaire = "Concessionnaire";
        String baseDate = "28/05/2025";
        String baseAdresse = "Rue de l'Exemple";
        String baseVille = "Paris";
        String baseResponsable = "Jean Dupont";
        String baseTelephone = "0123456789";

        // Liste d'images fictives (réutilisées pour tous les chantiers)
        String[] fakeImages = {
                "file:/home/lebugne/IdeaProjects/projet_orane/icon.jpeg",
                "file:/home/lebugne/Bureau/insta_photo.jpg"
        };

        // Nombre de chantiers à créer
        int numChantiers = 10;
        int numColumns = 3; // 2 colonnes
        int numRows = (int) Math.ceil((double) numChantiers / numColumns); // Calculer les lignes nécessaires

        // Boucle pour créer 10 chantiers
        for (int i = 0; i < numChantiers; i++) {
            // Créer un concessionnaire fictif
            Concessionaire fakeConcessionnaire = new Concessionaire(baseConcessionnaire + " " + (i + 1));

            // Adresse unique pour chaque chantier
            String fakeAdresse = (100 + i * 10) + " " + baseAdresse + ", " + baseVille;

            // Créer un chantier fictif
            Chantier fakeChantier = new Chantier(fakeConcessionnaire, baseDate, fakeAdresse);
            fakeChantier.setTelephone(baseTelephone);
            fakeChantier.setVille(baseVille);
            fakeChantier.setResponsable(baseResponsable + " " + (i + 1));

            // Ajouter des images fictives
            for (String imagePath : fakeImages) {
                try {
                    Image image = new Image(imagePath);
                    fakeChantier.ajouterImg(image);
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'ajout de l'image " + imagePath + " : " + e.getMessage());
                }
            }

            // Ajouter le chantier à Travaux
            Travaux.getInstance().ajouterChantier(fakeChantier.getId(),fakeChantier);

            // Charger ItemView.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemView.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            // Configurer le contrôleur
            ItemController itemController = fxmlLoader.getController();
            itemController.setData(fakeChantier);

            // Ajouter un gestionnaire de clic pour ouvrir DetailView
            anchorPane.setOnMouseClicked(event -> {
                try {
                    FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/fxml/DetailView.fxml"));
                    detailLoader.setControllerFactory(param -> {
                        DetailController detailController = new DetailController();
                        detailController.setChantier(fakeChantier); // Injecter le chantier
                        return detailController;
                    });
                    Scene newScene = new Scene(detailLoader.load());

                    Stage stage = (Stage) anchorPane.getScene().getWindow();
                    stage.setScene(newScene);
                    stage.show();
                } catch (IOException e) {
                    System.err.println("Erreur lors de l'ouverture de DetailView : " + e.getMessage());
                    e.printStackTrace();
                }
            });

            // Ajouter l’AnchorPane au GridPane
            int row = i / numColumns;
            int col = i % numColumns;
            this.grille.add(anchorPane, col, row);
        }

        // Optionnel : Ajuster les espacements du GridPane
        grille.setHgap(10); // Espacement horizontal
        grille.setVgap(10); // Espacement vertical
    }


    @Override
    public void reagir() throws IOException {
        update();
    }
}