package osrodek;

/**
 * Wspólna baza dla tras i wyciągów w grafie ośrodka.
 * Przechowuje dane wspólne dla każdej krawędzi oraz licznik przejazdów.
 */
public abstract class Krawedz {
    private final int numer;
    private final Wezel początek;
    private final Wezel koniec;
    private final int czasPrzejazdu;
    private int liczbaPrzejazdów;

    protected Krawedz(int numer, Wezel początek, Wezel koniec, int czasPrzejazdu) {
        assert numer >= 0;
        assert początek != null;
        assert koniec != null;
        assert początek != koniec;
        assert czasPrzejazdu > 0;

        this.numer = numer;
        this.początek = początek;
        this.koniec = koniec;
        this.czasPrzejazdu = czasPrzejazdu;
        this.liczbaPrzejazdów = 0;
    }

    public int numer() {
        return numer;
    }

    public Wezel początek() {
        return początek;
    }

    public Wezel koniec() {
        return koniec;
    }

    public int czasPrzejazdu() {
        return czasPrzejazdu;
    }

    public int liczbaPrzejazdów() {
        return liczbaPrzejazdów;
    }

    public void zarejestrujPrzejazd() {
        zarejestrujPrzejazd(1);
    }

    public void zarejestrujPrzejazd(int ile) {
        assert ile > 0;
        liczbaPrzejazdów += ile;
    }

    public abstract String opisStatystyki();
}
