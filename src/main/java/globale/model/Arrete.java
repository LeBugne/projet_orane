package globale.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Arrete {
    private String lienImg;
    private int numArrete;
    private LocalDate date;

    public Arrete(String lien, int num, String date){
        this.lienImg = lien;
        this.numArrete = num;
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate ld = LocalDate.parse(date,dt);
        this.date = ld;
    }


}
