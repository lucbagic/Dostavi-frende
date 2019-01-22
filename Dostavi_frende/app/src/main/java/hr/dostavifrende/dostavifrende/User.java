package hr.dostavifrende.dostavifrende;


public class User {
    public String lozinka, ime, prezime;
    public Integer godinaRodenja;

    public User(String ime, String prezime, String lozinka, Integer godinaRodenja)
    {
        this.ime = ime;
        this.prezime = prezime;
        this.lozinka = lozinka;
        this.godinaRodenja = godinaRodenja;
    }

    User(){

    }
}
