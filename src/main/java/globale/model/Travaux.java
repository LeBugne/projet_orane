package globale.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Travaux extends SujetObserve implements Iterable{

    static final Travaux instance = new Travaux();

    public ArrayList<Chantier> arrayList = new ArrayList<>();

    public Travaux(){
    }

    public static Travaux getInstance() { return instance; }


    public void ajouterChantier(Chantier c){
        System.out.println("ajoutez chantier !");
        this.arrayList.add(c);
        System.out.println("taille : " + this.arrayList.size());
        notifierObservateurs();
    }

    public void ajouterChantier(Chantier ... c){
        this.arrayList.addAll(List.of(c));
        notifierObservateurs();
    }


    @Override
    public Iterator iterator() {
        return this.arrayList.iterator();
    }
}
