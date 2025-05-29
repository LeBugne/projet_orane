package globale.model;

import java.io.IOException;
import java.util.*;

public class Travaux extends SujetObserve implements Iterable{
    static final Travaux instance = new Travaux();
    public ArrayList<Chantier> arrayList = new ArrayList<>();
    Map<Integer, Chantier> chantierMap = new HashMap<>();
    public Travaux(){}
    public static Travaux getInstance() { return instance; }

    public Map<Integer,Chantier> getChantierMap(){
        return this.chantierMap;
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
    @Override
    public Iterator iterator() {
        return this.arrayList.iterator();
    }
}
