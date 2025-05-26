package globale.controller;

import globale.Observateur;
import globale.model.Chantier;
import globale.model.SujetObserve;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ItemController implements Observateur {
    @FXML private AnchorPane racineItem;
    @FXML private ImageView img;
    @FXML private Label labelItem;
    private Chantier chantier;

    public ItemController(){
    }

    public void initialize() {
        this.racineItem.setOnMouseClicked(this::handleVignetteClick);
    }

    private void handleVignetteClick(MouseEvent event) {
        try {
            FXMLLoader detailLoader = new FXMLLoader(getClass().getResource("/fxml/DetailView.fxml"));
            detailLoader.setControllerFactory(param -> ControllerManager.getInstance().getDetailController());
            Scene newScene = new Scene(detailLoader.load());
            DetailController detailController = detailLoader.getController();
            detailController.setData(chantier); // Passer les données
            Stage stage = (Stage) this.racineItem.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de DetailView : " + e.getMessage());
            e.printStackTrace();
        }

    }
    public void setData(Chantier c){
        this.chantier = c;
        this.labelItem.setText(this.chantier.getConcessionaire().getNom());
        Image image = new Image(getClass().getResourceAsStream(this.chantier.getConcessionaire().getLogo()));
        this.img.setImage(image);
    }
    @Override
    public void reagir() {
        System.out.println("réagis");
    }
}
