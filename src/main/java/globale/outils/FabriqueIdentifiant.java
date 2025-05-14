package globale.outils;

import java.util.Random;
public class FabriqueIdentifiant {
    private static FabriqueIdentifiant instance = new FabriqueIdentifiant();
    private int id_chiffre;

    public static FabriqueIdentifiant getInstance() {
        if (instance == null) {
            instance = new FabriqueIdentifiant();
        }
        return instance;
    }

    FabriqueIdentifiant() {
    }

    public int getId() {
        Random rand = new Random();
        return id_chiffre = rand.nextInt(50000000);
    }

}