package globale.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Travaux extends SujetObserve implements Iterable{
    public ArrayList<Chantier> arrayList = new ArrayList<>();

    public Travaux(){
    }

    public void ajouterChantier(Chantier c){
        this.arrayList.add(c);
        notifyObservateur();
    }

    public void ajouterChantier(Chantier ... c){
        this.arrayList.addAll(List.of(c));
        notifyObservateur();
    }


    @Override
    public Iterator iterator() {
        return this.arrayList.iterator();
    }
}
