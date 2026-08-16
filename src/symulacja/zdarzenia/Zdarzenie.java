package symulacja.zdarzenia;

import symulacja.Czas;
import symulacja.Planer;

/**
 * Bazowa klasa wszystkich zdarzeń w symulacji.
 * Zdarzenia są porównywane najpierw po czasie, a przy remisie
 * po kolejności wstawienia do kolejki.
 */
public abstract class Zdarzenie implements Comparable<Zdarzenie> {
    private final Czas czas;
    private long kolejnośćWstawienia;

    protected Zdarzenie(Czas czas) {
        assert czas != null;
        this.czas = czas;
        this.kolejnośćWstawienia = -1;  // -1 = "jeszcze nieustawiona"
    }

    public Czas czas() {
        return this.czas;
    }

    public long kolejnośćWstawienia() {
        return this.kolejnośćWstawienia;
    }

    /**
     * Ustawiana raz, w momencie wstawienia zdarzenia do kolejki.
     */
    public void ustawKolejnośćWstawienia(long n) {
        assert n >= 0;
        assert kolejnośćWstawienia == -1;  // ustawiana tylko raz
        kolejnośćWstawienia = n;
    }

    /**
     * Reakcja zdarzenia na jego pobranie z kolejki.
     * Podklasy implementują tu logikę specyficzną dla rodzaju zdarzenia.
     */
    public abstract void obsłuż(Planer planer);

    @Override
    public int compareTo(Zdarzenie inne) {
        assert this.kolejnośćWstawienia >= 0;
        assert inne.kolejnośćWstawienia >= 0;

        int wynikCzasu = this.czas.compareTo(inne.czas);
        if (wynikCzasu != 0) {
            return wynikCzasu;
        }
        return Long.compare(this.kolejnośćWstawienia, inne.kolejnośćWstawienia);
    }
}
