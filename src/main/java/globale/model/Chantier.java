package globale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import globale.model.*;
import globale.outils.*;
import javafx.scene.image.Image;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Chantier {
    private Concessionaire concessionaire;
    private String date;
    private String adresse;
    private String telephone;
    private String ville;
    private String responsable;
    private int id;
    private Arrete arrete;
    private HashMap<String,String> imageFileNames = new HashMap<>(); // Noms de fichiers (ex: photo.jpg)
    @JsonIgnore // J'ajoute ceci pour éviter que les objets Image soient serialisé et avoir des erreurs
    private ArrayList<Image> arrayListFile = new ArrayList<>();
    @JsonIgnore // J'ajoute ceci pour éviter que les objets Image soient serialisé et avoir des erreurs
    private HashMap<String, Image> imagesByCategory = new HashMap<>();

    // Dans EditionController, c'est ici que l'on stocke les images sélectionnées
    private static final String IMAGE_DIR = System.getProperty("user.home") + "/myapp/images/";


    // Constructeur par défaut requis par Jackson
    public Chantier() {
        this.id = FabriqueIdentifiant.getInstance().getId();
        this.adresse = "";
        this.date = "";
    }
    public Chantier(Concessionaire c, String date, String adr){
        this.concessionaire = c;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        /* On formate la date de texte à objet */
        this.date = date;
        this.adresse = adr;
        FabriqueIdentifiant fi = FabriqueIdentifiant.getInstance();
        this.id = fi.getId();
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getVille() {
        return ville;
    }

    public HashMap<String,String> getImageFileNames(){
         return this.imageFileNames;
    }
    public void setVille(String ville) {
        this.ville = ville;
    }
    public String getResponsable() {
        return responsable;
    }
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Arrete getArrete() {
        return arrete;
    }

    public void setArrete(Arrete arrete) {
        this.arrete = arrete;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public void setConcessionnaire(Concessionaire concessionaire) {
        this.concessionaire = concessionaire;
    }

    public void setArrayListFile(ArrayList<Image> arrayListFile) {
        this.arrayListFile = arrayListFile;
    }
    public int getId(){
        return this.id;
    }
    public String getDate(){
        return this.date;
    }
    public String getAdresse(){
        return this.adresse;
    }
    public Concessionaire getConcessionaire(){
        return this.concessionaire;
    }
    public void ajouterImg(Image img){
        this.arrayListFile.add(img);
    }

    public void addImage(String category, Image image) {
        imagesByCategory.put(category, image);
    }

    public void addImage(String categorie, String nomFichier) {
        if (nomFichier != null && !nomFichier.isEmpty()) {
            // Former une URL valide : file:/home/user/myapp/images/nomFichier
            String url = "file:" + IMAGE_DIR + nomFichier;
            System.out.println("url ajouté : " + url);
            imageFileNames.put(categorie, url);
        }
    }

    public void remove(String category){
        this.imageFileNames.remove(category);
    }

    @JsonIgnore
    public ArrayList<Image> getArrayListImage(){
        return this.arrayListFile;
    }
    @JsonIgnore
    public Map<String, Image> getImagesByCategory() {
        return imagesByCategory;
    }
}
