public enum TypeChantier {
    GROS_OEUVRE("gros_oeuvre"),
    GAS("gas"),
    ELECTRICITE("electricite"),
    INTERNET("internet");
    private final String nom;
    TypeChantier(String nom) {
        this.nom = nom;
    }

    public String getTypeString(){
        return nom;
    }
}

