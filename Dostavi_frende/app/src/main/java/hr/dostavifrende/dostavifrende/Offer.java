package hr.dostavifrende.dostavifrende;


public class Offer {
    public String korisnik, napomena, datum, grad;

    public Offer(String korisnik, String grad, String napomena)
    {
        this.korisnik = korisnik;
        this.grad = grad;
        this.napomena = napomena;
    }

    Offer(){

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

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }
}
