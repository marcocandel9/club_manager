package gestoreClub.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GestoreLibri implements LibroInterface {

    public ArrayList<Libro> gestoreLibri;

    public GestoreLibri(ArrayList<Libro> gestoreLibri) {
        this.gestoreLibri = gestoreLibri;
    }

    public void setGestoreLibri(ArrayList<Libro> gestoreLibri) {
        this.gestoreLibri = gestoreLibri;
    }

    @Override
    public void aggiungiLibro(Libro libro) {
        this.gestoreLibri.add(libro);


    }

    @Override
    public void rimuoviLibro(Libro libro) {
        if (gestoreLibri.contains(libro)){
            gestoreLibri.remove(libro);
        }
        else {
            System.out.println("Il libro non è nella lista.");
        }
    }

    @Override
    public void noleggiaLibro(Libro libro) {

    }
}
