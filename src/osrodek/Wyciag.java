package osrodek;

import sportowcy.Sportowiec;
import symulacja.Czas;
import symulacja.Planer;
import struktury.KolejkaOczekujacych;
import struktury.StatystykiKolejki;

import java.util.Locale;

/**
 * Reprezentuje wyciąg prowadzący z niższego węzła do wyższego.
 * Wyciąg cyklicznie zabiera z kolejki grupy oczekujących sportowców.
 */
public class Wyciag extends Krawedz {
    private final int odstępMiędzyGrupami;
    private final int maksymalnaWielkośćGrupy;
    private final KolejkaOczekujacych kolejkaOczekujących;
    private final StatystykiKolejki statystykiKolejki;

    public Wyciag(
            int numer,
            Wezel początek,
            Wezel koniec,
            int odstępMiędzyGrupami,
            int maksymalnaWielkośćGrupy,
            int czasPrzejazdu
    ) {
        super(numer, początek, koniec, czasPrzejazdu);
        assert odstępMiędzyGrupami > 0;
        assert maksymalnaWielkośćGrupy > 0;
        assert początek.wysokość() < koniec.wysokość();

        this.odstępMiędzyGrupami = odstępMiędzyGrupami;
        this.maksymalnaWielkośćGrupy = maksymalnaWielkośćGrupy;
        this.kolejkaOczekujących = new KolejkaOczekujacych();
        this.statystykiKolejki = new StatystykiKolejki(Czas.POCZATEK_DNIA);
    }

    public int odstępMiędzyGrupami() {
        return odstępMiędzyGrupami;
    }

    public int maksymalnaWielkośćGrupy() {
        return maksymalnaWielkośćGrupy;
    }

    public int maksymalnaDługośćKolejki() {
        return kolejkaOczekujących.maksymalnaDługość();
    }

    public double średniaDługośćKolejki() {
        return statystykiKolejki.średniaDługość(Czas.KONIEC_PLANOWANIA);
    }

    /**
     * Maksymalna liczba wjazdów możliwa w ciągu dnia: liczba cykli wyciągu
     * pomnożona przez pojemność grupy.
     */
    public int maksymalnaLiczbaWjazdów() {
        int t = Czas.KONIEC_PLANOWANIA.sekundy() - Czas.POCZATEK_DNIA.sekundy();
        // Cykle są w chwilach 0, d, 2d, ... dopóki < t (cykl dokładnie o t już się
        // nie odbywa). Liczba takich chwil to sufit z t/d - stąd dzielenie w górę.
        int liczbaCykli = (t + odstępMiędzyGrupami - 1) / odstępMiędzyGrupami;
        return liczbaCykli * maksymalnaWielkośćGrupy;
    }

    public void dodajDoKolejki(Sportowiec sportowiec, Czas teraz) {
        assert sportowiec != null;
        assert teraz != null;

        kolejkaOczekujących.dodaj(sportowiec);
        statystykiKolejki.zarejestrujZmianę(kolejkaOczekujących.długość(), teraz);
    }

    /**
     * Zabiera z kolejki kolejną grupę (do maksymalnej wielkości) i rejestruje
     * przejazd. Zwraca zabranych pasażerów. Nie powiadamia ich ani planera -
     * tym zajmuje się obsłużCykl. Wydzielone, by ułatwić testowanie.
     */
    public Sportowiec[] zabierzGrupę(Czas teraz) {
        assert teraz != null;

        int doZdjęcia = Math.min(
                maksymalnaWielkośćGrupy,
                kolejkaOczekujących.długość()
        );

        Sportowiec[] pasażerowie = new Sportowiec[doZdjęcia];
        for (int i = 0; i < doZdjęcia; i++) {
            pasażerowie[i] = kolejkaOczekujących.pobierzPierwszego();
        }

        if (doZdjęcia > 0) {
            zarejestrujPrzejazd(doZdjęcia);
            statystykiKolejki.zarejestrujZmianę(kolejkaOczekujących.długość(), teraz);
        }

        return pasażerowie;
    }

    public void obsłużCykl(Planer planer) {
        assert planer != null;

        Sportowiec[] pasażerowie = zabierzGrupę(planer.teraz());

        if (pasażerowie.length > 0) {
            for (int i = 0; i < pasażerowie.length; i++) {
                pasażerowie[i].rozpocznijWjazd(this, planer);
            }
            planer.zaplanujKoniecWjazdu(pasażerowie, pasażerowie.length, this);
        }
        // Wyciąg działa cyklicznie nawet wtedy, gdy kolejka jest pusta.
        planer.zaplanujCyklWyciągu(this);
    }

    @Override
    public String opisStatystyki() {
        int wjazdy = liczbaPrzejazdów();
        int maks = maksymalnaLiczbaWjazdów();
        int procent = (int) Math.round(100.0 * wjazdy / maks);

        return String.format(Locale.US,
                "Wyciąg %d: kolejka: średnia %d, maksymalna %d; pasażerów: %d / %d (%d%%)",
                numer(),
                Math.round(średniaDługośćKolejki()),
                maksymalnaDługośćKolejki(),
                wjazdy, maks, procent);
    }

    @Override
    public String toString() {
        return "w" + numer();
    }
}