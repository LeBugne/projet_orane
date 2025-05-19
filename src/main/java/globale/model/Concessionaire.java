package globale.model ;

public class Concessionaire {
    private String nom;
    private String telephone;
    private String mail;
    private String adresseSiege;
    private String idConsultation;
    private String logo = "/images/photo.png";

    public Concessionaire(String n) {
        this.nom = n;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdresseSiege() {
        return adresseSiege;
    }

    public void setAdresseSiege(String adresseSiege) {
        this.adresseSiege = adresseSiege;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public void setLogo(String lien){
        this.logo = lien;
    }
    public String getLogo(){  return this.logo; }

}