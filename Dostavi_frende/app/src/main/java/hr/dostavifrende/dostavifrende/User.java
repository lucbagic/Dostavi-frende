package hr.dostavifrende.dostavifrende;


public class User {
    public String lozinka, ime, prezime, id;
    public Integer godinaRodenja;

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
}
