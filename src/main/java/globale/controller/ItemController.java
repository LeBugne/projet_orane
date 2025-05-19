package globale.controller;

import globale.Observateur;
import globale.model.Chantier;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ItemController implements Observateur {
    @FXML private ImageView img;
    @FXML private Label labelItem;
    private Chantier chantier;

    public ItemController(){

    }

    public void setData(Chantier c){
        this.chantier = c;
        this.labelItem.setText(this.chantier.getConcessionaire().getNom());
        Image image = new Image(getClass().getResourceAsStream(this.chantier.getConcessionaire().getLogo()));
        this.img.setImage(image);
    }
    @Override
    public void reagir() {

    }
}
