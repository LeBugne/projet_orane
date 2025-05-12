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

    public int getId_chiffre() {

        Random rand = new Random();
        return id_chiffre = rand.nextInt(100);
    }

}