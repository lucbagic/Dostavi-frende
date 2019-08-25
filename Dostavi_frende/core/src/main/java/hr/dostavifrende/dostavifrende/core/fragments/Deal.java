package hr.dostavifrende.dostavifrende.core.fragments;

import java.util.Date;

public class Deal {
    private String korisnik, status;
    private long dealTime;

    public Deal(String korisnik, String status)
    {
        this.korisnik = korisnik;
        this.status = status;
        dealTime = new Date().getTime();
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

    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }
}
