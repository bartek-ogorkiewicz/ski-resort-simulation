package osrodek;

import java.util.Arrays;

/**
 * Reprezentuje węzeł grafu ośrodka narciarskiego.
 * Przechowuje dane o położeniu węzła oraz krawędzie wychodzące:
 * trasy i wyciągi.
 */
public class Wezel {
    private final int numer;
    private final int wysokość;
    private final int x;
    private final int y;
    private final boolean skomunikowany;

    private Trasa[] wychodząceTrasy;
    private int liczbaWychodzącychTras;

    private Wyciag[] wychodząceWyciągi;
    private int liczbaWychodzącychWyciągów;

    public Wezel(int numer, int wysokość, int x, int y, boolean skomunikowany) {
        assert numer >= 0;

        this.numer = numer;
        this.wysokość = wysokość;
        this.x = x;
        this.y = y;
        this.skomunikowany = skomunikowany;

        this.wychodząceTrasy = new Trasa[1];
        this.liczbaWychodzącychTras = 0;

        this.wychodząceWyciągi = new Wyciag[1];
        this.liczbaWychodzącychWyciągów = 0;
    }

    public int numer() {
        return numer;
    }

    public int wysokość() {
        return wysokość;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public boolean czySkomunikowany() {
        return skomunikowany;
    }

    public int liczbaWychodzącychTras() {
        return liczbaWychodzącychTras;
    }

    public Trasa wychodzącaTrasa(int indeks) {
        assert indeks >= 0 && indeks < liczbaWychodzącychTras;
        return wychodząceTrasy[indeks];
    }

    public int liczbaWychodzącychWyciągów() {
        return liczbaWychodzącychWyciągów;
    }

    public Wyciag wychodzącyWyciąg(int indeks) {
        assert indeks >= 0 && indeks < liczbaWychodzącychWyciągów;
        return wychodząceWyciągi[indeks];
    }

    public void dodajWychodzącąTrasę(Trasa trasa) {
        assert trasa != null;
        assert trasa.początek() == this;

        if (liczbaWychodzącychTras == wychodząceTrasy.length) {
            wychodząceTrasy = Arrays.copyOf(wychodząceTrasy, 2 * wychodząceTrasy.length);
        }
        wychodząceTrasy[liczbaWychodzącychTras] = trasa;
        liczbaWychodzącychTras++;
    }

    public void dodajWychodzącyWyciąg(Wyciag wyciąg) {
        assert wyciąg != null;
        assert wyciąg.początek() == this;

        if (liczbaWychodzącychWyciągów == wychodząceWyciągi.length) {
            wychodząceWyciągi = Arrays.copyOf(wychodząceWyciągi, 2 * wychodząceWyciągi.length);
        }

        wychodząceWyciągi[liczbaWychodzącychWyciągów] = wyciąg;
        liczbaWychodzącychWyciągów++;
    }

    @Override
    public String toString() {
        return "Węzeł " + numer;
    }
}
