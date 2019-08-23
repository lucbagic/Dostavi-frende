package hr.dostavifrende.dostavifrende;

public class Deal {
    public String korisnik, status;

    public Deal(String korisnik, String status)
    {
        this.korisnik = korisnik;
        this.status = status;
    }

    public Deal(){

    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
