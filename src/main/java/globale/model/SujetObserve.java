package globale.model;

import globale.Observateur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SujetObserve {
    private final ArrayList<Observateur> observateurs = new ArrayList<>();

    public void ajouterObservateur(Observateur obs) {
        if (!observateurs.contains(obs)) {
            observateurs.add(obs);
        }
    }

    public void supprimerObservateur(Observateur obs) {
        observateurs.remove(obs);
    }

    public void notifierObservateurs() throws IOException {
        for (Observateur obs : observateurs) {
            obs.reagir();
        }
    }
}
