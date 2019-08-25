package hr.dostavifrende.dostavifrende.core.fragments;


public class User {
    public String lozinka, ime, prezime, id;
    public Integer godinaRodenja;
    int ocjena, brojac, zbroj, thumbsUp, thumbsDown;

    public User(String ime, String prezime, String lozinka, Integer godinaRodenja)
    {
        this.ime = ime;
        this.prezime = prezime;
        this.lozinka = lozinka;
        this.godinaRodenja = godinaRodenja;
    }

    public User(String ime, String id)
    {
        this.ime = ime;
        this.id = id;
    }

    public User(){

    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getGodinaRodenja() {
        return godinaRodenja;
    }

    public void setGodinaRodenja(Integer godinaRodenja) {
        this.godinaRodenja = godinaRodenja;
    }

    public int getOcjena() {
        return ocjena;
    }

    public void setOcjena(int ocjena) {
        this.ocjena = ocjena;
    }

    public int getBrojac() {
        return brojac;
    }

    public void setBrojac(int brojac) {
        this.brojac = brojac;
    }

    public int getZbroj() {
        return zbroj;
    }

    public void setZbroj(int zbroj) {
        this.zbroj = zbroj;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public int getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(int thumbsDown) {
        this.thumbsDown = thumbsDown;
    }
}
