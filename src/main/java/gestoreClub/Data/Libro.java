package gestoreClub.Data;
import org.bson.types.ObjectId;

import java.time.LocalDate;

//la classe libro avrà dati semplici utili solo al fine del noleggio. Non è complicata ulteriormente
public class Libro {
    private String isbn;
    private String titolo;
    private String autore;
    private boolean isRented;
    private double prezzo;
    private String proprietario;



    //costruttore libro non noleggiato
    public Libro(String isbn, String titolo, String autore, double Prezzo, String proprietario) {
        this.isbn = isbn;
        this.titolo = titolo;
        this.autore = autore;
        this.prezzo = prezzo;  //il prezzo sarà utile a gestire il prezzo della caparra da lasciare al noleggio del libro
        this.proprietario = proprietario;
    }


    public String getTitolo() {
        return titolo;
    }
    public String getAutore() {
        return autore;
    }
    public double getPrezzo() {
        return prezzo;
    }
    public String getProprietario() {
        return proprietario;
    }
    public String getIsbn() {
        return isbn;
    }
    public boolean getRented() {
        return isRented;
    }
}