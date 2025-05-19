package globale.model;

import globale.model.*;
import globale.outils.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Chantier {
    private Concessionaire concessionaire;
    private LocalDate date;
    private String adresse;
    private int id;

    private Arrete arrete;

    public Chantier(Concessionaire c, String date, String adr){
        this.concessionaire = c;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        /* On formate la date de texte à objet */
        LocalDate d = LocalDate.parse(date,df);
        this.date = d;
        this.adresse = adr;
        FabriqueIdentifiant fi = FabriqueIdentifiant.getInstance();
        this.id = fi.getId();
    }


    public int getId(){ return this.id; }

    public LocalDate getDate(){ return this.date; }

    public String getAdresse(){ return this.adresse; }
    public Concessionaire getConcessionaire(){ return this.concessionaire; }













}
