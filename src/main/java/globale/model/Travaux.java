package globale.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Travaux extends SujetObserve implements Iterable{
    static final Travaux instance = new Travaux();
    public ArrayList<Chantier> arrayList = new ArrayList<>();
    Map<Integer, Chantier> chantierMap = new HashMap<>();
    private static final String SAVE_PATH = System.getProperty("user.home") + "/.monApp/chantiers.json";

    public Travaux(){}
    public static Travaux getInstance() { return instance; }

    public Map<Integer,Chantier> getChantierMap(){
        return this.chantierMap;
    }

    public void setChantierMap(Map<Integer,Chantier> map) throws IOException {
        this.chantierMap = map;
        notifierObservateurs();
    }
    public void ajouterChantier(Chantier c) throws IOException {
        this.arrayList.add(c);
        notifierObservateurs();
    }

    public void ajouterChantier(Integer i,Chantier c) throws IOException {
        this.chantierMap.put(i,c);
        notifierObservateurs();
    }

    public void ajouterChantier(Chantier ... c) throws IOException {
        this.arrayList.addAll(List.of(c));
        notifierObservateurs();
    }

    /**
     * Si un objet Chantier à été modifié (dans le contexte du DetailController)
     * Il faut prévenir le modèle
     * Càd remplacer la version obsolète du Chantier par la bonne version
     * @param c
     */
    public void majDuModèle(Chantier c) throws IOException {
        this.chantierMap.put(c.getId(),c);
        notifierObservateurs();
    }

    public void deleteChantier(Chantier c) throws IOException {
        this.chantierMap.remove(c.getId());
        notifierObservateurs();
    }

    /**
     * Gestion de la sauvegarde au format .json
     * @throws IOException
     */
    public void saveToFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(SAVE_PATH);
        file.getParentFile().mkdirs(); // Créer dossier si nécessaire
        mapper.writeValue(file, chantierMap);
    }

    /**
     * Permet de charger une banque de données
     * L'ObjectMapper est une classe de la bibliothèque Jackson utilisée pour convertir des objets Java en JSON (sérialisation) et JSON en objets Java (désérialisation).
     * @throws IOException
     */
    public void loadFromFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(SAVE_PATH);
        if (file.exists()) {
            chantierMap = mapper.readValue(file, new TypeReference<Map<Integer, Chantier>>() {});
        }
        notifierObservateurs(); // le modèle a été modifié donc il faut prévenir tout le monde
    }
    @Override
    public Iterator iterator() {
        return this.arrayList.iterator();
    }
}
