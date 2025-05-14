package globale.model;

import globale.model.*;
import globale.outils.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Chantier {
    private String concessionaire;
    private LocalDate date;
    private String adresse;
    private int id;

    private Arrete arrete;

    public Chantier(String c, String dstring, String adr){
        this.concessionaire = c;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        /* On formate la date de texte à objet */
        LocalDate d = LocalDate.parse(dstring,df);
        this.date = d;
        this.adresse = adr;
        FabriqueIdentifiant fi = FabriqueIdentifiant.getInstance();
        this.id = fi.getId();
    }


    public int getId(){ return this.id; }

    public LocalDate getDate(){ return this.date; }

    public String getAdresse(){ return this.adresse; }

    public String getConcessionaire(){ return this.concessionaire; }













}
