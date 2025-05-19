package globale.model;

import globale.Observateur;

import java.util.ArrayList;

public abstract class SujetObserve {
    private ArrayList<Observateur> arrayList = new ArrayList<>();
    public SujetObserve(){

    }

    public void ajouterObservateur(Observateur obs){
        this.arrayList.add(obs);
    }


    public void notifyObservateur(){
        for (Observateur o : this.arrayList) {
            o.reagir();
        }
    }
}
