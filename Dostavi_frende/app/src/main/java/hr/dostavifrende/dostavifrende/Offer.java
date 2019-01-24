package hr.dostavifrende.dostavifrende;


public class Offer {
<<<<<<< HEAD
    public String korisnik, napomena, datum, grad;

    public Offer(String korisnik, String grad, String napomena)
    {
        this.korisnik = korisnik;
        this.grad = grad;
=======
    public String korisnik, napomena, datum, grad, imePrezime, urlSlike;

    public Offer(String korisnik, String imePrezime, String datum, String napomena, String grad, String urlSlike)
    {
        this.korisnik = korisnik;
        this.imePrezime = imePrezime;
        this.datum = datum;
>>>>>>> 747359a0ff97420e911ed1ded9342fe7dd4f4c3b
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

<<<<<<< HEAD
=======
    public String getDatum() {
        return datum;
    }

>>>>>>> 747359a0ff97420e911ed1ded9342fe7dd4f4c3b
    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }
<<<<<<< HEAD
}
=======

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
>>>>>>> 747359a0ff97420e911ed1ded9342fe7dd4f4c3b
