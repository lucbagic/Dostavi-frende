package hr.dostavifrende.dostavifrende;


public class Offer {
    public String korisnik, napomena, datum, grad, imePrezime, urlSlike;

    public Offer(String korisnik, String imePrezime, String datum, String napomena, String grad, String urlSlike)
    {
        this.korisnik = korisnik;
        this.imePrezime = imePrezime;
        this.datum = datum;
        this.napomena = napomena;
        this.grad = grad;
        this.urlSlike = urlSlike;
    }

    public Offer(){

    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime = imePrezime;
    }

    public String getUrlSlike() {
        return urlSlike;
    }

    public void setUrlSlike(String urlSlike) {
        this.urlSlike = urlSlike;
    }
}