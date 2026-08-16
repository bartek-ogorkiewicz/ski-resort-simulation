package struktury;

import java.util.Arrays;

import symulacja.zdarzenia.Zdarzenie;

/**
 * Implementacja kolejki zdarzeń oparta na posortowanej tablicy.
 * Wstawienie przesuwa późniejsze zdarzenia, a pobranie zwraca pierwszy element.
 */
public class PosortowanaTablicaZdarzen implements KolejkaZdarzen {
    private Zdarzenie[] dane;
    private int liczba;

    public PosortowanaTablicaZdarzen() {
        this.dane = new Zdarzenie[32];
        this.liczba = 0;
    }

    @Override
    public void dodaj(Zdarzenie zdarzenie) {
        assert zdarzenie != null;

        if (liczba == dane.length) {
            dane = Arrays.copyOf(dane, dane.length * 2);
        }

        int pozycja = znajdźPozycjęWstawienia(zdarzenie);
        System.arraycopy(dane, pozycja, dane, pozycja + 1, liczba - pozycja);
        dane[pozycja] = zdarzenie;
        liczba++;
    }

    private int znajdźPozycjęWstawienia(Zdarzenie nowe) {
        int i = 0;
        while (i < liczba && dane[i].compareTo(nowe) <= 0) {
            i++;
        }
        return i;
    }

    @Override
    public Zdarzenie pobierzPierwsze() {
        assert !czyPusta();

        Zdarzenie pierwszeZdarzenie = dane[0];
        System.arraycopy(dane, 1, dane, 0, liczba - 1);
        liczba--;
        dane[liczba] = null;

        return pierwszeZdarzenie;
    }

    @Override
    public boolean czyPusta() {
        return liczba == 0;
    }
}
