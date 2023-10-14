package gestoreClub.Data;

import org.bson.types.ObjectId;

import java.time.LocalDate;

public class LibroNoleggiato extends Libro {

    private LocalDate dataInizioNoleggio;
    private double caparra;
    private String renterName;
    private String renterFiscalCode;

    //costruttore libro noleggiato

    public LibroNoleggiato(String isbn, String titolo, String autore, double Prezzo, String proprietario, double caparra, String renterName, String renterFiscalCode) {
        super(isbn, titolo, autore, Prezzo, proprietario);
        this.caparra = caparra;
        this.renterName = renterName;
        this.renterFiscalCode=renterFiscalCode;
        this.dataInizioNoleggio=LocalDate.now();
    }
    public LocalDate getDataInizioNoleggio() {
        return dataInizioNoleggio;
    }
    public void setDataInizioNoleggio(LocalDate dataInizioNoleggio) {
        this.dataInizioNoleggio = dataInizioNoleggio;
    }
    public double getCaparra() {
        return caparra;
    }
    public void setCaparra(int caparra) {
        this.caparra = caparra;
    }
    public String getRenterName() {
        return renterName;
    }
    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }
    public String getRenterFiscalCode() {
        return renterFiscalCode;
    }
    public void setRenterFiscalCode(String renterFiscalCode) {
        this.renterFiscalCode = renterFiscalCode;
    }
}
