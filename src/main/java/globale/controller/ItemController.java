package globale.controller;

import globale.Observateur;
import globale.model.Chantier;
import globale.model.SujetObserve;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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
        // Action à exécuter lors du clic
        System.out.println("Vignette cliquée : " + (this.racineItem != null ? racineItem : labelItem.getText()));
        // Exemple : Afficher une alerte

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
